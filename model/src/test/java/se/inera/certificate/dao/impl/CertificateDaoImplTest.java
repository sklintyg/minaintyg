package se.inera.certificate.dao.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import se.inera.certificate.dao.CertificateDao;
import se.inera.certificate.model.Certificate;
import se.inera.certificate.model.CertificateMetaData;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:persistence-config.xml" })
@ActiveProfiles("dev")
@Transactional
public class CertificateDaoImplTest {

    private static final String CERTIFICATE_ID = "02468";
    private static final String CIVIC_REGISTRATION_NUMBER = "19220101-1234";
    private static final String FK7263 = "fk7263";

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    CertificateDao certificateDao;
    
    @Test
    public void testFindCertificateMetaDataWithoutUserId() {
        List<CertificateMetaData> metaData = certificateDao.findCertificateMetaData(null, null, null, null);
        assertTrue(metaData.isEmpty());
    }

    @Test
    public void testFindCertificateMetaDataWithoutNoCertificatesForUser() {
        List<CertificateMetaData> metaData = certificateDao.findCertificateMetaData(CIVIC_REGISTRATION_NUMBER, null, null, null);
        assertTrue(metaData.isEmpty());
    }

    @Test
    public void testFindCertificateMetaDataWithoutOneCertificateForUser() {

        createMetaData(CERTIFICATE_ID, CIVIC_REGISTRATION_NUMBER, FK7263);

        List<CertificateMetaData> metaData = certificateDao.findCertificateMetaData(CIVIC_REGISTRATION_NUMBER, null, null, null);
        assertEquals(1, metaData.size());
    }

    @Test
    public void testFindCertificateMetaDataWithCertificateTypeFilter() {

        String otherCertificateType = "other";

        // create an FK7263 and another certificate
        createMetaData(CERTIFICATE_ID, CIVIC_REGISTRATION_NUMBER, FK7263);
        createMetaData("otherCertificateId", CIVIC_REGISTRATION_NUMBER, otherCertificateType);

        // no certificate type -> no filtering by certificate type
        List<CertificateMetaData> metaData = certificateDao.findCertificateMetaData(CIVIC_REGISTRATION_NUMBER, null, null, null);
        assertEquals(2, metaData.size());

        // filter by FK7263 -> only return FK7263
        metaData = certificateDao.findCertificateMetaData(CIVIC_REGISTRATION_NUMBER, Collections.singletonList(FK7263), null, null);
        assertEquals(1, metaData.size());
        assertEquals(FK7263, metaData.get(0).getType());

        // filter by other type -> only return other certificate
        metaData = certificateDao.findCertificateMetaData(CIVIC_REGISTRATION_NUMBER, Collections.singletonList(otherCertificateType), null, null);
        assertEquals(1, metaData.size());
        assertEquals(otherCertificateType, metaData.get(0).getType());

        // filter by both types -> both certificates are returned
        metaData = certificateDao.findCertificateMetaData(CIVIC_REGISTRATION_NUMBER, Arrays.asList(FK7263, otherCertificateType), null, null);
        assertEquals(2, metaData.size());
    }

    private void createMetaData(String certificateId, String civicRegistrationNumber, String certificateType) {

        Certificate certificate = new Certificate(certificateId, "abc");

        CertificateMetaData metaData = new CertificateMetaData(certificate);
        metaData.setCivicRegistrationNumber(civicRegistrationNumber);
        metaData.setType(certificateType);
        metaData.setDeleted(false);
        entityManager.persist(metaData);
    }

    @Test
    public void testGetDocument() throws Exception {
        CertificateMetaData metaData = certificateDao.getCertificate("1");
        String document = metaData.getDocument();
        
        assertEquals("This is a document", document);
    }
    
    @Test
    public void testStore() throws Exception {
        Certificate certificate = new Certificate("12345", "Ett dokument");
        CertificateMetaData certificateMetaData = new CertificateMetaData(certificate);
        certificateDao.store(certificateMetaData);
        entityManager.flush();
    }
}
