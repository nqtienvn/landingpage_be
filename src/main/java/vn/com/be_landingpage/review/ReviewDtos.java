package vn.com.be_landingpage.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;

public final class ReviewDtos {

    private ReviewDtos() {
    }

    public record CreateReviewRequest(
            @NotNull(message = "rating là bắt buộc") @Min(1) @Max(5) Integer rating,
            @NotBlank(message = "customerName là bắt buộc") String customerName,
            String comment
    ) {
    }

    public record ReviewResponse(
            Long id,
            Long productId,
            String customerName,
            Integer rating,
            String comment,
            boolean active,
            Instant createdAt
    ) {
        public static ReviewResponse from(Review review) {
            return new ReviewResponse(
                    review.getId(),
                    review.getProductId(),
                    review.getCustomerName(),
                    review.getRating(),
                    review.getComment(),
                    review.isActive(),
                    review.getCreatedAt()
            );
        }
    }

    public record ProductRatingSummary(
            Double averageRating,
            long reviewCount,
            List<ReviewResponse> reviews
    ) {
    }

    public record TopReviewResponse(
            String customerName,
            Integer rating,
            String comment,
            String productName,
            Instant createdAt
    ) {
        public static TopReviewResponse from(Review review, String productName) {
            return new TopReviewResponse(
                    review.getCustomerName(),
                    review.getRating(),
                    review.getComment(),
                    productName,
                    review.getCreatedAt()
            );
        }
    }
}
