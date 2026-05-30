package vn.com.be_landingpage.orders;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.com.be_landingpage.common.BaseEntity;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "bank_transfer_configs")
public class BankTransferConfig extends BaseEntity {

    @Column(length = 120)
    private String bankName;

    @Column(length = 40)
    private String bankCode;

    @Column(length = 50)
    private String accountNumber;

    @Column(length = 160)
    private String accountName;

    @Column(length = 1000)
    private String qrImageUrl;

    @Column(length = 300)
    private String qrImagePublicId;

    @Column(nullable = false)
    private boolean active = true;
}
