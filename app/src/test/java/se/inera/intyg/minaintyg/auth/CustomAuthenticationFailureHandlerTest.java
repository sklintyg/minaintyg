package se.inera.intyg.minaintyg.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.saml2.core.Saml2Error;
import org.springframework.security.saml2.provider.service.authentication.Saml2AuthenticationException;
import se.inera.intyg.minaintyg.exception.CitizenInactiveException;
import se.inera.intyg.minaintyg.exception.LoginAgeLimitException;
import se.inera.intyg.minaintyg.logging.service.MonitoringLogService;

@ExtendWith(MockitoExtension.class)
class CustomAuthenticationFailureHandlerTest {

  @Mock
  private MonitoringLogService monitoringLogService;

  @Captor
  private ArgumentCaptor<String> stringArgumentCaptor;

  private static final String AUTHENTICATION_FAILED = "Authentication failed";
  private static final String CONTEXT_PATH = "contextpath";
  private static final String ERROR_LOGIN_UNDERAGE_URL = "/errorLoginUnderageUrl";
  private static final String ERROR_LOGIN_INACTIVE_URL = "/errorInactiveUser";
  private static final String ERROR_LOGIN_URL = "/errorLoginUrl/{errorId}";
  private static final AuthenticationException exception = new AuthenticationException(
      AUTHENTICATION_FAILED) {
  };

  private CustomAuthenticationFailureHandler authenticationFailureHandler;
  private MockHttpServletRequest request;
  private MockHttpServletResponse response;

  @BeforeEach
  void setUp() {
    authenticationFailureHandler = new CustomAuthenticationFailureHandler(
        ERROR_LOGIN_URL,
        ERROR_LOGIN_UNDERAGE_URL,
        monitoringLogService,
        ERROR_LOGIN_INACTIVE_URL
    );

    request = new MockHttpServletRequest();
    request.setContextPath(CONTEXT_PATH);
    response = new MockHttpServletResponse();
  }

  @Test
  void shouldRedirectToErrorUrl() throws IOException {
    authenticationFailureHandler.onAuthenticationFailure(request, response, exception);
    assertTrue(Objects.requireNonNull(response.getHeader("Location"))
        .contains(CONTEXT_PATH + "/errorLoginUrl/"));
  }

  @Test
  void shouldLogUserLoginFailedWithMessageFromException() throws IOException {
    authenticationFailureHandler.onAuthenticationFailure(request, response, exception);
    verify(monitoringLogService).logUserLoginFailed(stringArgumentCaptor.capture(), eq("-"));
    assertEquals(AUTHENTICATION_FAILED, stringArgumentCaptor.getValue());
  }

  @Test
  void shouldLogClientError() throws IOException {
    authenticationFailureHandler.onAuthenticationFailure(request, response, exception);
    verify(monitoringLogService).logClientError(anyString(), anyString(), anyString(), eq(null));
  }

  @Nested
  class HandleLoginAgeLimitException {

    private static final String AGE_LIMIT_EXCEPTION_MESSAGE = "underage person";

    private final Saml2Error saml2Error = new Saml2Error("ERROR_CODE", "description");
    private final LoginAgeLimitException ageLimitException =
        new LoginAgeLimitException(AGE_LIMIT_EXCEPTION_MESSAGE, LoginMethod.ELVA77);
    private final Saml2AuthenticationException saml2Exception =
        new Saml2AuthenticationException(saml2Error, ageLimitException);

    @Test
    void shouldRedirectToErrorUnderageUrl() throws IOException {
      authenticationFailureHandler.onAuthenticationFailure(request, response, saml2Exception);
      assertEquals(CONTEXT_PATH + ERROR_LOGIN_UNDERAGE_URL, response.getHeader("Location"));
    }

    @Test
    void shouldMonitorLogLoginFailure() throws IOException {
      authenticationFailureHandler.onAuthenticationFailure(request, response, saml2Exception);
      verify(monitoringLogService).logUserLoginFailed(AGE_LIMIT_EXCEPTION_MESSAGE,
          LoginMethod.ELVA77.name());
    }
  }

  @Nested
  class HandleCitizenInactiveException {

    private static final String MESSAGE = "message";

    private final Saml2Error saml2Error = new Saml2Error("ERROR_CODE", "description");
    private final CitizenInactiveException citizenInactiveException =
        new CitizenInactiveException(MESSAGE, LoginMethod.ELVA77);
    private final Saml2AuthenticationException saml2Exception =
        new Saml2AuthenticationException(saml2Error, citizenInactiveException);

    @Test
    void shouldRedirectToErrorInactiveUrl() throws IOException {
      authenticationFailureHandler.onAuthenticationFailure(request, response, saml2Exception);
      assertEquals(CONTEXT_PATH + ERROR_LOGIN_INACTIVE_URL, response.getHeader("Location"));
    }

    @Test
    void shouldMonitorLogLoginFailure() throws IOException {
      authenticationFailureHandler.onAuthenticationFailure(request, response, saml2Exception);
      verify(monitoringLogService).logUserLoginFailed(MESSAGE,
          LoginMethod.ELVA77.name());
    }
  }
}