package se.inera.certificate.dao.impl;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import se.inera.certificate.dao.CertificateDao;
import se.inera.certificate.model.CertificateMetaData;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContexts/certificateMetaDataTest.xml"})
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback = true)
@Transactional
public class CertificateDaoImplTest {

    @Autowired
    CertificateDao certificateDao;
    
    @Test
    public void testFindMetaDataByCivicRegistrationNumberAndType() throws Exception {
        List<CertificateMetaData> data = certificateDao.findMetaDataByCivicRegistrationNumberAndType("121212-1212", Arrays.asList(new String[]{"INTYG"}));
        assertEquals(1,data.size());
    }
    
}
