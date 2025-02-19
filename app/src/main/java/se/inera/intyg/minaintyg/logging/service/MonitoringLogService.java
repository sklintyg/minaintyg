package se.inera.intyg.minaintyg.logging.service;


import com.google.common.base.Strings;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.logging.HashUtility;
import se.inera.intyg.minaintyg.logging.LogMarkers;
import se.inera.intyg.minaintyg.logging.MdcCloseableMap;
import se.inera.intyg.minaintyg.logging.MdcLogConstants;

@Slf4j
@Service
@RequiredArgsConstructor
public class MonitoringLogService {

  private final HashUtility hashUtility;

  private static final String SPACE = " ";
  private static final String NO_STACK_TRACE = "NO_STACK_TRACE";

  public void logUserLogin(String personId, String loginMethod) {
    final var hashedPersonId = hashUtility.hash(personId);
    try (MdcCloseableMap ignored =
        MdcCloseableMap.builder()
            .put(MdcLogConstants.EVENT_ACTION, toEventType(MonitoringEvent.CITIZEN_LOGIN))
            .put(MdcLogConstants.EVENT_TYPE, MdcLogConstants.EVENT_TYPE_INFO)
            .put(MdcLogConstants.EVENT_LOGIN_METHOD, loginMethod)
            .put(MdcLogConstants.USER_ID, hashedPersonId)
            .build()
    ) {
      logEvent(MonitoringEvent.CITIZEN_LOGIN, hashedPersonId, loginMethod);
    }
  }

  public void logUserLoginFailed(String exceptionMessage, String loginMethod) {
    try (MdcCloseableMap ignored =
        MdcCloseableMap.builder()
            .put(MdcLogConstants.EVENT_ACTION, toEventType(MonitoringEvent.CITIZEN_LOGIN_FAILURE))
            .put(MdcLogConstants.EVENT_TYPE, MdcLogConstants.EVENT_TYPE_INFO)
            .put(MdcLogConstants.EVENT_LOGIN_METHOD, loginMethod)
            .build()
    ) {
      logEvent(MonitoringEvent.CITIZEN_LOGIN_FAILURE, exceptionMessage);
    }
  }

  public void logUserLogout(String personId, String loginMethod) {
    final var hashedPersonId = hashUtility.hash(personId);
    try (MdcCloseableMap ignored =
        MdcCloseableMap.builder()
            .put(MdcLogConstants.EVENT_ACTION, toEventType(MonitoringEvent.CITIZEN_LOGOUT))
            .put(MdcLogConstants.EVENT_TYPE, MdcLogConstants.EVENT_TYPE_INFO)
            .put(MdcLogConstants.EVENT_LOGIN_METHOD, loginMethod)
            .put(MdcLogConstants.USER_ID, hashedPersonId)
            .build()
    ) {
      logEvent(MonitoringEvent.CITIZEN_LOGOUT, hashedPersonId, loginMethod);
    }
  }

  public void logListCertificates(String personId, int nbrOfCertificates) {
    try (MdcCloseableMap ignored =
        MdcCloseableMap.builder()
            .put(MdcLogConstants.EVENT_ACTION, toEventType(MonitoringEvent.LIST_CERTIFICATES))
            .put(MdcLogConstants.EVENT_TYPE, MdcLogConstants.EVENT_TYPE_ACCESSED)
            .build()
    ) {
      logEvent(MonitoringEvent.LIST_CERTIFICATES, hashUtility.hash(personId), nbrOfCertificates);
    }
  }

  public void logCertificateRead(String certificateId, String type) {
    try (MdcCloseableMap ignored =
        MdcCloseableMap.builder()
            .put(MdcLogConstants.EVENT_ACTION, toEventType(MonitoringEvent.CERTIFICATE_READ))
            .put(MdcLogConstants.EVENT_TYPE, MdcLogConstants.EVENT_TYPE_ACCESSED)
            .put(MdcLogConstants.EVENT_CERTIFICATE_ID, certificateId)
            .put(MdcLogConstants.EVENT_CERTIFICATE_TYPE, type)
            .build()
    ) {
      logEvent(MonitoringEvent.CERTIFICATE_READ, certificateId, type);
    }
  }

