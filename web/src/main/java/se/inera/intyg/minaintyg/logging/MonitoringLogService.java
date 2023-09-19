package se.inera.intyg.minaintyg.logging;

public interface MonitoringLogService {

  void logUserLogin(String personId, String loginMethod);

  void logUserLogout(String personId, String loginMethod);

}
