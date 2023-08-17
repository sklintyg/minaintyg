package se.inera.intyg.minaintyg.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import se.inera.intyg.minaintyg.dto.HelloResponseDTO;
import se.inera.intyg.minaintyg.service.HelloService;

@RestController("/api/hello")
@RequiredArgsConstructor
public class HelloController {

    private final HelloService helloService;

    @GetMapping("/message")
    public HelloResponseDTO message() {
        return helloService.hello();
    }
}
