package ai.afrilab.datavault.users.dto;

import ai.afrilab.datavault.users.User;
import ai.afrilab.datavault.users.enums.Role;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for {@link User}
 */
public record UserDto(
    UUID id,
    String fullName,
    String username,
    String email,
    String phone,
    Role role,
    LocalDateTime createdDate,
    AuditUserDto createdBy
) implements Serializable {
}