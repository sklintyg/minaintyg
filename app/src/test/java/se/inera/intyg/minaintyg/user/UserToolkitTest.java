package se.inera.intyg.minaintyg.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import se.inera.intyg.minaintyg.auth.LoginMethod;
import se.inera.intyg.minaintyg.auth.MinaIntygUser;
import se.inera.intyg.minaintyg.testhelper.TestPrincipalHelper;

class UserToolkitTest {

  private static final String PERSON_ID = "personId";
  private static final String PERSON_NAME = "personName";
  private static final LoginMethod LOGIN_METHOD = LoginMethod.ELVA77;

  @Test
  void shouldReturnOptionalEmptyIfAuthenticationIsNull() {
    TestPrincipalHelper.setUnknownPrincipal(null);
    final var userFromPrincipal = UserToolkit.getUserFromPrincipal();
    assertNull(userFromPrincipal.orElse(null));
  }

  @Test
  void shouldReturnOptionalEmptyPrincipalIsNotInstanceOfMinaIntygUser() {
    final var expectedResult = MinaIntygUser.builder()
        .userId(PERSON_ID)
        .userName(PERSON_NAME)
        .loginMethod(LOGIN_METHOD)
        .build();
    TestPrincipalHelper.setMinaIntygUserAsPrincipal(expectedResult);
    final var userFromPrincipal = UserToolkit.getUserFromPrincipal();
    assertEquals(expectedResult, userFromPrincipal.orElse(null));
  }
}