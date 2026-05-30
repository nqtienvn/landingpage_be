package vn.com.be_landingpage.orders;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import vn.com.be_landingpage.catalog.CatalogService;
import vn.com.be_landingpage.catalog.Product;
import vn.com.be_landingpage.catalog.ProductImage;
import vn.com.be_landingpage.exception.BadRequestException;
import vn.com.be_landingpage.exception.ResourceNotFoundException;
import vn.com.be_landingpage.media.MediaAsset;
import vn.com.be_landingpage.media.MediaService;

@Service
@RequiredArgsConstructor
public class OrderService {

    private static final SecureRandom RANDOM = new SecureRandom();

    private final CustomerOrderRepository orderRepository;
    private final BankTransferConfigRepository bankTransferConfigRepository;
    private final CatalogService catalogService;
    private final MediaService mediaService;

    @Transactional
    public OrderDtos.OrderResponse createOrder(OrderDtos.CreateOrderRequest request) {
        CustomerOrder order = new CustomerOrder();
        order.setOrderCode(generateOrderCode());
        order.setCustomerName(request.customerName().trim());
        order.setPhone(request.phone().trim());
        order.setAddress(request.address().trim());
        order.setNote(request.note());
        order.setPaymentMethod(request.paymentMethod());
        order.setStatus(request.paymentMethod() == PaymentMethod.BANK_TRANSFER
                ? OrderStatus.PENDING_PAYMENT
                : OrderStatus.PROCESSING);

        BigDecimal subtotal = BigDecimal.ZERO;
        for (OrderDtos.OrderItemRequest itemRequest : request.items()) {
            OrderItem item = buildOrderItem(order, itemRequest);
            subtotal = subtotal.add(item.getLineTotal());
            order.getItems().add(item);
        }
        order.setSubtotal(subtotal);
        order.setTotalAmount(subtotal);
        validateTotalAmount(request.totalAmount(), subtotal);

        if (request.paymentMethod() == PaymentMethod.BANK_TRANSFER) {
            String transferContent = order.getOrderCode();
            BigDecimal finalSubtotal = subtotal;
            order.setTransferContent(transferContent);
            bankTransferConfigRepository.findFirstByActiveTrueOrderByIdAsc().ifPresent(config -> {
                order.setPaymentQrUrl(config.getQrImageUrl());
                order.setVietQrUrl(buildVietQrUrl(config, finalSubtotal, transferContent));
            });
        }

        return OrderDtos.OrderResponse.from(orderRepository.save(order));
    }

