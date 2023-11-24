package se.inera.intyg.minaintyg.logging.service;


import com.google.common.base.Strings;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.common.logging.LogMarkers;
import se.inera.intyg.minaintyg.util.HashUtility;

@Slf4j
@Service
public class MonitoringLogService {

  private static final String SPACE = " ";
  private static final String NO_STACK_TRACE = "NO_STACK_TRACE";

  public void logUserLogin(String personId, String loginMethod) {
    logEvent(MonitoringEvent.CITIZEN_LOGIN, HashUtility.hash(personId), loginMethod);
  }

  public void logUserLoginFailed(String exceptionMessage) {
    logEvent(MonitoringEvent.CITIZEN_LOGIN_FAILURE, exceptionMessage);
  }

  public void logUserLogout(String personId, String loginMethod) {
    logEvent(MonitoringEvent.CITIZEN_LOGOUT, HashUtility.hash(personId), loginMethod);
  }

  public void logListCertificates(String personId, int nbrOfCertificates) {
    logEvent(MonitoringEvent.LIST_CERTIFICATES, HashUtility.hash(personId), nbrOfCertificates);
  }

  public void logCertificateRead(String certificateId, String type) {
    logEvent(MonitoringEvent.CERTIFICATE_READ, certificateId, type);
  }

  public void logCertificateSent(String certificateId, String type, String recipient) {
    logEvent(MonitoringEvent.CERTIFICATE_SEND, certificateId, type, recipient);
  }

  public void logCertificatePrinted(
      String certificateId,
      String certificateType,
      boolean printCompleteCertificate) {
    if (!printCompleteCertificate) {
      logEvent(
          MonitoringEvent.CERTIFICATE_PRINTED_EMPLOYER_COPY,
          certificateId,
          certificateType
      );
    } else {
      logEvent(
          MonitoringEvent.CERTIFICATE_PRINTED_FULLY,
          certificateId,
          certificateType
      );
    }
  }

  public void logClientError(String id, String code, String message, String stackTrace) {
    logEvent(MonitoringEvent.CLIENT_ERROR, id, code, message,
        Strings.isNullOrEmpty(stackTrace) ? NO_STACK_TRACE : stackTrace);
  }

  public void logIllegalCertificateAccess(String message) {
    logEvent(MonitoringEvent.ILLEGAL_CERTIFICATE_ACCESS, message);
  }

  private void logEvent(MonitoringEvent event, Object... logMsgArgs) {
    log.info(LogMarkers.MONITORING, buildMessage(event), logMsgArgs);
  }

  private String buildMessage(MonitoringEvent logEvent) {
    final var logMsg = new StringBuilder();
    logMsg.append(logEvent.name()).append(SPACE).append(logEvent.getMessage());
    return logMsg.toString();
  }

  @Getter
  private enum MonitoringEvent {
    CITIZEN_LOGIN("Citizen '{}' logged in using login method '{}'"),
    CITIZEN_LOGIN_FAILURE(
        "Citizen failed to login, exception message '{}'"),
    CITIZEN_LOGOUT("Citizen '{}' logged out using login method '{}'"),
    LIST_CERTIFICATES("Citizen '{}' listed '{}' certificates"),
    CERTIFICATE_READ("Certificate '{}' of type '{}' was read"),
    CERTIFICATE_SEND("Certificate '{}' of type '{}' sent to '{}'"),
    CERTIFICATE_PRINTED_FULLY(
        "Certificate '{}' of type '{}' was printed including all information"),
    CERTIFICATE_PRINTED_EMPLOYER_COPY("Certificate '{}' of type '{}' was printed as employer copy"),
    CLIENT_ERROR(
        "Received error from client with errorId '{}' with error code '{}', message '{}' and stacktrace '{}'"),
    ILLEGAL_CERTIFICATE_ACCESS("{}");

    private final String message;

    MonitoringEvent(String message) {
      this.message = message;
    }
  }
}
