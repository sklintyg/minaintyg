package se.inera.intyg.minaintyg.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import se.inera.intyg.minaintyg.logging.service.MonitoringLogService;

@ExtendWith(MockitoExtension.class)
class CustomAuthenticationFailureHandlerTest {

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

  private CustomAuthenticationFailureHandler authenticationFailureHandler;

  @Captor
  private ArgumentCaptor<String> stringArgumentCaptor;

  @BeforeEach
  void setUp() {
    authenticationFailureHandler = new CustomAuthenticationFailureHandler(
        "errorLoginUrl/{errorId}",
        monitoringLogService
    );
    when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
  }

  @Test
  void shouldForward() throws ServletException, IOException {
    authenticationFailureHandler.onAuthenticationFailure(request, response, exception);
    verify(request.getRequestDispatcher(anyString())).forward(request, response);
  }

  @Test
  void shouldLogUserLoginFailedWithMessageFromException() throws ServletException, IOException {
    authenticationFailureHandler.onAuthenticationFailure(request, response, exception);
    verify(monitoringLogService).logUserLoginFailed(stringArgumentCaptor.capture());
    assertEquals(AUTHENTICATION_FAILED, stringArgumentCaptor.getValue());
  }

  @Test
  void shouldLogClientError() throws ServletException, IOException {
    authenticationFailureHandler.onAuthenticationFailure(request, response, exception);
    verify(monitoringLogService).logClientError(anyString(), anyString(), anyString(), eq(null));
  }

  @Test
  void shouldSetCorrectRequestAttribute() throws ServletException, IOException {
    authenticationFailureHandler.onAuthenticationFailure(request, response, exception);
    verify(request).setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, exception);
  }
}
