package se.inera.certificate.web.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import se.inera.certificate.logging.HashUtility;
import se.inera.certificate.logging.LogMarkers;

@Service
public class MonitoringLogServiceImpl implements MonitoringLogService {

    private static final String SPACE = " ";

    private static final Logger LOG = LoggerFactory.getLogger(MonitoringLogService.class);

    @Override
    public void logCitizenLogin(String userId, String loginMethod) {
        logEvent(MonitoringEvent.CITIZEN_LOGIN, HashUtility.hash(userId), loginMethod);
    }

    @Override
    public void logCitizenLogout(String userId, String loginMethod) {
        logEvent(MonitoringEvent.CITIZEN_LOGOUT, HashUtility.hash(userId), loginMethod);
    }

    @Override
    public void logCitizenConsentGiven(String userId) {
        logEvent(MonitoringEvent.CONSENT_GIVEN, HashUtility.hash(userId));
    }

    @Override
    public void logCitizenConsentRevoked(String userId) {
        logEvent(MonitoringEvent.CONSENT_REVOKED, HashUtility.hash(userId));
    }

    @Override
    public void logCertificateRead(String certificateId, String certificateType) {
        logEvent(MonitoringEvent.CERTIFICATE_READ, certificateId, certificateType);
    }

    @Override
    public void logCertificateSend(String certificateId, String recipientId) {
        logEvent(MonitoringEvent.CERTIFICATE_SEND, certificateId, recipientId);
    }

    @Override
    public void logCertificateArchived(String certificateId) {
        logEvent(MonitoringEvent.CERTIFICATE_ARCHIVED, certificateId);
    }

    @Override
    public void logCertificateRestored(String certificateId) {
        logEvent(MonitoringEvent.CERTIFICATE_RESTORED, certificateId);
    }

    private void logEvent(MonitoringEvent logEvent, Object... logMsgArgs) {

        StringBuilder logMsg = new StringBuilder();
        logMsg.append(logEvent.name()).append(SPACE).append(logEvent.getMessage());

        LOG.info(LogMarkers.MONITORING, logMsg.toString(), logMsgArgs);
    }

    private enum MonitoringEvent {
        CITIZEN_LOGIN("Citizen '{}' logged in using login method '{}'"),
        CITIZEN_LOGOUT("Citizen '{}' logged out using login method '{}'"),
        CERTIFICATE_READ("Certificate '{}' of type '{}' was read"),
        CERTIFICATE_SEND("Certificate '{}' sent to '{}'"),
        CERTIFICATE_ARCHIVED("Certificate '{}' archived"),
        CERTIFICATE_RESTORED("Certificate '{}' restored"),
        CONSENT_GIVEN("Consent given by citizen '{}'"),
        CONSENT_REVOKED("Consent revoked by citizen '{}'");

        private String msg;

        MonitoringEvent(String msg) {
            this.msg = msg;
        }

        public String getMessage() {
            return msg;
        }
    }

}
