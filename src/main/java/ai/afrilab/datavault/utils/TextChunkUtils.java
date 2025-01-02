package ai.afrilab.datavault.utils;

import ai.afrilab.datavault.datavault.textchunks.TextChunk;
import ai.afrilab.datavault.datavault.textchunks.dto.NoContentTextChunkDto;
import ai.afrilab.datavault.universal.PagedApiResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static ai.afrilab.datavault.mpastruct.MapstructMapper.INSTANCE;

public class TextChunkUtils {
  @NotNull
  public static ResponseEntity<PagedApiResponse<NoContentTextChunkDto>> getPagedDataVaultDto(String title, int page, Page<TextChunk> textChunks, String bucketUrl) {
    List<NoContentTextChunkDto> textChunkDto = textChunks.getContent().stream().map(
        textChunk -> {
          if (textChunk.getAudioLink() != null) textChunk.setAudioLink(bucketUrl + textChunk.getAudioLink());
          if (textChunk.getTranslationLink() != null)
            textChunk.setTranslationLink(bucketUrl + textChunk.getTranslator());
          return INSTANCE.mapTextChunkEntityToDto(textChunk);
        }
    ).toList();

    return ResponseEntity.ok(
        PagedApiResponse.<NoContentTextChunkDto>builder()
            .statusCode(HttpStatus.OK.value())
            .status("Success")
            .message("Page %s Text Chunks of [ %s ] Data Vault Fetched Successfully".formatted(page, title))
            .data(
                textChunkDto
            )
            .pageNumber(page == 0 ? 1 : page)
            .totalPages(textChunks.getTotalPages())
            .totalData(textChunks.getTotalElements())
            .isLastPage(textChunks.isLast())
            .build()
    );
  }
}
