package se.inera.intyg.minaintyg.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;
import se.inera.intyg.minaintyg.service.monitoring.MonitoringLogService;

public class MinaIntygLoggingSessionRegistryImpl<T extends Session> extends SpringSessionBackedSessionRegistry<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MinaIntygLoggingSessionRegistryImpl.class);

    private final MonitoringLogService monitoringService;
    private final FindByIndexNameSessionRepository<T> sessionRepository;

    public MinaIntygLoggingSessionRegistryImpl(FindByIndexNameSessionRepository<T> sessionRepository, MonitoringLogService monitoringService) {
        super(sessionRepository);
        this.sessionRepository = sessionRepository;
        this.monitoringService = monitoringService;

    }

    @Override
    public void registerNewSession(String sessionId, Object principal) {
        LOGGER.debug("Attempting to register new session '{}'", sessionId);

        if (!isMinaIntygUser(principal)) {
            return;
        }

        final var user = (MinaIntygUser) principal;
        monitoringService.logUserLogin(user.getPatientId());

        super.registerNewSession(sessionId, principal);
    }

    private boolean isMinaIntygUser(Object principal) {
        return principal instanceof MinaIntygUser;
    }

    @Override
    public void removeSessionInformation(String sessionId) {
        LOGGER.debug("Attempting to remove session '{}'", sessionId);

        //TODO: Add logging and related logic

        super.removeSessionInformation(sessionId);
    }
}
