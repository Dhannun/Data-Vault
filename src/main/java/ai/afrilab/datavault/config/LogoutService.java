package ai.afrilab.datavault.config;


import ai.afrilab.datavault.exceptions.InvalidResourceException;
import ai.afrilab.datavault.exceptions.ResourceNotFoundException;
import ai.afrilab.datavault.jwt.AccessTokenRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Service
public class LogoutService implements LogoutHandler {

  private final AccessTokenRepository accessTokenRepository;

  public LogoutService(AccessTokenRepository accessTokenRepository) {
    this.accessTokenRepository = accessTokenRepository;
  }

  @Override
  public void logout(
      HttpServletRequest request,
      HttpServletResponse response,
      Authentication authentication
  ) {
    final String authHeader = request.getHeader(AUTHORIZATION);
    final String jwt;

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      throw new InvalidResourceException("Access Token Invalid");
    }

    jwt = authHeader.substring(7);

    var storedToken = accessTokenRepository.findByToken(jwt).orElse(null);

    if (storedToken != null) {
      storedToken.setRevoked(true);
      accessTokenRepository.save(storedToken);
      response.setStatus(OK.value());
      response.setContentType(APPLICATION_JSON_VALUE);
      try {
        new ObjectMapper().writeValue(response.getOutputStream(), Map.of("message", "Logged out Successfully"));
      } catch (IOException e) {
//        throw new (e.getMessage());
      }
    } else {
      throw new ResourceNotFoundException("Invalid Access Token");
    }
  }
}
