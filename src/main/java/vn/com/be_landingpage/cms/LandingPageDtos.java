package vn.com.be_landingpage.cms;

import java.util.List;
import vn.com.be_landingpage.catalog.CatalogDtos;

public final class LandingPageDtos {

    private LandingPageDtos() {
    }

    public record LandingPageResponse(
            CmsDtos.HeroResponse hero,
            CmsDtos.ManifestoResponse manifesto,
            List<CmsDtos.VisualNarrativeResponse> visualNarrative,
            CmsDtos.CraftsmanshipResponse craftsmanship,
            CmsDtos.FooterResponse footer,
            List<CatalogDtos.ChapterResponse> chapters,
            List<CatalogDtos.ProductResponse> products
    ) {
    }
}
