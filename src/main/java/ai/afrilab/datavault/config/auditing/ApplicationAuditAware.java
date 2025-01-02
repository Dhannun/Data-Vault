package ai.afrilab.datavault.config.auditing;

import ai.afrilab.datavault.users.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class ApplicationAuditAware implements AuditorAware<User> {

  @Override
  public Optional<User> getCurrentAuditor() {
    Authentication authentication =
        SecurityContextHolder
            .getContext()
            .getAuthentication();

    if (
        authentication == null ||
            !authentication.isAuthenticated() ||
            authentication instanceof AnonymousAuthenticationToken
    ) {
      return Optional.empty();
    }

    User userPrincipal = (User) authentication.getPrincipal();

    return Optional.ofNullable(userPrincipal);
  }
}
