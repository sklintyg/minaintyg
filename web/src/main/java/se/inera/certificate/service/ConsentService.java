package se.inera.certificate.service;

public interface ConsentService {

    boolean isConsent(String personnummer);

    void setConsent(String personnummer, boolean consentGiven);

}
