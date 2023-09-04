package se.inera.intyg.minaintyg.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MinaIntygController {

    @RequestMapping("/")
    public String index() {
        System.out.println("Principal: " + SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return "index";
    }

    @GetMapping("/error")
    public String error() {
        return "error";
    }

    @GetMapping("/config")
    public String config() {
        return "index";
    }

    @GetMapping("/test")
    public String test(@AuthenticationPrincipal Object user) {
        return "index";
    }

}
