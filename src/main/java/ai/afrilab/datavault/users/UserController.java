package ai.afrilab.datavault.users;

import ai.afrilab.datavault.universal.ApiResponse;
import ai.afrilab.datavault.users.dto.UserDto;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Users Secured Controller")
@SecurityRequirement(name = "BearerAuth")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/me")
  public ResponseEntity<ApiResponse<UserDto>> getMyProfile() {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    return userService.getMyProfile(username);
  }
}
