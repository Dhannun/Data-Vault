package ai.afrilab.datavault.config.security;


import ai.afrilab.datavault.exceptions.ResourceNotFoundException;
import ai.afrilab.datavault.users.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  private final UserRepository userRepository;

  public UserDetailsServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
    return userRepository.findByUsernameIgnoreCase(userEmail)
        .orElseThrow(() -> new ResourceNotFoundException("Invalid Login Credentials"));
  }
}
