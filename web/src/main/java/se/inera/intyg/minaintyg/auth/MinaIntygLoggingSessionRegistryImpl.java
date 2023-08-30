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
        // Session Redis
    }

    @Override
    public void registerNewSession(String sessionId, Object principal) {
        LOGGER.debug("Attempting to register new session '{}'", sessionId);

        //TODO: Add logging and related logic

        super.registerNewSession(sessionId, principal);
    }

    @Override
    public void removeSessionInformation(String sessionId) {
        LOGGER.debug("Attempting to remove session '{}'", sessionId);

        //TODO: Add logging and related logic

        super.removeSessionInformation(sessionId);
    }
}
