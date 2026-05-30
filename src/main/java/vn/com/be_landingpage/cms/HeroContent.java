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
@Table(name = "cms_hero_content")
public class HeroContent extends BaseEntity {

    @Column(nullable = false, length = 180)
    private String title = "World Cup 2028.";

    @Lob
    @Column(columnDefinition = "TEXT")
    private String subtitle;

    @Column(length = 80)
    private String ctaText = "Khám phá ngay";

    @Column(length = 255)
    private String ctaHref = "#brand-story";

    @Column(length = 1000)
    private String bannerImageUrl;

    @Column(length = 300)
    private String bannerImagePublicId;

    @Column(length = 255)
    private String imageAlt;
}
