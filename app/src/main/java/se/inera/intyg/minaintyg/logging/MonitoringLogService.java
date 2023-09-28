package se.inera.intyg.minaintyg.logging;

public interface MonitoringLogService {

  void logUserLogin(String personId, String loginMethod);

  void logUserLogout(String personId, String loginMethod);

  void logListCertificates(String personId, int nbrOfCertificates);

  void logCertificateRead(String personId, String certificateId);
}