    @Transactional(readOnly = true)
    public List<OrderDtos.OrderResponse> findOrders(OrderStatus status, PaymentMethod paymentMethod) {
        List<CustomerOrder> orders;
        if (status != null && paymentMethod != null) {
            orders = orderRepository.findAllByStatusAndPaymentMethodOrderByCreatedAtDesc(status, paymentMethod);
        } else if (status != null) {
            orders = orderRepository.findAllByStatusOrderByCreatedAtDesc(status);
        } else if (paymentMethod != null) {
            orders = orderRepository.findAllByPaymentMethodOrderByCreatedAtDesc(paymentMethod);
        } else {
            orders = orderRepository.findAllByOrderByCreatedAtDesc();
        }
        return orders.stream().map(OrderDtos.OrderResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public OrderDtos.OrderResponse findById(Long id) {
        return OrderDtos.OrderResponse.from(orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng id=" + id)));
    }

    @Transactional(readOnly = true)
    public OrderDtos.OrderResponse findByCode(String orderCode) {
        return OrderDtos.OrderResponse.from(orderRepository.findByOrderCode(orderCode)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng code=" + orderCode)));
    }

    @Transactional
    public OrderDtos.OrderResponse updateStatus(Long id, OrderDtos.UpdateOrderStatusRequest request) {
        CustomerOrder order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng id=" + id));
        order.setStatus(request.status());
        order.setAdminNote(request.adminNote());
        if (request.status() == OrderStatus.PAID && order.getPaidAt() == null) {
            order.setPaidAt(Instant.now());
        }
        return OrderDtos.OrderResponse.from(orderRepository.save(order));
    }

    @Transactional
    public OrderDtos.BankTransferConfigResponse getBankTransferConfig() {
        return OrderDtos.BankTransferConfigResponse.from(bankConfig());
    }

    @Transactional
    public OrderDtos.BankTransferConfigResponse updateBankTransferConfig(
            OrderDtos.BankTransferConfigRequest request
    ) {
        BankTransferConfig config = bankConfig();
        config.setBankName(request.bankName());
        config.setBankCode(request.bankCode());
        config.setAccountNumber(request.accountNumber());
        config.setAccountName(request.accountName());
        config.setQrImageUrl(request.qrImageUrl());
        config.setQrImagePublicId(request.qrImagePublicId());
        config.setActive(request.active() == null || request.active());
        return OrderDtos.BankTransferConfigResponse.from(bankTransferConfigRepository.save(config));
    }

    @Transactional
    public OrderDtos.BankTransferConfigResponse uploadBankQr(MultipartFile file, String folder) {
        BankTransferConfig config = bankConfig();
        MediaAsset asset = mediaService.uploadAsset(
                file,
                folder == null || folder.isBlank() ? "payment/bank-transfer" : folder,
                "Bank transfer QR"
        );
        config.setQrImageUrl(asset.getSecureUrl());
        config.setQrImagePublicId(asset.getPublicId());
        return OrderDtos.BankTransferConfigResponse.from(bankTransferConfigRepository.save(config));
    }

    private OrderItem buildOrderItem(CustomerOrder order, OrderDtos.OrderItemRequest request) {
        int quantity = request.quantity() == null ? 1 : request.quantity();
        if (quantity < 1) {
            throw new BadRequestException("quantity phải lớn hơn 0");
        }

        Product product = catalogService.findActiveProductByIdOrSlug(request.productId(), request.productSlug());
        OrderItem item = new OrderItem();
        item.setOrder(order);
        item.setSize(request.size());
        item.setQuantity(quantity);

        if (product != null) {
            if (!product.getSizes().isEmpty() && request.size() != null && !request.size().isBlank()
                    && !product.getSizes().contains(request.size().trim())) {
                throw new BadRequestException("Size " + request.size() + " không hợp lệ cho sản phẩm " + product.getName());
            }
            item.setProductId(product.getId());
            item.setProductSlug(product.getSlug());
            item.setProductName(product.getName());
            item.setUnitPrice(product.getPrice());
            item.setImageUrl(primaryImageUrl(product));
        } else {
            if (request.productName() == null || request.productName().isBlank()) {
                throw new BadRequestException("productName là bắt buộc nếu không truyền productId/productSlug hợp lệ");
            }
            if (request.unitPrice() == null) {
                throw new BadRequestException("unitPrice là bắt buộc nếu không truyền productId/productSlug hợp lệ");
            }
            item.setProductId(request.productId());
            item.setProductSlug(request.productSlug());
            item.setProductName(request.productName());
            item.setUnitPrice(request.unitPrice());
            item.setImageUrl(request.imageUrl());
        }

        item.setLineTotal(item.getUnitPrice().multiply(BigDecimal.valueOf(quantity)));
        return item;
    }

    private void validateTotalAmount(BigDecimal requestTotal, BigDecimal calculatedTotal) {
        if (requestTotal == null) {
            return;
        }
        if (requestTotal.compareTo(calculatedTotal) != 0) {
            throw new BadRequestException("Tổng tiền không khớp. Backend tính được: " + calculatedTotal);
        }
    }

    private String primaryImageUrl(Product product) {
        return product.getImages().stream()
                .filter(ProductImage::isPrimaryImage)
                .findFirst()
                .or(() -> product.getImages().stream().findFirst())
                .map(ProductImage::getImageUrl)
                .orElse(null);
    }

    private String generateOrderCode() {
        for (int attempt = 0; attempt < 30; attempt++) {
            String code = "DH" + (10000 + RANDOM.nextInt(90000));
            if (!orderRepository.existsByOrderCode(code)) {
                return code;
            }
        }
        throw new BadRequestException("Không thể tạo mã đơn hàng duy nhất, vui lòng thử lại");
    }

    private BankTransferConfig bankConfig() {
        return bankTransferConfigRepository.findAll().stream()
                .findFirst()
                .orElseGet(() -> bankTransferConfigRepository.save(new BankTransferConfig()));
    }

    private String buildVietQrUrl(BankTransferConfig config, BigDecimal amount, String transferContent) {
        if (config.getBankCode() == null || config.getBankCode().isBlank()
                || config.getAccountNumber() == null || config.getAccountNumber().isBlank()) {
            return null;
        }
        String bankCode = encode(config.getBankCode().trim());
        String accountNumber = encode(config.getAccountNumber().trim());
        String amountValue = amount.setScale(0, RoundingMode.HALF_UP).toPlainString();
        String addInfo = encode(transferContent);
        String accountName = config.getAccountName() == null ? "" : "&accountName=" + encode(config.getAccountName());
        return "https://img.vietqr.io/image/"
                + bankCode
                + "-"
                + accountNumber
                + "-compact2.png?amount="
                + amountValue
                + "&addInfo="
                + addInfo
                + accountName;
    }

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
