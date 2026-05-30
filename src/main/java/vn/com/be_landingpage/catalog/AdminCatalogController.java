package vn.com.be_landingpage.catalog;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminCatalogController {

    private final CatalogService catalogService;

    @GetMapping("/chapters")
    public List<CatalogDtos.ChapterResponse> chapters() {
        return catalogService.getAdminChapters();
    }

    @PostMapping("/chapters")
    public CatalogDtos.ChapterResponse createChapter(@Valid @RequestBody CatalogDtos.ChapterRequest request) {
        return catalogService.createChapter(request);
    }

    @PutMapping("/chapters/{id}")
    public CatalogDtos.ChapterResponse updateChapter(
            @PathVariable Long id,
            @Valid @RequestBody CatalogDtos.ChapterRequest request
    ) {
        return catalogService.updateChapter(id, request);
    }

    @DeleteMapping("/chapters/{id}")
    public void deleteChapter(@PathVariable Long id) {
        catalogService.deleteChapter(id);
    }

    @GetMapping("/products")
    public List<CatalogDtos.ProductResponse> products() {
        return catalogService.getAdminProducts();
    }

    @PostMapping("/products")
    public CatalogDtos.ProductResponse createProduct(@Valid @RequestBody CatalogDtos.ProductRequest request) {
        return catalogService.createProduct(request);
    }

    @PutMapping("/products/{id}")
    public CatalogDtos.ProductResponse updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody CatalogDtos.ProductRequest request
    ) {
        return catalogService.updateProduct(id, request);
    }

    @DeleteMapping("/products/{id}")
    public void deleteProduct(@PathVariable Long id) {
        catalogService.deleteProduct(id);
    }

    @PostMapping(value = "/products/{id}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CatalogDtos.ProductResponse uploadProductImages(
            @PathVariable Long id,
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam(value = "folder", required = false) String folder
    ) {
        return catalogService.uploadProductImages(id, files, folder);
    }

    @DeleteMapping("/products/{productId}/images/{imageId}")
    public CatalogDtos.ProductResponse deleteProductImage(
            @PathVariable Long productId,
            @PathVariable Long imageId
    ) {
        return catalogService.deleteProductImage(productId, imageId);
    }
}
