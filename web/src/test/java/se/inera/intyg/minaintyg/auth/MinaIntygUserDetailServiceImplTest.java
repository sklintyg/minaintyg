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
  private static final String PERSON_FIRSTNAME = "Arnold";
  private static final String PERSON_LASTNAME = "Johansson";
  private static final String PERSON_NAME = "Arnold Johansson";
  private static final String PERSON_NAME_WITH_MIDDLENAME = "Arnold React Johansson";
  private static final String PERSON_MIDDLENAME = "React";
  @Mock
  private GetPersonService getPersonService;
  @InjectMocks
  private MinaIntygUserDetailServiceImpl minaIntygUserDetailService;

  @Test
  void shouldThrowIlligalArgumentExceptionIfNoPersonIdIsNull() {
    assertThrows(IllegalArgumentException.class,
        () -> minaIntygUserDetailService.getPrincipal(null, LoginMethod.ELVA77));
  }

  @Test
  void shouldThrowRuntimeExceptionIfResponseHasStatusNotFound() {
    final var puResponse = getPuResponse(Status.NOT_FOUND, PERSON_MIDDLENAME);
    when(getPersonService.getPerson(any(PersonRequest.class))).thenReturn(puResponse);
    assertThrows(RuntimeException.class,
        () -> minaIntygUserDetailService.getPrincipal(PERSON_ID, LoginMethod.ELVA77));
  }

  @Test
  void shouldThrowRuntimeExceptionIfResponseHasStatusError() {
    final var puResponse = getPuResponse(Status.ERROR, PERSON_MIDDLENAME);
    when(getPersonService.getPerson(any(PersonRequest.class))).thenReturn(puResponse);
    assertThrows(RuntimeException.class,
        () -> minaIntygUserDetailService.getPrincipal(PERSON_ID, LoginMethod.ELVA77));
  }

  @Test
  void shouldThrowIlligalArgumentExceptionIfNoPersonIdIsEmtpy() {
    assertThrows(IllegalArgumentException.class, () -> minaIntygUserDetailService.getPrincipal("",
        LoginMethod.ELVA77));
  }

  @Test
  void shouldReturnTypeMinaIntygUser() {
    final var puResponse = getPuResponse(Status.FOUND, PERSON_MIDDLENAME);
    when(getPersonService.getPerson(any(PersonRequest.class))).thenReturn(puResponse);
    final var principal = minaIntygUserDetailService.getPrincipal(PERSON_ID, LoginMethod.ELVA77);
    assertEquals(principal.getClass(), MinaIntygUser.class);
  }

  @Test
  void shouldSetPersonIdFromPUResponseToUserObject() {
    final var puResponse = getPuResponse(Status.FOUND, PERSON_MIDDLENAME);
    when(getPersonService.getPerson(any(PersonRequest.class))).thenReturn(puResponse);
    final var principal = (MinaIntygUser) minaIntygUserDetailService.getPrincipal(PERSON_ID,
        LoginMethod.ELVA77);
    assertEquals(PERSON_ID, principal.getPersonId());
  }

  @Test
  void shouldSetNameFromPUResponseToUserObject() {
    final var puResponse = getPuResponse(Status.FOUND, null);
    when(getPersonService.getPerson(any(PersonRequest.class))).thenReturn(puResponse);
    final var principal = (MinaIntygUser) minaIntygUserDetailService.getPrincipal(PERSON_ID,
        LoginMethod.ELVA77);
    assertEquals(PERSON_NAME, principal.getPersonName());
  }

  @Test
  void shouldSetNameIfSurnamePresentFromPUResponseToUserObject() {
    final var puResponse = getPuResponse(Status.FOUND, PERSON_MIDDLENAME);
    when(getPersonService.getPerson(any(PersonRequest.class))).thenReturn(puResponse);
    final var principal = (MinaIntygUser) minaIntygUserDetailService.getPrincipal(PERSON_ID,
        LoginMethod.ELVA77);
    assertEquals(PERSON_NAME_WITH_MIDDLENAME, principal.getPersonName());
  }

  @Test
  void shouldSetLoginMethodToUserObject() {
    final var expectedLoginMethod = LoginMethod.ELVA77;
    final var puResponse = getPuResponse(Status.FOUND, PERSON_MIDDLENAME);
    when(getPersonService.getPerson(any(PersonRequest.class))).thenReturn(puResponse);
    final var principal = (MinaIntygUser) minaIntygUserDetailService.getPrincipal(PERSON_ID,
        expectedLoginMethod);
    assertEquals(expectedLoginMethod, principal.getLoginMethod());
  }

  private static PersonResponse getPuResponse(Status status, String middleName) {
    return PersonResponse.builder()
        .person(
            Person.builder()
                .personnummer(PERSON_ID)
                .fornamn(PERSON_FIRSTNAME)
                .mellannamn(middleName)
                .efternamn(PERSON_LASTNAME)
                .build()
        )
        .status(status)
        .build();
  }
}
