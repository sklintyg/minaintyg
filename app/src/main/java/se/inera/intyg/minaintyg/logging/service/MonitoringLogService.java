package se.inera.intyg.minaintyg.logging.service;

public interface MonitoringLogService {

  void logUserLogin(String personId, String loginMethod);

  void logUserLogout(String personId, String loginMethod);

  void logListCertificates(String personId, int nbrOfCertificates);

  void logCertificateRead(String certificateId, String type);

  void logCertificateSent(String certificateId, String type, String recipient);

  void logClientError(String id, String code, String message, String stackTrace);

  void logCertificatePrinted(String certificateId, String certificateType, boolean isEmployerCopy);
}
