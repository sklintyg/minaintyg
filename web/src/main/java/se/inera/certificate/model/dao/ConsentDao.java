package se.inera.certificate.model.dao;

/**
 * @author andreaskaltenbach
 */
public interface ConsentDao {

    void setConsent(String civicRegistrationNumber);

    void revokeConsent(String civicRegistrationNumber);

    boolean hasConsent(String civicRegistrationNumber);
}
