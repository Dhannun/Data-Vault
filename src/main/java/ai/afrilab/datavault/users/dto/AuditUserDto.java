package ai.afrilab.datavault.users.dto;

import ai.afrilab.datavault.users.User;
import ai.afrilab.datavault.users.enums.Role;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link User}
 */
public record AuditUserDto(
    UUID id,
    String fullName,
    String username,
    String email,
    String phone,
    Role role
) implements Serializable {
}