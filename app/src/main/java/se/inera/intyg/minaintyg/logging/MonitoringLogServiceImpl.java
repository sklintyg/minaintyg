package se.inera.intyg.minaintyg.logging;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.common.logging.LogMarkers;
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

  @Override
  public void logListCertificates(String personId, int nbrOfCertificates) {
    logEvent(MonitoringEvent.LIST_CERTIFICATES, HashUtility.hash(personId), nbrOfCertificates);
  }

  @Override
  public void logCertificateRead(String certificateId, String type) {
    logEvent(MonitoringEvent.CERTIFICATE_READ, certificateId, type);
  }

  @Override
  public void logCertificateSent(String certificateId, String type, String recipient) {
    logEvent(MonitoringEvent.CERTIFICATE_SEND, certificateId, type, recipient);
  }
  
  @Override
  public void logCertificatePrinted(
      String certificateId,
      String certificateType,
      boolean isEmployerCopy) {
    logEvent(
        MonitoringEvent.INTYG_PRINT_PDF,
        certificateId,
        certificateType,
        isEmployerCopy ? "MINIMAL" : "FULL"
    );
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
    CITIZEN_LOGOUT("Citizen '{}' logged out using login method '{}'"),
    LIST_CERTIFICATES("Citizen '{}' listed '{}' certificates"),
    CERTIFICATE_READ("Certificate '{}' of type '{}' was read"),
    CERTIFICATE_SEND("Certificate '{}' of type '{}' sent to '{}'"),
    INTYG_PRINT_PDF("Intyg '{}' of type '{}' was printed as PDF with '{}' content");
    private final String message;

    MonitoringEvent(String message) {
      this.message = message;
    }

    public String getMessage() {
      return message;
    }
  }
}
