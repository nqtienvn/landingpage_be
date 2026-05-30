package vn.com.be_landingpage.cms;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/cms")
@RequiredArgsConstructor
public class AdminCmsController {

    private final CmsService cmsService;

    @GetMapping("/hero")
    public CmsDtos.HeroResponse getHero() {
        return cmsService.getHero();
    }

    @PutMapping("/hero")
    public CmsDtos.HeroResponse updateHero(@Valid @RequestBody CmsDtos.HeroRequest request) {
        return cmsService.updateHero(request);
    }

    @GetMapping("/manifesto")
    public CmsDtos.ManifestoResponse getManifesto() {
        return cmsService.getManifesto();
    }

    @PutMapping("/manifesto")
    public CmsDtos.ManifestoResponse updateManifesto(@Valid @RequestBody CmsDtos.ManifestoRequest request) {
        return cmsService.updateManifesto(request);
    }

    @GetMapping("/visual-narrative")
    public List<CmsDtos.VisualNarrativeResponse> getVisualNarrative() {
        return cmsService.getAdminVisualNarrative();
    }

    @PostMapping("/visual-narrative")
    public CmsDtos.VisualNarrativeResponse createVisualNarrative(
            @Valid @RequestBody CmsDtos.VisualNarrativeRequest request
    ) {
        return cmsService.createVisualNarrative(request);
    }

    @PutMapping("/visual-narrative/{id}")
    public CmsDtos.VisualNarrativeResponse updateVisualNarrative(
            @PathVariable Long id,
            @Valid @RequestBody CmsDtos.VisualNarrativeRequest request
    ) {
        return cmsService.updateVisualNarrative(id, request);
    }

    @DeleteMapping("/visual-narrative/{id}")
    public void deleteVisualNarrative(@PathVariable Long id) {
        cmsService.deleteVisualNarrative(id);
    }

    @GetMapping("/craftsmanship")
    public CmsDtos.CraftsmanshipResponse getCraftsmanship() {
        return cmsService.getAdminCraftsmanship();
    }

    @PutMapping("/craftsmanship")
    public CmsDtos.CraftsmanshipResponse updateCraftsmanship(
            @Valid @RequestBody CmsDtos.CraftsmanshipRequest request
    ) {
        return cmsService.updateCraftsmanship(request);
    }

    @GetMapping("/footer")
    public CmsDtos.FooterResponse getFooter() {
        return cmsService.getAdminFooter();
    }

    @PutMapping("/footer")
    public CmsDtos.FooterResponse updateFooter(@Valid @RequestBody CmsDtos.FooterRequest request) {
        return cmsService.updateFooter(request);
    }
}
