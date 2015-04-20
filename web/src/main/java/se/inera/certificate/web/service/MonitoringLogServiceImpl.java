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

    private boolean hashSensitiveValues = true;

    @PostConstruct
    public void initMessageDigest() {
        try {
            msgDigest = MessageDigest.getInstance(DIGEST);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    /* (non-Javadoc)
     * @see se.inera.certificate.web.service.MonitoringLogService#logCitizenLogin(java.lang.String, java.lang.String)
     */
    @Override
    public void logCitizenLogin(String userId, String loginMethod) {
        logEvent(MonitoringEvent.CITIZEN_LOGIN, hash(userId), loginMethod);
    }

    /* (non-Javadoc)
     * @see se.inera.certificate.web.service.MonitoringLogService#logCitizenLogout(java.lang.String, java.lang.String)
     */
    @Override
    public void logCitizenLogout(String userId, String loginMethod) {
        logEvent(MonitoringEvent.CITIZEN_LOGOUT, hash(userId), loginMethod);
    }

    private void logEvent(MonitoringEvent logEvent, Object... logMsgArgs) {

        StringBuilder logMsg = new StringBuilder();
        logMsg.append(logEvent.name()).append(SPACE).append(logEvent.getMessage());

        LOG.info(LogMarkers.MONITORING, logMsg.toString(), logMsgArgs);
    }

    private String hash(String payload) {
        try {
            if (!hashSensitiveValues) {
                return payload;
            }
            msgDigest.update(payload.getBytes("UTF-8"));
            byte[] digest = msgDigest.digest();
            return new String(Hex.encodeHex(digest));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }

    private enum MonitoringEvent {
        CITIZEN_LOGIN("Citizen '{}' logged in using login method '{}'"),
        CITIZEN_LOGOUT("Citizen '{}' logged out using login method '{}'");

        private String msg;

        private MonitoringEvent(String msg) {
            this.msg = msg;
        }

        public String getMessage() {
            return msg;
        }
    }

}
