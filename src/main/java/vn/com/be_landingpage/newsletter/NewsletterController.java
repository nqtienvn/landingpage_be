package vn.com.be_landingpage.newsletter;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
public class NewsletterController {

    private final NewsletterSubscriberRepository subscriberRepository;

    @PostMapping("/newsletter/subscribe")
    public ResponseEntity<String> subscribe(@Valid @RequestBody SubscribeRequest request) {
        if (subscriberRepository.existsByEmail(request.email().trim().toLowerCase())) {
            return ResponseEntity.ok("Bạn đã đăng ký rồi!");
        }
        NewsletterSubscriber subscriber = new NewsletterSubscriber();
        subscriber.setEmail(request.email().trim().toLowerCase());
        subscriberRepository.save(subscriber);
        return ResponseEntity.ok("Đăng ký thành công!");
    }

    public record SubscribeRequest(
            @NotBlank(message = "Email là bắt buộc") @Email(message = "Email không hợp lệ") String email
    ) {
    }
}
