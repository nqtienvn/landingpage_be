package vn.com.be_landingpage.review;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.be_landingpage.exception.ResourceNotFoundException;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    @Transactional
    public ReviewDtos.ReviewResponse createReview(Long productId, ReviewDtos.CreateReviewRequest request) {
        Review review = new Review();
        review.setProductId(productId);
        review.setCustomerName(request.customerName().trim());
        review.setRating(request.rating());
        review.setComment(request.comment() != null ? request.comment().trim() : null);
        review.setActive(true);
        return ReviewDtos.ReviewResponse.from(reviewRepository.save(review));
    }

    @Transactional(readOnly = true)
    public ReviewDtos.ProductRatingSummary getProductRatingSummary(Long productId) {
        Double avg = reviewRepository.findAverageRatingByProductId(productId);
        long count = reviewRepository.countByProductIdAndActiveTrue(productId);
        List<ReviewDtos.ReviewResponse> reviews = reviewRepository
                .findByProductIdAndActiveTrueOrderByCreatedAtDesc(productId)
                .stream()
                .map(ReviewDtos.ReviewResponse::from)
                .toList();
        return new ReviewDtos.ProductRatingSummary(
                avg != null ? Math.round(avg * 10.0) / 10.0 : 0.0,
                count,
                reviews
        );
    }

    @Transactional(readOnly = true)
    public List<ReviewDtos.TopReviewResponse> getTopReviews(int limit) {
        return reviewRepository.findByActiveTrueOrderByRatingDescCreatedAtDesc()
                .stream()
                .limit(limit)
                .map(r -> ReviewDtos.TopReviewResponse.from(r, null))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ReviewDtos.ReviewResponse> getAllActiveReviews() {
        return reviewRepository.findByActiveTrueOrderByCreatedAtDesc()
                .stream()
                .map(ReviewDtos.ReviewResponse::from)
                .toList();
    }

    @Transactional
    public ReviewDtos.ReviewResponse toggleActive(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đánh giá id=" + id));
        review.setActive(!review.isActive());
        return ReviewDtos.ReviewResponse.from(reviewRepository.save(review));
    }

    @Transactional
    public void deleteReview(Long id) {
        if (!reviewRepository.existsById(id)) {
            throw new ResourceNotFoundException("Không tìm thấy đánh giá id=" + id);
        }
        reviewRepository.deleteById(id);
    }
}
