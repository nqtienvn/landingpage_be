package vn.com.be_landingpage;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
class ProductApiTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void publicProductListReturnsSeededProducts() throws Exception {
        mockMvc.perform(get("/api/public/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").exists())
                .andExpect(jsonPath("$[0].slug").exists())
                .andExpect(jsonPath("$[0].price").exists());
    }

    @Test
    void publicProductBySlugReturnsProduct() throws Exception {
        String listResult = mockMvc.perform(get("/api/public/products"))
                .andReturn().getResponse().getContentAsString();
        String slug = extractFirstSlug(listResult);

        mockMvc.perform(get("/api/public/products/" + slug))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.slug").value(slug))
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.price").isNumber())
                .andExpect(jsonPath("$.images").isArray());
    }

    @Test
    void publicProductBySlugReturns404ForUnknown() throws Exception {
        mockMvc.perform(get("/api/public/products/non-existent-slug-xyz"))
                .andExpect(status().isNotFound());
    }

    @Test
    void errorResponseStaysJsonWhenClientAcceptsImage() throws Exception {
        mockMvc.perform(get("/api/public/products/non-existent-slug-xyz")
                        .accept(MediaType.IMAGE_PNG))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void publicChaptersReturnsSeededChapters() throws Exception {
        mockMvc.perform(get("/api/public/chapters"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].title").exists())
                .andExpect(jsonPath("$[0].slug").exists());
    }

    @Test
    void publicLandingPageIncludesAllSections() throws Exception {
        mockMvc.perform(get("/api/public/landing-page"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hero").exists())
                .andExpect(jsonPath("$.manifesto").exists())
                .andExpect(jsonPath("$.visualNarrative").isArray())
                .andExpect(jsonPath("$.craftsmanship").exists())
                .andExpect(jsonPath("$.footer").exists())
                .andExpect(jsonPath("$.chapters").isArray())
                .andExpect(jsonPath("$.products").isArray())
                .andExpect(jsonPath("$.topReviews").isArray());
    }

    private String extractFirstSlug(String json) {
        int idx = json.indexOf("\"slug\":\"");
        if (idx < 0) throw new RuntimeException("slug not found");
        int start = idx + "\"slug\":\"".length();
        int end = json.indexOf("\"", start);
        return json.substring(start, end);
    }
}
