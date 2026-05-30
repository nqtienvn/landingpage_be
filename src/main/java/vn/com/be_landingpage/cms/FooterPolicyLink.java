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
@Table(name = "cms_footer_policy_links")
public class FooterPolicyLink extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private FooterContent footer;

    @Column(nullable = false, length = 120)
    private String label;

    @Column(nullable = false, length = 500)
    private String url;

    @Column(nullable = false)
    private Integer sortOrder = 0;

    @Column(nullable = false)
    private boolean active = true;
}
