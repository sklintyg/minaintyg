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
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.test.util.ReflectionTestUtils;
import se.inera.intyg.minaintyg.exception.CitizenInactiveException;
import se.inera.intyg.minaintyg.exception.LoginAgeLimitException;
import se.inera.intyg.minaintyg.integration.api.citizen.GetCitizenIntegrationService;
import se.inera.intyg.minaintyg.integration.api.person.GetPersonIntegrationRequest;
import se.inera.intyg.minaintyg.integration.api.person.GetPersonIntegrationResponse;
import se.inera.intyg.minaintyg.integration.api.person.GetPersonIntegrationService;
import se.inera.intyg.minaintyg.integration.api.person.model.Person;
import se.inera.intyg.minaintyg.integration.api.person.model.Status;
import se.inera.intyg.minaintyg.logging.HashUtility;

@ExtendWith(MockitoExtension.class)
class MinaIntygUserDetailServiceTest {

  private static final String PERSON_ID = "191212121212";
  private static final String PERSON_NAME = "Arnold Johansson";
  private static final long LOGIN_AGE_LIMIT = 16L;

  @Mock
  private GetPersonIntegrationService getPersonIntegrationService;
  @Mock
  private GetCitizenIntegrationService getCitizenIntegrationService;
  @Mock
  private Environment environment;
  @Spy
  private HashUtility hashUtility;

  @InjectMocks
  private MinaIntygUserDetailService minaIntygUserDetailService;

  @Nested
  class ExceptionTests {

    @Test
    void shouldThrowIllegalArgumentExceptionIfPersonIdIsNull() {
      assertThrows(IllegalArgumentException.class,
          () -> minaIntygUserDetailService.buildPrincipal(null, LoginMethod.ELVA77));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionIfPersonIdIsEmpty() {
      assertThrows(IllegalArgumentException.class, () -> minaIntygUserDetailService.buildPrincipal("",
          LoginMethod.ELVA77));
    }

  }

