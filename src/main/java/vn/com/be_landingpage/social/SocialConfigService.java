package vn.com.be_landingpage.social;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.be_landingpage.exception.ResourceNotFoundException;

@Service
@RequiredArgsConstructor
public class SocialConfigService {

    private final SocialConfigRepository repository;

    @Transactional(readOnly = true)
    public List<SocialConfigDtos.SocialConfigResponse> getPublicConfigs() {
        return repository.findAllByActiveTrueOrderBySortOrderAscIdAsc()
                .stream().map(SocialConfigDtos.SocialConfigResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<SocialConfigDtos.SocialConfigResponse> getAllConfigs() {
        return repository.findAllByOrderBySortOrderAscIdAsc()
                .stream().map(SocialConfigDtos.SocialConfigResponse::from).toList();
    }

    @Transactional
    public SocialConfigDtos.SocialConfigResponse createConfig(SocialConfigDtos.SocialConfigRequest request) {
        SocialConfig config = new SocialConfig();
        applyFields(config, request);
        return SocialConfigDtos.SocialConfigResponse.from(repository.save(config));
    }

    @Transactional
    public SocialConfigDtos.SocialConfigResponse updateConfig(Long id, SocialConfigDtos.SocialConfigRequest request) {
        SocialConfig config = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy cấu hình mạng xã hội id=" + id));
        applyFields(config, request);
        return SocialConfigDtos.SocialConfigResponse.from(repository.save(config));
    }

    @Transactional
    public void deleteConfig(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Không tìm thấy cấu hình mạng xã hội id=" + id);
        }
        repository.deleteById(id);
    }

    private void applyFields(SocialConfig config, SocialConfigDtos.SocialConfigRequest request) {
        config.setPlatform(request.platform().trim().toLowerCase());
        config.setUrl(request.url());
        config.setImageUrl(request.imageUrl());
        config.setPageTitle(request.pageTitle());
        config.setDescription(request.description());
        config.setFollowUrl(request.followUrl());
        config.setSortOrder(request.sortOrder() != null ? request.sortOrder() : 0);
        config.setActive(request.active() == null || request.active());
    }
}