  public void logCertificateSent(String certificateId, String type, String recipient) {
    try (MdcCloseableMap ignored =
        MdcCloseableMap.builder()
            .put(MdcLogConstants.EVENT_ACTION, toEventType(MonitoringEvent.CERTIFICATE_SEND))
            .put(MdcLogConstants.EVENT_TYPE, MdcLogConstants.EVENT_TYPE_CHANGE)
            .put(MdcLogConstants.EVENT_CERTIFICATE_ID, certificateId)
            .put(MdcLogConstants.EVENT_CERTIFICATE_TYPE, type)
            .put(MdcLogConstants.EVENT_RECIPIENT, recipient)
            .build()
    ) {
      logEvent(MonitoringEvent.CERTIFICATE_SEND, certificateId, type, recipient);
    }
  }

  public void logCertificatePrinted(
      String certificateId,
      String certificateType,
      boolean printCompleteCertificate) {
    if (printCompleteCertificate) {
      logCertificatePrintedFully(certificateId, certificateType);
      return;
    }
    logCertificatePrintedEmployeeCopy(certificateId, certificateType);
  }

  private void logCertificatePrintedFully(String certificateId, String certificateType) {
    try (MdcCloseableMap ignored =
        MdcCloseableMap.builder()
            .put(MdcLogConstants.EVENT_ACTION,
                toEventType(MonitoringEvent.CERTIFICATE_PRINTED_FULLY))
            .put(MdcLogConstants.EVENT_TYPE, MdcLogConstants.EVENT_TYPE_ACCESSED)
            .put(MdcLogConstants.EVENT_CERTIFICATE_ID, certificateId)
            .put(MdcLogConstants.EVENT_CERTIFICATE_TYPE, certificateType)
            .build()
    ) {
      logEvent(
          MonitoringEvent.CERTIFICATE_PRINTED_FULLY,
          certificateId,
          certificateType
      );
    }
  }

  private void logCertificatePrintedEmployeeCopy(String certificateId, String certificateType) {
    try (MdcCloseableMap ignored =
        MdcCloseableMap.builder()
            .put(MdcLogConstants.EVENT_ACTION,
                toEventType(MonitoringEvent.CERTIFICATE_PRINTED_EMPLOYER_COPY))
            .put(MdcLogConstants.EVENT_TYPE, MdcLogConstants.EVENT_TYPE_ACCESSED)
            .put(MdcLogConstants.EVENT_CERTIFICATE_ID, certificateId)
            .put(MdcLogConstants.EVENT_CERTIFICATE_TYPE, certificateType)
            .build()
    ) {
      logEvent(
          MonitoringEvent.CERTIFICATE_PRINTED_EMPLOYER_COPY,
          certificateId,
          certificateType
      );
    }
  }

  public void logClientError(String id, String code, String message, String stackTrace) {
    final var stackTraceText = Strings.isNullOrEmpty(stackTrace) ? NO_STACK_TRACE : stackTrace;
    try (MdcCloseableMap ignored =
        MdcCloseableMap.builder()
            .put(MdcLogConstants.EVENT_ACTION, toEventType(MonitoringEvent.CLIENT_ERROR))
            .put(MdcLogConstants.EVENT_TYPE, MdcLogConstants.EVENT_TYPE_INFO)
            .put(MdcLogConstants.ERROR_ID, code)
            .put(MdcLogConstants.ERROR_CODE, code)
            .put(MdcLogConstants.ERROR_MESSAGE, message)
            .put(MdcLogConstants.ERROR_STACKTRACE, stackTraceText)
            .build()
    ) {
      logEvent(MonitoringEvent.CLIENT_ERROR, id, code, message, stackTraceText);
    }
  }

  public void logIllegalCertificateAccess(String message) {
    try (MdcCloseableMap ignored =
        MdcCloseableMap.builder()
            .put(MdcLogConstants.EVENT_ACTION,
                toEventType(MonitoringEvent.ILLEGAL_CERTIFICATE_ACCESS))
            .put(MdcLogConstants.EVENT_TYPE, MdcLogConstants.EVENT_TYPE_INFO)
            .put(MdcLogConstants.EVENT_CATEGORY, MdcLogConstants.EVENT_CATEGORY_INTRUSION_DETECTION)
            .put(MdcLogConstants.EVENT_OUTCOME, MdcLogConstants.EVENT_OUTCOME_DENIED)
            .build()
    ) {
      logEvent(MonitoringEvent.ILLEGAL_CERTIFICATE_ACCESS, message);
    }
  }

  private void logEvent(MonitoringEvent event, Object... logMsgArgs) {
    log.info(LogMarkers.MONITORING, buildMessage(event), logMsgArgs);
  }

  private String buildMessage(MonitoringEvent logEvent) {
    return logEvent.name() + SPACE + logEvent.getMessage();
  }

  private String toEventType(MonitoringEvent monitoringEvent) {
    return monitoringEvent.name().toLowerCase().replace("_", "-");
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
