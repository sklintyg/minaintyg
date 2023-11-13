package se.inera.intyg.minaintyg.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import se.inera.intyg.minaintyg.logging.MonitoringLogService;

@ExtendWith(MockitoExtension.class)
class CustomAuthenticationFailureHandlerTest {

  private static final String ERROR_LOGIN_URL = "/error/login/";

  private static final String AUTHENTICATION_FAILED = "Authentication failed";
  private static final AuthenticationException exception = new AuthenticationException(
      AUTHENTICATION_FAILED) {
  };

  @Mock
  private MonitoringLogService monitoringLogService;

  @Mock
  private HttpServletRequest request;
  @Mock
  private HttpServletResponse response;
  @Mock
  private RequestDispatcher requestDispatcher;

  @InjectMocks
  private CustomAuthenticationFailureHandler authenticationFailureHandler;

  @Captor
  private ArgumentCaptor<String> stringArgumentCaptor;

  @Captor
  private ArgumentCaptor<StackTraceElement[]> stackTraceCaptor;

  @BeforeEach
  void setUp() {
    when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
  }

  @Test
  void shouldForwardWithGeneratedErrorId() throws ServletException, IOException {
    authenticationFailureHandler.onAuthenticationFailure(request, response, exception);
    verify(monitoringLogService).logUserLoginFailed(stringArgumentCaptor.capture(),
        eq(exception.getMessage()), any());
    verify(request.getRequestDispatcher(ERROR_LOGIN_URL + stringArgumentCaptor.getValue())).forward(
        request, response);
  }

  @Test
  void shouldLogUserLoginFailedWithErrorId() throws ServletException, IOException {
    authenticationFailureHandler.onAuthenticationFailure(request, response, exception);
    verify(monitoringLogService).logUserLoginFailed(stringArgumentCaptor.capture(),
        eq(exception.getMessage()), any());
    assertNotNull(stringArgumentCaptor.getValue());
  }

  @Test
  void shouldLogUserLoginFailedWithMessageFromException() throws ServletException, IOException {
    authenticationFailureHandler.onAuthenticationFailure(request, response, exception);
    verify(monitoringLogService).logUserLoginFailed(anyString(), stringArgumentCaptor.capture(),
        any());
    assertEquals(AUTHENTICATION_FAILED, stringArgumentCaptor.getValue());
  }

  @Test
  void shouldLogUserLoginFailedWithStacktraceFromException() throws ServletException, IOException {
    authenticationFailureHandler.onAuthenticationFailure(request, response, exception);
    verify(monitoringLogService).logUserLoginFailed(anyString(), eq(exception.getMessage()),
        stackTraceCaptor.capture());
    assertEquals(exception.getStackTrace().length, stackTraceCaptor.getValue().length);
  }

  @Test
  void shouldSetCorrectRequestAttribute() throws ServletException, IOException {
    authenticationFailureHandler.onAuthenticationFailure(request, response, exception);
    verify(request).setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, exception);
  }
}
