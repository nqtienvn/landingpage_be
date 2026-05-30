package vn.com.be_landingpage.cms;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
public class PublicLandingPageController {

    private final LandingPageService landingPageService;

    @GetMapping("/landing-page")
    public LandingPageDtos.LandingPageResponse landingPage() {
        return landingPageService.getLandingPage();
    }
}
