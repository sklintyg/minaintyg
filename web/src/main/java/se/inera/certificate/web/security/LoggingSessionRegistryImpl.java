package se.inera.certificate.web.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistryImpl;

import se.inera.certificate.web.service.MonitoringLogService;

public class LoggingSessionRegistryImpl extends SessionRegistryImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingSessionRegistryImpl.class);

    @Autowired
    private MonitoringLogService monitoringService;
    
    @Override
    public void registerNewSession(String sessionId, Object principal) {

        LOGGER.debug("Attempting to register new session '{}'", sessionId);

        if (principal != null && principal instanceof Citizen) {
            Citizen user = (Citizen) principal;
            monitoringService.logCitizenLogin(user.getUsername(), user.getLoginMethod().name());
        }
        super.registerNewSession(sessionId, principal);
    }

    @Override
    public void removeSessionInformation(String sessionId) {

        LOGGER.debug("Attempting to remove session '{}'", sessionId);

        SessionInformation sessionInformation = getSessionInformation(sessionId);

        if (sessionInformation == null) {
            super.removeSessionInformation(sessionId);
            return;
        }

        Object principal = sessionInformation.getPrincipal();

        if (principal instanceof Citizen) {
            Citizen user = (Citizen) principal;
            monitoringService.logCitizenLogout(user.getUsername(), user.getLoginMethod().name());
        }

        super.removeSessionInformation(sessionId);
    }

}
