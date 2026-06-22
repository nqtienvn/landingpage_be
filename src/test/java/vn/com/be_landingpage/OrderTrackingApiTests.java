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
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class OrderTrackingApiTests {

    @Autowired
    private MockMvc mockMvc;

    private static final String COD_ORDER_BODY = """
            {
              "customerName": "Tran Van B",
              "phone": "0912345678",
              "address": "Quan 3, TP.HCM",
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
            """;

    private MvcResult createOrder(String body) throws Exception {
        return mockMvc.perform(post("/api/public/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderCode").isNotEmpty())
                .andReturn();
    }

    @Test
    void lookupOrderByPhoneReturnsOrders() throws Exception {
        createOrder(COD_ORDER_BODY);

        mockMvc.perform(get("/api/public/orders/phone/0912345678"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].phone").value("0912345678"))
                .andExpect(jsonPath("$[0].orderCode").isNotEmpty())
                .andExpect(jsonPath("$[0].customerName").value("Tran Van B"))
                .andExpect(jsonPath("$[0].status").value("PROCESSING"))
                .andExpect(jsonPath("$[0].paymentMethod").value("COD"));
    }

    @Test
    void lookupOrderByPhoneReturnsEmptyForUnknown() throws Exception {
        mockMvc.perform(get("/api/public/orders/phone/0000000000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void lookupOrderByPhoneReturnsNewestFirst() throws Exception {
        String order1Body = """
                {
                  "customerName": "Le Thi C",
                  "phone": "0987654321",
                  "address": "Quan 7, TP.HCM",
                  "paymentMethod": "COD",
                  "totalAmount": 2500000,
                  "items": [
                    {
                      "productId": 1,
                      "size": "L",
                      "quantity": 1
                    }
                  ]
                }
                """;
        String order2Body = """
                {
                  "customerName": "Pham Van D",
                  "phone": "0987654321",
                  "address": "Binh Thanh, TP.HCM",
                  "paymentMethod": "BANK_TRANSFER",
                  "totalAmount": 5000000,
                  "items": [
                    {
                      "productId": 1,
                      "size": "S",
                      "quantity": 2
                    }
                  ]
                }
                """;

        createOrder(order1Body);
        createOrder(order2Body);

        mockMvc.perform(get("/api/public/orders/phone/0987654321"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].customerName").value("Pham Van D"))
                .andExpect(jsonPath("$[1].customerName").value("Le Thi C"));
    }

    @Test
    void lookupOrderByCodeReturnsOrder() throws Exception {
        MvcResult result = createOrder(COD_ORDER_BODY);
        String responseBody = result.getResponse().getContentAsString();
        String orderCode = extractOrderCode(responseBody);

        mockMvc.perform(get("/api/public/orders/" + orderCode))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderCode").value(orderCode))
                .andExpect(jsonPath("$.phone").value("0912345678"))
                .andExpect(jsonPath("$.customerName").value("Tran Van B"))
                .andExpect(jsonPath("$.status").value("PROCESSING"))
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items[0].productId").value(1));
    }

    @Test
    void lookupOrderByCodeReturns404ForUnknown() throws Exception {
        mockMvc.perform(get("/api/public/orders/DH000000"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

    @Test
    void createAndTrackCodOrder() throws Exception {
        createOrder(COD_ORDER_BODY);

        mockMvc.perform(get("/api/public/orders/phone/0912345678"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].phone").value("0912345678"))
                .andExpect(jsonPath("$[0].status").value("PROCESSING"))
                .andExpect(jsonPath("$[0].paymentMethod").value("COD"))
                .andExpect(jsonPath("$[0].totalAmount").value(2500000))
                .andExpect(jsonPath("$[0].address").value("Quan 3, TP.HCM"))
                .andExpect(jsonPath("$[0].items").isArray())
                .andExpect(jsonPath("$[0].items[0].size").value("M"))
                .andExpect(jsonPath("$[0].items[0].quantity").value(1));
    }

    private String extractOrderCode(String json) {
        int idx = json.indexOf("\"orderCode\":\"");
        if (idx < 0) throw new RuntimeException("orderCode not found in response");
        int start = idx + "\"orderCode\":\"".length();
        int end = json.indexOf("\"", start);
        return json.substring(start, end);
    }
}
