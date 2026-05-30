package vn.com.be_landingpage.catalog;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public final class CatalogDtos {

    private CatalogDtos() {
    }

    public record ChapterRequest(
            String slug,
            @NotBlank(message = "title là bắt buộc") String title,
            String subtitle,
            String description,
            String coverImageUrl,
            String coverImagePublicId,
            Integer sortOrder,
            Boolean active
    ) {
    }

    public record ChapterResponse(
            Long id,
            String slug,
            String title,
            String subtitle,
            String description,
            String coverImageUrl,
            String coverImagePublicId,
            Integer sortOrder,
            Boolean active,
            Instant updatedAt
    ) {
        public static ChapterResponse from(CollectionChapter chapter) {
            if (chapter == null) {
                return null;
            }
            return new ChapterResponse(
                    chapter.getId(),
                    chapter.getSlug(),
                    chapter.getTitle(),
                    chapter.getSubtitle(),
                    chapter.getDescription(),
                    chapter.getCoverImageUrl(),
                    chapter.getCoverImagePublicId(),
                    chapter.getSortOrder(),
                    chapter.isActive(),
                    chapter.getUpdatedAt()
            );
        }
    }

    public record ProductImageRequest(
            @NotBlank(message = "imageUrl là bắt buộc") String imageUrl,
            String imagePublicId,
            String altText,
            Integer sortOrder,
            Boolean primaryImage
    ) {
    }

    public record ProductRequest(
            String slug,
            @NotBlank(message = "name là bắt buộc") String name,
            String tag,
            @NotNull(message = "price là bắt buộc")
            @PositiveOrZero(message = "price phải lớn hơn hoặc bằng 0")
            BigDecimal price,
            String description,
            String material,
            List<String> sizes,
            Long chapterId,
            Integer sortOrder,
            Boolean active,
            @Valid List<ProductImageRequest> images
    ) {
    }

    public record ProductImageResponse(
            Long id,
            String imageUrl,
            String imagePublicId,
            String altText,
            Integer sortOrder,
            Boolean primaryImage
    ) {
        public static ProductImageResponse from(ProductImage image) {
            return new ProductImageResponse(
                    image.getId(),
                    image.getImageUrl(),
                    image.getImagePublicId(),
                    image.getAltText(),
                    image.getSortOrder(),
                    image.isPrimaryImage()
            );
        }
    }

    public record ProductResponse(
            Long id,
            String slug,
            String name,
            String tag,
            BigDecimal price,
            String description,
            String material,
            List<String> sizes,
            ChapterResponse chapter,
            List<ProductImageResponse> images,
            String primaryImageUrl,
            Integer sortOrder,
            Boolean active,
            Instant createdAt,
            Instant updatedAt
    ) {
        public static ProductResponse from(Product product) {
            List<ProductImageResponse> imageResponses = product.getImages().stream()
                    .map(ProductImageResponse::from)
                    .toList();
            String primaryImageUrl = product.getImages().stream()
                    .filter(ProductImage::isPrimaryImage)
                    .findFirst()
                    .or(() -> product.getImages().stream().findFirst())
                    .map(ProductImage::getImageUrl)
                    .orElse(null);
            return new ProductResponse(
                    product.getId(),
                    product.getSlug(),
                    product.getName(),
                    product.getTag(),
                    product.getPrice(),
                    product.getDescription(),
                    product.getMaterial(),
                    List.copyOf(product.getSizes()),
                    ChapterResponse.from(product.getChapter()),
                    imageResponses,
                    primaryImageUrl,
                    product.getSortOrder(),
                    product.isActive(),
                    product.getCreatedAt(),
                    product.getUpdatedAt()
            );
        }
    }
}
