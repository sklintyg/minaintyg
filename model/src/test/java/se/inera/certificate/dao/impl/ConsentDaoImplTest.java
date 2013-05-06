package se.inera.certificate.dao.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import se.inera.certificate.dao.ConsentDao;
import se.inera.certificate.model.Consent;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author andreaskaltenbach
 */
@RunWith( SpringJUnit4ClassRunner.class )
@ContextConfiguration( locations = {"classpath:persistence-config.xml"} )
@ActiveProfiles( "dev" )
@Transactional
public class ConsentDaoImplTest {

    public static final String CIVIC_REGISTRATION_NUMBER = "19001122-3344";
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ConsentDao consentDao;

    @Test
    public void testSetConsent() {
        assertEquals(0, allConsents().size());

        consentDao.setConsent(CIVIC_REGISTRATION_NUMBER);

        assertEquals(1, allConsents().size());
        assertEquals(CIVIC_REGISTRATION_NUMBER, allConsents().get(0).getCivicRegistrationNumber());
    }

    @Test
    public void testSetConsentWithExistingConsent() {
        entityManager.persist(new Consent(CIVIC_REGISTRATION_NUMBER));
        assertEquals(1, allConsents().size());
        assertEquals(CIVIC_REGISTRATION_NUMBER, allConsents().get(0).getCivicRegistrationNumber());

        consentDao.setConsent(CIVIC_REGISTRATION_NUMBER);

        assertEquals(1, allConsents().size());
        assertEquals(CIVIC_REGISTRATION_NUMBER, allConsents().get(0).getCivicRegistrationNumber());
    }

    @Test
    public void testHasConsent() {

        assertFalse(consentDao.hasConsent(CIVIC_REGISTRATION_NUMBER));

        consentDao.setConsent(CIVIC_REGISTRATION_NUMBER);

        assertTrue(consentDao.hasConsent(CIVIC_REGISTRATION_NUMBER));

        consentDao.revokeConsent(CIVIC_REGISTRATION_NUMBER);

        assertFalse(consentDao.hasConsent(CIVIC_REGISTRATION_NUMBER));
    }

    @Test
    public void testRevokeConsent() {

        entityManager.persist(new Consent(CIVIC_REGISTRATION_NUMBER));
        assertEquals(1, allConsents().size());

        consentDao.revokeConsent(CIVIC_REGISTRATION_NUMBER);

        assertEquals(0, allConsents().size());
    }

    @Test
    public void testRevokeConsentWithoutExistingConsent() {
        assertEquals(0, allConsents().size());

        consentDao.revokeConsent(CIVIC_REGISTRATION_NUMBER);

        assertEquals(0, allConsents().size());
    }

    private List<Consent> allConsents() {
        return entityManager.createQuery("SELECT c FROM Consent c").getResultList();
    }
}
