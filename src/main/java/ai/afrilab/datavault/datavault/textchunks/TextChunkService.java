package ai.afrilab.datavault.datavault.textchunks;

import ai.afrilab.datavault.datavault.DataVault;
import ai.afrilab.datavault.datavault.DataVaultService;
import ai.afrilab.datavault.datavault.dto.DataVaultDto;
import ai.afrilab.datavault.datavault.textchunks.dto.*;
import ai.afrilab.datavault.exceptions.InvalidResourceException;
import ai.afrilab.datavault.exceptions.OperationFailedException;
import ai.afrilab.datavault.exceptions.ResourceNotFoundException;
import ai.afrilab.datavault.s3.S3Buckets;
import ai.afrilab.datavault.s3.S3Service;
import ai.afrilab.datavault.universal.ApiResponse;
import ai.afrilab.datavault.universal.PagedApiResponse;
import ai.afrilab.datavault.users.UserService;
import ai.afrilab.datavault.utils.FileUtils;
import ai.afrilab.datavault.utils.PaginationUtils;
import ai.afrilab.datavault.utils.TextChunkUtils;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.apache.tika.exception.TikaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

import static ai.afrilab.datavault.mpastruct.MapstructMapper.INSTANCE;

@Service
public class TextChunkService {

  private static final Logger log = LoggerFactory.getLogger(TextChunkService.class);

  private final TextChunkRepository textChunkRepository;
  private final UserService userService;
  private final DataVaultService dataVaultService;
  private final S3Service s3Service;
  private final S3Buckets s3Buckets;

  public TextChunkService(TextChunkRepository textChunkRepository, UserService userService, DataVaultService dataVaultService, S3Service s3Service, S3Buckets s3Buckets) {
    this.textChunkRepository = textChunkRepository;
    this.userService = userService;
    this.dataVaultService = dataVaultService;
    this.s3Service = s3Service;
    this.s3Buckets = s3Buckets;
  }

  @Transactional
  public TextChunk addTextChunks(String username, UUID dataId, AddTextChunk textChunk) {

    try {

      log.info("Adding text chunks to Data Vault ID: [ {} ] by user: [ {} ]", dataId, username);

      userService.getUserByUsername(username);

      DataVault dataVault = dataVaultService.findDataVaultById(dataId);

      boolean textChunkExists = textChunkRepository.existsByTitleIgnoreCaseAndDataVault(textChunk.title(), dataVault);

      if (textChunkExists) {
        log.warn("Text chunk with title [ {} ] already exists in Data Vault [ {} ]", textChunk.title(), dataId);
        throw new InvalidResourceException("Text chunk with title [ %s ] already exists in Data Vault [ %s ]".formatted(textChunk.title(), dataId));
      }

      String text = extractChunkText(textChunk.content());
      TextChunk saved = TextChunk.builder()
          .title(textChunk.title())
          .dataVault(dataVault)
          .content(text)
          .transcriber(textChunk.transcriber())
          .build();

      log.info("[ {} ] added successfully to Data Vault ID: [ {} ]", textChunk.title(), dataId);

      return textChunkRepository.save(saved);

    } catch (InvalidResourceException | ResourceNotFoundException e) {
      throw e;
    } catch (Exception e) {
      log.error(e.toString());
      log.error("Error adding text chunks to Data Vault ID: [ {} ] by user: [ {} ]", dataId, username);
      throw new OperationFailedException("Error adding text chunks to Data Vault ID: [ %s ] by user: [ %s ]".formatted(dataId, username));
    }
  }

