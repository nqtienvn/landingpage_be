package vn.com.be_landingpage.orders;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {
    boolean existsByOrderCode(String orderCode);

    Optional<CustomerOrder> findByOrderCode(String orderCode);

    Optional<CustomerOrder> findByPayosOrderCode(Long payosOrderCode);

    List<CustomerOrder> findAllByOrderByCreatedAtDesc();

    List<CustomerOrder> findAllByStatusOrderByCreatedAtDesc(OrderStatus status);

    List<CustomerOrder> findAllByPaymentMethodOrderByCreatedAtDesc(PaymentMethod paymentMethod);

    List<CustomerOrder> findAllByStatusAndPaymentMethodOrderByCreatedAtDesc(
            OrderStatus status,
            PaymentMethod paymentMethod
    );

    List<CustomerOrder> findByPhoneOrderByCreatedAtDesc(String phone);
}
