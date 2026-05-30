package vn.com.be_landingpage.orders;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public final class OrderDtos {

    private OrderDtos() {
    }

    public record OrderItemRequest(
            Long productId,
            String productSlug,
            String productName,
            String size,
            @Min(value = 1, message = "quantity phải lớn hơn 0") Integer quantity,
            @PositiveOrZero(message = "unitPrice phải lớn hơn hoặc bằng 0") BigDecimal unitPrice,
            String imageUrl
    ) {
    }

    public record CreateOrderRequest(
            @NotBlank(message = "customerName là bắt buộc") String customerName,
            @NotBlank(message = "phone là bắt buộc") String phone,
            @NotBlank(message = "address là bắt buộc") String address,
            String note,
            @NotNull(message = "paymentMethod là bắt buộc") PaymentMethod paymentMethod,
            @PositiveOrZero(message = "totalAmount phải lớn hơn hoặc bằng 0") BigDecimal totalAmount,
            @NotEmpty(message = "items là bắt buộc") @Valid List<OrderItemRequest> items
    ) {
    }

    public record OrderItemResponse(
            Long id,
            Long productId,
            String productSlug,
            String productName,
            String size,
            Integer quantity,
            BigDecimal unitPrice,
            BigDecimal lineTotal,
            String imageUrl
    ) {
        public static OrderItemResponse from(OrderItem item) {
            return new OrderItemResponse(
                    item.getId(),
                    item.getProductId(),
                    item.getProductSlug(),
                    item.getProductName(),
                    item.getSize(),
                    item.getQuantity(),
                    item.getUnitPrice(),
                    item.getLineTotal(),
                    item.getImageUrl()
            );
        }
    }

    public record OrderResponse(
            Long id,
            String orderCode,
            String customerName,
            String phone,
            String address,
            String note,
            PaymentMethod paymentMethod,
            OrderStatus status,
            BigDecimal subtotal,
            BigDecimal totalAmount,
            String transferContent,
            String paymentQrUrl,
            String vietQrUrl,
            String adminNote,
            Instant paidAt,
            Instant createdAt,
            Instant updatedAt,
            List<OrderItemResponse> items
    ) {
        public static OrderResponse from(CustomerOrder order) {
            return new OrderResponse(
                    order.getId(),
                    order.getOrderCode(),
                    order.getCustomerName(),
                    order.getPhone(),
                    order.getAddress(),
                    order.getNote(),
                    order.getPaymentMethod(),
                    order.getStatus(),
                    order.getSubtotal(),
                    order.getTotalAmount(),
                    order.getTransferContent(),
                    order.getPaymentQrUrl(),
                    order.getVietQrUrl(),
                    order.getAdminNote(),
                    order.getPaidAt(),
                    order.getCreatedAt(),
                    order.getUpdatedAt(),
                    order.getItems().stream().map(OrderItemResponse::from).toList()
            );
        }
    }

    public record UpdateOrderStatusRequest(
            @NotNull(message = "status là bắt buộc") OrderStatus status,
            String adminNote
    ) {
    }

    public record BankTransferConfigRequest(
            String bankName,
            String bankCode,
            String accountNumber,
            String accountName,
            String qrImageUrl,
            String qrImagePublicId,
            Boolean active
    ) {
    }

    public record BankTransferConfigResponse(
            Long id,
            String bankName,
            String bankCode,
            String accountNumber,
            String accountName,
            String qrImageUrl,
            String qrImagePublicId,
            Boolean active,
            Instant updatedAt
    ) {
        public static BankTransferConfigResponse from(BankTransferConfig config) {
            return new BankTransferConfigResponse(
                    config.getId(),
                    config.getBankName(),
                    config.getBankCode(),
                    config.getAccountNumber(),
                    config.getAccountName(),
                    config.getQrImageUrl(),
                    config.getQrImagePublicId(),
                    config.isActive(),
                    config.getUpdatedAt()
            );
        }
    }
}
