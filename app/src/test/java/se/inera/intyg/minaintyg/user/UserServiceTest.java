package se.inera.intyg.minaintyg.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import se.inera.intyg.minaintyg.auth.LoginMethod;
import se.inera.intyg.minaintyg.auth.MinaIntygUser;
import se.inera.intyg.minaintyg.integration.api.user.model.LoggedInMinaIntygUser;
import se.inera.intyg.minaintyg.testhelper.TestPrincipalHelper;

class UserServiceTest {

  private static final String PERSON_ID = "personId";
  private static final String PERSON_NAME = "personName";
  private static final LoginMethod LOGIN_METHOD = LoginMethod.ELVA77;

  private final UserService userService = new UserService();

  @Test
  void shouldReturnUserFromPrincipal() {
    final var expectedUser = Optional.of(
        MinaIntygUser.builder()
            .personId(PERSON_ID)
            .personName(PERSON_NAME)
            .loginMethod(LOGIN_METHOD)
            .build()
    );
    TestPrincipalHelper.setMinaIntygUserAsPrincipal(expectedUser.get());
    final var actualUser = userService.getLoggedInUser();
    assertEquals(expectedUser, actualUser);
  }

  @Test
  void shouldNotReturnUserFromPrincipalIfPrincipalNotIsInstanceOfMinaIntygUser() {
    final var user = new Object();
    TestPrincipalHelper.setUnknownPrincipal(user);
    final var actualUser = userService.getLoggedInUser();
    assertTrue(actualUser.isEmpty());
  }

  @Test
  void shallReturnLoggedInMinaIntygUserWhenUserIsLoggedIn() {
    final var expected = LoggedInMinaIntygUser.builder()
        .personId(PERSON_ID)
        .build();

    final var expectedUser = Optional.of(
        MinaIntygUser.builder()
            .personId(PERSON_ID)
            .personName(PERSON_NAME)
            .loginMethod(LOGIN_METHOD)
            .build()
    );
    TestPrincipalHelper.setMinaIntygUserAsPrincipal(expectedUser.get());

    final var actual = userService.loggedInMinaIntygUser();

    assertEquals(expected, actual);
  }

  @Test
  void shallThrowExceptionForLoggedInMinaIntygUserIfNoUserLoggedIn() {
    assertThrows(IllegalStateException.class, userService::loggedInMinaIntygUser);
  }
}
