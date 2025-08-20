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
import org.springframework.test.util.ReflectionTestUtils;
import se.inera.intyg.minaintyg.exception.LoginAgeLimitException;
import se.inera.intyg.minaintyg.integration.api.person.GetUserIntegrationRequest;
import se.inera.intyg.minaintyg.integration.api.person.GetUserIntegrationResponse;
import se.inera.intyg.minaintyg.integration.api.person.GetUserIntegrationService;
import se.inera.intyg.minaintyg.integration.api.person.model.User;
import se.inera.intyg.minaintyg.integration.api.person.model.Status;
import se.inera.intyg.minaintyg.logging.HashUtility;

@ExtendWith(MockitoExtension.class)
class MinaIntygUserDetailServiceTest {

  private static final String USER_ID = "191212121212";
  private static final String USER_NAME = "Arnold Johansson";
  private static final long LOGIN_AGE_LIMIT = 16L;

  @Mock
  private GetUserIntegrationService getUserIntegrationService;
  @Spy
  private HashUtility hashUtility;

  @InjectMocks
  private MinaIntygUserDetailService minaIntygUserDetailService;

  @BeforeEach
  void setup() {
    ReflectionTestUtils.setField(minaIntygUserDetailService, "loginAgeLimit", LOGIN_AGE_LIMIT);
    ReflectionTestUtils.setField(hashUtility, "salt", "salt");
  }

  @Test
  void shouldThrowIllegalArgumentExceptionIfUserIdIsNull() {
    assertThrows(IllegalArgumentException.class,
        () -> minaIntygUserDetailService.buildPrincipal(null, LoginMethod.ELVA77));
  }

  @Test
  void shouldThrowRuntimeExceptionIfResponseHasStatusNotFound() {
    final var puResponse = getUserResponse(Status.NOT_FOUND);
    when(getUserIntegrationService.getUser(any(GetUserIntegrationRequest.class))).thenReturn(
        puResponse);
    assertThrows(RuntimeException.class,
        () -> minaIntygUserDetailService.buildPrincipal(USER_ID, LoginMethod.ELVA77));
  }

  @Test
  void shouldThrowRuntimeExceptionIfResponseHasStatusError() {
    final var puResponse = getUserResponse(Status.ERROR);
    when(getUserIntegrationService.getUser(any(GetUserIntegrationRequest.class))).thenReturn(
        puResponse);
    assertThrows(RuntimeException.class,
        () -> minaIntygUserDetailService.buildPrincipal(USER_ID, LoginMethod.ELVA77));
  }

  @Test
  void shouldThrowIllegalArgumentExceptionIfUserIdIsEmpty() {
    assertThrows(IllegalArgumentException.class, () -> minaIntygUserDetailService.buildPrincipal("",
        LoginMethod.ELVA77));
  }

  @Test
  void shouldReturnTypeMinaIntygUser() {
    final var puResponse = getUserResponse(Status.FOUND);
    when(getUserIntegrationService.getUser(any(GetUserIntegrationRequest.class)))
        .thenReturn(puResponse);
    final var principal = minaIntygUserDetailService.buildPrincipal(USER_ID, LoginMethod.ELVA77);
    assertEquals(principal.getClass(), MinaIntygUser.class);
  }

  @Test
  void shouldSetUserIdFromPUResponseToUserObject() {
    final var puResponse = getUserResponse(Status.FOUND);
    when(getUserIntegrationService.getUser(any(GetUserIntegrationRequest.class))).thenReturn(
        puResponse);
    final var principal = (MinaIntygUser) minaIntygUserDetailService.buildPrincipal(USER_ID,
        LoginMethod.ELVA77);
    assertEquals(USER_ID, principal.getUserId());
  }

  @Test
  void shouldSetNameFromPUResponseToUserObject() {
    final var puResponse = getUserResponse(Status.FOUND);
    when(getUserIntegrationService.getUser(any(GetUserIntegrationRequest.class))).thenReturn(
        puResponse);
    final var principal = (MinaIntygUser) minaIntygUserDetailService.buildPrincipal(USER_ID,
        LoginMethod.ELVA77);
    assertEquals(USER_NAME, principal.getUserName());
  }

  @Test
  void shouldSetLoginMethodToUserObject() {
    final var expectedUser = getUser();
    final var puResponse = getUserResponse(Status.FOUND);
    when(getUserIntegrationService.getUser(any(GetUserIntegrationRequest.class))).thenReturn(
        puResponse);
    final var principal = (MinaIntygUser) minaIntygUserDetailService.buildPrincipal(USER_ID,
        expectedUser.getLoginMethod());
    assertEquals(expectedUser.getLoginMethod(), principal.getLoginMethod());
  }

  @Nested
  class LoginAgeLimit {

