package se.inera.intyg.minaintyg.testability;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.session.Session;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.inera.intyg.minaintyg.auth.MinaIntygLoggingSessionRegistryImpl;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/testability")
public class TestabilityController {

  private final TestPersonService testPersonService;
  private final MinaIntygLoggingSessionRegistryImpl<? extends Session> minaIntygLoggingSessionRegistry;

  @PostMapping("/logout")
  public void logout(HttpServletRequest request) {
    minaIntygLoggingSessionRegistry.removeSessionInformation(request.getSession().getId());
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
