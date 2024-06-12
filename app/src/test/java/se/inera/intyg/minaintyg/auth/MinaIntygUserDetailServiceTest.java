package se.inera.intyg.minaintyg.auth;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import se.inera.intyg.minaintyg.exception.LoginAgeLimitException;
import se.inera.intyg.minaintyg.integration.api.person.GetPersonIntegrationRequest;
import se.inera.intyg.minaintyg.integration.api.person.GetPersonIntegrationResponse;
import se.inera.intyg.minaintyg.integration.api.person.GetPersonIntegrationService;
import se.inera.intyg.minaintyg.integration.api.person.model.Person;
import se.inera.intyg.minaintyg.integration.api.person.model.Status;
import se.inera.intyg.minaintyg.util.HashUtility;

@ExtendWith(MockitoExtension.class)
class MinaIntygUserDetailServiceTest {

  private static final String PERSON_ID = "191212121212";
  private static final String PERSON_NAME = "Arnold Johansson";
  private static final long LOGIN_AGE_LIMIT = 16L;

  @Mock
  private GetPersonIntegrationService getPersonIntegrationService;

  @InjectMocks
  private MinaIntygUserDetailService minaIntygUserDetailService;

  @BeforeEach
  void setup() {
    ReflectionTestUtils.setField(minaIntygUserDetailService, "loginAgeLimit", LOGIN_AGE_LIMIT);
  }

  @Test
  void shouldThrowIllegalArgumentExceptionIfPersonIdIsNull() {
    assertThrows(IllegalArgumentException.class,
        () -> minaIntygUserDetailService.buildPrincipal(null, LoginMethod.ELVA77));
  }

  @Test
  void shouldThrowRuntimeExceptionIfResponseHasStatusNotFound() {
    final var puResponse = getPersonResponse(Status.NOT_FOUND);
    when(getPersonIntegrationService.getPerson(any(GetPersonIntegrationRequest.class))).thenReturn(
        puResponse);
    assertThrows(RuntimeException.class,
        () -> minaIntygUserDetailService.buildPrincipal(PERSON_ID, LoginMethod.ELVA77));
  }

  @Test
  void shouldThrowRuntimeExceptionIfResponseHasStatusError() {
    final var puResponse = getPersonResponse(Status.ERROR);
    when(getPersonIntegrationService.getPerson(any(GetPersonIntegrationRequest.class))).thenReturn(
        puResponse);
    assertThrows(RuntimeException.class,
        () -> minaIntygUserDetailService.buildPrincipal(PERSON_ID, LoginMethod.ELVA77));
  }

  @Test
  void shouldThrowIllegalArgumentExceptionIfPersonIdIsEmpty() {
    assertThrows(IllegalArgumentException.class, () -> minaIntygUserDetailService.buildPrincipal("",
        LoginMethod.ELVA77));
  }

  @Test
  void shouldReturnTypeMinaIntygUser() {
    final var puResponse = getPersonResponse(Status.FOUND);
    when(getPersonIntegrationService.getPerson(any(GetPersonIntegrationRequest.class)))
        .thenReturn(puResponse);
    final var principal = minaIntygUserDetailService.buildPrincipal(PERSON_ID, LoginMethod.ELVA77);
    assertEquals(principal.getClass(), MinaIntygUser.class);
  }

  @Test
  void shouldSetPersonIdFromPUResponseToUserObject() {
    final var puResponse = getPersonResponse(Status.FOUND);
    when(getPersonIntegrationService.getPerson(any(GetPersonIntegrationRequest.class))).thenReturn(
        puResponse);
    final var principal = (MinaIntygUser) minaIntygUserDetailService.buildPrincipal(PERSON_ID,
        LoginMethod.ELVA77);
    assertEquals(PERSON_ID, principal.getPersonId());
  }

  @Test
  void shouldSetNameFromPUResponseToUserObject() {
    final var puResponse = getPersonResponse(Status.FOUND);
    when(getPersonIntegrationService.getPerson(any(GetPersonIntegrationRequest.class))).thenReturn(
        puResponse);
    final var principal = (MinaIntygUser) minaIntygUserDetailService.buildPrincipal(PERSON_ID,
        LoginMethod.ELVA77);
    assertEquals(PERSON_NAME, principal.getPersonName());
  }

  @Test
  void shouldSetLoginMethodToUserObject() {
    final var expectedUser = getUser();
    final var puResponse = getPersonResponse(Status.FOUND);
    when(getPersonIntegrationService.getPerson(any(GetPersonIntegrationRequest.class))).thenReturn(
        puResponse);
    final var principal = (MinaIntygUser) minaIntygUserDetailService.buildPrincipal(PERSON_ID,
        expectedUser.getLoginMethod());
    assertEquals(expectedUser.getLoginMethod(), principal.getLoginMethod());
  }

