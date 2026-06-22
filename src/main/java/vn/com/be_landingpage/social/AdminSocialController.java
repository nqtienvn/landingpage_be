package vn.com.be_landingpage.social;

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
@RequestMapping("/api/admin/social-configs")
@RequiredArgsConstructor
public class AdminSocialController {

    private final SocialConfigService service;

    @GetMapping
    public List<SocialConfigDtos.SocialConfigResponse> getAll() {
        return service.getAllConfigs();
    }

    @PostMapping
    public SocialConfigDtos.SocialConfigResponse create(@Valid @RequestBody SocialConfigDtos.SocialConfigRequest request) {
        return service.createConfig(request);
    }

    @PutMapping("/{id}")
    public SocialConfigDtos.SocialConfigResponse update(
            @PathVariable Long id,
            @Valid @RequestBody SocialConfigDtos.SocialConfigRequest request
    ) {
        return service.updateConfig(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteConfig(id);
    }
}
