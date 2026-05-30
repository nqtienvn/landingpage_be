package vn.com.be_landingpage.catalog;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsBySlug(String slug);

    boolean existsBySlugAndIdNot(String slug, Long id);

    Optional<Product> findBySlug(String slug);

    List<Product> findAllByActiveTrueOrderBySortOrderAscIdAsc();

    List<Product> findAllByOrderBySortOrderAscIdAsc();
}
