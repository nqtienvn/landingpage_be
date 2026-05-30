package vn.com.be_landingpage.cms;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisualNarrativeImageRepository extends JpaRepository<VisualNarrativeImage, Long> {
    List<VisualNarrativeImage> findAllByActiveTrueOrderBySortOrderAscIdAsc();

    List<VisualNarrativeImage> findAllByOrderBySortOrderAscIdAsc();
}
