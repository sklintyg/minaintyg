package se.inera.certificate.web.service;

import se.inera.certificate.web.service.dto.HealthStatus;

public interface MonitoringService {

    HealthStatus checkIntygstjanst();
    
    String getApplicationVersion();

    String getApplicationBuildNumber();

    String getApplicationBuildTime();
    
    HealthStatus getNbrOfLoggedInUsers();

}
