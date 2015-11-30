package se.inera.intyg.minaintyg.web.web.service;

import se.inera.intyg.minaintyg.web.web.service.dto.HealthStatus;

public interface MonitoringService {

    HealthStatus checkIntygstjanst();

    String getApplicationVersion();

    String getApplicationBuildNumber();

    String getApplicationBuildTime();

    HealthStatus getNbrOfLoggedInUsers();

}
