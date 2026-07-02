package vn.com.be_landingpage.orders;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {
    boolean existsByOrderCode(String orderCode);

    Optional<CustomerOrder> findByOrderCode(String orderCode);

    Optional<CustomerOrder> findByPayosOrderCode(Long payosOrderCode);

    List<CustomerOrder> findAllByOrderByCreatedAtDesc();

    List<CustomerOrder> findAllByOrderByCreatedAtDesc(Pageable pageable);

    List<CustomerOrder> findAllByStatusOrderByCreatedAtDesc(OrderStatus status);

    List<CustomerOrder> findAllByStatusOrderByCreatedAtDesc(OrderStatus status, Pageable pageable);

    List<CustomerOrder> findAllByPaymentMethodOrderByCreatedAtDesc(PaymentMethod paymentMethod);

    List<CustomerOrder> findAllByPaymentMethodOrderByCreatedAtDesc(PaymentMethod paymentMethod, Pageable pageable);

    List<CustomerOrder> findAllByStatusAndPaymentMethodOrderByCreatedAtDesc(
            OrderStatus status,
            PaymentMethod paymentMethod
    );

    List<CustomerOrder> findAllByStatusAndPaymentMethodOrderByCreatedAtDesc(
            OrderStatus status,
            PaymentMethod paymentMethod,
            Pageable pageable
    );

    List<CustomerOrder> findByPhoneOrderByCreatedAtDesc(String phone);

    long countByStatus(OrderStatus status);

    @Query("select coalesce(sum(o.totalAmount), 0) from CustomerOrder o where o.status in :statuses")
    BigDecimal sumTotalAmountByStatusIn(@Param("statuses") Collection<OrderStatus> statuses);

    @Query("""
            select coalesce(sum(o.totalAmount), 0)
            from CustomerOrder o
            where o.status in :statuses and o.createdAt > :createdAfter
            """)
    BigDecimal sumTotalAmountByStatusInAndCreatedAtAfter(
            @Param("statuses") Collection<OrderStatus> statuses,
            @Param("createdAfter") Instant createdAfter
    );
}
