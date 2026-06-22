package vn.com.be_landingpage.config;

import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vn.com.be_landingpage.auth.AdminRole;
import vn.com.be_landingpage.auth.AdminUser;
import vn.com.be_landingpage.auth.AdminUserRepository;
import vn.com.be_landingpage.catalog.CollectionChapter;
import vn.com.be_landingpage.catalog.CollectionChapterRepository;
import vn.com.be_landingpage.catalog.Product;
import vn.com.be_landingpage.catalog.ProductImage;
import vn.com.be_landingpage.catalog.ProductRepository;
import vn.com.be_landingpage.cms.CraftsmanshipContent;
import vn.com.be_landingpage.cms.CraftsmanshipContentRepository;
import vn.com.be_landingpage.cms.CraftsmanshipItem;
import vn.com.be_landingpage.cms.FooterContent;
import vn.com.be_landingpage.cms.FooterContentRepository;
import vn.com.be_landingpage.cms.FooterPolicyLink;
import vn.com.be_landingpage.cms.HeroContent;
import vn.com.be_landingpage.cms.HeroContentRepository;
import vn.com.be_landingpage.cms.ManifestoContent;
import vn.com.be_landingpage.cms.ManifestoContentRepository;
import vn.com.be_landingpage.cms.SocialLink;
import vn.com.be_landingpage.cms.VisualNarrativeImage;
import vn.com.be_landingpage.cms.VisualNarrativeImageRepository;
import vn.com.be_landingpage.orders.BankTransferConfig;
import vn.com.be_landingpage.orders.BankTransferConfigRepository;
import vn.com.be_landingpage.review.Review;
import vn.com.be_landingpage.review.ReviewRepository;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final AdminUserRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final HeroContentRepository heroContentRepository;
    private final ManifestoContentRepository manifestoContentRepository;
    private final VisualNarrativeImageRepository visualNarrativeImageRepository;
    private final CraftsmanshipContentRepository craftsmanshipContentRepository;
    private final FooterContentRepository footerContentRepository;
    private final CollectionChapterRepository chapterRepository;
    private final ProductRepository productRepository;
    private final vn.com.be_landingpage.social.SocialConfigRepository socialConfigRepository;
    private final BankTransferConfigRepository bankTransferConfigRepository;
    private final ReviewRepository reviewRepository;

    @Value("${app.admin.username}")
    private String adminUsername;

    @Value("${app.admin.password}")
    private String adminPassword;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Override
    @Transactional
    public void run(String... args) {
        seedAdmin();
        seedCms();
        seedCatalog();
        seedBankTransferConfig();
        seedReviews();
        seedSocialConfigs();
        repairMojibakeSeedData();
    }

    private void seedAdmin() {
        if (adminUserRepository.existsByUsername(adminUsername)) {
            return;
        }
        AdminUser admin = new AdminUser();
        admin.setUsername(adminUsername);
        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.setEmail(adminEmail);
        admin.setRole(AdminRole.ADMIN);
        admin.setActive(true);
        adminUserRepository.save(admin);
    }

    private void seedCms() {
        if (heroContentRepository.count() == 0) {
            HeroContent hero = new HeroContent();
            hero.setTitle("World Cup 2028.");
            hero.setSubtitle("Bộ sưu tập nơi bóng đá tương lai gặp sự sang trọng tĩnh lặng.");
            hero.setBannerImageUrl("/assets/hero_banner_wc28_1779910952112.png");
            hero.setImageAlt("BloomEcho World Cup 2028 hero banner");
            heroContentRepository.save(hero);
        }

        if (manifestoContentRepository.count() == 0) {
            ManifestoContent manifesto = new ManifestoContent();
            manifesto.setEyebrow("Thiết kế");
            manifesto.setTitle("Chế tác từ sự tĩnh lặng.");
            manifesto.setBody("Mọi chi tiết thừa thãi đều được loại bỏ, nhường chỗ cho đường cắt may khí động học và chất liệu được thử nghiệm kỹ lưỡng.");
            manifesto.setSecondaryBody("Mỗi nguyên liệu phải vượt qua hàng nghìn giờ thử nghiệm trước khi được chọn cho bộ sưu tập.");
            manifesto.setImageUrl("/assets/fabric_wc28_1779910987191.png");
            manifesto.setImageAlt("Close up detail shot of premium textured fabric");
            manifestoContentRepository.save(manifesto);
        }

        if (visualNarrativeImageRepository.count() == 0) {
            visualNarrativeImageRepository.save(visual("Runway silhouette", "World Cup 2028", "/assets/spotlight_wc28_1779910971469.png", 0));
            visualNarrativeImageRepository.save(visual("Home jersey", "Phiên bản giới hạn", "/assets/jersey_1_1779911012494.png", 1));
            visualNarrativeImageRepository.save(visual("Away jersey", "Tối giản", "/assets/jersey_3_1779911038854.png", 2));
            visualNarrativeImageRepository.save(visual("Training kit", "Phong cách thể thao", "/assets/jersey_2_1779911026559.png", 3));
        }

        if (craftsmanshipContentRepository.count() == 0) {
            CraftsmanshipContent craftsmanship = new CraftsmanshipContent();
            craftsmanship.setTitle("May đo cho tương lai.");
            craftsmanship.setDescription("Cấu trúc, chất liệu và kỹ thuật gia công được quản lý như một phần nội dung độc lập của landing page.");
            craftsmanship.setPrimaryImageUrl("/assets/hero_banner_wc28_1779910952112.png");
            craftsmanship.setSecondaryImageUrl("/assets/jersey_1_1779911012494.png");
            craftsmanship.getItems().add(craftItem(craftsmanship, "Sợi dệt bền vững", "Được dệt từ vật liệu tái chế, thoáng khí nhưng vẫn giữ cảm giác cao cấp.", 0));
            craftsmanship.getItems().add(craftItem(craftsmanship, "Độ chính xác trong may đo", "Các đường may liền mạch và phom dáng được tính toán cho chuyển động tự nhiên.", 1));
            craftsmanshipContentRepository.save(craftsmanship);
        }

        if (footerContentRepository.count() == 0) {
            FooterContent footer = new FooterContent();
            footer.setBrandName("BloomEcho");
            footer.setTagline("Được thiết kế dành cho sự tinh tế.");
            footer.setContactEmail("concierge@bloomecho.com");
            footer.setCopyrightText("© 2024 BloomEcho. Bảo lưu mọi quyền.");
            footer.getSocialLinks().add(social(footer, "Facebook", "#", "facebook", 0));
            footer.getSocialLinks().add(social(footer, "Instagram", "#", "instagram", 1));
            footer.getPolicyLinks().add(policy(footer, "Chính sách bảo mật", "#", 0));
            footer.getPolicyLinks().add(policy(footer, "Điều khoản dịch vụ", "#", 1));
            footer.getPolicyLinks().add(policy(footer, "Vận chuyển & Đổi trả", "#", 2));
            footerContentRepository.save(footer);
        }
    }

    private void seedCatalog() {
        CollectionChapter chapter = chapterRepository.findBySlug("world-cup-2028")
                .orElseGet(() -> {
                    CollectionChapter created = new CollectionChapter();
                    created.setSlug("world-cup-2028");
                    created.setTitle("Bộ sưu tập World Cup 2028");
                    created.setSubtitle("Tuyệt tác sân cỏ.");
                    created.setDescription("Sự kết hợp giữa công nghệ dệt may và phom dáng thời trang cao cấp.");
                    created.setCoverImageUrl("/assets/spotlight_wc28_1779910971469.png");
                    created.setSortOrder(0);
                    return chapterRepository.save(created);
                });

        // Thêm 2 chapters nữa cho FE
        if (chapterRepository.findBySlug("street-elite").isEmpty()) {
            CollectionChapter street = new CollectionChapter();
            street.setSlug("street-elite");
            street.setTitle("Street Elite");
            street.setSubtitle("Phong cách đường phố.");
            street.setDescription("Bộ sưu tập dành cho những ai yêu thích sự tự do và cá tính trên đường phố.");
            street.setCoverImageUrl("/assets/jersey_1_1779911012494.png");
            street.setSortOrder(1);
            street.setActive(true);
            chapterRepository.save(street);
        }

        if (chapterRepository.findBySlug("midnight-series").isEmpty()) {
            CollectionChapter midnight = new CollectionChapter();
            midnight.setSlug("midnight-series");
            midnight.setTitle("Midnight Series");
            midnight.setSubtitle("Đêm xuống, phong cách lên.");
            midnight.setDescription("Dòng sản phẩm tối giản, sang trọng cho buổi tối và sự kiện đặc biệt.");
            midnight.setCoverImageUrl("/assets/jersey_3_1779911038854.png");
            midnight.setSortOrder(2);
            midnight.setActive(true);
            chapterRepository.save(midnight);
        }

        if (productRepository.count() > 0) {
            return;
        }

        seedProduct(chapter, "ao-dau-world-cup-2028-san-nha", "Áo Đấu World Cup 2028 - Sân Nhà", "Phiên bản Giới hạn", "2500000", "/assets/jersey_1_1779911012494.png", 0);
        seedProduct(chapter, "quan-the-thao-world-cup-2028", "Quần Thể Thao World Cup 2028", "Thiết kế Tương lai", "1300000", "/assets/jersey_2_1779911026559.png", 1);
        seedProduct(chapter, "ao-khoac-world-cup-2028", "Áo Khoác World Cup 2028", "Phiên bản Đặc biệt", "3250000", "/assets/jersey_3_1779911038854.png", 2);
        seedProduct(chapter, "ao-dau-world-cup-2028-san-khach", "Áo Đấu World Cup 2028 - Sân Khách", "Phiên bản Tối giản", "2500000", "/assets/spotlight_wc28_1779910971469.png", 3);
    }

    private VisualNarrativeImage visual(String title, String caption, String imageUrl, int sortOrder) {
        VisualNarrativeImage image = new VisualNarrativeImage();
        image.setTitle(title);
        image.setCaption(caption);
        image.setImageUrl(imageUrl);
        image.setImageAlt(title);
        image.setSortOrder(sortOrder);
        image.setActive(true);
        return image;
    }

    private CraftsmanshipItem craftItem(CraftsmanshipContent content, String title, String description, int sortOrder) {
        CraftsmanshipItem item = new CraftsmanshipItem();
        item.setContent(content);
        item.setTitle(title);
        item.setDescription(description);
        item.setSortOrder(sortOrder);
        item.setActive(true);
        return item;
    }

    private SocialLink social(FooterContent footer, String platform, String url, String icon, int sortOrder) {
        SocialLink link = new SocialLink();
        link.setFooter(footer);
        link.setPlatform(platform);
        link.setUrl(url);
        link.setIcon(icon);
        link.setSortOrder(sortOrder);
        link.setActive(true);
        return link;
    }

    private FooterPolicyLink policy(FooterContent footer, String label, String url, int sortOrder) {
        FooterPolicyLink link = new FooterPolicyLink();
        link.setFooter(footer);
        link.setLabel(label);
        link.setUrl(url);
        link.setSortOrder(sortOrder);
        link.setActive(true);
        return link;
    }

    private void seedBankTransferConfig() {
        if (bankTransferConfigRepository.count() > 0) return;
        BankTransferConfig config = new BankTransferConfig();
        config.setBankName("Vietcombank");
        config.setBankCode("VCB");
        config.setAccountNumber("0123456789");
        config.setAccountName("CONG TY TNHH BLOOMECHO");
        config.setActive(true);
        bankTransferConfigRepository.save(config);
    }

    private void seedReviews() {
        if (reviewRepository.count() > 0) return;

        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) return;

        Product p1 = products.get(0);
        reviewRepository.save(createReview(p1.getId(), "Minh Tuấn", 5, "Áo mặc rất thoải mái, chất liệu tốt!"));
        reviewRepository.save(createReview(p1.getId(), "Hoàng Anh", 4, "Phom dáng đẹp, giao hàng nhanh."));
        reviewRepository.save(createReview(p1.getId(), "Thanh Hằng", 5, "Đỉnh cao của thể thao & thời trang."));

        if (products.size() > 1) {
            Product p2 = products.get(1);
            reviewRepository.save(createReview(p2.getId(), "Quốc Bảo", 4, "Quần đẹp, form chuẩn."));
            reviewRepository.save(createReview(p2.getId(), "Ngọc Trâm", 5, "Mua làm quà tặng, người nhận rất thích."));
        }

        if (products.size() > 2) {
            Product p3 = products.get(2);
            reviewRepository.save(createReview(p3.getId(), "Văn Hùng", 5, "Áo khoác giữ ấm tốt, thiết kế cực chất."));
            reviewRepository.save(createReview(p3.getId(), "Mai Phương", 4, "Đáng tiền, sẽ mua thêm."));
        }
    }

    private Review createReview(Long productId, String customerName, int rating, String comment) {
        Review review = new Review();
        review.setProductId(productId);
        review.setCustomerName(customerName);
        review.setRating(rating);
        review.setComment(comment);
        review.setActive(true);
        return review;
    }

    private void seedSocialConfigs() {
        if (socialConfigRepository.count() > 0) return;
        socialConfigRepository.save(social("instagram", "https://instagram.com/bloomecho", null, "BloomEcho Instagram", 0));
        socialConfigRepository.save(social("facebook", "https://facebook.com/bloomecho", null, "BloomEcho Facebook", 1));
    }

    private vn.com.be_landingpage.social.SocialConfig social(String platform, String url, String imageUrl, String title, int sortOrder) {
        vn.com.be_landingpage.social.SocialConfig config = new vn.com.be_landingpage.social.SocialConfig();
        config.setPlatform(platform);
        config.setUrl(url);
        config.setImageUrl(imageUrl);
        config.setPageTitle(title);
        config.setSortOrder(sortOrder);
        config.setActive(true);
        return config;
    }

    private void seedProduct(CollectionChapter chapter, String slug, String name, String tag, String price, String imageUrl, int sortOrder) {
        Product product = new Product();
        product.setChapter(chapter);
        product.setSlug(slug);
        product.setName(name);
        product.setTag(tag);
        product.setPrice(new BigDecimal(price));
        product.setDescription("Thiết kế thuộc bộ sưu tập World Cup 2028, tối ưu cho trải nghiệm thời trang thể thao cao cấp.");
        product.setMaterial("Polyester tái chế, cotton kỹ thuật và sợi co giãn cao cấp.");
        product.getSizes().addAll(List.of("XS", "S", "M", "L", "XL"));
        product.setSortOrder(sortOrder);
        product.setActive(true);

        ProductImage image = new ProductImage();
        image.setProduct(product);
        image.setImageUrl(imageUrl);
        image.setAltText(name);
        image.setSortOrder(0);
        image.setPrimaryImage(true);
        product.getImages().add(image);

        productRepository.save(product);
    }

    private void repairMojibakeSeedData() {
        heroContentRepository.findAll().stream().findFirst().ifPresent(hero -> {
            if (looksMojibake(hero.getSubtitle())) {
                hero.setTitle("World Cup 2028.");
                hero.setSubtitle("Bộ sưu tập nơi bóng đá tương lai gặp sự sang trọng tĩnh lặng.");
                hero.setCtaText("Khám phá ngay");
                hero.setImageAlt("BloomEcho World Cup 2028 hero banner");
                heroContentRepository.save(hero);
            }
        });

        manifestoContentRepository.findAll().stream().findFirst().ifPresent(manifesto -> {
            if (looksMojibake(manifesto.getTitle()) || looksMojibake(manifesto.getBody())) {
                manifesto.setEyebrow("Thiết kế");
                manifesto.setTitle("Chế tác từ sự tĩnh lặng.");
                manifesto.setBody("Mọi chi tiết thừa thãi đều được loại bỏ, nhường chỗ cho đường cắt may khí động học và chất liệu được thử nghiệm kỹ lưỡng.");
                manifesto.setSecondaryBody("Mỗi nguyên liệu phải vượt qua hàng nghìn giờ thử nghiệm trước khi được chọn cho bộ sưu tập.");
                manifestoContentRepository.save(manifesto);
            }
        });

        craftsmanshipContentRepository.findAll().stream().findFirst().ifPresent(craftsmanship -> {
            if (looksMojibake(craftsmanship.getTitle()) || looksMojibake(craftsmanship.getDescription())) {
                craftsmanship.setTitle("May đo cho tương lai.");
                craftsmanship.setDescription("Cấu trúc, chất liệu và kỹ thuật gia công được quản lý như một phần nội dung độc lập của landing page.");
                if (craftsmanship.getItems().size() >= 2) {
                    craftsmanship.getItems().get(0).setTitle("Sợi dệt bền vững");
                    craftsmanship.getItems().get(0).setDescription("Được dệt từ vật liệu tái chế, thoáng khí nhưng vẫn giữ cảm giác cao cấp.");
                    craftsmanship.getItems().get(1).setTitle("Độ chính xác trong may đo");
                    craftsmanship.getItems().get(1).setDescription("Các đường may liền mạch và phom dáng được tính toán cho chuyển động tự nhiên.");
                }
                craftsmanshipContentRepository.save(craftsmanship);
            }
        });

        footerContentRepository.findAll().stream().findFirst().ifPresent(footer -> {
            if (looksMojibake(footer.getTagline()) || looksMojibake(footer.getCopyrightText())) {
                footer.setTagline("Được thiết kế dành cho sự tinh tế.");
                footer.setCopyrightText("© 2024 BloomEcho. Bảo lưu mọi quyền.");
                footer.getPolicyLinks().forEach(link -> {
                    if (looksMojibake(link.getLabel())) {
                        if (link.getSortOrder() == 0) link.setLabel("Chính sách bảo mật");
                        if (link.getSortOrder() == 1) link.setLabel("Điều khoản dịch vụ");
                        if (link.getSortOrder() == 2) link.setLabel("Vận chuyển & Đổi trả");
                    }
                });
                footerContentRepository.save(footer);
            }
        });

        chapterRepository.findBySlug("world-cup-2028").ifPresent(chapter -> {
            if (looksMojibake(chapter.getTitle()) || looksMojibake(chapter.getSubtitle())) {
                chapter.setTitle("Bộ sưu tập World Cup 2028");
                chapter.setSubtitle("Tuyệt tác sân cỏ.");
                chapter.setDescription("Sự kết hợp giữa công nghệ dệt may và phom dáng thời trang cao cấp.");
                chapterRepository.save(chapter);
            }
        });

        productRepository.findBySlug("ao-dau-world-cup-2028-san-nha").ifPresent(product ->
                repairProduct(product, "Áo Đấu World Cup 2028 - Sân Nhà", "Phiên bản Giới hạn"));
        productRepository.findBySlug("quan-the-thao-world-cup-2028").ifPresent(product ->
                repairProduct(product, "Quần Thể Thao World Cup 2028", "Thiết kế Tương lai"));
        productRepository.findBySlug("ao-khoac-world-cup-2028").ifPresent(product ->
                repairProduct(product, "Áo Khoác World Cup 2028", "Phiên bản Đặc biệt"));
        productRepository.findBySlug("ao-dau-world-cup-2028-san-khach").ifPresent(product ->
                repairProduct(product, "Áo Đấu World Cup 2028 - Sân Khách", "Phiên bản Tối giản"));
    }

    private void repairProduct(Product product, String name, String tag) {
        if (!looksMojibake(product.getName()) && !looksMojibake(product.getTag())) {
            return;
        }
        product.setName(name);
        product.setTag(tag);
        product.setDescription("Thiết kế thuộc bộ sưu tập World Cup 2028, tối ưu cho trải nghiệm thời trang thể thao cao cấp.");
        product.setMaterial("Polyester tái chế, cotton kỹ thuật và sợi co giãn cao cấp.");
        product.getImages().forEach(image -> image.setAltText(name));
        productRepository.save(product);
    }

    private boolean looksMojibake(String value) {
        return value != null && (
                value.contains("\u00C3")
                        || value.contains("\u00C2")
                        || value.contains("\u00C4")
                        || value.contains("\u00C6")
                        || value.contains("\u00C5")
                        || value.contains("á»")
        );
    }
}
