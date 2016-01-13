package se.inera.certificate.web.service;

import se.inera.certificate.modules.support.api.dto.Personnummer;

public interface MonitoringLogService {

    void logCitizenLogin(Personnummer userId, String loginMethod);

    void logCitizenLogout(Personnummer userId, String loginMethod);

    void logCitizenConsentGiven(Personnummer userId);

    void logCitizenConsentRevoked(Personnummer userId);

    void logCertificateRead(String id, String typ);

    void logCertificateSend(String certificateId, String recipientId);

    void logCertificateArchived(String certificateId);

    void logCertificateRestored(String certificateId);

}
