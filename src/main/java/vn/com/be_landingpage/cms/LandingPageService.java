package vn.com.be_landingpage.cms;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import vn.com.be_landingpage.catalog.CatalogService;
import vn.com.be_landingpage.review.ReviewDtos;
import vn.com.be_landingpage.review.ReviewService;

@Service
@RequiredArgsConstructor
public class LandingPageService {

    private final CmsService cmsService;
    private final CatalogService catalogService;
    private final ReviewService reviewService;

    @Cacheable(value = "landingPage", key = "'home'")
    public LandingPageDtos.LandingPageResponse getLandingPage() {
        return new LandingPageDtos.LandingPageResponse(
                cmsService.getHero(),
                cmsService.getManifesto(),
                cmsService.getPublicVisualNarrative(),
                cmsService.getPublicCraftsmanship(),
                cmsService.getPublicFooter(),
                catalogService.getPublicChapters(),
                catalogService.getPublicProducts(),
                reviewService.getTopReviews(10)
        );
    }
}
