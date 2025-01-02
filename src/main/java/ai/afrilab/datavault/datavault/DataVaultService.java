package ai.afrilab.datavault.datavault;

import ai.afrilab.datavault.datavault.dto.CreateData;
import ai.afrilab.datavault.datavault.dto.DataVaultDto;
import ai.afrilab.datavault.datavault.enums.Status;
import ai.afrilab.datavault.datavault.metadata.MetaData;
import ai.afrilab.datavault.datavault.metadata.MetaDataRepository;
import ai.afrilab.datavault.exceptions.InvalidResourceException;
import ai.afrilab.datavault.exceptions.ResourceExistsException;
import ai.afrilab.datavault.exceptions.ResourceNotFoundException;
import ai.afrilab.datavault.s3.S3Buckets;
import ai.afrilab.datavault.s3.S3Service;
import ai.afrilab.datavault.universal.ApiResponse;
import ai.afrilab.datavault.universal.PagedApiResponse;
import ai.afrilab.datavault.users.User;
import ai.afrilab.datavault.users.UserService;
import ai.afrilab.datavault.utils.PaginationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.UUID;

import static ai.afrilab.datavault.mpastruct.MapstructMapper.INSTANCE;
import static ai.afrilab.datavault.utils.DataVaultUtils.getPagedDataVaultDto;

@Service
public class DataVaultService {

  private static final Logger log = LoggerFactory.getLogger(DataVaultService.class);

  private final DataVaultRepository dataVaultRepository;
  private final UserService userService;
  private final S3Service s3Service;
  private final S3Buckets s3Bucket;
  private final MetaDataRepository metaDataRepository;

  public DataVaultService(DataVaultRepository dataVaultRepository, UserService userService, S3Service s3Service, S3Buckets s3Bucket, MetaDataRepository metaDataRepository) {
    this.dataVaultRepository = dataVaultRepository;
    this.userService = userService;
    this.s3Service = s3Service;
    this.s3Bucket = s3Bucket;
    this.metaDataRepository = metaDataRepository;
  }

  public DataVault save(DataVault dataVault) {
    return dataVaultRepository.save(dataVault);
  }

  public ResponseEntity<ApiResponse<DataVaultDto>> createData(CreateData request) {
    log.info("Received request to create Data with Identity: {} and Title: {}", request.identifier(), request.title());

    boolean dataExistsByIdentifier = dataVaultRepository.existsByIdentifier(request.identifier());

    if (dataExistsByIdentifier) {
      log.warn("Data with Identity {} already exists", request.identifier());
      throw new ResourceExistsException("Data with Identity [ %s ] already exists".formatted(request.identifier()));
    }

    MetaData metaData = MetaData.builder()
        .author(request.metaData().author())
        .edition(request.metaData().edition())
        .category(request.metaData().category().toUpperCase())
        .source(request.metaData().source())
        .location(request.metaData().location())
        .year(request.metaData().year())
        .build();

    DataVault newDataVault = DataVault.builder()
        .identifier(request.identifier())
        .title(request.title())
        .metaData(metaData)
        .status(Status.IN_PROGRESS)
        .build();

    DataVault savedDataVault = dataVaultRepository.save(newDataVault);

    DataVaultDto vaultDto = INSTANCE.mapDataVaultEntityToDto(savedDataVault);

    log.info("Data with Identity: {} and Title: {} Created Successfully", request.identifier(), request.title());

    return ResponseEntity.ok(
        ApiResponse.<DataVaultDto>builder()
            .statusCode(HttpStatus.CREATED.value())
            .status(HttpStatus.CREATED.getReasonPhrase())
            .message("Data Vault Record Created Successfully")
            .data(vaultDto)
            .build()
    );
  }

