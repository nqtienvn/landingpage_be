package vn.com.be_landingpage.media;

import java.time.Instant;

public final class MediaDtos {

    private MediaDtos() {
    }

    public record MediaAssetResponse(
            Long id,
            String publicId,
            String url,
            String secureUrl,
            String folder,
            String originalFilename,
            String format,
            String resourceType,
            Long bytes,
            Integer width,
            Integer height,
            String altText,
            Instant createdAt
    ) {
        public static MediaAssetResponse from(MediaAsset asset) {
            return new MediaAssetResponse(
                    asset.getId(),
                    asset.getPublicId(),
                    asset.getUrl(),
                    asset.getSecureUrl(),
                    asset.getFolder(),
                    asset.getOriginalFilename(),
                    asset.getFormat(),
                    asset.getResourceType(),
                    asset.getBytes(),
                    asset.getWidth(),
                    asset.getHeight(),
                    asset.getAltText(),
                    asset.getCreatedAt()
            );
        }
    }
}
