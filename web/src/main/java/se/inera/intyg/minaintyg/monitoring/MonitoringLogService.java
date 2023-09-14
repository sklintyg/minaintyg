package se.inera.intyg.minaintyg.monitoring;

public interface MonitoringLogService {

  void logUserLogin(String personId, String loginMethod);

  void logUserLogout(String personId, String loginMethod);
  
}
