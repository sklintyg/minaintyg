package se.inera.certificate.web.service;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.annotation.PostConstruct;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import se.inera.certificate.logging.LogMarkers;

@Service
public class MonitoringLogServiceImpl implements MonitoringLogService {

    private static final String SPACE = " ";

    private static final String DIGEST = "SHA-256";

    private static final Logger LOG = LoggerFactory.getLogger(MonitoringLogService.class);

    private MessageDigest msgDigest;

    @PostConstruct
    public void initMessageDigest() {
        try {
            msgDigest = MessageDigest.getInstance(DIGEST);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void logCitizenLogin(String userId, String loginMethod) {
        logEvent(MonitoringEvent.CITIZEN_LOGIN, hash(userId), loginMethod);
    }

    @Override
    public void logCitizenLogout(String userId, String loginMethod) {
        logEvent(MonitoringEvent.CITIZEN_LOGOUT, hash(userId), loginMethod);
    }

    @Override
    public void logCitizenConsentGiven(String userId) {
        logEvent(MonitoringEvent.CONSENT_GIVEN, hash(userId));
    }

    @Override
    public void logCitizenConsentRevoked(String userId) {
        logEvent(MonitoringEvent.CONSENT_REVOKED, hash(userId));
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

    private String hash(String payload) {
        try {
            msgDigest.update(payload.getBytes("UTF-8"));
            byte[] digest = msgDigest.digest();
            return new String(Hex.encodeHex(digest));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }

    private enum MonitoringEvent {
        CITIZEN_LOGIN("Citizen '{}' logged in using login method '{}'"),
        CITIZEN_LOGOUT("Citizen '{}' logged out using login method '{}'"),
        CERTIFICATE_READ("Certificate '{}' of type '{}' was read"),
        CERTIFICATE_SEND("Certificate '{}' of type '{}' sent to '{}'"),
        CERTIFICATE_ARCHIVED("Certificate '{}' archived"),
        CERTIFICATE_RESTORED("Certificate '{}' restored"),
        CONSENT_GIVEN("Consent given by citizen '{}'"),
        CONSENT_REVOKED("Consent revoked by citizen '{}'");

        private String msg;

        private MonitoringEvent(String msg) {
            this.msg = msg;
        }

        public String getMessage() {
            return msg;
        }
    }

}
