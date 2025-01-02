package ai.afrilab.datavault.utils;

import ai.afrilab.datavault.universal.PagedApiResponse;
import ai.afrilab.datavault.users.User;
import ai.afrilab.datavault.users.dto.UserDto;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static ai.afrilab.datavault.mpastruct.MapstructMapper.INSTANCE;

public class UsersUtils {

  @NotNull
  public static ResponseEntity<PagedApiResponse<UserDto>> getPagedUsersDto(int page, Page<User> usersPage) {
    List<UserDto> usersDto = usersPage.getContent().stream().map(INSTANCE::mapUserEntityToDto).toList();

    return ResponseEntity.ok(
        PagedApiResponse.<UserDto>builder()
            .statusCode(HttpStatus.OK.value())
            .status("Success")
            .message("Books Data Fetched Successfully")
            .data(
                usersDto
            )
            .pageNumber(page == 0 ? 1 : page)
            .totalPages(usersPage.getTotalPages())
            .totalData(usersPage.getTotalElements())
            .isLastPage(usersPage.isLast())
            .build()
    );
  }
}
