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
class ReviewApiTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void publicProductReviewsReturnsRatingSummary() throws Exception {
        mockMvc.perform(get("/api/public/products/1/reviews"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.averageRating").isNumber())
                .andExpect(jsonPath("$.reviewCount").isNumber())
                .andExpect(jsonPath("$.reviews").isArray());
    }

    @Test
    void publicCanSubmitReviewForProduct() throws Exception {
        mockMvc.perform(post("/api/public/products/1/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "rating": 5,
                                  "customerName": "Le Van Test",
                                  "comment": "San pham rat tot!"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.rating").value(5))
                .andExpect(jsonPath("$.customerName").value("Le Van Test"))
                .andExpect(jsonPath("$.comment").value("San pham rat tot!"))
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    void publicReviewRejectedWithoutRating() throws Exception {
        mockMvc.perform(post("/api/public/products/1/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "customerName": "No Rating User",
                                  "comment": "Missing rating"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void publicReviewRejectedWithInvalidRating() throws Exception {
        mockMvc.perform(post("/api/public/products/1/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "rating": 6,
                                  "customerName": "Bad Rating",
                                  "comment": "Rating too high"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }
}
