package ai.afrilab.datavault.config.auditing;

import ai.afrilab.datavault.users.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

@Configuration
public class AuditAwareBean {
  @Bean
  public AuditorAware<User> auditorAware() {
    return new ApplicationAuditAware();
  }
}
