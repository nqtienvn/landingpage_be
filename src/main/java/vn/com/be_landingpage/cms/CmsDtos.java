package vn.com.be_landingpage.cms;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.List;

public final class CmsDtos {

    private CmsDtos() {
    }

    public record HeroRequest(
            @NotBlank(message = "title là bắt buộc") String title,
            String subtitle,
            String ctaText,
            String ctaHref,
            String bannerImageUrl,
            String bannerImagePublicId,
            String imageAlt
    ) {
    }

    public record HeroResponse(
            Long id,
            String title,
            String subtitle,
            String ctaText,
            String ctaHref,
            String bannerImageUrl,
            String bannerImagePublicId,
            String imageAlt,
            Instant updatedAt
    ) {
        public static HeroResponse from(HeroContent content) {
            return new HeroResponse(
                    content.getId(),
                    content.getTitle(),
                    content.getSubtitle(),
                    content.getCtaText(),
                    content.getCtaHref(),
                    content.getBannerImageUrl(),
                    content.getBannerImagePublicId(),
                    content.getImageAlt(),
                    content.getUpdatedAt()
            );
        }
    }

    public record ManifestoRequest(
            String eyebrow,
            @NotBlank(message = "title là bắt buộc") String title,
            String body,
            String secondaryBody,
            String imageUrl,
            String imagePublicId,
            String imageAlt
    ) {
    }

    public record ManifestoResponse(
            Long id,
            String eyebrow,
            String title,
            String body,
            String secondaryBody,
            String imageUrl,
            String imagePublicId,
            String imageAlt,
            Instant updatedAt
    ) {
        public static ManifestoResponse from(ManifestoContent content) {
            return new ManifestoResponse(
                    content.getId(),
                    content.getEyebrow(),
                    content.getTitle(),
                    content.getBody(),
                    content.getSecondaryBody(),
                    content.getImageUrl(),
                    content.getImagePublicId(),
                    content.getImageAlt(),
                    content.getUpdatedAt()
            );
        }
    }

    public record VisualNarrativeRequest(
            @NotBlank(message = "title là bắt buộc") String title,
            String caption,
            String description,
            @NotBlank(message = "imageUrl là bắt buộc") String imageUrl,
            String imagePublicId,
            String imageAlt,
            Integer sortOrder,
            Boolean active
    ) {
    }

    public record VisualNarrativeResponse(
            Long id,
            String title,
            String caption,
            String description,
            String imageUrl,
            String imagePublicId,
            String imageAlt,
            Integer sortOrder,
            Boolean active,
            Instant updatedAt
    ) {
        public static VisualNarrativeResponse from(VisualNarrativeImage image) {
            return new VisualNarrativeResponse(
                    image.getId(),
                    image.getTitle(),
                    image.getCaption(),
                    image.getDescription(),
                    image.getImageUrl(),
                    image.getImagePublicId(),
                    image.getImageAlt(),
                    image.getSortOrder(),
                    image.isActive(),
                    image.getUpdatedAt()
            );
        }
    }

    public record CraftsmanshipItemRequest(
            @NotBlank(message = "title là bắt buộc") String title,
            String description,
            String imageUrl,
            String imagePublicId,
            Integer sortOrder,
            Boolean active
    ) {
    }

    public record CraftsmanshipRequest(
            @NotBlank(message = "title là bắt buộc") String title,
            String description,
            String primaryImageUrl,
            String primaryImagePublicId,
            String secondaryImageUrl,
            String secondaryImagePublicId,
            @Valid List<CraftsmanshipItemRequest> items
    ) {
    }

    public record CraftsmanshipItemResponse(
            Long id,
            String title,
            String description,
            String imageUrl,
            String imagePublicId,
            Integer sortOrder,
            Boolean active
    ) {
        public static CraftsmanshipItemResponse from(CraftsmanshipItem item) {
            return new CraftsmanshipItemResponse(
                    item.getId(),
                    item.getTitle(),
                    item.getDescription(),
                    item.getImageUrl(),
                    item.getImagePublicId(),
                    item.getSortOrder(),
                    item.isActive()
            );
        }
    }

