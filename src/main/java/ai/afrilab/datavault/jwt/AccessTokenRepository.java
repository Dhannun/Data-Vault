package ai.afrilab.datavault.jwt;

import ai.afrilab.datavault.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AccessTokenRepository extends JpaRepository<AccessToken, UUID> {

    Optional<AccessToken> findByToken(String token);

    Optional<AccessToken> findByUser(User user);
}
