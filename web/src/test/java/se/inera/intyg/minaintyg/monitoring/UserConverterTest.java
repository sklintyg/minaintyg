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
  private static final String PERSON_NAME = "personName";
  private UserConverter userConverter;

  @BeforeEach
  void setUp() {
    userConverter = new UserConverter();
  }

  @Test
  void shouldNotReturnUserInfoIfGetUserFromPrincipalIsEmpty() {
    TestPrincipalHelper.setUnknownPrincipal(new Object());
    final var result = userConverter.convert(null);
    assertEquals(NO_USER, result);
  }

  @Test
  void shouldReturnUserInfoIfGetUserFromPrincipalIsNotEmpty() {
    final var user = MinaIntygUser.builder()
        .personId(PERSON_ID)
        .personName(PERSON_NAME)
        .loginMethod(LoginMethod.ELVA77)
        .build();
    final var expectedResult = HashUtility.hash(user.getPersonId()) + "," + user.getLoginMethod();
    TestPrincipalHelper.setMinaIntygUserAsPrincipal(user);
    final var result = userConverter.convert(null);
    assertEquals(expectedResult, result);
  }
}
