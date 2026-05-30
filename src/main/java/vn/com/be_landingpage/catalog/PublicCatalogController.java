package vn.com.be_landingpage.catalog;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
public class PublicCatalogController {

    private final CatalogService catalogService;

    @GetMapping("/chapters")
    public List<CatalogDtos.ChapterResponse> chapters() {
        return catalogService.getPublicChapters();
    }

    @GetMapping("/products")
    public List<CatalogDtos.ProductResponse> products() {
        return catalogService.getPublicProducts();
    }

    @GetMapping("/products/{slug}")
    public CatalogDtos.ProductResponse product(@PathVariable String slug) {
        return catalogService.getPublicProduct(slug);
    }
}
