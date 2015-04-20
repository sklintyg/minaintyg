package se.inera.certificate.web.service;

public interface MonitoringLogService {

    void logCitizenLogin(String userId, String loginMethod);

    void logCitizenLogout(String userId, String loginMethod);

}
