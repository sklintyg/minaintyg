package se.inera.intyg.minaintyg.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.minaintyg.integration.api.person.GetPersonService;
import se.inera.intyg.minaintyg.integration.api.person.Person;
import se.inera.intyg.minaintyg.integration.api.person.PersonRequest;
import se.inera.intyg.minaintyg.integration.api.person.PersonResponse;
import se.inera.intyg.minaintyg.integration.api.person.Status;

@ExtendWith(MockitoExtension.class)
class MinaIntygUserDetailServiceImplTest {

  private static final String PERSON_ID = "191212121212";
  private static final String PERSON_NAME = "Arnold Johansson";
  @Mock
  private GetPersonService getPersonService;
  @InjectMocks
  private MinaIntygUserDetailServiceImpl minaIntygUserDetailService;

  @Test
  void shouldThrowIlligalArgumentExceptionIfNoPersonIdIsNull() {
    assertThrows(IllegalArgumentException.class,
        () -> minaIntygUserDetailService.buildPrincipal(null, LoginMethod.ELVA77));
  }

  @Test
  void shouldThrowRuntimeExceptionIfResponseHasStatusNotFound() {
    final var puResponse = getPersonResponse(Status.NOT_FOUND);
    when(getPersonService.getPerson(any(PersonRequest.class))).thenReturn(puResponse);
    assertThrows(RuntimeException.class,
        () -> minaIntygUserDetailService.buildPrincipal(PERSON_ID, LoginMethod.ELVA77));
  }

  @Test
  void shouldThrowRuntimeExceptionIfResponseHasStatusError() {
    final var puResponse = getPersonResponse(Status.ERROR);
    when(getPersonService.getPerson(any(PersonRequest.class))).thenReturn(puResponse);
    assertThrows(RuntimeException.class,
        () -> minaIntygUserDetailService.buildPrincipal(PERSON_ID, LoginMethod.ELVA77));
  }

  @Test
  void shouldThrowIlligalArgumentExceptionIfNoPersonIdIsEmtpy() {
    assertThrows(IllegalArgumentException.class, () -> minaIntygUserDetailService.buildPrincipal("",
        LoginMethod.ELVA77));
  }

  @Test
  void shouldReturnTypeMinaIntygUser() {
    final var puResponse = getPersonResponse(Status.FOUND);
    when(getPersonService.getPerson(any(PersonRequest.class))).thenReturn(puResponse);
    final var principal = minaIntygUserDetailService.buildPrincipal(PERSON_ID, LoginMethod.ELVA77);
    assertEquals(principal.getClass(), MinaIntygUser.class);
  }

  @Test
  void shouldSetPersonIdFromPUResponseToUserObject() {
    final var puResponse = getPersonResponse(Status.FOUND);
    when(getPersonService.getPerson(any(PersonRequest.class))).thenReturn(puResponse);
    final var principal = (MinaIntygUser) minaIntygUserDetailService.buildPrincipal(PERSON_ID,
        LoginMethod.ELVA77);
    assertEquals(PERSON_ID, principal.getPersonId());
  }

  @Test
  void shouldSetNameFromPUResponseToUserObject() {
    final var puResponse = getPersonResponse(Status.FOUND);
    when(getPersonService.getPerson(any(PersonRequest.class))).thenReturn(puResponse);
    final var principal = (MinaIntygUser) minaIntygUserDetailService.buildPrincipal(PERSON_ID,
        LoginMethod.ELVA77);
    assertEquals(PERSON_NAME, principal.getPersonName());
  }

  @Test
  void shouldSetLoginMethodToUserObject() {
    final var expectedUser = getUser();
    final var puResponse = getPersonResponse(Status.FOUND);
    when(getPersonService.getPerson(any(PersonRequest.class))).thenReturn(puResponse);
    final var principal = (MinaIntygUser) minaIntygUserDetailService.buildPrincipal(PERSON_ID,
        expectedUser.getLoginMethod());
    assertEquals(expectedUser.getLoginMethod(), principal.getLoginMethod());
  }

  private static PersonResponse getPersonResponse(Status status) {
    return PersonResponse.builder()
        .person(
            Person.builder()
                .name(PERSON_NAME)
                .personId(PERSON_ID)
                .build()
        )
        .status(status)
        .build();
  }

  private static MinaIntygUser getUser() {
    return MinaIntygUser.builder()
        .personId(PERSON_ID)
        .personName(PERSON_NAME)
        .loginMethod(LoginMethod.ELVA77)
        .build();
  }
}
