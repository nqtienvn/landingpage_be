package vn.com.be_landingpage.social;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SocialConfigRepository extends JpaRepository<SocialConfig, Long> {
    List<SocialConfig> findAllByOrderBySortOrderAscIdAsc();
    List<SocialConfig> findAllByActiveTrueOrderBySortOrderAscIdAsc();
}
