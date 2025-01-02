package ai.afrilab.datavault.auth;

import ai.afrilab.datavault.universal.ApiResponse;
import ai.afrilab.datavault.users.dto.LoginRequest;
import ai.afrilab.datavault.users.dto.LoginResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/users")
@Tag(name = "Authentication Controller")
public class UserAuthController {

  private final UserAuthService userAuthService;

  public UserAuthController(UserAuthService userAuthService) {
    this.userAuthService = userAuthService;
  }

  @PostMapping("login")
  public ResponseEntity<ApiResponse<LoginResponse>> login(
      @Valid @RequestBody LoginRequest request
  ) {
    return userAuthService.login(request);
  }

  @PostMapping("refresh-token")
  public ResponseEntity<ApiResponse<LoginResponse>> refreshToken(HttpServletRequest request) {
    return userAuthService.refreshToken(request);
  }

}
