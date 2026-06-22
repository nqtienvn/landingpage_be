package vn.com.be_landingpage.social;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.com.be_landingpage.common.BaseEntity;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "social_configs")
public class SocialConfig extends BaseEntity {

    @Column(nullable = false, length = 50)
    private String platform; // instagram, facebook, tiktok, youtube...

    @Column(length = 500)
    private String url;

    @Column(length = 500)
    private String imageUrl;

    @Column(length = 200)
    private String pageTitle;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 500)
    private String followUrl;

    @Column(nullable = false)
    private Integer sortOrder = 0;

    @Column(nullable = false)
    private boolean active = true;
}
