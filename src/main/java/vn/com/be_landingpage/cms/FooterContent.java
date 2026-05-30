package vn.com.be_landingpage.cms;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.com.be_landingpage.common.BaseEntity;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "cms_footer_content")
public class FooterContent extends BaseEntity {

    @Column(nullable = false, length = 120)
    private String brandName = "BloomEcho";

    @Lob
    @Column(columnDefinition = "TEXT")
    private String tagline;

    @Column(length = 160)
    private String contactEmail;

    @Column(length = 50)
    private String contactPhone;

    @Column(length = 500)
    private String address;

    @Column(length = 255)
    private String copyrightText;

    @OneToMany(mappedBy = "footer", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sortOrder ASC, id ASC")
    private List<SocialLink> socialLinks = new ArrayList<>();

    @OneToMany(mappedBy = "footer", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sortOrder ASC, id ASC")
    private List<FooterPolicyLink> policyLinks = new ArrayList<>();
}
