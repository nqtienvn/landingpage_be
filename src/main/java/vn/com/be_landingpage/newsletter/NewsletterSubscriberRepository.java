package vn.com.be_landingpage.newsletter;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsletterSubscriberRepository extends JpaRepository<NewsletterSubscriber, Long> {
    boolean existsByEmail(String email);
    Optional<NewsletterSubscriber> findByEmail(String email);
}
