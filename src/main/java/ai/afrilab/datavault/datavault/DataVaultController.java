package ai.afrilab.datavault.datavault;

import ai.afrilab.datavault.datavault.dto.CreateData;
import ai.afrilab.datavault.datavault.dto.DataVaultDto;
import ai.afrilab.datavault.universal.ApiResponse;
import ai.afrilab.datavault.universal.PagedApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("/api/v1/data-vault")
@Tag(name = "Data Vault Controller")
@SecurityRequirement(name = "BearerAuth")
public class DataVaultController {

  private final DataVaultService dataVaultService;

  public DataVaultController(DataVaultService dataVaultService) {
    this.dataVaultService = dataVaultService;
  }

  // 1. Add Basic DataVault Data
  @PostMapping
  public ResponseEntity<ApiResponse<DataVaultDto>> createData(@Valid @RequestBody CreateData request) {
    return dataVaultService.createData(request);
  }

  @PutMapping(
      value = "/{dataId}/file",
      consumes = MULTIPART_FORM_DATA_VALUE
  )
  public ResponseEntity<ApiResponse<DataVaultDto>> updateDataVaultFile(
      @PathVariable(name = "dataId") UUID dataId,
      @RequestParam(name = "file") MultipartFile file
  ) {
    return dataVaultService.updateDataVaultFile(dataId, file);
  }

  @GetMapping("/pending")
  public ResponseEntity<PagedApiResponse<DataVaultDto>> getMyPendingDataVaults(
      @RequestParam(required = false, defaultValue = "1") int page,
      @RequestParam(required = false, defaultValue = "10") int size
  ) {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    return dataVaultService.getMyPendingDataVaults(username, page, size);
  }

  @PatchMapping("/{dataId}/complete")
  public ResponseEntity<ApiResponse<DataVaultDto>> markDataVaultAsCompleted(
      @PathVariable UUID dataId
  ) {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    return dataVaultService.markDataVaultAsCompleted(username, dataId);
  }

  @GetMapping("/{dataId}")
  public ResponseEntity<ApiResponse<DataVaultDto>> getDataVaultById(
      @PathVariable(name = "dataId") UUID dataId
  ) {
    return dataVaultService.getDataVaultById(dataId);
  }

  @GetMapping("/categories")
  public ResponseEntity<ApiResponse<String[]>> getDataVaultCategories() {
    return dataVaultService.getDataVaultCategories();
  }

}
