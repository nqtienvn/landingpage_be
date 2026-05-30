package vn.com.be_landingpage.media;

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
@Table(name = "media_assets")
public class MediaAsset extends BaseEntity {

    @Column(nullable = false, unique = true, length = 300)
    private String publicId;

    @Column(nullable = false, length = 1000)
    private String url;

    @Column(nullable = false, length = 1000)
    private String secureUrl;

    @Column(nullable = false, length = 255)
    private String folder;

    @Column(length = 255)
    private String originalFilename;

    @Column(length = 100)
    private String format;

    @Column(length = 50)
    private String resourceType;

    private Long bytes;

    private Integer width;

    private Integer height;

    @Column(length = 255)
    private String altText;
}