    @Test
    void shouldNotThrowLoginAgeLimitExceptionIfAboveAgeLimit() {
      final var userId = getUserId(-1);
      assertDoesNotThrow(
          () -> minaIntygUserDetailService.buildPrincipal(userId, LoginMethod.ELVA77));
    }

    @Test
    void shouldNotThrowLoginAgeLimitExceptionIfAboveAgeLimitForCoordinationNumber() {
      final var coordinationNumber = getCoordinationNumber(-1);
      assertDoesNotThrow(
          () -> minaIntygUserDetailService.buildPrincipal(coordinationNumber, LoginMethod.ELVA77));
    }

    @Test
    void shouldNotThrowLoginAgeLimitExceptionIfExactlyAgeLimit() {
      final var userId = getUserId(0);
      assertDoesNotThrow(
          () -> minaIntygUserDetailService.buildPrincipal(userId, LoginMethod.ELVA77));
    }

    @Test
    void shouldNotThrowLoginAgeLimitExceptionIfExactlyAgeLimitForCoordinationNumber() {
      final var coordinationNumber = getCoordinationNumber(0);
      assertDoesNotThrow(
          () -> minaIntygUserDetailService.buildPrincipal(coordinationNumber, LoginMethod.ELVA77));
    }

    @Test
    void shouldThrowLoginAgeLimitExceptionIfBelowAgeLimit() {
      final var userId = getUserId(1);
      assertThrows(LoginAgeLimitException.class,
          () -> minaIntygUserDetailService.buildPrincipal(userId, LoginMethod.ELVA77));
    }

    @Test
    void shouldThrowLoginAgeLimitExceptionIfBelowAgeLimitForCoordinationNumber() {
      final var getCoordinationNumber = getCoordinationNumber(1);
      assertThrows(LoginAgeLimitException.class,
          () -> minaIntygUserDetailService.buildPrincipal(getCoordinationNumber, LoginMethod.ELVA77));
    }

    @Test
    void shouldHashUserIdForLogMessage() {
      final var userId = getUserId(1);
      final var e = assertThrows(LoginAgeLimitException.class,
          () -> minaIntygUserDetailService.buildPrincipal(userId, LoginMethod.ELVA77));

      assertFalse(e.getMessage().contains(userId));
      assertTrue(e.getMessage().contains(hashUtility.hash(userId)));
    }

    @Test
    void shouldCheckAgeLimitAgainstUserIdFromPU() {
      final var UserIdResponse = LocalDate.now(ZoneId.systemDefault()).minusYears(LOGIN_AGE_LIMIT)
          .plusDays(1).format(DateTimeFormatter.BASIC_ISO_DATE).concat("1234");

      when(getUserIntegrationService.getUser(any(GetUserIntegrationRequest.class)))
          .thenReturn(getUserResponse(UserIdResponse));

      assertThrows(LoginAgeLimitException.class,
          () -> minaIntygUserDetailService.buildPrincipal(USER_ID, LoginMethod.ELVA77));
    }

    private String getUserId(int plusDays) {
      final var userId = LocalDate.now(ZoneId.systemDefault()).minusYears(LOGIN_AGE_LIMIT)
          .plusDays(plusDays).format(DateTimeFormatter.BASIC_ISO_DATE).concat("1234");
      when(getUserIntegrationService.getUser(any(GetUserIntegrationRequest.class)))
          .thenReturn(getUserResponse(userId));
      return userId;
    }

    private String getCoordinationNumber(int plusDays) {
      final var birthDate = LocalDate.now(ZoneId.systemDefault()).minusYears(LOGIN_AGE_LIMIT)
          .plusDays(plusDays).format(DateTimeFormatter.BASIC_ISO_DATE);
      final var dayOfBirth = Integer.parseInt(birthDate.substring(6, 8));
      final var coordinationNumber= birthDate.substring(0, 6) + (dayOfBirth + 60) + "1234";
      when(getUserIntegrationService.getUser(any(GetUserIntegrationRequest.class)))
          .thenReturn(getUserResponse(coordinationNumber));
      return coordinationNumber;
    }
  }

  private static GetUserIntegrationResponse getUserResponse(String userId) {
    return getUserResponse(Status.FOUND, userId);
  }

  private static GetUserIntegrationResponse getUserResponse(Status status) {
    return getUserResponse(status, USER_ID);
  }

  private static GetUserIntegrationResponse getUserResponse(Status status, String userId) {
    return GetUserIntegrationResponse.builder()
        .user(
            User.builder()
                .name(USER_NAME)
                .userId(userId)
                .build()
        )
        .status(status)
        .build();
  }

  private static MinaIntygUser getUser() {
    return MinaIntygUser.builder()
        .userId(USER_ID)
        .userName(USER_NAME)
        .loginMethod(LoginMethod.ELVA77)
        .build();
  }
}
