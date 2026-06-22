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
class AdminCatalogApiTests {

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
    void adminCanListChapters() throws Exception {
        String token = loginAndGetToken();

        mockMvc.perform(get("/api/admin/chapters")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].title").exists());
    }

    @Test
    void adminCanCreateChapter() throws Exception {
        String token = loginAndGetToken();

        mockMvc.perform(post("/api/admin/chapters")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "Test Chapter",
                                  "slug": "test-chapter-admin",
                                  "subtitle": "Test Subtitle",
                                  "description": "Test description",
                                  "sortOrder": 10,
                                  "active": true
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("Test Chapter"))
                .andExpect(jsonPath("$.slug").value("test-chapter-admin"));
    }

    @Test
    void adminCanUpdateChapter() throws Exception {
        String token = loginAndGetToken();

        // Get existing chapter ID
        String listResult = mockMvc.perform(get("/api/admin/chapters")
                        .header("Authorization", "Bearer " + token))
                .andReturn().getResponse().getContentAsString();
        long chapterId = extractFirstId(listResult);

        mockMvc.perform(put("/api/admin/chapters/" + chapterId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "Updated Chapter",
                                  "slug": "updated-chapter",
                                  "subtitle": "Updated Subtitle",
                                  "description": "Updated description",
                                  "sortOrder": 5,
                                  "active": true
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Chapter"));
    }

    @Test
    void adminCanListProducts() throws Exception {
        String token = loginAndGetToken();

        mockMvc.perform(get("/api/admin/products")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").exists());
    }

    @Test
    void adminCanCreateProduct() throws Exception {
        String token = loginAndGetToken();

        mockMvc.perform(post("/api/admin/products")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Test Product Admin",
                                  "slug": "test-product-admin-create",
                                  "tag": "NEW",
                                  "price": 1500000,
                                  "description": "Test product description",
                                  "material": "Cotton",
                                  "sizes": ["S", "M", "L"],
                                  "chapterId": 1,
                                  "sortOrder": 10,
                                  "active": true
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Test Product Admin"))
                .andExpect(jsonPath("$.slug").value("test-product-admin-create"));
    }

    private long extractFirstId(String json) {
        int idx = json.indexOf("\"id\":");
        if (idx < 0) throw new RuntimeException("id not found");
        int start = idx + "\"id\":".length();
        int end = json.indexOf(",", start);
        if (end < 0) end = json.indexOf("}", start);
        return Long.parseLong(json.substring(start, end).trim());
    }
}
