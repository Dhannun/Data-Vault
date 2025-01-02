package ai.afrilab.datavault.utils;

import ai.afrilab.datavault.datavault.DataVault;
import ai.afrilab.datavault.datavault.dto.DataVaultDto;
import ai.afrilab.datavault.universal.PagedApiResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static ai.afrilab.datavault.mpastruct.MapstructMapper.INSTANCE;

public class DataVaultUtils {

  @NotNull
  public static ResponseEntity<PagedApiResponse<DataVaultDto>> getPagedDataVaultDto(int page, Page<DataVault> booksPage, String bucketUrl) {
    List<DataVaultDto> booksDto = booksPage.getContent().stream().map(
        book -> {
          if (book.getAudioSummary() != null) book.setAudioSummary(bucketUrl + book.getAudioSummary());
          if (book.getFileLink() != null) book.setFileLink(bucketUrl + book.getFileLink());
          return INSTANCE.mapDataVaultEntityToDto(book);
        }
    ).toList();

    return ResponseEntity.ok(
        PagedApiResponse.<DataVaultDto>builder()
            .statusCode(HttpStatus.OK.value())
            .status("Success")
            .message("Books Data Fetched Successfully")
            .data(
                booksDto
            )
            .pageNumber(page == 0 ? 1 : page)
            .totalPages(booksPage.getTotalPages())
            .totalData(booksPage.getTotalElements())
            .isLastPage(booksPage.isLast())
            .build()
    );
  }
}
