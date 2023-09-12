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
import se.inera.intyg.minaintyg.user.MinaIntygUserService;

@ExtendWith(MockitoExtension.class)
class MinaIntygUserDetailServiceImplTest {

  private static final String PERSON_ID = "191212121212";
  private static final String PERSON_FIRSTNAME = "Arnold";
  private static final String PERSON_LASTNAME = "Johansson";
  private static final String PERSON_NAME = "Arnold Johansson";
  private static final String PERSON_NAME_WITH_MIDDLENAME = "Arnold React Johansson";
  private static final String PERSON_MIDDLENAME = "React";
  @Mock
  private MinaIntygUserService minaIntygUserService;
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
    final var puResponse = getPuResponse(Status.NOT_FOUND, PERSON_MIDDLENAME);
    when(getPersonService.getPerson(any(PersonRequest.class))).thenReturn(puResponse);
    assertThrows(RuntimeException.class,
        () -> minaIntygUserDetailService.buildPrincipal(PERSON_ID, LoginMethod.ELVA77));
  }

  @Test
  void shouldThrowRuntimeExceptionIfResponseHasStatusError() {
    final var puResponse = getPuResponse(Status.ERROR, PERSON_MIDDLENAME);
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
    final var puResponse = getPuResponse(Status.FOUND, PERSON_MIDDLENAME);
    when(getPersonService.getPerson(any(PersonRequest.class))).thenReturn(puResponse);
    when(minaIntygUserService.buildUserFromPersonResponse(puResponse,
        LoginMethod.ELVA77)).thenReturn(getUser());
    final var principal = minaIntygUserDetailService.buildPrincipal(PERSON_ID, LoginMethod.ELVA77);
    assertEquals(principal.getClass(), MinaIntygUser.class);
  }

  @Test
  void shouldSetPersonIdFromPUResponseToUserObject() {
    final var expectedUser = getUser();
    final var puResponse = getPuResponse(Status.FOUND, PERSON_MIDDLENAME);
    when(getPersonService.getPerson(any(PersonRequest.class))).thenReturn(puResponse);
    when(minaIntygUserService.buildUserFromPersonResponse(puResponse,
        LoginMethod.ELVA77)).thenReturn(expectedUser);
    final var principal = (MinaIntygUser) minaIntygUserDetailService.buildPrincipal(PERSON_ID,
        LoginMethod.ELVA77);
    assertEquals(PERSON_ID, principal.getPersonId());
  }

  @Test
  void shouldSetNameFromPUResponseToUserObject() {
    final var expectedUser = getUser();
    final var puResponse = getPuResponse(Status.FOUND, null);
    when(getPersonService.getPerson(any(PersonRequest.class))).thenReturn(puResponse);
    when(minaIntygUserService.buildUserFromPersonResponse(puResponse,
        LoginMethod.ELVA77)).thenReturn(expectedUser);
    final var principal = (MinaIntygUser) minaIntygUserDetailService.buildPrincipal(PERSON_ID,
        LoginMethod.ELVA77);
    assertEquals(PERSON_NAME, principal.getPersonName());
  }

  @Test
  void shouldSetLoginMethodToUserObject() {
    final var expectedUser = getUser();
    final var puResponse = getPuResponse(Status.FOUND, PERSON_MIDDLENAME);
    when(getPersonService.getPerson(any(PersonRequest.class))).thenReturn(puResponse);
    when(minaIntygUserService.buildUserFromPersonResponse(puResponse,
        LoginMethod.ELVA77)).thenReturn(expectedUser);
    final var principal = (MinaIntygUser) minaIntygUserDetailService.buildPrincipal(PERSON_ID,
        expectedUser.getLoginMethod());
    assertEquals(expectedUser.getLoginMethod(), principal.getLoginMethod());
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

  private static MinaIntygUser getUser() {
    return MinaIntygUser.builder()
        .personId(PERSON_ID)
        .personName(PERSON_NAME)
        .loginMethod(LoginMethod.ELVA77)
        .build();
  }
}
