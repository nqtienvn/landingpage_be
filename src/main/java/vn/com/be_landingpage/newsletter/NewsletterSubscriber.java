package vn.com.be_landingpage.newsletter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.com.be_landingpage.common.BaseEntity;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "newsletter_subscribers", uniqueConstraints = {
    @UniqueConstraint(columnNames = "email")
})
public class NewsletterSubscriber extends BaseEntity {

    @Column(nullable = false, length = 200)
    private String email;
}
