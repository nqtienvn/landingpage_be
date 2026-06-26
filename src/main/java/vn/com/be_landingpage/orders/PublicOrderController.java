package vn.com.be_landingpage.orders;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
public class PublicOrderController {

    private final OrderService orderService;

    @PostMapping("/orders")
    public OrderDtos.OrderResponse createOrder(@Valid @RequestBody OrderDtos.CreateOrderRequest request) {
        return orderService.createOrder(request);
    }

    @GetMapping("/orders/{orderCode}")
    public OrderDtos.OrderResponse findByCode(@PathVariable String orderCode) {
        return orderService.findByCode(orderCode);
    }

    @PostMapping("/orders/{orderCode}/payos/sync")
    public OrderDtos.OrderResponse syncPayOSPaymentStatus(@PathVariable String orderCode) {
        return orderService.syncPayOSPaymentStatus(orderCode);
    }

    @GetMapping("/orders/phone/{phone}")
    public List<OrderDtos.OrderResponse> findByPhone(@PathVariable String phone) {
        return orderService.findByPhone(phone);
    }

    @GetMapping("/payment/bank-transfer")
    public OrderDtos.BankTransferConfigResponse bankTransferConfig() {
        return orderService.getBankTransferConfig();
    }
}
