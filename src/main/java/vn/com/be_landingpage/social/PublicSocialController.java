package vn.com.be_landingpage.social;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
public class PublicSocialController {

    private final SocialConfigService service;

    @GetMapping("/social-configs")
    public List<SocialConfigDtos.SocialConfigResponse> getSocialConfigs() {
        return service.getPublicConfigs();
    }
}