  public ResponseEntity<PagedApiResponse<DataVaultDto>> getMyPendingDataVaults(String username, int page, int size) {
    log.info("Fetching pending Data Vaults for user: {}", username);
    User user = userService.getUserByUsername(username);

    Pageable pageable = PaginationUtils.getPageable(page, size);

    Page<DataVault> userPendingDataVaultsPage = dataVaultRepository.findByCreatedByAndStatusOrderByCreatedDateDesc(user, Status.IN_PROGRESS, pageable);

    log.info("Found {} pending Data Vaults for user: {}", userPendingDataVaultsPage.getTotalElements(), username);

    return getPagedDataVaultDto(page, userPendingDataVaultsPage, s3Bucket.getUrl());
  }

  public ResponseEntity<ApiResponse<DataVaultDto>> updateDataVaultFile(UUID dataId, MultipartFile file) {
    log.info("Updating Data Vault file for Data Vault ID: {}", dataId);
    DataVault dataVault = this.findDataVaultById(dataId);

    String imageKey = s3Service.putObject(
        s3Bucket.getBucket(),
        "vault/%s/file/%s".formatted(dataVault.getTitle().replace(" ", "_"), dataVault.getTitle().replace(" ", "_") + "_file"),
        file
    );

    dataVault.setFileLink(imageKey);

    DataVault save = dataVaultRepository.save(dataVault);

    save.setFileLink(s3Bucket.getUrl() + save.getFileLink());
    if (save.getAudioSummary() != null) save.setAudioSummary(s3Bucket.getUrl() + save.getAudioSummary());

    DataVaultDto vaultDto = INSTANCE.mapDataVaultEntityToDto(save);

    log.info("Data Vault file updated successfully for Data Vault ID: {}", dataId);

    return ResponseEntity.ok(
        ApiResponse.<DataVaultDto>builder()
            .statusCode(HttpStatus.OK.value())
            .status("Success")
            .message("Data Vault File Updated Successfully")
            .data(vaultDto)
            .build()
    );
  }

  public ResponseEntity<ApiResponse<DataVaultDto>> markDataVaultAsCompleted(String username, UUID dataId) {
    log.info("Marking Data Vault as completed for Data Vault ID: {} by user: {}", dataId, username);
    User user = userService.getUserByUsername(username);

    dataVaultRepository.findByIdAndCreatedBy(dataId, user)
        .orElseThrow(() -> new ResourceExistsException("Data Vault with ID [ %s ] not found for [ %s ]"
            .formatted(dataId, user.getFullName())));

    DataVault dataVault = this.findDataVaultById(dataId);

    ArrayList<String> nullFields = this.verifyMissingFields(dataVault);

    if (!nullFields.isEmpty()) {
      log.warn("Data Vault with ID [ {} ] is not complete. Missing fields: {}", dataId, nullFields);
      throw new InvalidResourceException("Data Vault with ID [ %s ] is not complete. Missing fields: %s"
          .formatted(dataId, nullFields));
    }
    dataVault.setStatus(Status.COMPLETED);

    DataVault save = dataVaultRepository.save(dataVault);
    if (save.getFileLink() != null) save.setFileLink(s3Bucket.getUrl() + save.getFileLink());
    if (save.getAudioSummary() != null) save.setAudioSummary(s3Bucket.getUrl() + save.getAudioSummary());
    DataVaultDto vaultDto = INSTANCE.mapDataVaultEntityToDto(save);

    log.info("Data Vault marked as completed successfully for Data Vault ID: {}", dataId);

    return ResponseEntity.ok(
        ApiResponse.<DataVaultDto>builder()
            .statusCode(HttpStatus.OK.value())
            .status("Success")
            .message("Data Vault Marked as Completed Successfully")
            .data(vaultDto)
            .build()
    );
  }

