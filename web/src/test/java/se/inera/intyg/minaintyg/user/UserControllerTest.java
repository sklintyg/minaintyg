package se.inera.intyg.minaintyg.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.minaintyg.auth.LoginMethod;
import se.inera.intyg.minaintyg.auth.MinaIntygUser;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

  @Mock
  private MinaIntygUserServiceImpl minaIntygUserService;

  @InjectMocks
  private UserController userController;

  private static final String PERSON_ID = "personId";
  private static final String PERSON_NAME = "personName";

  @Test
  void shouldReturnUser() {
    final var expectedResult = Optional.of(
        MinaIntygUser.builder()
            .personId(PERSON_ID)
            .personName(PERSON_NAME)
            .loginMethod(LoginMethod.ELVA77)
            .build());
    when(minaIntygUserService.getUser()).thenReturn(expectedResult);
    final var actualResult = userController.getUser();
    assertEquals(expectedResult.get(), actualResult);
  }
}