package se.inera.intyg.minaintyg.user;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import se.inera.intyg.minaintyg.auth.LoginMethod;
import se.inera.intyg.minaintyg.auth.MinaIntygUser;
import se.inera.intyg.minaintyg.testhelper.TestPrincipalHelper;

class MinaIntygUserServiceImplTest {

  private static final String PERSON_ID = "personId";
  private static final String PERSON_NAME = "personName";
  private static final LoginMethod LOGIN_METHOD = LoginMethod.ELVA77;

  private final MinaIntygUserServiceImpl minaIntygUserService = new MinaIntygUserServiceImpl();

  @Test
  void shouldReturnUserFromPrincipal() {
    final var expectedUser = new MinaIntygUser(PERSON_ID, PERSON_NAME, LOGIN_METHOD);
    TestPrincipalHelper.setMinaIntygUserAsPrincipal(expectedUser);
    final var actualUser = minaIntygUserService.getUser();
    assertEquals(expectedUser, actualUser);
  }
}