  private ArrayList<String> verifyMissingFields(DataVault dataVault) {
    var nullFields = new ArrayList<String>();

//    if (!textChunkService.existsByDataVault(dataVault)) nullFields.add("<Data Vault Text> not added");
//    if (dataVault.getMetaData() == null) nullFields.add("<Meta data> is not provided");
//    if (dataVault.getFileLink() == null) nullFields.add("<Data Vault file> is not uploaded");
//    if (dataVault.getAudioSummary() == null) nullFields.add("<Audio file> is not uploaded");
//    if (dataVault.getMetaData().getAudioBy() == null) nullFields.add("<Audio by> is not provided");
//    if (dataVault.getMetaData().getConvertedBy() == null) nullFields.add("<Converted by> is not provided");
//    if (dataVault.getMetaData().getLibrary() == null) nullFields.add("<Library> is not provided");
//    if (dataVault.getMetaData().getLocation() == null) nullFields.add("<Location> is not provided");
//    if (dataVault.getMetaData().getYear() == null) nullFields.add("<Year> is not provided");
//    if (dataVault.getMetaData().getGenre() == null) nullFields.add("<Genre> is not provided");
//    if (dataVault.getMetaData().getAuthor() == null) nullFields.add("<Author> is not provided");

    return nullFields;
  }

  public ResponseEntity<ApiResponse<DataVaultDto>> getDataVaultById(UUID dataId) {
    log.info("Fetching Data Vault by ID: {}", dataId);
    DataVault dataVault = findDataVaultById(dataId);
    DataVaultDto vaultDto = INSTANCE.mapDataVaultEntityToDto(dataVault);

    log.info("Data Vault fetched successfully for Data Vault ID: {}", dataId);

    return ResponseEntity.ok(
        ApiResponse.<DataVaultDto>builder()
            .statusCode(HttpStatus.OK.value())
            .status("Success")
            .message("Data Vault Fetched Successfully")
            .data(vaultDto)
            .build()
    );
  }

  public DataVault findDataVaultById(UUID dataId) {
    log.info("Finding Data Vault by ID: {}", dataId);
    return dataVaultRepository.findById(dataId)
        .orElseThrow(() -> new ResourceNotFoundException("Data Vault with ID [ %s ] not found".formatted(dataId)));
  }

  public ResponseEntity<PagedApiResponse<DataVaultDto>> getAllDataVaults(int page, int size) {
    log.info("Fetching all Data Vaults from the database");

    Pageable pageable = PaginationUtils.getPageable(page, size);

    Page<DataVault> dataVaultsPage = dataVaultRepository.findAllOrderByCreatedDate(pageable);

    log.info("Found [{}] Data Vaults in the database", dataVaultsPage.getTotalElements());

    return getPagedDataVaultDto(page, dataVaultsPage, s3Bucket.getUrl());
  }

  public ResponseEntity<PagedApiResponse<DataVaultDto>> getAllDataVaultsByStatus(Status status, int page, int size) {
    log.info("Fetching all Data Vaults with status: {}", status);

    Pageable pageable = PaginationUtils.getPageable(page, size);

    Page<DataVault> dataVaultsPage = dataVaultRepository.findByStatusOrderByCreatedDateDesc(status, pageable);

    log.info("Found [{}] Data Vaults with status: {}", dataVaultsPage.getTotalElements(), status);

    return getPagedDataVaultDto(page, dataVaultsPage, s3Bucket.getUrl());
  }

  public ResponseEntity<PagedApiResponse<DataVaultDto>> getAllDataVaultsBySource(String source, int page, int size) {
    log.info("Fetching all Data Vaults sourced from [ {} ]", source);

    Pageable pageable = PaginationUtils.getPageable(page, size);

    Page<DataVault> dataVaultsPage = dataVaultRepository.findByMetaDataSourceOrderByCreatedDateDesc(source, pageable);

    log.info("Found [{}] Data Vaults from library: {}", dataVaultsPage.getTotalElements(), source);

    return getPagedDataVaultDto(page, dataVaultsPage, s3Bucket.getUrl());
  }

  public ResponseEntity<ApiResponse<String[]>> getDataVaultCategories() {
    log.info("Fetching all Data Vault categories from the database");

    String[] categories = metaDataRepository.findDistinctCategories();

    log.info("Found [{}] Data Vault categories in the database", categories.length);

    return ResponseEntity.ok(
        ApiResponse.<String[]>builder()
            .statusCode(HttpStatus.OK.value())
            .status("Success")
            .message("Data Vault Categories Fetched Successfully")
            .data(categories)
            .build()
    );
  }
}