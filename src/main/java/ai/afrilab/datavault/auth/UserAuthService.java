package ai.afrilab.datavault.auth;

import ai.afrilab.datavault.config.security.JwtService;
import ai.afrilab.datavault.exceptions.InvalidResourceException;
import ai.afrilab.datavault.jwt.AccessToken;
import ai.afrilab.datavault.jwt.AccessTokenService;
import ai.afrilab.datavault.universal.ApiResponse;
import ai.afrilab.datavault.users.User;
import ai.afrilab.datavault.users.UserService;
import ai.afrilab.datavault.users.dto.LoginRequest;
import ai.afrilab.datavault.users.dto.LoginResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static ai.afrilab.datavault.mpastruct.MapstructMapper.INSTANCE;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
public class UserAuthService {

  private static final Logger log = LoggerFactory.getLogger(UserAuthService.class);
  private final AuthenticationManager authenticationManager;
  private final UserService userService;
  private final JwtService jwtService;
  private final AccessTokenService accessTokenService;

  public UserAuthService(AuthenticationManager authenticationManager, UserService userService, JwtService jwtService, AccessTokenService accessTokenService) {
    this.authenticationManager = authenticationManager;
    this.userService = userService;
    this.jwtService = jwtService;
    this.accessTokenService = accessTokenService;
  }

  public ResponseEntity<ApiResponse<LoginResponse>> login(LoginRequest request) {
    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              request.username(),
              request.password()
          )
      );

      var user = userService.getUserByUsername(request.username());

      var jwtToken = jwtService.generateToken(user);
      var refreshToken = jwtService.generateRefreshToken(user);

      var payload = new LoginResponse(
          jwtToken,
          jwtService.getJwtExpirationInMinutes(),
          refreshToken,
          jwtService.getRefreshExpirationInMinuted(),
          INSTANCE.mapUserEntityToDto(user)
      );

      saveUserToken(user, jwtToken);

      return ResponseEntity.ok(
          ApiResponse.<LoginResponse>builder()
              .statusCode(HttpStatus.OK.value())
              .status("Success")
              .message("Login Successfully")
              .data(
                  payload
              )
              .build()
      );
    } catch (Exception e) {
      log.error(e.toString());
      throw new InvalidResourceException("Invalid Username or Password");
    }
  }

  /**
   * Request for new access JWT Token using the provided Refresh Token issued at last Login
   */
  public ResponseEntity<ApiResponse<LoginResponse>> refreshToken(
      HttpServletRequest request
  ) {
    final String authHeader = request.getHeader(AUTHORIZATION);
    final String refreshToken;
    final String userEmail;

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      throw new InvalidResourceException("No or Invalid refresh token");
    }

    refreshToken = authHeader.substring(7);
    userEmail = jwtService.extractUsername(refreshToken);

    if (userEmail != null) {

      var user = userService.getUserByUsername(userEmail);

      if (jwtService.isTokenValid(refreshToken, user)) {
        var newAccessToken = jwtService.generateToken(user);

        saveUserToken(user, newAccessToken);

        var payload = new LoginResponse(
            newAccessToken,
            jwtService.getJwtExpirationInMinutes(),
            refreshToken,
            jwtService.getRefreshExpirationInMinuted(),
            INSTANCE.mapUserEntityToDto(user)
        );

        return ResponseEntity.ok(
            ApiResponse.<LoginResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .status("Success")
                .message("Token Refreshed Successfully")
                .data(
                    payload
                )
                .build()
        );
      }
    }
    throw new IllegalStateException("Invalid refresh token");
  }

  /**
   * Saving the generated JWT Token when a user logged in
   **/
  private void saveUserToken(User user, String jwtToken) {
    Optional<AccessToken> accessTokenOptional = accessTokenService.getAccessTokenByUser(user);

    AccessToken token;

    if (accessTokenOptional.isPresent()) {
      token = accessTokenOptional.get();
      token.setToken(jwtToken);
    } else {
      token = AccessToken.builder()
          .user(user)
          .token(jwtToken)
          .revoked(false)
          .build();
    }

    accessTokenService.saveToken(token);
  }
}
