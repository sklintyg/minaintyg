package se.inera.certificate.web.service;

public interface MonitoringLogService {

    void logCitizenLogin(String userId, String loginMethod);

    void logCitizenLogout(String userId, String loginMethod);

    void logCitizenConsentGiven(String userId);

    void logCitizenConsentRevoked(String userId);

    void logCertificateRead(String id, String typ);

    void logCertificateSend(String certificateId, String recipientId);

    void logCertificateArchived(String certificateId);

    void logCertificateRestored(String certificateId);

}
