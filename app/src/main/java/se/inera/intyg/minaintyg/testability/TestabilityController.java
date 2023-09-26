package se.inera.intyg.minaintyg.testability;

import static se.inera.intyg.minaintyg.config.WebSecurityConfig.TESTABILITY_PROFILE;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.inera.intyg.minaintyg.auth.FakeCredentials;

@RestController
@RequiredArgsConstructor
@Profile(TESTABILITY_PROFILE)
@RequestMapping("/api/testability")
public class TestabilityController {

  private final FakeLoginService fakeAuthService;
  private final TestPersonService testPersonService;

  @PostMapping("/fake")
  public void login(@RequestBody FakeCredentials fakeCredentials,
      final HttpServletRequest request) {
    fakeAuthService.login(fakeCredentials, request);
  }

  @PostMapping("/logout")
  public void logout(HttpServletRequest request) {
    fakeAuthService.logout(request.getSession(false));
  }

  @GetMapping(value = "/person", produces = MediaType.APPLICATION_JSON_VALUE)
  public TestPersonResponse person() {
    final var persons = testPersonService.getPersons();
    return TestPersonResponse.builder()
        .testPerson(persons)
        .build();
  }
}
