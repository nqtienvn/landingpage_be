package vn.com.be_landingpage.catalog;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollectionChapterRepository extends JpaRepository<CollectionChapter, Long> {
    boolean existsBySlug(String slug);

    boolean existsBySlugAndIdNot(String slug, Long id);

    Optional<CollectionChapter> findBySlug(String slug);

    List<CollectionChapter> findAllByActiveTrueOrderBySortOrderAscIdAsc();

    List<CollectionChapter> findAllByOrderBySortOrderAscIdAsc();
}