  @Transactional
  public ResponseEntity<ApiResponse<NoContentTextChunkDto>> updateTextChunk(String username, UUID dataId, UUID chunkId, @NotNull UpdateTextChunk updateTextChunk) {

    try {
      log.info("Updating text chunk with ID: [ {} ] in Data Vault ID: [ {} ] by user: [ {} ]", chunkId, dataId, username);
      userService.getUserByUsername(username);

      DataVault dataVault = dataVaultService.findDataVaultById(dataId);

      TextChunk existingTextChunk = textChunkRepository.findByIdAndDataVault(chunkId, dataVault)
          .orElseThrow(() -> new ResourceNotFoundException("Text chunk with ID [ %s ] in Data Vault [ %s ] not found".formatted(chunkId, dataId)));

      if(updateTextChunk.title().isBlank()) {
        existingTextChunk.setTitle(updateTextChunk.title());
      }
      if(updateTextChunk.transcriber().isBlank()) {
        existingTextChunk.setTranscriber(updateTextChunk.transcriber());
      }

      if(updateTextChunk.content().isEmpty()) {
          String text = extractChunkText(updateTextChunk.content());
          existingTextChunk.setContent(text);
      }

      textChunkRepository.save(existingTextChunk);

      NoContentTextChunkDto textChunkDto = INSTANCE.mapTextChunkEntityToDto(existingTextChunk);

      log.info("Text chunk ID: [ {} ] updated successfully in Data Vault ID: [ {} ]", chunkId, dataId);

      return ResponseEntity.ok(
          ApiResponse.<NoContentTextChunkDto>builder()
              .statusCode(HttpStatus.OK.value())
              .status("Success")
              .message("Text Chunk Updated Successfully")
              .data(textChunkDto)
              .build()
      );
    } catch (Exception e) {
      log.error(e.toString());
      log.error("Error updating text chunk with ID: [ {} ] in Data Vault ID: [ {} ] by user: [ {} ] : Error:~ [ {} ]", chunkId, dataId, username, e.getMessage());
      throw new OperationFailedException("Error updating text chunk with ID: [ %s ] in Data Vault ID: [ %s ] by user: [ %s ]".formatted(chunkId, dataId, username));
    }
  }

  @Transactional
  public TextChunk uploadTextChunkAudio(UUID dataId, UUID chunkId, AddTextChunkAudio textChunkAudio) {

    try {
      log.info("Adding text audio for chunk with ID: [ {} ]", dataId);

      DataVault dataVault = dataVaultService.findDataVaultById(dataId);
      TextChunk textChunk = textChunkRepository.findByIdAndDataVault(chunkId, dataVault)
          .orElseThrow(() -> new ResourceNotFoundException("Text chunk with ID [ %s ] in Data Vault [ %s ] not found".formatted(chunkId, dataId)));

      String imageKey = s3Service.putObject(
          s3Buckets.getBucket(),
          "vault/%s/audio/%s".formatted(dataVault.getTitle().replace(" ", "_"), textChunk.getTitle().replace(" ", "_") + "_audio"),
          textChunkAudio.audio()
      );

      textChunk.setAudioLink(imageKey);
      textChunk.setRecorder(textChunkAudio.recorder());

      log.info("Text Chunk [ {} ] audio uploaded successfully for Data Vault ID: [ {} ]", chunkId, dataId);

      return textChunkRepository.save(textChunk);

    } catch (ResourceNotFoundException | InvalidResourceException e) {
      throw e;
    }catch(Exception e) {
      log.error(e.toString());
      log.error("Error uploading text audio for text chunk with ID: [ {} ]", dataId);
      throw new OperationFailedException("Error uploading text audio for chunk with ID: [ %s ]".formatted(dataId));
    }
  }

  private String extractChunkText(MultipartFile content) {
    if (!FileUtils.isValidTextFile(content)) {
      log.warn("Invalid file format. Only [text file (.txt), pdf (.pdf) or word (.doc or .docx)] files are allowed, you uploaded [ {} ]", content.getContentType());
      throw new InvalidResourceException("Invalid file format. Only [text file (.txt), pdf (.pdf) or word (.doc or .docx)] files are allowed, you uploaded [ %s ]".formatted(content.getContentType()));
    }

    try {
      return FileUtils.extractText(content);
    } catch (IOException | TikaException e) {
      log.error("Error extracting text from file: {}", e.getMessage());
      throw new InvalidResourceException("Error extracting text from file: %s".formatted(e.getMessage()));
    }
  }

