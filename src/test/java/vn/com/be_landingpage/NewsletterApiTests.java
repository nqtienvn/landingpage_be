package vn.com.be_landingpage;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

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
class NewsletterApiTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void publicCanSubscribeNewsletter() throws Exception {
        mockMvc.perform(post("/api/public/newsletter/subscribe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "newuser@example.com"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(content().string("Đăng ký thành công!"));
    }

    @Test
    void newsletterReturnsDuplicateMessageForExistingEmail() throws Exception {
        mockMvc.perform(post("/api/public/newsletter/subscribe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "duplicate@example.com"
                                }
                                """))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/public/newsletter/subscribe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "duplicate@example.com"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(content().string("Bạn đã đăng ký rồi!"));
    }

    @Test
    void newsletterRejectedWithInvalidEmail() throws Exception {
        mockMvc.perform(post("/api/public/newsletter/subscribe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "not-an-email"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }
}
