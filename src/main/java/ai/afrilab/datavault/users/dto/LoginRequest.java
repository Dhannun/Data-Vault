package ai.afrilab.datavault.users.dto;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public record LoginRequest(
    @NotBlank(message = "Username is mandatory") String username,
    @NotBlank(message = "Password is mandatory") String password
) implements Serializable {
}
