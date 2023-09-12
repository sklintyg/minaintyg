package se.inera.intyg.minaintyg.monitoring;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.minaintyg.auth.LoginMethod;
import se.inera.intyg.minaintyg.auth.MinaIntygUser;
import se.inera.intyg.minaintyg.testhelper.TestPrincipalHelper;
import se.inera.intyg.minaintyg.util.HashUtility;

@ExtendWith(MockitoExtension.class)
class UserConverterTest {

  private static final String NO_USER = "noUser";
  private static final String PERSON_ID = "191212121212";
  private UserConverter userConverter;

  @BeforeEach
  void setUp() {
    userConverter = new UserConverter();
  }

  @Test
  void shouldReturnUserIfGetUserFromPrincipalIsEmpty() {
    TestPrincipalHelper.setUnknownPrincipal(new Object());
    final var result = userConverter.convert(null);
    assertEquals(NO_USER, result);
  }

  @Test
  void shouldReturnUserIfGetUserFromPrincipalIsNotEmpty() {
    final var user = new MinaIntygUser(PERSON_ID, "name", LoginMethod.FAKE);
    final var expectedResult = HashUtility.hash(user.getPersonId()) + "," + user.getLoginMethod();
    TestPrincipalHelper.setMinaIntygUserAsPrincipal(user);
    final var result = userConverter.convert(null);
    assertEquals(expectedResult, result);
  }
}