  @Transactional
  public ResponseEntity<PagedApiResponse<NoContentTextChunkDto>> getTextChunksByDataVaultId(UUID dataId, int page, int size) {
    try {
      log.info("Fetching text chunks for Data Vault ID: [ {} ]", dataId);

      DataVault dataVault = dataVaultService.findDataVaultById(dataId);

      Pageable pageable = PaginationUtils.getPageable(page, size);

      Page<TextChunk> dataVaultPage = textChunkRepository.findByDataVault(dataVault, pageable);

      log.info("Found [ {} ] text chunks for Data Vault ID: [ {} ]", dataVaultPage.getTotalElements(), dataId);

      return TextChunkUtils.getPagedDataVaultDto(dataVault.getTitle(), page, dataVaultPage, s3Buckets.getUrl());
    } catch (ResourceNotFoundException | InvalidResourceException e) {
      throw e;
    } catch (Exception e) {
      log.error(e.toString());
      log.error("Error fetching text chunks for Data Vault ID: [ {} ]", dataId);
      throw new OperationFailedException("Error fetching text chunks for Data Vault ID: [ %s ]".formatted(dataId));
    }
  }

  @Transactional
  public ResponseEntity<ApiResponse<TextChunkContentDto>> getTextChunkContent(UUID chunkId) {

    try {
      log.info("Fetching text chunk content for ID: [ {} ]", chunkId);

      TextChunk textChunk = textChunkRepository.findById(chunkId)
          .orElseThrow(() -> new ResourceNotFoundException("Text chunk with ID [ %s ] not found".formatted(chunkId)));

      TextChunkContentDto textChunkContentDto = INSTANCE.mapTextChunkToTextChunkContentDto(textChunk);

      log.info("Text chunk content for ID: [ {} ] fetched successfully", chunkId);

      return ResponseEntity.ok(
          ApiResponse.<TextChunkContentDto>builder()
              .statusCode(HttpStatus.OK.value())
              .status("Success")
              .message("Text Chunk Content Fetched Successfully")
              .data(textChunkContentDto)
              .build()
      );
    } catch (ResourceNotFoundException e) {
      throw e;
    } catch (Exception e) {
      log.error(e.toString());
      log.error("Error fetching text chunk content for ID: [ {} ]", chunkId);
      throw new OperationFailedException("Error fetching text chunk content for ID: [ %s ]".formatted(chunkId));
    }
  }

  @Transactional
  public TextChunk uploadTextChunkTranslation(UUID dataId, UUID chunkId, MultipartFile translation, String translator) {

    try {
      log.info("Adding text translation for chunk with ID: [ {} ]", dataId);

      DataVault dataVault = dataVaultService.findDataVaultById(dataId);
      TextChunk textChunk = textChunkRepository.findByIdAndDataVault(chunkId, dataVault)
          .orElseThrow(() -> new ResourceNotFoundException("Text chunk with ID [ %s ] in Data Vault [ %s ] not found".formatted(chunkId, dataId)));

      String imageKey = s3Service.putObject(
          s3Buckets.getBucket(),
          "vault/%s/translation/%s".formatted(dataVault.getTitle().replace(" ", "_"), textChunk.getTitle().replace(" ", "_") + "_translation"),
          translation
      );

      textChunk.setTranslationLink(imageKey);
      textChunk.setTranslator(translator);


      log.info("Text Chunk [ {} ] translation uploaded successfully for Data Vault ID: [ {} ]", chunkId, dataId);
      return textChunkRepository.save(textChunk);

    } catch (ResourceNotFoundException | InvalidResourceException e) {
      throw e;
    } catch (Exception e) {
      log.error(e.toString());
      log.error("Error uploading text translation for text chunk with ID: [ {} ]", dataId);
      throw new OperationFailedException("Error uploading text translation for chunk with ID: [ %s ]".formatted(dataId));
    }
  }

  @Transactional
  public TextChunk getTextChunkByChunkId(UUID chunkId) {

    try {
      log.info("Fetching text chunk with ID: [ {} ]", chunkId);

      TextChunk textChunk = textChunkRepository.findById(chunkId)
          .orElseThrow(() -> new ResourceNotFoundException("Text chunk with ID [ %s ] not found".formatted(chunkId)));


      log.info("Text chunk with ID: [ {} ] fetched successfully", chunkId);

      return textChunk;
    } catch (ResourceNotFoundException e) {
      throw e;
    } catch (Exception e) {
      log.error(e.toString());
      log.error("Error fetching text chunk with ID: [ {} ]", chunkId);
      throw new OperationFailedException("Error fetching text chunk with ID: [ %s ]".formatted(chunkId));
    }
  }
}
