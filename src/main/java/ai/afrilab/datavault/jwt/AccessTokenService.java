package ai.afrilab.datavault.jwt;

import ai.afrilab.datavault.exceptions.ResourceNotFoundException;
import ai.afrilab.datavault.users.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccessTokenService {

  private final AccessTokenRepository accessTokenRepository;

  public AccessTokenService(AccessTokenRepository accessTokenRepository) {
    this.accessTokenRepository = accessTokenRepository;
  }

  public void saveToken(AccessToken accessToken) {
    accessTokenRepository.save(accessToken);
  }

  public AccessToken getAccessTokenByToken(String token) {
    return accessTokenRepository.findByToken(token)
        .orElseThrow(() -> new ResourceNotFoundException("Token not found"));
  }

  public void revokeToken(AccessToken accessToken) {
    accessToken.setRevoked(true);
    accessTokenRepository.save(accessToken);
  }

  public Optional<AccessToken> getAccessTokenByUser(User user) {
    return accessTokenRepository.findByUser(user);
  }
}