  @Nested
  class LoginAgeLimit {

    @Test
    void shouldNotThrowLoginAgeLimitExceptionIfAboveAgeLimit() {
      final var personId = getPersonId(-1);
      assertDoesNotThrow(
          () -> minaIntygUserDetailService.buildPrincipal(personId, LoginMethod.ELVA77));
    }

    @Test
    void shouldNotThrowLoginAgeLimitExceptionIfAboveAgeLimitForCoordinationNumber() {
      final var coordinationNumber = getCoordinationNumber(-1);
      assertDoesNotThrow(
          () -> minaIntygUserDetailService.buildPrincipal(coordinationNumber, LoginMethod.ELVA77));
    }

    @Test
    void shouldNotThrowLoginAgeLimitExceptionIfExactlyAgeLimit() {
      final var personId = getPersonId(0);
      assertDoesNotThrow(
          () -> minaIntygUserDetailService.buildPrincipal(personId, LoginMethod.ELVA77));
    }

    @Test
    void shouldNotThrowLoginAgeLimitExceptionIfExactlyAgeLimitForCoordinationNumber() {
      final var coordinationNumber = getCoordinationNumber(0);
      assertDoesNotThrow(
          () -> minaIntygUserDetailService.buildPrincipal(coordinationNumber, LoginMethod.ELVA77));
    }

    @Test
    void shouldThrowLoginAgeLimitExceptionIfBelowAgeLimit() {
      final var personId = getPersonId(1);
      assertThrows(LoginAgeLimitException.class,
          () -> minaIntygUserDetailService.buildPrincipal(personId, LoginMethod.ELVA77));
    }

    @Test
    void shouldThrowLoginAgeLimitExceptionIfBelowAgeLimitForCoordinationNumber() {
      final var getCoordinationNumber = getCoordinationNumber(1);
      assertThrows(LoginAgeLimitException.class,
          () -> minaIntygUserDetailService.buildPrincipal(getCoordinationNumber, LoginMethod.ELVA77));
    }

    @Test
    void shouldHashPersonIdForLogMessage() {
      final var personId = getPersonId(1);
      final var e = assertThrows(LoginAgeLimitException.class,
          () -> minaIntygUserDetailService.buildPrincipal(personId, LoginMethod.ELVA77));

      assertFalse(e.getMessage().contains(personId));
      assertTrue(e.getMessage().contains(HashUtility.hash(personId)));
    }

    @Test
    void shouldCheckAgeLimitAgainstPersonIdFromPU() {
      final var personIdResponse = LocalDate.now(ZoneId.systemDefault()).minusYears(LOGIN_AGE_LIMIT)
          .plusDays(1).format(DateTimeFormatter.BASIC_ISO_DATE).concat("1234");

      when(getPersonIntegrationService.getPerson(any(GetPersonIntegrationRequest.class)))
          .thenReturn(getPersonResponse(personIdResponse));

      assertThrows(LoginAgeLimitException.class,
          () -> minaIntygUserDetailService.buildPrincipal(PERSON_ID, LoginMethod.ELVA77));
    }

    private String getPersonId(int plusDays) {
      final var personId = LocalDate.now(ZoneId.systemDefault()).minusYears(LOGIN_AGE_LIMIT)
          .plusDays(plusDays).format(DateTimeFormatter.BASIC_ISO_DATE).concat("1234");
      when(getPersonIntegrationService.getPerson(any(GetPersonIntegrationRequest.class)))
          .thenReturn(getPersonResponse(personId));
      return personId;
    }

    private String getCoordinationNumber(int plusDays) {
      final var birthDate = LocalDate.now(ZoneId.systemDefault()).minusYears(LOGIN_AGE_LIMIT)
          .plusDays(plusDays).format(DateTimeFormatter.BASIC_ISO_DATE);
      final var dayOfBirth = Integer.parseInt(birthDate.substring(6, 8));
      final var coordinationNumber= birthDate.substring(0, 6) + (dayOfBirth + 60) + "1234";
      when(getPersonIntegrationService.getPerson(any(GetPersonIntegrationRequest.class)))
          .thenReturn(getPersonResponse(coordinationNumber));
      return coordinationNumber;
    }
  }

  private static GetPersonIntegrationResponse getPersonResponse(String personId) {
    return getPersonResponse(Status.FOUND, personId);
  }

  private static GetPersonIntegrationResponse getPersonResponse(Status status) {
    return getPersonResponse(status, PERSON_ID);
  }

  private static GetPersonIntegrationResponse getPersonResponse(Status status, String personId) {
    return GetPersonIntegrationResponse.builder()
        .person(
            Person.builder()
                .name(PERSON_NAME)
                .personId(personId)
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
