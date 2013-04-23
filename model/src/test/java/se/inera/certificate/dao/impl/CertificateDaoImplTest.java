package se.inera.certificate.dao.impl;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import se.inera.certificate.dao.CertificateDao;
import se.inera.certificate.model.Certificate;
import se.inera.certificate.model.CertificateMetaData;

/** Test. */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:persistence-config.xml" })
@ActiveProfiles("dev")
public class CertificateDaoImplTest {

    /** Dao. */
    @Autowired
    CertificateDao certificateDao;
    
    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void testFindMetaDataByCivicRegistrationNumberAndType() throws Exception {
        List<CertificateMetaData> data = certificateDao.findMetaDataByCivicRegistrationNumberAndType("121212-1212", Arrays.asList(new String[]{"INTYG"}));
        assertEquals(1,data.size());
    }

    @Test
    @Transactional(propagation=Propagation.REQUIRED)
    public void testGetDocument() throws Exception {
        CertificateMetaData metaData = certificateDao.getCertificate("1");
        String document = metaData.getDocument();
        
        assertEquals("This is a document", document);
    }
    
    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void testStore() throws Exception {
        Certificate certificate = new Certificate("12345", "Ett dokument");
        CertificateMetaData certificateMetaData = new CertificateMetaData(certificate);
        certificateDao.store(certificateMetaData);
        entityManager.flush();
    }
}
