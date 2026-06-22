package vn.com.be_landingpage.orders;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderService orderService;

    @GetMapping("/orders/stats")
    public OrderDtos.OrderStatsResponse orderStats() {
        return orderService.getOrderStats();
    }

    @GetMapping("/orders")
    public List<OrderDtos.OrderResponse> orders(
            @RequestParam(value = "status", required = false) OrderStatus status,
            @RequestParam(value = "paymentMethod", required = false) PaymentMethod paymentMethod
    ) {
        return orderService.findOrders(status, paymentMethod);
    }

    @GetMapping("/orders/{id}")
    public OrderDtos.OrderResponse order(@PathVariable Long id) {
        return orderService.findById(id);
    }

    @GetMapping("/orders/code/{orderCode}")
    public OrderDtos.OrderResponse orderByCode(@PathVariable String orderCode) {
        return orderService.findByCode(orderCode);
    }

    @PatchMapping("/orders/{id}/status")
    public OrderDtos.OrderResponse updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody OrderDtos.UpdateOrderStatusRequest request
    ) {
        return orderService.updateStatus(id, request);
    }

    @GetMapping("/payment/bank-transfer")
    public OrderDtos.BankTransferConfigResponse getBankTransferConfig() {
        return orderService.getBankTransferConfig();
    }

    @PutMapping("/payment/bank-transfer")
    public OrderDtos.BankTransferConfigResponse updateBankTransferConfig(
            @RequestBody OrderDtos.BankTransferConfigRequest request
    ) {
        return orderService.updateBankTransferConfig(request);
    }

    @PostMapping(value = "/payment/bank-transfer/qr", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public OrderDtos.BankTransferConfigResponse uploadBankQr(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "folder", required = false) String folder
    ) {
        return orderService.uploadBankQr(file, folder);
    }
}
