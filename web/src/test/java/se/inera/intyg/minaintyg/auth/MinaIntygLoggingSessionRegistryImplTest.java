package se.inera.intyg.minaintyg.auth;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import se.inera.intyg.minaintyg.monitoring.MonitoringLogService;

@ExtendWith(MockitoExtension.class)
class MinaIntygLoggingSessionRegistryImplTest {

  @Mock
  private MonitoringLogService monitoringLogService;
  @Mock
  private FindByIndexNameSessionRepository<? extends Session> findByIndexNameSessionRepository;
  @InjectMocks
  private MinaIntygLoggingSessionRegistryImpl<? extends Session> minaIntygLoggingSessionRegistry;

  private static final String PERSON_ID = "personId";
  private static final String SESSION_ID = "sessionId";
  private static final String PERSON_NAMN = "personName";
  private static final LoginMethod LOGIN_METHOD = LoginMethod.ELVA77;

  @Nested
  class Logging {

    @Test
    void shouldLogIfPrincipalIsMinaIntygUser() {
      final var principal = new MinaIntygUser(PERSON_ID, PERSON_NAMN, LOGIN_METHOD);
      minaIntygLoggingSessionRegistry.registerNewSession(SESSION_ID, principal);
      verify(monitoringLogService).logUserLogin(principal.getPersonName(),
          null);
    }

    @Test
    void shouldNotLogIfPrincipalIsNotMinaIntygUser() {
      final var principal = new Object();
      minaIntygLoggingSessionRegistry.registerNewSession(SESSION_ID, principal);
      verifyNoInteractions(monitoringLogService);
    }
  }
}