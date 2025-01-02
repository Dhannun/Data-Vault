package ai.afrilab.datavault.users.dto;

public record LoginResponse(
    String accessToken,
    long accessTokenExpiresInMinutes,
    String refreshToken,
    long refreshTokenExpiresInMinutes,
    UserDto user
) {
}
