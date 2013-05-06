package se.inera.certificate.service;

public interface ConsentService {

    boolean isConsent(String civicRegistrationNumber);

    void setConsent(String civicRegistrationNumber, boolean consentGiven);
}
