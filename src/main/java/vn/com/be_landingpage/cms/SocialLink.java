package vn.com.be_landingpage.cms;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.com.be_landingpage.common.BaseEntity;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "cms_social_links")
public class SocialLink extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private FooterContent footer;

    @Column(nullable = false, length = 80)
    private String platform;

    @Column(nullable = false, length = 500)
    private String url;

    @Column(length = 80)
    private String icon;

    @Column(nullable = false)
    private Integer sortOrder = 0;

    @Column(nullable = false)
    private boolean active = true;
}
