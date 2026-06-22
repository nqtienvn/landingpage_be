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
class AdminCmsApiTests {

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

    @Test
    void adminCanGetHeroContent() throws Exception {
        String token = loginAndGetToken();

        mockMvc.perform(get("/api/admin/cms/hero")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").exists());
    }

    @Test
    void adminCanUpdateHeroContent() throws Exception {
        String token = loginAndGetToken();

        mockMvc.perform(put("/api/admin/cms/hero")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "Updated Hero Title",
                                  "subtitle": "Updated subtitle",
                                  "ctaText": "Shop Now",
                                  "ctaHref": "/products"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Hero Title"));
    }

    @Test
    void adminCanGetManifestoContent() throws Exception {
        String token = loginAndGetToken();

        mockMvc.perform(get("/api/admin/cms/manifesto")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").exists());
    }

    @Test
    void adminCanUpdateManifestoContent() throws Exception {
        String token = loginAndGetToken();

        mockMvc.perform(put("/api/admin/cms/manifesto")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "Updated Manifesto",
                                  "body": "Updated manifesto body text",
                                  "eyebrow": "Our Story"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Manifesto"));
    }

    @Test
    void adminCanGetCraftsmanshipContent() throws Exception {
        String token = loginAndGetToken();

        mockMvc.perform(get("/api/admin/cms/craftsmanship")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").exists());
    }

    @Test
    void adminCanGetFooterContent() throws Exception {
        String token = loginAndGetToken();

        mockMvc.perform(get("/api/admin/cms/footer")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brandName").exists());
    }
}
