package vn.com.be_landingpage.social;

import jakarta.validation.constraints.NotBlank;
import java.time.Instant;

public final class SocialConfigDtos {

    private SocialConfigDtos() {
    }

    public record SocialConfigRequest(
            @NotBlank(message = "platform là bắt buộc") String platform,
            String url,
            String imageUrl,
            String pageTitle,
            String description,
            String followUrl,
            Integer sortOrder,
            Boolean active
    ) {
    }

    public record SocialConfigResponse(
            Long id,
            String platform,
            String url,
            String imageUrl,
            String pageTitle,
            String description,
            String followUrl,
            Integer sortOrder,
            boolean active,
            Instant createdAt
    ) {
        public static SocialConfigResponse from(SocialConfig config) {
            return new SocialConfigResponse(
                    config.getId(),
                    config.getPlatform(),
                    config.getUrl(),
                    config.getImageUrl(),
                    config.getPageTitle(),
                    config.getDescription(),
                    config.getFollowUrl(),
                    config.getSortOrder(),
                    config.isActive(),
                    config.getCreatedAt()
            );
        }
    }
}
