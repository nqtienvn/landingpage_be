package vn.com.be_landingpage.catalog;

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
@Table(name = "collection_chapters")
public class CollectionChapter extends BaseEntity {

    @Column(nullable = false, unique = true, length = 140)
    private String slug;

    @Column(nullable = false, length = 180)
    private String title;

    @Column(length = 220)
    private String subtitle;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 1000)
    private String coverImageUrl;

    @Column(length = 300)
    private String coverImagePublicId;

    @Column(nullable = false)
    private Integer sortOrder = 0;

    @Column(nullable = false)
    private boolean active = true;
}
