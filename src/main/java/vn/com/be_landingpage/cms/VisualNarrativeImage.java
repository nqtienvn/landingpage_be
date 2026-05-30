package vn.com.be_landingpage.cms;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.com.be_landingpage.common.BaseEntity;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "cms_visual_narrative_images")
public class VisualNarrativeImage extends BaseEntity {

    @Column(nullable = false, length = 160)
    private String title;

    @Column(length = 220)
    private String caption;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, length = 1000)
    private String imageUrl;

    @Column(length = 300)
    private String imagePublicId;

    @Column(length = 255)
    private String imageAlt;

    @Column(nullable = false)
    private Integer sortOrder = 0;

    @Column(nullable = false)
    private boolean active = true;
}
