package vn.com.be_landingpage;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CmsApiTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void publicCmsHeroReturnsSeededContent() throws Exception {
        mockMvc.perform(get("/api/public/cms/hero"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("World Cup 2028."))
                .andExpect(jsonPath("$.subtitle").exists())
                .andExpect(jsonPath("$.bannerImageUrl").exists());
    }

    @Test
    void publicCmsManifestoReturnsSeededContent() throws Exception {
        mockMvc.perform(get("/api/public/cms/manifesto"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.body").exists());
    }

    @Test
    void publicCmsVisualNarrativeReturnsArray() throws Exception {
        mockMvc.perform(get("/api/public/cms/visual-narrative"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].title").exists())
                .andExpect(jsonPath("$[0].imageUrl").exists());
    }

    @Test
    void publicCmsCraftsmanshipReturnsSeededContent() throws Exception {
        mockMvc.perform(get("/api/public/cms/craftsmanship"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.description").exists())
                .andExpect(jsonPath("$.items").isArray());
    }

    @Test
    void publicCmsFooterReturnsSeededContent() throws Exception {
        mockMvc.perform(get("/api/public/cms/footer"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brandName").exists())
                .andExpect(jsonPath("$.copyrightText").exists());
    }

    @Test
    void publicSocialConfigsReturnsArray() throws Exception {
        mockMvc.perform(get("/api/public/social-configs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].platform").exists())
                .andExpect(jsonPath("$[0].url").exists());
    }
}
