package vn.com.be_landingpage.cms;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/cms")
@RequiredArgsConstructor
public class PublicCmsController {

    private final CmsService cmsService;

    @GetMapping("/hero")
    public CmsDtos.HeroResponse hero() {
        return cmsService.getHero();
    }

    @GetMapping("/manifesto")
    public CmsDtos.ManifestoResponse manifesto() {
        return cmsService.getManifesto();
    }

    @GetMapping("/visual-narrative")
    public List<CmsDtos.VisualNarrativeResponse> visualNarrative() {
        return cmsService.getPublicVisualNarrative();
    }

    @GetMapping("/craftsmanship")
    public CmsDtos.CraftsmanshipResponse craftsmanship() {
        return cmsService.getPublicCraftsmanship();
    }

    @GetMapping("/footer")
    public CmsDtos.FooterResponse footer() {
        return cmsService.getPublicFooter();
    }
}
