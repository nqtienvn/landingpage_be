package vn.com.be_landingpage.cms;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.be_landingpage.exception.ResourceNotFoundException;

@Service
@RequiredArgsConstructor
public class CmsService {

    private final HeroContentRepository heroContentRepository;
    private final ManifestoContentRepository manifestoContentRepository;
    private final VisualNarrativeImageRepository visualNarrativeImageRepository;
    private final CraftsmanshipContentRepository craftsmanshipContentRepository;
    private final FooterContentRepository footerContentRepository;

    @Cacheable(value = "cms", key = "'hero'")
    @Transactional
    public CmsDtos.HeroResponse getHero() {
        return CmsDtos.HeroResponse.from(hero());
    }

    @Transactional
    @CacheEvict(value = {"cms", "landingPage"}, allEntries = true)
    public CmsDtos.HeroResponse updateHero(CmsDtos.HeroRequest request) {
        HeroContent content = hero();
        content.setTitle(request.title());
        content.setSubtitle(request.subtitle());
        content.setCtaText(request.ctaText());
        content.setCtaHref(request.ctaHref());
        content.setBannerImageUrl(request.bannerImageUrl());
        content.setBannerImagePublicId(request.bannerImagePublicId());
        content.setImageAlt(request.imageAlt());
        return CmsDtos.HeroResponse.from(heroContentRepository.save(content));
    }

    @Cacheable(value = "cms", key = "'manifesto'")
    @Transactional
    public CmsDtos.ManifestoResponse getManifesto() {
        return CmsDtos.ManifestoResponse.from(manifesto());
    }

    @Transactional
    @CacheEvict(value = {"cms", "landingPage"}, allEntries = true)
    public CmsDtos.ManifestoResponse updateManifesto(CmsDtos.ManifestoRequest request) {
        ManifestoContent content = manifesto();
        content.setEyebrow(request.eyebrow());
        content.setTitle(request.title());
        content.setBody(request.body());
        content.setSecondaryBody(request.secondaryBody());
        content.setImageUrl(request.imageUrl());
        content.setImagePublicId(request.imagePublicId());
        content.setImageAlt(request.imageAlt());
        return CmsDtos.ManifestoResponse.from(manifestoContentRepository.save(content));
    }

