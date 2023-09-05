package se.inera.intyg.minaintyg.auth;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;
import se.inera.intyg.minaintyg.service.monitoring.MonitoringLogService;

@Slf4j
public class MinaIntygLoggingSessionRegistryImpl<T extends Session> extends SpringSessionBackedSessionRegistry<T> {

    private final MonitoringLogService monitoringService;
    private final FindByIndexNameSessionRepository<T> sessionRepository;

    public MinaIntygLoggingSessionRegistryImpl(FindByIndexNameSessionRepository<T> sessionRepository, MonitoringLogService monitoringService) {
        super(sessionRepository);
        this.sessionRepository = sessionRepository;
        this.monitoringService = monitoringService;

    }

    @Override
    public void registerNewSession(String sessionId, Object principal) {
        log.debug("Attempting to register new session '{}'", sessionId);

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
        log.debug("Attempting to remove session '{}'", sessionId);

        final var session = sessionRepository.findById(sessionId);
        final var user = getUser(session);
        if (user == null) {
            super.removeSessionInformation(sessionId);
            return;
        }

        if (session.isExpired()) {
            //TODO: Add loggging
        } else {
            //TODO: Add loggging
        }

        super.removeSessionInformation(sessionId);
    }
    private MinaIntygUser getUser(Session session) {
        if (session == null) {
            return null;
        }
        final var authenticator = (SecurityContext) session.getAttribute("SPRING_SECURITY_CONTEXT");
        final var principal = authenticator != null ? authenticator.getAuthentication().getPrincipal() : null;
        return isMinaIntygUser(principal) ? (MinaIntygUser) principal : null;
    }
}
