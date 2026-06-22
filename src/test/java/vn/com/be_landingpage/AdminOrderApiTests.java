package vn.com.be_landingpage;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AdminOrderApiTests {

    @Autowired
    private MockMvc mockMvc;

    private String loginAndGetToken() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "admin",
                                  "password": "admin123"
                                }
                                """))
                .andExpect(status().isOk())
                .andReturn();
        String body = result.getResponse().getContentAsString();
        int idx = body.indexOf("\"accessToken\":\"");
        int start = idx + "\"accessToken\":\"".length();
        int end = body.indexOf("\"", start);
        return body.substring(start, end);
    }

    private long createTestOrderAndGetId() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/public/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "customerName": "Admin Test Order",
                                  "phone": "0999999999",
                                  "address": "Quan 1, TP.HCM",
                                  "paymentMethod": "COD",
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
                .andReturn();
        String body = result.getResponse().getContentAsString();
        int idx = body.indexOf("\"id\":");
        int start = idx + "\"id\":".length();
        int end = body.indexOf(",", start);
        return Long.parseLong(body.substring(start, end).trim());
    }

    @Test
    void adminCanGetOrderStats() throws Exception {
        String token = loginAndGetToken();

        mockMvc.perform(get("/api/admin/orders/stats")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalOrders").isNumber())
                .andExpect(jsonPath("$.totalRevenue").isNumber());
    }

    @Test
    void adminCanListOrders() throws Exception {
        String token = loginAndGetToken();

        mockMvc.perform(get("/api/admin/orders")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void adminCanListOrdersWithStatusFilter() throws Exception {
        String token = loginAndGetToken();

        mockMvc.perform(get("/api/admin/orders")
                        .param("status", "PROCESSING")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void adminCanGetOrderById() throws Exception {
        String token = loginAndGetToken();
        long orderId = createTestOrderAndGetId();

        mockMvc.perform(get("/api/admin/orders/" + orderId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderId))
                .andExpect(jsonPath("$.customerName").value("Admin Test Order"));
    }

    @Test
    void adminCanUpdateOrderStatus() throws Exception {
        String token = loginAndGetToken();
        long orderId = createTestOrderAndGetId();

        mockMvc.perform(patch("/api/admin/orders/" + orderId + "/status")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "status": "SHIPPING",
                                  "adminNote": "Dang giao hang"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SHIPPING"))
                .andExpect(jsonPath("$.adminNote").value("Dang giao hang"));
    }

    @Test
    void adminCanGetBankTransferConfig() throws Exception {
        String token = loginAndGetToken();

        mockMvc.perform(get("/api/admin/payment/bank-transfer")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bankName").exists());
    }
}
