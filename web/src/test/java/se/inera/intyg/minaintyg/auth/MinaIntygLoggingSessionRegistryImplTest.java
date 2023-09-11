package se.inera.intyg.minaintyg.auth;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoSession;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import se.inera.intyg.minaintyg.monitoring.MonitoringLogService;

@ExtendWith(MockitoExtension.class)
class MinaIntygLoggingSessionRegistryImplTest {

  private MockitoSession mockito;
  @Mock
  private MonitoringLogService monitoringLogService;
  @Mock
  private FindByIndexNameSessionRepository<? extends Session> sessionRepository;
  @InjectMocks
  private MinaIntygLoggingSessionRegistryImpl<? extends Session> minaIntygLoggingSessionRegistry;

  private static final String PERSON_ID = "personId";
  private static final String SESSION_ID = "sessionId";
  private static final String PERSON_NAMN = "personName";
  private static final LoginMethod LOGIN_METHOD = LoginMethod.ELVA77;

  @Nested
  class RegisterNewSession {

    @Test
    void shouldLogIfPrincipalIsMinaIntygUser() {
      final var principal = new MinaIntygUser(PERSON_ID, PERSON_NAMN, LOGIN_METHOD);
      minaIntygLoggingSessionRegistry.registerNewSession(SESSION_ID, principal);
      verify(monitoringLogService).logUserLogin(principal.getPersonId(),
          principal.getLoginMethod().name());
    }

    @Test
    void shouldNotLogIfPrincipalIsNotMinaIntygUser() {
      final var principal = new Object();
      minaIntygLoggingSessionRegistry.registerNewSession(SESSION_ID, principal);
      verifyNoInteractions(monitoringLogService);
    }
  }

  @Nested
  class RemoveSessionInformation {

    @Test
    void shouldLogIfPrincipalIsMinaIntygUser() {
      final var principal = new MinaIntygUser(PERSON_ID, PERSON_NAMN, LOGIN_METHOD);
      mockSession(principal);
      minaIntygLoggingSessionRegistry.removeSessionInformation(SESSION_ID);
      verify(monitoringLogService).logUserLogout(principal.getPersonId(),
          principal.getLoginMethod().name());
    }

    @Test
    void shouldNotLogIfPrincipalIsNotMinaIntygUser() {
      final var principal = new Object();
      mockSession(principal);
      minaIntygLoggingSessionRegistry.removeSessionInformation(SESSION_ID);
      verifyNoInteractions(monitoringLogService);
    }
  }

  private void mockSession(Object principal) {
    final var mockedSession = mock(Session.class);
    final var mockedSecurityContext = mock(SecurityContext.class);
    final var mockedAuthentication = mock(Authentication.class);
    doReturn(mockedSession).when(sessionRepository).findById(SESSION_ID);
    doReturn(mockedSecurityContext).when(mockedSession).getAttribute("SPRING_SECURITY_CONTEXT");
    doReturn(mockedAuthentication).when(mockedSecurityContext).getAuthentication();
    doReturn(principal).when(mockedAuthentication).getPrincipal();
  }
}