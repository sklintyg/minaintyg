package se.inera.certificate.service;

public interface ConsentService {

    /**
     * Consent status for given civicRegistrationNumber.
     * 
     * @param civicRegistrationNumber
     * @return true if consent is registered for or civicRegistrationNumer is null 
     */
    boolean isConsent(String civicRegistrationNumber);

    /**
     * Set consent status for given cuvucRegistrationNumber. True means consent is given and false means no consent is given.
     * 
     * @param civicRegistrationNumber
     * @param consentGiven
     */
    void setConsent(String civicRegistrationNumber, boolean consentGiven);
}
