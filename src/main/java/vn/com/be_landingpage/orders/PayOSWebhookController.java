package vn.com.be_landingpage.orders;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.payos.model.webhooks.Webhook;

@Slf4j
@RestController
@RequestMapping("/api/webhook")
@RequiredArgsConstructor
public class PayOSWebhookController {

    private final OrderService orderService;

    @PostMapping("/payos")
    public ResponseEntity<String> handlePayOSWebhook(@RequestBody Webhook webhook) {
        log.info("Received PayOS webhook");
        try {
            orderService.handlePayOSWebhook(webhook);
            return ResponseEntity.ok("OK");
        } catch (Exception e) {
            log.error("Error processing PayOS webhook: {}", e.getMessage());
            return ResponseEntity.ok("OK");
        }
    }
}
