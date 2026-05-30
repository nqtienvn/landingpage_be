package vn.com.be_landingpage.orders;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankTransferConfigRepository extends JpaRepository<BankTransferConfig, Long> {
    Optional<BankTransferConfig> findFirstByActiveTrueOrderByIdAsc();
}
