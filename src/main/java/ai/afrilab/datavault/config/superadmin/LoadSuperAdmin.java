package ai.afrilab.datavault.config.superadmin;

import ai.afrilab.datavault.users.User;
import ai.afrilab.datavault.users.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

import static ai.afrilab.datavault.users.enums.Role.SUPER_ADMIN;


@Component
public class LoadSuperAdmin implements CommandLineRunner {

  private static final Logger log = LoggerFactory.getLogger(LoadSuperAdmin.class);
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public LoadSuperAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public void run(String... args) {
    Optional<User> superAdminOptional = userRepository.getSuperAdmin();

    if (superAdminOptional.isEmpty()) {
      log.info("No Super Admin Found, Creating One...");
      final UUID id = UUID.randomUUID();
      User superAdmin = User.builder()
          .id(id)
          .email("superadmin@afrila.ai")
          .phone("0000000000")
          .username("superadmin")
          .password(passwordEncoder.encode("superadmin"))
          .role(SUPER_ADMIN)
          .fullName("Super Admin")
          .enabled(true)
          .locked(false)
          .build();

      superAdmin.setCreatedBy(superAdmin);

      userRepository.save(superAdmin);

      log.info("Super Admin Created Successfully ✨");
    }
    log.info("API Ready to Accept Requests 🚀");
  }
}
