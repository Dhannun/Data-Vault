package ai.afrilab.datavault.users;

import ai.afrilab.datavault.exceptions.InvalidResourceException;
import ai.afrilab.datavault.exceptions.ResourceExistsException;
import ai.afrilab.datavault.exceptions.ResourceNotFoundException;
import ai.afrilab.datavault.universal.ApiResponse;
import ai.afrilab.datavault.universal.PagedApiResponse;
import ai.afrilab.datavault.users.dto.CreateUserRequest;
import ai.afrilab.datavault.users.dto.UserDto;
import ai.afrilab.datavault.users.enums.Role;
import ai.afrilab.datavault.utils.PaginationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static ai.afrilab.datavault.mpastruct.MapstructMapper.INSTANCE;
import static ai.afrilab.datavault.utils.UsersUtils.getPagedUsersDto;

@Service
public class UserService {

  private static final Logger log = LoggerFactory.getLogger(UserService.class);
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public User getUserByUsername(String username) {
    return userRepository.findByUsernameIgnoreCase(username)
        .orElseThrow(() -> new ResourceNotFoundException("User not found"));
  }

  public ResponseEntity<ApiResponse<UserDto>> createUser(CreateUserRequest request) {

    if (request.role() == Role.SUPER_ADMIN)
      throw new InvalidResourceException("Super Admin Role is not allowed to be created");

    boolean existsByUsernameOrEmail = userRepository.existsByUsernameOrEmailAllIgnoreCase(request.username(), request.email());

    if (existsByUsernameOrEmail)
      throw new ResourceExistsException("User already with email [ %s ] or username [ %s ] exists".formatted(request.email(), request.username()));

    User user = User.builder()
        .username(request.username())
        .email(request.email())
        .password(passwordEncoder.encode(request.password()))
        .fullName(request.fullName())
        .phone(request.phone())
        .role(request.role())
        .enabled(true)
        .locked(false)
        .build();

    User savedUser = userRepository.save(user);
    UserDto userDto = INSTANCE.mapUserEntityToDto(savedUser);

    // TODO: How to tell them their password.

    return ResponseEntity.ok(
        ApiResponse.<UserDto>builder()
            .statusCode(HttpStatus.CREATED.value())
            .status(HttpStatus.CREATED.getReasonPhrase())
            .message("User Created Successfully")
            .data(userDto)
            .build()
    );
  }

  public ResponseEntity<PagedApiResponse<UserDto>> getAllUsers(int page, int size) {

    log.info("Fetching all users from the database");

    Pageable pageable = PaginationUtils.getPageable(page, size);

    Page<User> usersPage = userRepository.findAllOrderByCreatedDate(pageable);

    log.info("Found [{}] users", usersPage.getTotalElements());

    return getPagedUsersDto(page, usersPage);
  }

  public ResponseEntity<ApiResponse<UserDto>> getMyProfile(String username) {
    log.info("Fetching user profile for [{}]", username);

    User user = getUserByUsername(username);

    log.info("User profile retrieved successfully for [{}]", username);

    UserDto userDto = INSTANCE.mapUserEntityToDto(user);

    return ResponseEntity.ok(
        ApiResponse.<UserDto>builder()
            .statusCode(HttpStatus.OK.value())
            .status(HttpStatus.OK.getReasonPhrase())
            .message("User Profile Retrieved Successfully")
            .data(userDto)
            .build()
    );
  }
}
