package vn.com.be_landingpage.review;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/products")
@RequiredArgsConstructor
public class PublicReviewController {

    private final ReviewService reviewService;

    @GetMapping("/{productId}/reviews")
    public ReviewDtos.ProductRatingSummary getReviews(@PathVariable Long productId) {
        return reviewService.getProductRatingSummary(productId);
    }

    @PostMapping("/{productId}/reviews")
    public ReviewDtos.ReviewResponse createReview(
            @PathVariable Long productId,
            @Valid @RequestBody ReviewDtos.CreateReviewRequest request
    ) {
        return reviewService.createReview(productId, request);
    }
}
