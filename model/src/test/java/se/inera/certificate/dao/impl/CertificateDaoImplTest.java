/**
 * Copyright (C) 2013 Inera AB (http://www.inera.se)
 *
 * This file is part of Inera Certificate (http://code.google.com/p/inera-certificate).
 *
 * Inera Certificate is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Inera Certificate is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.certificate.dao.impl;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import se.inera.certificate.dao.CertificateDao;
import se.inera.certificate.exception.InvalidCertificateIdentifierException;
import se.inera.certificate.model.CertificateMetaData;
import se.inera.certificate.model.CertificateState;
import se.inera.certificate.model.builder.CertificateMetaDataBuilder;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.*;
import static se.inera.certificate.model.CertificateState.DELETED;
import static se.inera.certificate.model.CertificateState.RECEIVED;
import static se.inera.certificate.support.CertificateMetaDataFactory.*;

@RunWith( SpringJUnit4ClassRunner.class )
@ContextConfiguration( locations = {"classpath:persistence-config.xml"} )
@ActiveProfiles( "dev" )
@Transactional
public class CertificateDaoImplTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private CertificateDao certificateDao;

    @Test
    public void testFindCertificateMetaDataWithoutUserId() {
        List<CertificateMetaData> metaData = certificateDao.findCertificateMetaData(null, null, null, null);
        assertTrue(metaData.isEmpty());
    }

    @Test
    public void testFindCertificateMetaDataForUserWithoutCertificates() {
        List<CertificateMetaData> metaData = certificateDao.findCertificateMetaData(CIVIC_REGISTRATION_NUMBER, null, null, null);
        assertTrue(metaData.isEmpty());
    }

    @Test
    public void testFindCertificateMetaDataWithoutTypeForUserWithOneCertificate() {

        entityManager.persist(buildCertificateMetaData());

        List<CertificateMetaData> metaData = certificateDao.findCertificateMetaData(CIVIC_REGISTRATION_NUMBER, null, null, null);
        assertEquals(1, metaData.size());
    }

    @Test
    public void testFindCertificateMetaDataWithEmptyTypeForUserWithOneCertificate() {
        entityManager.persist(buildCertificateMetaData());

        List<CertificateMetaData> metaData = certificateDao.findCertificateMetaData(CIVIC_REGISTRATION_NUMBER, Collections.<String>emptyList(), null, null);
        assertEquals(1, metaData.size());
    }

    @Test
    public void testFindCertificateMetaDataWithCertificateTypeFilter() {

        String otherCertificateType = "other";

        // create an FK7263 and another certificate
        entityManager.persist(buildCertificateMetaData());
        entityManager.persist(buildCertificateMetaData("otherCertificateId", otherCertificateType));

        // no certificate type -> no filtering by certificate type
        List<CertificateMetaData> metaData = certificateDao.findCertificateMetaData(CIVIC_REGISTRATION_NUMBER, null, null, null);
        assertEquals(2, metaData.size());

        // filter by FK7263 -> only return FK7263
        metaData = certificateDao.findCertificateMetaData(CIVIC_REGISTRATION_NUMBER, singletonList(FK7263), null, null);
        assertEquals(1, metaData.size());
        assertEquals(FK7263, metaData.get(0).getType());

        // filter by other type -> only return other certificate
        metaData = certificateDao.findCertificateMetaData(CIVIC_REGISTRATION_NUMBER,
                singletonList(otherCertificateType), null, null);
        assertEquals(1, metaData.size());
        assertEquals(otherCertificateType, metaData.get(0).getType());

        // filter by both types -> both certificates are returned
        metaData = certificateDao.findCertificateMetaData(CIVIC_REGISTRATION_NUMBER,
                asList(FK7263, otherCertificateType), null, null);
        assertEquals(2, metaData.size());
    }

    @Test
    public void testFindCertificateMetaDataWithValidityFilter() throws Exception {
        int certificateId = Integer.parseInt(CERTIFICATE_ID);

        entityManager.persist(buildCertificateMetaData(String.valueOf(certificateId++), "2013-04-13", "2013-05-13"));
        entityManager.persist(buildCertificateMetaData(String.valueOf(certificateId++), "2013-03-13", "2013-04-12"));
        entityManager.persist(buildCertificateMetaData(String.valueOf(certificateId++), "2013-05-13", "2013-06-13"));

        List<CertificateMetaData> metaData = certificateDao.findCertificateMetaData(CIVIC_REGISTRATION_NUMBER,
                singletonList(FK7263), new LocalDate("2013-04-01"), new LocalDate("2013-04-15"));

        assertEquals(2, metaData.size());
    }

    @Test
    public void testGetDocument() throws Exception {
        CertificateMetaData metaData = certificateDao.getCertificate("1");

        assertEquals(new LocalDate("2013-04-24"), metaData.getSignedDate());
        assertEquals(new LocalDate("2013-04-25"), metaData.getValidFromDate());
        assertEquals(new LocalDate("2013-05-25"), metaData.getValidToDate());

        String document = metaData.getDocument();
        assertEquals("<RegisterMedicalCertificate xmlns=\"urn:riv:insuranceprocess:healthreporting:RegisterMedicalCertificateResponder:3\"><lakarutlatande></lakarutlatande></RegisterMedicalCertificate>", document);
    }

    @Test
    public void testStore() throws Exception {

        assertNull(certificateDao.getCertificate(CERTIFICATE_ID));

        CertificateMetaData certificateMetaData = buildCertificateMetaData();
        certificateDao.store(certificateMetaData);

        assertNotNull(certificateDao.getCertificate(CERTIFICATE_ID));
    }

    @Test( expected = InvalidCertificateIdentifierException.class )
    public void testSetCertificateStatusForNonExistingCertificate() {

        certificateDao.updateStatus("12345", "asd", CertificateState.IN_PROGRESS, "fk", null);
    }

    @Test
    public void testSetCertificateStatusForDifferentPatients() {

        // store a certificate for reference patient
        CertificateMetaData certificateMetaData = buildCertificateMetaData();
        entityManager.persist(certificateMetaData);

        assertEquals(0, certificateMetaData.getStates().size());

        try {
            certificateDao.updateStatus(CERTIFICATE_ID, "another patient", RECEIVED, "fk", null);
            fail("Exception expected.");
        } catch (InvalidCertificateIdentifierException e) {
        }

        assertEquals(0, certificateMetaData.getStates().size());
        certificateDao.updateStatus(CERTIFICATE_ID, CIVIC_REGISTRATION_NUMBER, RECEIVED, "fk", null);
        assertEquals(1, certificateMetaData.getStates().size());
    }

    public void testSetCertificateStatus() {

        // store a certificate for patient 19101112-1314
        CertificateMetaData metaData = new CertificateMetaDataBuilder("no1", "<diagnosis>")
                .civicRegistrationNumber("19101112-1314").build();
        entityManager.persist(metaData);

        assertEquals(0, metaData.getStates().size());

        certificateDao.updateStatus("no1", "19101112-1314", DELETED, "fk", null);

        assertEquals(1, metaData.getStates().size());
        assertEquals(DELETED, metaData.getStates().get(0).getState());
        assertEquals("fk", metaData.getStates().get(0).getTarget());
        assertNull(metaData.getStates().get(0).getTimestamp());
    }
}
