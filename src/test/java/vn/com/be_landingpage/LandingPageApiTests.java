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
class LandingPageApiTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void publicLandingPageReturnsSeededContent() throws Exception {
        mockMvc.perform(get("/api/public/landing-page"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hero.title").value("World Cup 2028."))
                .andExpect(jsonPath("$.products[0].name").exists());
    }

    @Test
    void adminCanLoginWithSeededAccount() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "admin",
                                  "password": "admin123"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.role").value("ADMIN"));
    }

    @Test
    void authMeWithoutLoginReturnsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/auth/me"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.error").value("Unauthorized"))
                .andExpect(jsonPath("$.message").value("Chưa đăng nhập"));
    }

    @Test
    void publicCanCreateCodOrder() throws Exception {
        mockMvc.perform(post("/api/public/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "customerName": "Nguyen Van A",
                                  "phone": "0900000000",
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
                .andExpect(jsonPath("$.orderCode").isNotEmpty())
                .andExpect(jsonPath("$.status").value("PROCESSING"));
    }
}
