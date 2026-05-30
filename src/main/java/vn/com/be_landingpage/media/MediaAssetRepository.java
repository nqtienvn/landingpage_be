package vn.com.be_landingpage.media;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MediaAssetRepository extends JpaRepository<MediaAsset, Long> {
    List<MediaAsset> findAllByFolderContainingIgnoreCaseOrderByCreatedAtDesc(String folder);
}
