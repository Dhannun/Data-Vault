package ai.afrilab.datavault.datavault.textchunks;

import ai.afrilab.datavault.datavault.textchunks.dto.*;
import ai.afrilab.datavault.s3.S3Buckets;
import ai.afrilab.datavault.universal.ApiResponse;
import ai.afrilab.datavault.universal.PagedApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static ai.afrilab.datavault.mpastruct.MapstructMapper.INSTANCE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("/api/v1/text-chunks")
@Tag(name = "Text Chunks Controller")
@SecurityRequirement(name = "BearerAuth")
public class TextChunkController {

  private final TextChunkService textChunkService;
  private final S3Buckets s3Buckets;

  public TextChunkController(TextChunkService textChunkService, S3Buckets s3Buckets) {
    this.textChunkService = textChunkService;
    this.s3Buckets = s3Buckets;
  }

  @PostMapping(
      value = "/{dataId}/text-chunk",
      consumes = MULTIPART_FORM_DATA_VALUE
  )
  public ResponseEntity<ApiResponse<NoContentTextChunkDto>> addTextChunks(
      @PathVariable(name = "dataId") UUID dataId,
      @RequestParam(name = "title") String title,
      @RequestParam(name = "textFile") MultipartFile content,
      @RequestParam(name = "transcriber") String transcriber
  ) {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();

    // FIXME: Find a way to move this to mapstruct or Utils

    TextChunk textChunk = textChunkService.addTextChunks(username, dataId, new AddTextChunk(title, content, transcriber));

    NoContentTextChunkDto textChunkDto = INSTANCE.mapTextChunkEntityToDto(textChunk);

    return ResponseEntity.ok(
        ApiResponse.<NoContentTextChunkDto>builder()
            .statusCode(HttpStatus.OK.value())
            .status("Success")
            .message("[ %s ] Added to Data Vault [ %s ] Successfully".formatted(title, dataId))
            .data(textChunkDto)
            .build()
    );
  }

  @PutMapping(
      value = "/{dataId}/{chunkId}",
      consumes = MULTIPART_FORM_DATA_VALUE
  )
  public ResponseEntity<ApiResponse<NoContentTextChunkDto>> updateTextChunk(
      @PathVariable(name = "dataId") UUID dataId,
      @PathVariable(name = "chunkId") UUID chunkId,
      @RequestParam(name = "title", required = false, defaultValue = "null") String title,
      @RequestParam(name = "textFile", required = false, defaultValue = "null") MultipartFile content,
      @RequestParam(name = "transcriber", required = false, defaultValue = "null") String transcriber
//      @Valid @RequestBody UpdateTextChunk textChunk
  ) {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    return textChunkService.updateTextChunk(username, dataId, chunkId, new UpdateTextChunk(title, content, transcriber));
  }

  @PutMapping(
      value = "/{dataId}/{chunkId}/audio",
      consumes = MULTIPART_FORM_DATA_VALUE
  )
  public ResponseEntity<ApiResponse<NoContentTextChunkDto>> updateTextChunkAudio(
      @PathVariable(name = "dataId") UUID dataId,
      @PathVariable(name = "chunkId") UUID chunkId,
      @RequestParam(name = "audio") MultipartFile audio,
      @RequestParam(name = "recorder") String recorder
//      @Valid @RequestBody AddTextChunkAudio textChunkAudio
  ) {
    // FIXME: Find a way to move this to mapstruct or Utils
    TextChunk saved = textChunkService.uploadTextChunkAudio(dataId, chunkId, new AddTextChunkAudio(audio, recorder));

    saved.setAudioLink(s3Buckets.getUrl() + saved.getAudioLink());

    if (saved.getTranslationLink() != null) saved.setTranslationLink(s3Buckets.getUrl() + saved.getTranslationLink());

    NoContentTextChunkDto textChunkDto = INSTANCE.mapTextChunkEntityToDto(saved);

    return ResponseEntity.ok(
        ApiResponse.<NoContentTextChunkDto>builder()
            .statusCode(HttpStatus.OK.value())
            .status("Success")
            .message("Audio Uploaded Successfully")
            .data(textChunkDto)
            .build()
    );
  }

  @PutMapping(
      value = "/{dataId}/{chunkId}/translation",
      consumes = MULTIPART_FORM_DATA_VALUE
  )
  public ResponseEntity<ApiResponse<NoContentTextChunkDto>> updateTextChunkTranslation(
      @PathVariable(name = "dataId") UUID dataId,
      @PathVariable(name = "chunkId") UUID chunkId,
      @RequestParam(name = "translation") MultipartFile translation,
      @RequestParam(name = "translator") String translator
//      @Valid @RequestBody AddTextChunkAudio textChunkAudio
  ) {

    // FIXME: Find a way to move this to mapstruct or Utils
    TextChunk saved = textChunkService.uploadTextChunkTranslation(dataId, chunkId, translation, translator);

    saved.setTranslationLink(s3Buckets.getUrl() + saved.getTranslationLink());
    if (saved.getAudioLink() != null) saved.setAudioLink(s3Buckets.getUrl() + saved.getAudioLink());

    NoContentTextChunkDto textChunkDto = INSTANCE.mapTextChunkEntityToDto(saved);

    return ResponseEntity.ok(
        ApiResponse.<NoContentTextChunkDto>builder()
            .statusCode(HttpStatus.OK.value())
            .status("Success")
            .message("Translation Uploaded Successfully")
            .data(textChunkDto)
            .build()
    );
  }

  @GetMapping("/{dataId}/all")
  public ResponseEntity<PagedApiResponse<NoContentTextChunkDto>> getTextChunksByVault(
      @PathVariable(name = "dataId") UUID dataId,
      @RequestParam(required = false, defaultValue = "1") int page,
      @RequestParam(required = false, defaultValue = "10") int size
  ) {
//    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    return textChunkService.getTextChunksByDataVaultId(dataId, page, size);
  }

  @GetMapping("/{chunkId}")
  public ResponseEntity<ApiResponse<NoContentTextChunkDto>> getTextChunkById(
      @PathVariable(name = "chunkId") UUID chunkId
  ) {
//    String username = SecurityContextHolder.getContext().getAuthentication().getName();

    // FIXME: Find a way to move this to mapstruct or Utils
    TextChunk textChunk = textChunkService.getTextChunkByChunkId(chunkId);

    if (textChunk.getAudioLink() != null) textChunk.setAudioLink(s3Buckets.getUrl() + textChunk.getAudioLink());
    if (textChunk.getTranslationLink() != null) textChunk.setTranslationLink(s3Buckets.getUrl() + textChunk.getTranslationLink());

    NoContentTextChunkDto textChunkDto = INSTANCE.mapTextChunkEntityToDto(textChunk);

    return ResponseEntity.ok(
        ApiResponse.<NoContentTextChunkDto>builder()
            .statusCode(HttpStatus.OK.value())
            .status("Success")
            .message("Text Chunk Fetched Successfully")
            .data(textChunkDto)
            .build()
    );
  }


  @GetMapping("/content/{chunkId}")
  public ResponseEntity<ApiResponse<TextChunkContentDto>> getTextChunkContent(
      @PathVariable(name = "chunkId") UUID chunkId
  ) {
    return textChunkService.getTextChunkContent(chunkId);
  }


}
