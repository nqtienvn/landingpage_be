package vn.com.be_landingpage.catalog;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import vn.com.be_landingpage.common.SlugUtils;
import vn.com.be_landingpage.exception.BadRequestException;
import vn.com.be_landingpage.exception.ResourceNotFoundException;
import vn.com.be_landingpage.media.MediaAsset;
import vn.com.be_landingpage.media.MediaService;

@Service
@RequiredArgsConstructor
public class CatalogService {

    private final CollectionChapterRepository chapterRepository;
    private final ProductRepository productRepository;
    private final MediaService mediaService;

    @Cacheable(value = "chapters", key = "'public'")
    @Transactional(readOnly = true)
    public List<CatalogDtos.ChapterResponse> getPublicChapters() {
        return chapterRepository.findAllByActiveTrueOrderBySortOrderAscIdAsc().stream()
                .map(CatalogDtos.ChapterResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CatalogDtos.ChapterResponse> getAdminChapters() {
        return chapterRepository.findAllByOrderBySortOrderAscIdAsc().stream()
                .map(CatalogDtos.ChapterResponse::from)
                .toList();
    }

    @Transactional
    @CacheEvict(value = {"chapters", "products", "landingPage"}, allEntries = true)
    public CatalogDtos.ChapterResponse createChapter(CatalogDtos.ChapterRequest request) {
        CollectionChapter chapter = new CollectionChapter();
        applyChapter(chapter, request, null);
        return CatalogDtos.ChapterResponse.from(chapterRepository.save(chapter));
    }

    @Transactional
    @CacheEvict(value = {"chapters", "products", "landingPage"}, allEntries = true)
    public CatalogDtos.ChapterResponse updateChapter(Long id, CatalogDtos.ChapterRequest request) {
        CollectionChapter chapter = chapterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy chapter id=" + id));
        applyChapter(chapter, request, id);
        return CatalogDtos.ChapterResponse.from(chapterRepository.save(chapter));
    }

    @Transactional
    @CacheEvict(value = {"chapters", "products", "landingPage"}, allEntries = true)
    public void deleteChapter(Long id) {
        CollectionChapter chapter = chapterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy chapter id=" + id));
        chapterRepository.delete(chapter);
    }

    @Cacheable(value = "products", key = "'public'")
    @Transactional(readOnly = true)
    public List<CatalogDtos.ProductResponse> getPublicProducts() {
        return productRepository.findAllByActiveTrueOrderBySortOrderAscIdAsc().stream()
                .map(CatalogDtos.ProductResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CatalogDtos.ProductResponse> getAdminProducts() {
        return productRepository.findAllByOrderBySortOrderAscIdAsc().stream()
                .map(CatalogDtos.ProductResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public CatalogDtos.ProductResponse getPublicProduct(String slug) {
        Product product = productRepository.findBySlug(slug)
                .filter(Product::isActive)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm slug=" + slug));
        return CatalogDtos.ProductResponse.from(product);
    }

    public Product getProductEntity(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm id=" + id));
    }

    @Transactional(readOnly = true)
    public Product findActiveProductByIdOrSlug(Long productId, String productSlug) {
        Product product = null;
        if (productId != null) {
            product = productRepository.findById(productId).orElse(null);
        }
        if (product == null && productSlug != null && !productSlug.isBlank()) {
            product = productRepository.findBySlug(productSlug.trim()).orElse(null);
        }
        if (product == null || !product.isActive()) {
            return null;
        }
        return product;
    }

    @Transactional
    @CacheEvict(value = {"products", "landingPage"}, allEntries = true)
    public CatalogDtos.ProductResponse createProduct(CatalogDtos.ProductRequest request) {
        Product product = new Product();
        applyProduct(product, request, null);
        return CatalogDtos.ProductResponse.from(productRepository.save(product));
    }

    @Transactional
    @CacheEvict(value = {"products", "landingPage"}, allEntries = true)
    public CatalogDtos.ProductResponse updateProduct(Long id, CatalogDtos.ProductRequest request) {
        Product product = getProductEntity(id);
        applyProduct(product, request, id);
        return CatalogDtos.ProductResponse.from(productRepository.save(product));
    }

    @Transactional
    @CacheEvict(value = {"products", "landingPage"}, allEntries = true)
    public void deleteProduct(Long id) {
        Product product = getProductEntity(id);
        productRepository.delete(product);
    }

    @Transactional
    @CacheEvict(value = {"products", "landingPage"}, allEntries = true)
    public CatalogDtos.ProductResponse uploadProductImages(Long id, List<MultipartFile> files, String folder) {
        Product product = getProductEntity(id);
        if (files == null || files.isEmpty()) {
            throw new BadRequestException("files là bắt buộc");
        }
        String uploadFolder = (folder == null || folder.isBlank()) ? "products/" + product.getSlug() : folder;
        boolean hasPrimary = product.getImages().stream().anyMatch(ProductImage::isPrimaryImage);
        int nextOrder = product.getImages().stream()
                .map(ProductImage::getSortOrder)
                .max(Integer::compareTo)
                .orElse(-1) + 1;

        for (MultipartFile file : files) {
            MediaAsset asset = mediaService.uploadAsset(file, uploadFolder, product.getName());
            ProductImage image = new ProductImage();
            image.setProduct(product);
            image.setImageUrl(asset.getSecureUrl());
            image.setImagePublicId(asset.getPublicId());
            image.setAltText(product.getName());
            image.setSortOrder(nextOrder++);
            image.setPrimaryImage(!hasPrimary);
            product.getImages().add(image);
            hasPrimary = true;
        }
        return CatalogDtos.ProductResponse.from(productRepository.save(product));
    }

    @Transactional
    @CacheEvict(value = {"products", "landingPage"}, allEntries = true)
    public CatalogDtos.ProductResponse deleteProductImage(Long productId, Long imageId) {
        Product product = getProductEntity(productId);
        boolean removed = product.getImages().removeIf(image -> image.getId().equals(imageId));
        if (!removed) {
            throw new ResourceNotFoundException("Không tìm thấy ảnh sản phẩm id=" + imageId);
        }
        ensureOnePrimaryImage(product);
        return CatalogDtos.ProductResponse.from(productRepository.save(product));
    }

    private void applyChapter(CollectionChapter chapter, CatalogDtos.ChapterRequest request, Long currentId) {
        String slug = request.slug() == null || request.slug().isBlank()
                ? SlugUtils.toSlug(request.title())
                : SlugUtils.toSlug(request.slug());
        if (currentId == null && chapterRepository.existsBySlug(slug)) {
            throw new BadRequestException("Chapter slug đã tồn tại: " + slug);
        }
        if (currentId != null && chapterRepository.existsBySlugAndIdNot(slug, currentId)) {
            throw new BadRequestException("Chapter slug đã tồn tại: " + slug);
        }
        chapter.setSlug(slug);
        chapter.setTitle(request.title());
        chapter.setSubtitle(request.subtitle());
        chapter.setDescription(request.description());
        chapter.setCoverImageUrl(request.coverImageUrl());
        chapter.setCoverImagePublicId(request.coverImagePublicId());
        chapter.setSortOrder(request.sortOrder() == null ? 0 : request.sortOrder());
        chapter.setActive(request.active() == null || request.active());
    }

    private void applyProduct(Product product, CatalogDtos.ProductRequest request, Long currentId) {
        String slug = request.slug() == null || request.slug().isBlank()
                ? SlugUtils.toSlug(request.name())
                : SlugUtils.toSlug(request.slug());
        if (currentId == null && productRepository.existsBySlug(slug)) {
            throw new BadRequestException("Product slug đã tồn tại: " + slug);
        }
        if (currentId != null && productRepository.existsBySlugAndIdNot(slug, currentId)) {
            throw new BadRequestException("Product slug đã tồn tại: " + slug);
        }
        product.setSlug(slug);
        product.setName(request.name());
        product.setTag(request.tag());
        product.setPrice(request.price());
        product.setDescription(request.description());
        product.setMaterial(request.material());
        product.setSortOrder(request.sortOrder() == null ? 0 : request.sortOrder());
        product.setActive(request.active() == null || request.active());

        product.getSizes().clear();
        product.getSizes().addAll(normalizeSizes(request.sizes()));

        if (request.chapterId() == null) {
            product.setChapter(null);
        } else {
            product.setChapter(chapterRepository.findById(request.chapterId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy chapter id=" + request.chapterId())));
        }

        product.getImages().clear();
        if (request.images() != null) {
            int index = 0;
            for (CatalogDtos.ProductImageRequest imageRequest : request.images()) {
                ProductImage image = new ProductImage();
                image.setProduct(product);
                image.setImageUrl(imageRequest.imageUrl());
                image.setImagePublicId(imageRequest.imagePublicId());
                image.setAltText(imageRequest.altText());
                image.setSortOrder(imageRequest.sortOrder() == null ? index : imageRequest.sortOrder());
                image.setPrimaryImage(Boolean.TRUE.equals(imageRequest.primaryImage()));
                product.getImages().add(image);
                index++;
            }
        }
        ensureOnePrimaryImage(product);
    }

    private List<String> normalizeSizes(List<String> sizes) {
        if (sizes == null) {
            return new ArrayList<>();
        }
        return sizes.stream()
                .filter(size -> size != null && !size.isBlank())
                .map(String::trim)
                .distinct()
                .toList();
    }

    private void ensureOnePrimaryImage(Product product) {
        boolean foundPrimary = false;
        for (ProductImage image : product.getImages()) {
            if (image.isPrimaryImage() && !foundPrimary) {
                foundPrimary = true;
            } else {
                image.setPrimaryImage(false);
            }
        }
        if (!foundPrimary && !product.getImages().isEmpty()) {
            product.getImages().get(0).setPrimaryImage(true);
        }
    }
}