  @Nested
  class PersonTests {
    @BeforeEach
    void setup() {
      ReflectionTestUtils.setField(minaIntygUserDetailService, "loginAgeLimit", LOGIN_AGE_LIMIT);
      ReflectionTestUtils.setField(hashUtility, "salt", "salt");
      when(environment.getActiveProfiles()).thenReturn(new String[]{});
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
          assertTrue(e.getMessage().contains(hashUtility.hash(personId)));
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

  @Nested
  class CitizenTests {
    private static final String CITIZEN_ID = "191212121212";
    private static final String CITIZEN_NAME = "Anna Svensson";

    @BeforeEach
    void setup() {
      ReflectionTestUtils.setField(minaIntygUserDetailService, "loginAgeLimit", LOGIN_AGE_LIMIT);
      ReflectionTestUtils.setField(hashUtility, "salt", "salt");
      when(environment.getActiveProfiles()).thenReturn(new String[]{"citizen"});
    }

    @Test
    void shoudlThrowCitizenInactiveExceptionIfCitizenIsInactive() {
      final var response = getCitizenResponse(se.inera.intyg.minaintyg.integration.api.citizen.model.Status.FOUND,
          CITIZEN_ID, false);
      when(getCitizenIntegrationService.getCitizen(any())).thenReturn(response);
      assertThrows(CitizenInactiveException.class,
          () -> minaIntygUserDetailService.buildPrincipal(CITIZEN_ID, LoginMethod.ELVA77));
    }

    @Test
    void shouldThrowRuntimeExceptionIfResponseHasStatusNotFound() {
      final var response = getCitizenResponse(se.inera.intyg.minaintyg.integration.api.citizen.model.Status.NOT_FOUND);
      when(getCitizenIntegrationService.getCitizen(any())).thenReturn(response);
      assertThrows(RuntimeException.class,
          () -> minaIntygUserDetailService.buildPrincipal(CITIZEN_ID, LoginMethod.ELVA77));
    }

    @Test
    void shouldThrowRuntimeExceptionIfResponseHasStatusError() {
      final var response = getCitizenResponse(se.inera.intyg.minaintyg.integration.api.citizen.model.Status.ERROR);
      when(getCitizenIntegrationService.getCitizen(any())).thenReturn(response);
      assertThrows(RuntimeException.class,
          () -> minaIntygUserDetailService.buildPrincipal(CITIZEN_ID, LoginMethod.ELVA77));
    }

    @Test
    void shouldReturnTypeMinaIntygUser() {
      final var response = getCitizenResponse(se.inera.intyg.minaintyg.integration.api.citizen.model.Status.FOUND);
      when(getCitizenIntegrationService.getCitizen(any())).thenReturn(response);
      final var principal = minaIntygUserDetailService.buildPrincipal(CITIZEN_ID, LoginMethod.ELVA77);
      assertEquals(principal.getClass(), MinaIntygUser.class);
    }

    @Test
    void shouldSetCitizenIdFromResponseToUserObject() {
      final var response = getCitizenResponse(se.inera.intyg.minaintyg.integration.api.citizen.model.Status.FOUND);
      when(getCitizenIntegrationService.getCitizen(any())).thenReturn(response);
      final var principal = (MinaIntygUser) minaIntygUserDetailService.buildPrincipal(CITIZEN_ID, LoginMethod.ELVA77);
      assertEquals(CITIZEN_ID, principal.getPersonId());
    }

    @Test
    void shouldSetNameFromResponseToUserObject() {
      final var response = getCitizenResponse(se.inera.intyg.minaintyg.integration.api.citizen.model.Status.FOUND);
      when(getCitizenIntegrationService.getCitizen(any())).thenReturn(response);
      final var principal = (MinaIntygUser) minaIntygUserDetailService.buildPrincipal(CITIZEN_ID, LoginMethod.ELVA77);
      assertEquals(CITIZEN_NAME, principal.getPersonName());
    }

    @Test
    void shouldSetLoginMethodToUserObject() {
      final var expectedUser = getCitizenUser();
      final var response = getCitizenResponse(se.inera.intyg.minaintyg.integration.api.citizen.model.Status.FOUND);
      when(getCitizenIntegrationService.getCitizen(any())).thenReturn(response);
      final var principal = (MinaIntygUser) minaIntygUserDetailService.buildPrincipal(CITIZEN_ID, expectedUser.getLoginMethod());
      assertEquals(expectedUser.getLoginMethod(), principal.getLoginMethod());
    }

    @Nested
    class LoginAgeLimit {
      @Test
      void shouldNotThrowLoginAgeLimitExceptionIfAboveAgeLimit() {
        final var citizenId = getCitizenId(-1);
        assertDoesNotThrow(
            () -> minaIntygUserDetailService.buildPrincipal(citizenId, LoginMethod.ELVA77));
      }

      @Test
      void shouldNotThrowLoginAgeLimitExceptionIfAboveAgeLimitForCoordinationNumber() {
        final var coordinationNumber = getCoordinationNumber(-1);
        assertDoesNotThrow(
            () -> minaIntygUserDetailService.buildPrincipal(coordinationNumber, LoginMethod.ELVA77));
      }

      @Test
      void shouldNotThrowLoginAgeLimitExceptionIfExactlyAgeLimit() {
        final var citizenId = getCitizenId(0);
        assertDoesNotThrow(
            () -> minaIntygUserDetailService.buildPrincipal(citizenId, LoginMethod.ELVA77));
      }

      @Test
      void shouldNotThrowLoginAgeLimitExceptionIfExactlyAgeLimitForCoordinationNumber() {
        final var coordinationNumber = getCoordinationNumber(0);
        assertDoesNotThrow(
            () -> minaIntygUserDetailService.buildPrincipal(coordinationNumber, LoginMethod.ELVA77));
      }

      @Test
      void shouldThrowLoginAgeLimitExceptionIfBelowAgeLimit() {
        final var citizenId = getCitizenId(1);
        assertThrows(LoginAgeLimitException.class,
            () -> minaIntygUserDetailService.buildPrincipal(citizenId, LoginMethod.ELVA77));
      }

      @Test
      void shouldThrowLoginAgeLimitExceptionIfBelowAgeLimitForCoordinationNumber() {
        final var getCoordinationNumber = getCoordinationNumber(1);
        assertThrows(LoginAgeLimitException.class,
            () -> minaIntygUserDetailService.buildPrincipal(getCoordinationNumber, LoginMethod.ELVA77));
      }

      @Test
      void shouldHashCitizenIdForLogMessage() {
        final var citizenId = getCitizenId(1);
        final var e = assertThrows(LoginAgeLimitException.class,
            () -> minaIntygUserDetailService.buildPrincipal(citizenId, LoginMethod.ELVA77));

        assertFalse(e.getMessage().contains(citizenId));
        assertTrue(e.getMessage().contains(hashUtility.hash(citizenId)));
      }

      @Test
      void shouldCheckAgeLimitAgainstCitizenIdFromResponse() {
        final var citizenIdResponse = LocalDate.now(ZoneId.systemDefault()).minusYears(LOGIN_AGE_LIMIT)
            .plusDays(1).format(DateTimeFormatter.BASIC_ISO_DATE).concat("1234");

        when(getCitizenIntegrationService.getCitizen(any())).thenReturn(getCitizenResponse(
            se.inera.intyg.minaintyg.integration.api.citizen.model.Status.FOUND, citizenIdResponse));

        assertThrows(LoginAgeLimitException.class,
            () -> minaIntygUserDetailService.buildPrincipal(CITIZEN_ID, LoginMethod.ELVA77));
      }

      private String getCitizenId(int plusDays) {
        final var citizenId = LocalDate.now(ZoneId.systemDefault()).minusYears(LOGIN_AGE_LIMIT)
            .plusDays(plusDays).format(DateTimeFormatter.BASIC_ISO_DATE).concat("1234");
        when(getCitizenIntegrationService.getCitizen(any())).thenReturn(getCitizenResponse(
            se.inera.intyg.minaintyg.integration.api.citizen.model.Status.FOUND, citizenId));
        return citizenId;
      }

      private String getCoordinationNumber(int plusDays) {
        final var birthDate = LocalDate.now(ZoneId.systemDefault()).minusYears(LOGIN_AGE_LIMIT)
            .plusDays(plusDays).format(DateTimeFormatter.BASIC_ISO_DATE);
        final var dayOfBirth = Integer.parseInt(birthDate.substring(6, 8));
        final var coordinationNumber = birthDate.substring(0, 6) + (dayOfBirth + 60) + "1234";
        when(getCitizenIntegrationService.getCitizen(any())).thenReturn(getCitizenResponse(
            se.inera.intyg.minaintyg.integration.api.citizen.model.Status.FOUND, coordinationNumber));
        return coordinationNumber;
      }
    }

    private static se.inera.intyg.minaintyg.integration.api.citizen.GetCitizenIntegrationResponse getCitizenResponse(
        se.inera.intyg.minaintyg.integration.api.citizen.model.Status status) {
      return getCitizenResponse(status, CITIZEN_ID);
    }

    private static se.inera.intyg.minaintyg.integration.api.citizen.GetCitizenIntegrationResponse getCitizenResponse(
        se.inera.intyg.minaintyg.integration.api.citizen.model.Status status, String citizenId) {
      return se.inera.intyg.minaintyg.integration.api.citizen.GetCitizenIntegrationResponse.builder()
          .citizen(
              se.inera.intyg.minaintyg.integration.api.citizen.model.Citizen.builder()
                  .name(CITIZEN_NAME)
                  .citizenId(citizenId)
                  .isActive(true)
                  .build()
          )
          .status(status)
          .build();
    }

    private static se.inera.intyg.minaintyg.integration.api.citizen.GetCitizenIntegrationResponse getCitizenResponse(
        se.inera.intyg.minaintyg.integration.api.citizen.model.Status status, String citizenId, boolean isActive) {
      return se.inera.intyg.minaintyg.integration.api.citizen.GetCitizenIntegrationResponse.builder()
          .citizen(
              se.inera.intyg.minaintyg.integration.api.citizen.model.Citizen.builder()
                  .name(CITIZEN_NAME)
                  .citizenId(citizenId)
                  .isActive(isActive)
                  .build()
          )
          .status(status)
          .build();
    }

    private static MinaIntygUser getCitizenUser() {
      return MinaIntygUser.builder()
          .personId(CITIZEN_ID)
          .personName(CITIZEN_NAME)
          .loginMethod(LoginMethod.ELVA77)
          .build();
    }
  }

  }
