package se.inera.intyg.minaintyg.service.monitoring;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MonitoringLogServiceImpl implements MonitoringLogService{
    private static final Object SPACE = " ";
    private static final Logger LOGGER = LoggerFactory.getLogger(MonitoringLogServiceImpl.class);
    @Override
    public void logUserLogin(String personId) {
        logEvent(MonitoringEvent.USER_LOGIN, personId);
    }

    private void logEvent(MonitoringEvent event, Object... logMsgArgs) {
        LOGGER.info(buildMessage(event), logMsgArgs);
    }

    private String buildMessage(MonitoringEvent logEvent) {
        final var logMsg = new StringBuilder();
        logMsg.append(logEvent.name()).append(SPACE).append(logEvent.getMessage());
        return logMsg.toString();
    }

    private enum MonitoringEvent {
        USER_LOGIN("Login user '{}'");

        private final String message;
        MonitoringEvent(String message) {
            this.message = message;
        }
        public String getMessage() {
            return message;
        }
    }
}
