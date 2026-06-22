package vn.com.be_landingpage;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class OrderValidationApiTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void publicBankTransferConfigReturnsData() throws Exception {
        mockMvc.perform(get("/api/public/payment/bank-transfer"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bankName").exists())
                .andExpect(jsonPath("$.accountNumber").exists())
                .andExpect(jsonPath("$.accountName").exists());
    }

    @Test
    void publicCreateOrderRejectedWithoutCustomerName() throws Exception {
        mockMvc.perform(post("/api/public/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "phone": "0900000000",
                                  "address": "Quan 1, TP.HCM",
                                  "paymentMethod": "COD",
                                  "totalAmount": 100000,
                                  "items": [{"productId": 1, "size": "M", "quantity": 1}]
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void publicCreateOrderRejectedWithoutPhone() throws Exception {
        mockMvc.perform(post("/api/public/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "customerName": "Test User",
                                  "address": "Quan 1, TP.HCM",
                                  "paymentMethod": "COD",
                                  "totalAmount": 100000,
                                  "items": [{"productId": 1, "size": "M", "quantity": 1}]
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void publicCreateOrderRejectedWithEmptyItems() throws Exception {
        mockMvc.perform(post("/api/public/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "customerName": "Test User",
                                  "phone": "0900000000",
                                  "address": "Quan 1, TP.HCM",
                                  "paymentMethod": "COD",
                                  "totalAmount": 100000,
                                  "items": []
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void publicCreateBankTransferOrderReturnsPendingPayment() throws Exception {
        mockMvc.perform(post("/api/public/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "customerName": "Nguyen Van BT",
                                  "phone": "0911111111",
                                  "address": "Quan 10, TP.HCM",
                                  "paymentMethod": "BANK_TRANSFER",
                                  "totalAmount": 2500000,
                                  "items": [
                                    {
                                      "productId": 1,
                                      "size": "M",
                                      "quantity": 1
                                    }
                                  ]
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderCode").isNotEmpty())
                .andExpect(jsonPath("$.status").value("PENDING_PAYMENT"))
                .andExpect(jsonPath("$.paymentMethod").value("BANK_TRANSFER"));
    }
}
