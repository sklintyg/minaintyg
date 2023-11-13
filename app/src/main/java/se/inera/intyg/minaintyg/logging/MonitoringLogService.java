package se.inera.intyg.minaintyg.logging;

public interface MonitoringLogService {

  void logUserLogin(String personId, String loginMethod);

  void logUserLoginFailed(String errorId, String exceptionMessage, StackTraceElement[] stackTrace);

  void logUserLogout(String personId, String loginMethod);

  void logListCertificates(String personId, int nbrOfCertificates);

  void logCertificateRead(String certificateId, String type);

  void logCertificateSent(String certificateId, String type, String recipient);
}