    public record CraftsmanshipResponse(
            Long id,
            String title,
            String description,
            String primaryImageUrl,
            String primaryImagePublicId,
            String secondaryImageUrl,
            String secondaryImagePublicId,
            List<CraftsmanshipItemResponse> items,
            Instant updatedAt
    ) {
        public static CraftsmanshipResponse from(CraftsmanshipContent content, boolean activeOnly) {
            List<CraftsmanshipItemResponse> itemResponses = content.getItems().stream()
                    .filter(item -> !activeOnly || item.isActive())
                    .map(CraftsmanshipItemResponse::from)
                    .toList();
            return new CraftsmanshipResponse(
                    content.getId(),
                    content.getTitle(),
                    content.getDescription(),
                    content.getPrimaryImageUrl(),
                    content.getPrimaryImagePublicId(),
                    content.getSecondaryImageUrl(),
                    content.getSecondaryImagePublicId(),
                    itemResponses,
                    content.getUpdatedAt()
            );
        }
    }

    public record SocialLinkRequest(
            @NotBlank(message = "platform là bắt buộc") String platform,
            @NotBlank(message = "url là bắt buộc") String url,
            String icon,
            Integer sortOrder,
            Boolean active
    ) {
    }

    public record FooterPolicyLinkRequest(
            @NotBlank(message = "label là bắt buộc") String label,
            @NotBlank(message = "url là bắt buộc") String url,
            Integer sortOrder,
            Boolean active
    ) {
    }

    public record FooterRequest(
            @NotBlank(message = "brandName là bắt buộc") String brandName,
            String tagline,
            String contactEmail,
            String contactPhone,
            String address,
            String copyrightText,
            @Valid List<SocialLinkRequest> socialLinks,
            @Valid List<FooterPolicyLinkRequest> policyLinks
    ) {
    }

    public record SocialLinkResponse(
            Long id,
            String platform,
            String url,
            String icon,
            Integer sortOrder,
            Boolean active
    ) {
        public static SocialLinkResponse from(SocialLink link) {
            return new SocialLinkResponse(
                    link.getId(),
                    link.getPlatform(),
                    link.getUrl(),
                    link.getIcon(),
                    link.getSortOrder(),
                    link.isActive()
            );
        }
    }

    public record FooterPolicyLinkResponse(
            Long id,
            String label,
            String url,
            Integer sortOrder,
            Boolean active
    ) {
        public static FooterPolicyLinkResponse from(FooterPolicyLink link) {
            return new FooterPolicyLinkResponse(
                    link.getId(),
                    link.getLabel(),
                    link.getUrl(),
                    link.getSortOrder(),
                    link.isActive()
            );
        }
    }

    public record FooterResponse(
            Long id,
            String brandName,
            String tagline,
            String contactEmail,
            String contactPhone,
            String address,
            String copyrightText,
            List<SocialLinkResponse> socialLinks,
            List<FooterPolicyLinkResponse> policyLinks,
            Instant updatedAt
    ) {
        public static FooterResponse from(FooterContent content, boolean activeOnly) {
            List<SocialLinkResponse> socials = content.getSocialLinks().stream()
                    .filter(link -> !activeOnly || link.isActive())
                    .map(SocialLinkResponse::from)
                    .toList();
            List<FooterPolicyLinkResponse> policies = content.getPolicyLinks().stream()
                    .filter(link -> !activeOnly || link.isActive())
                    .map(FooterPolicyLinkResponse::from)
                    .toList();
            return new FooterResponse(
                    content.getId(),
                    content.getBrandName(),
                    content.getTagline(),
                    content.getContactEmail(),
                    content.getContactPhone(),
                    content.getAddress(),
                    content.getCopyrightText(),
                    socials,
                    policies,
                    content.getUpdatedAt()
            );
        }
    }
}