    @Cacheable(value = "cms", key = "'visualNarrative'")
    @Transactional(readOnly = true)
    public List<CmsDtos.VisualNarrativeResponse> getPublicVisualNarrative() {
        return visualNarrativeImageRepository.findAllByActiveTrueOrderBySortOrderAscIdAsc().stream()
                .map(CmsDtos.VisualNarrativeResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CmsDtos.VisualNarrativeResponse> getAdminVisualNarrative() {
        return visualNarrativeImageRepository.findAllByOrderBySortOrderAscIdAsc().stream()
                .map(CmsDtos.VisualNarrativeResponse::from)
                .toList();
    }

    @Transactional
    @CacheEvict(value = {"cms", "landingPage"}, allEntries = true)
    public CmsDtos.VisualNarrativeResponse createVisualNarrative(CmsDtos.VisualNarrativeRequest request) {
        VisualNarrativeImage image = new VisualNarrativeImage();
        applyVisualNarrative(image, request);
        return CmsDtos.VisualNarrativeResponse.from(visualNarrativeImageRepository.save(image));
    }

    @Transactional
    @CacheEvict(value = {"cms", "landingPage"}, allEntries = true)
    public CmsDtos.VisualNarrativeResponse updateVisualNarrative(Long id, CmsDtos.VisualNarrativeRequest request) {
        VisualNarrativeImage image = visualNarrativeImageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy ảnh visual narrative id=" + id));
        applyVisualNarrative(image, request);
        return CmsDtos.VisualNarrativeResponse.from(visualNarrativeImageRepository.save(image));
    }

    @Transactional
    @CacheEvict(value = {"cms", "landingPage"}, allEntries = true)
    public void deleteVisualNarrative(Long id) {
        if (!visualNarrativeImageRepository.existsById(id)) {
            throw new ResourceNotFoundException("Không tìm thấy ảnh visual narrative id=" + id);
        }
        visualNarrativeImageRepository.deleteById(id);
    }

    @Cacheable(value = "cms", key = "'craftsmanship'")
    @Transactional
    public CmsDtos.CraftsmanshipResponse getPublicCraftsmanship() {
        return CmsDtos.CraftsmanshipResponse.from(craftsmanship(), true);
    }

    @Transactional
    public CmsDtos.CraftsmanshipResponse getAdminCraftsmanship() {
        return CmsDtos.CraftsmanshipResponse.from(craftsmanship(), false);
    }

    @Transactional
    @CacheEvict(value = {"cms", "landingPage"}, allEntries = true)
    public CmsDtos.CraftsmanshipResponse updateCraftsmanship(CmsDtos.CraftsmanshipRequest request) {
        CraftsmanshipContent content = craftsmanship();
        content.setTitle(request.title());
        content.setDescription(request.description());
        content.setPrimaryImageUrl(request.primaryImageUrl());
        content.setPrimaryImagePublicId(request.primaryImagePublicId());
        content.setSecondaryImageUrl(request.secondaryImageUrl());
        content.setSecondaryImagePublicId(request.secondaryImagePublicId());
        content.getItems().clear();
        if (request.items() != null) {
            for (CmsDtos.CraftsmanshipItemRequest itemRequest : request.items()) {
                CraftsmanshipItem item = new CraftsmanshipItem();
                item.setContent(content);
                item.setTitle(itemRequest.title());
                item.setDescription(itemRequest.description());
                item.setImageUrl(itemRequest.imageUrl());
                item.setImagePublicId(itemRequest.imagePublicId());
                item.setSortOrder(itemRequest.sortOrder() == null ? 0 : itemRequest.sortOrder());
                item.setActive(itemRequest.active() == null || itemRequest.active());
                content.getItems().add(item);
            }
        }
        return CmsDtos.CraftsmanshipResponse.from(craftsmanshipContentRepository.save(content), false);
    }

    @Cacheable(value = "cms", key = "'footer'")
    @Transactional
    public CmsDtos.FooterResponse getPublicFooter() {
        return CmsDtos.FooterResponse.from(footer(), true);
    }

    @Transactional
    public CmsDtos.FooterResponse getAdminFooter() {
        return CmsDtos.FooterResponse.from(footer(), false);
    }

    @Transactional
    @CacheEvict(value = {"cms", "landingPage"}, allEntries = true)
    public CmsDtos.FooterResponse updateFooter(CmsDtos.FooterRequest request) {
        FooterContent content = footer();
        content.setBrandName(request.brandName());
        content.setTagline(request.tagline());
        content.setContactEmail(request.contactEmail());
        content.setContactPhone(request.contactPhone());
        content.setAddress(request.address());
        content.setCopyrightText(request.copyrightText());

        content.getSocialLinks().clear();
        if (request.socialLinks() != null) {
            for (CmsDtos.SocialLinkRequest linkRequest : request.socialLinks()) {
                SocialLink link = new SocialLink();
                link.setFooter(content);
                link.setPlatform(linkRequest.platform());
                link.setUrl(linkRequest.url());
                link.setIcon(linkRequest.icon());
                link.setSortOrder(linkRequest.sortOrder() == null ? 0 : linkRequest.sortOrder());
                link.setActive(linkRequest.active() == null || linkRequest.active());
                content.getSocialLinks().add(link);
            }
        }

        content.getPolicyLinks().clear();
        if (request.policyLinks() != null) {
            for (CmsDtos.FooterPolicyLinkRequest linkRequest : request.policyLinks()) {
                FooterPolicyLink link = new FooterPolicyLink();
                link.setFooter(content);
                link.setLabel(linkRequest.label());
                link.setUrl(linkRequest.url());
                link.setSortOrder(linkRequest.sortOrder() == null ? 0 : linkRequest.sortOrder());
                link.setActive(linkRequest.active() == null || linkRequest.active());
                content.getPolicyLinks().add(link);
            }
        }

        return CmsDtos.FooterResponse.from(footerContentRepository.save(content), false);
    }

    private void applyVisualNarrative(VisualNarrativeImage image, CmsDtos.VisualNarrativeRequest request) {
        image.setTitle(request.title());
        image.setCaption(request.caption());
        image.setDescription(request.description());
        image.setImageUrl(request.imageUrl());
        image.setImagePublicId(request.imagePublicId());
        image.setImageAlt(request.imageAlt());
        image.setSortOrder(request.sortOrder() == null ? 0 : request.sortOrder());
        image.setActive(request.active() == null || request.active());
    }

    private HeroContent hero() {
        return heroContentRepository.findAll().stream()
                .findFirst()
                .orElseGet(() -> heroContentRepository.save(new HeroContent()));
    }

    private ManifestoContent manifesto() {
        return manifestoContentRepository.findAll().stream()
                .findFirst()
                .orElseGet(() -> manifestoContentRepository.save(new ManifestoContent()));
    }

    private CraftsmanshipContent craftsmanship() {
        return craftsmanshipContentRepository.findAll().stream()
                .findFirst()
                .orElseGet(() -> craftsmanshipContentRepository.save(new CraftsmanshipContent()));
    }

    private FooterContent footer() {
        return footerContentRepository.findAll().stream()
                .findFirst()
                .orElseGet(() -> footerContentRepository.save(new FooterContent()));
    }
}
