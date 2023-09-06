package se.inera.intyg.minaintyg.testability;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/testability")
public class TestabilityController {

    private final TestPersonService testPersonService;
    @PostMapping("/logout")
    public void logout(HttpServletRequest request) {
        invalidateSessionAndClearContext(request);
    }

    @GetMapping(value = "/person", produces = MediaType.APPLICATION_JSON_VALUE)
    public TestPersonResponse person() {
        final var persons = testPersonService.getPersons();
        return TestPersonResponse.builder()
                .testPerson(persons)
                .build();
    }

    private void invalidateSessionAndClearContext(HttpServletRequest request) {
        request.getSession().invalidate();
        SecurityContextHolder.getContext().setAuthentication(null);
        SecurityContextHolder.clearContext();
    }
}
