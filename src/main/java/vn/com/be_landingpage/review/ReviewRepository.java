package vn.com.be_landingpage.review;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByProductIdAndActiveTrueOrderByCreatedAtDesc(Long productId);

    List<Review> findByActiveTrueOrderByRatingDescCreatedAtDesc();

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.productId = :productId AND r.active = true")
    Double findAverageRatingByProductId(@Param("productId") Long productId);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.productId = :productId AND r.active = true")
    long countByProductIdAndActiveTrue(@Param("productId") Long productId);

    List<Review> findByActiveTrueOrderByCreatedAtDesc();
}
