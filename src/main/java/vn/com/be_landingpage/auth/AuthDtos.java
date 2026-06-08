package vn.com.be_landingpage.auth;

import jakarta.validation.constraints.NotBlank;
import java.time.Instant;

public final class AuthDtos {

    private AuthDtos() {
    }

    public record LoginRequest(
            @NotBlank(message = "username là bắt buộc") String username,
            @NotBlank(message = "password là bắt buộc") String password
    ) {
    }

    public record LoginResponse(
            String accessToken,
            String tokenType,
            Instant expiresAt,
            String username,
            String role
    ) {
    }

    public record UserResponse(
            String username,
            String role
    ) {
    }
}
