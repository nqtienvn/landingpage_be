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
@Table(name = "cms_manifesto_content")
public class ManifestoContent extends BaseEntity {

    @Column(length = 120)
    private String eyebrow = "Thiết kế";

    @Column(nullable = false, length = 180)
    private String title = "Chế tác từ sự tĩnh lặng.";

    @Lob
    @Column(columnDefinition = "TEXT")
    private String body;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String secondaryBody;

    @Column(length = 1000)
    private String imageUrl;

    @Column(length = 300)
    private String imagePublicId;

    @Column(length = 255)
    private String imageAlt;
}
