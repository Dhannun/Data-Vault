package ai.afrilab.datavault.users.dto;

import ai.afrilab.datavault.users.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;

/**
 * DTO for {@link ai.afrilab.datavault.users.User}
 */
public record CreateUserRequest(
    @NotEmpty(message = "Full name is Required")
    @NotBlank(message = "Full name is Required")
    String fullName,
    @NotEmpty(message = "Username is Required")
    @NotBlank(message = "Username is Required")
    String username,
    @NotEmpty(message = "Email is Required")
    @NotBlank(message = "Email is Required")
    String email,
    @NotEmpty(message = "Phone Number is Required")
    @NotBlank(message = "Phone Number is Required")
    String phone,
    @NotEmpty(message = "Password is Required")
    @NotBlank(message = "Password is Required")
    String password,
    Role role
) implements Serializable {
}