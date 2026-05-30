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
@Table(name = "cms_craftsmanship_content")
public class CraftsmanshipContent extends BaseEntity {

    @Column(nullable = false, length = 180)
    private String title = "May đo cho tương lai.";

    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 1000)
    private String primaryImageUrl;

    @Column(length = 300)
    private String primaryImagePublicId;

    @Column(length = 1000)
    private String secondaryImageUrl;

    @Column(length = 300)
    private String secondaryImagePublicId;

    @OneToMany(mappedBy = "content", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sortOrder ASC, id ASC")
    private List<CraftsmanshipItem> items = new ArrayList<>();
}
