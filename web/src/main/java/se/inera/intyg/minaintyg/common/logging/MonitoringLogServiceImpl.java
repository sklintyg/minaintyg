package se.inera.intyg.minaintyg.common.logging;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.util.HashUtility;

@Slf4j
@Service
public class MonitoringLogServiceImpl implements MonitoringLogService {

  private static final String SPACE = " ";

  @Override
  public void logUserLogin(String personId, String loginMethod) {
    logEvent(MonitoringEvent.CITIZEN_LOGIN, HashUtility.hash(personId), loginMethod);
  }

  @Override
  public void logUserLogout(String personId, String loginMethod) {
    logEvent(MonitoringEvent.CITIZEN_LOGOUT, HashUtility.hash(personId), loginMethod);
  }


  private void logEvent(MonitoringEvent event, Object... logMsgArgs) {
    log.info(LogMarkers.MONITORING, buildMessage(event), logMsgArgs);
  }

  private String buildMessage(MonitoringEvent logEvent) {
    final var logMsg = new StringBuilder();
    logMsg.append(logEvent.name()).append(SPACE).append(logEvent.getMessage());
    return logMsg.toString();
  }

  private enum MonitoringEvent {
    CITIZEN_LOGIN("Citizen '{}' logged in using login method '{}'"),
    CITIZEN_LOGOUT("Citizen '{}' logged out using login method '{}'");
    private final String message;

    MonitoringEvent(String message) {
      this.message = message;
    }

    public String getMessage() {
      return message;
    }
  }
}
