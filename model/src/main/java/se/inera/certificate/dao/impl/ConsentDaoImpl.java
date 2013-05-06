package se.inera.certificate.dao.impl;

import org.springframework.stereotype.Repository;
import se.inera.certificate.dao.ConsentDao;
import se.inera.certificate.model.Consent;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author andreaskaltenbach
 */
@Repository
public class ConsentDaoImpl implements ConsentDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void setConsent(String civicRegistrationNumber) {
        if (!hasConsent(civicRegistrationNumber)) {
            entityManager.persist(new Consent(civicRegistrationNumber));
        }
    }

    @Override
    public void revokeConsent(String civicRegistrationNumber) {
        Consent consent = findConsent(civicRegistrationNumber);
        if (consent != null) {
            entityManager.remove(consent);
        }
    }

    @Override
    public boolean hasConsent(String civicRegistrationNumber) {
        return findConsent(civicRegistrationNumber) != null;
    }

    private Consent findConsent(String civicRegistrationNumber) {
        return entityManager.find(Consent.class, civicRegistrationNumber);
    }
}
