package se.inera.intyg.minaintyg.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
    final var expectedResult = new MinaIntygUser(PERSON_ID, PERSON_NAME);
    when(minaIntygUserService.getUser()).thenReturn(expectedResult);
    final var actualResult = userController.getUser();
    assertEquals(expectedResult, actualResult);
  }
}