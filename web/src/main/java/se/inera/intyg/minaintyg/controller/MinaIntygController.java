package se.inera.intyg.minaintyg.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.saml2.provider.service.authentication.Saml2AuthenticatedPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MinaIntygController {

    @RequestMapping("/")
    public String index(Model model, @AuthenticationPrincipal Saml2AuthenticatedPrincipal principal) {
        if (principal != null) {
            model.addAttribute("userAttributes", principal.getAttributes());
        }
        return "index";
    }

    @GetMapping("/error")
    public String error() {
        return "error";
    }

}
