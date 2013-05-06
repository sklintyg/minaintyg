package se.inera.certificate.dao;

/**
 * @author andreaskaltenbach
 */
public interface ConsentDao {

    void setConsent(String civicRegistrationNumber);

    void revokeConsent(String civicRegistrationNumber);

    boolean hasConsent(String civicRegistrationNumber);
}
