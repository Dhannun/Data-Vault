package ai.afrilab.datavault.admin;

import ai.afrilab.datavault.datavault.DataVaultService;
import ai.afrilab.datavault.datavault.dto.DataVaultDto;
import ai.afrilab.datavault.datavault.enums.Status;
import ai.afrilab.datavault.universal.ApiResponse;
import ai.afrilab.datavault.universal.PagedApiResponse;
import ai.afrilab.datavault.users.UserService;
import ai.afrilab.datavault.users.dto.CreateUserRequest;
import ai.afrilab.datavault.users.dto.UserDto;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admins")
@Tag(name = "Admin Secured Controller")
@SecurityRequirement(name = "BearerAuth")
public class AdminController {

  private final UserService userService;
  private final DataVaultService dataVaultService;

  public AdminController(UserService userService, DataVaultService dataVaultService) {
    this.userService = userService;
    this.dataVaultService = dataVaultService;
  }

  @PostMapping("/create-user")
  public ResponseEntity<ApiResponse<UserDto>> createUser(
      @Valid @RequestBody CreateUserRequest request
  ) {
    return userService.createUser(request);
  }

  @GetMapping("/users")
  public ResponseEntity<PagedApiResponse<UserDto>> getAllUsers(
      @RequestParam(required = false, defaultValue = "1") int page,
      @RequestParam(required = false, defaultValue = "10") int size
  ) {
    return userService.getAllUsers(page, size);
  }

  @GetMapping("/data-vault")
  public ResponseEntity<PagedApiResponse<DataVaultDto>> getAllBooks(
      @RequestParam(required = false, defaultValue = "1") int page,
      @RequestParam(required = false, defaultValue = "10") int size
  ) {
    return dataVaultService.getAllDataVaults(page, size);
  }

  @GetMapping("/data-vault/{status}")
  public ResponseEntity<PagedApiResponse<DataVaultDto>> getAllBooksByStatus(
      @PathVariable(name = "status") Status status,
      @RequestParam(required = false, defaultValue = "1") int page,
      @RequestParam(required = false, defaultValue = "10") int size
  ) {
    return dataVaultService.getAllDataVaultsByStatus(status, page, size);
  }

  @GetMapping("/data-vault/source/{name}")
  public ResponseEntity<PagedApiResponse<DataVaultDto>> getAllBooksByStatus(
      @PathVariable(name = "name") String name,
      @RequestParam(required = false, defaultValue = "1") int page,
      @RequestParam(required = false, defaultValue = "10") int size
  ) {
    return dataVaultService.getAllDataVaultsBySource(name, page, size);
  }


}
