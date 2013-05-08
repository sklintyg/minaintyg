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
import se.inera.certificate.model.Certificate;
import se.inera.certificate.model.CertificateState;
import se.inera.certificate.model.builder.CertificateBuilder;
import se.inera.certificate.support.CertificateFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static se.inera.certificate.model.CertificateState.DELETED;
import static se.inera.certificate.model.CertificateState.RECEIVED;
import static se.inera.certificate.support.CertificateFactory.CIVIC_REGISTRATION_NUMBER;
import static se.inera.certificate.support.CertificateFactory.CERTIFICATE_ID;
import static se.inera.certificate.support.CertificateFactory.FK7263;
import static se.inera.certificate.support.CertificateFactory.buildCertificate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:persistence-config.xml" })
@ActiveProfiles("dev")
@Transactional
public class CertificateDaoImplTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private CertificateDao certificateDao;

    @Test
    public void testFindCertificateWithoutUserId() {
        List<Certificate> certificate = certificateDao.findCertificate(null, null, null, null);
        assertTrue(certificate.isEmpty());
    }

    @Test
    public void testFindCertificateForUserWithoutCertificates() {
        List<Certificate> certificate = certificateDao.findCertificate(CIVIC_REGISTRATION_NUMBER, null, null, null);
        assertTrue(certificate.isEmpty());
    }

    @Test
    public void testFindCertificateWithoutTypeForUserWithOneCertificate() {

        entityManager.persist(buildCertificate());

        List<Certificate> certificate = certificateDao.findCertificate(CIVIC_REGISTRATION_NUMBER, null, null, null);
        assertEquals(1, certificate.size());
    }

    @Test
    public void testFindCertificateWithEmptyTypeForUserWithOneCertificate() {
        entityManager.persist(buildCertificate());

        List<Certificate> certificate = certificateDao.findCertificate(CIVIC_REGISTRATION_NUMBER, Collections.<String>emptyList(), null, null);
        assertEquals(1, certificate.size());
    }

    @Test
    public void testFindCertificateWithCertificateTypeFilter() {

        String otherCertificateType = "other";

        // create an FK7263 and another certificate
        entityManager.persist(buildCertificate());
        entityManager.persist(buildCertificate("otherCertificateId", otherCertificateType));

        // no certificate type -> no filtering by certificate type
        List<Certificate> certificate = certificateDao.findCertificate(CIVIC_REGISTRATION_NUMBER, null, null, null);
        assertEquals(2, certificate.size());

        // filter by FK7263 -> only return FK7263
        certificate = certificateDao.findCertificate(CIVIC_REGISTRATION_NUMBER, singletonList(FK7263), null, null);
        assertEquals(1, certificate.size());
        assertEquals(FK7263, certificate.get(0).getType());

        // filter by other type -> only return other certificate
        certificate = certificateDao.findCertificate(CIVIC_REGISTRATION_NUMBER,
                singletonList(otherCertificateType), null, null);
        assertEquals(1, certificate.size());
        assertEquals(otherCertificateType, certificate.get(0).getType());

        // filter by both types -> both certificates are returned
        certificate = certificateDao.findCertificate(CIVIC_REGISTRATION_NUMBER,
                asList(FK7263, otherCertificateType), null, null);
        assertEquals(2, certificate.size());
    }

    @Test
    public void testFindCertificateWithValidityFilter() throws Exception {
        int certificateId = Integer.parseInt(CERTIFICATE_ID);

        entityManager.persist(buildCertificate(String.valueOf(certificateId++), "2013-04-13", "2013-05-13"));
        entityManager.persist(buildCertificate(String.valueOf(certificateId++), "2013-03-13", "2013-04-12"));
        entityManager.persist(buildCertificate(String.valueOf(certificateId++), "2013-05-13", "2013-06-13"));

        List<Certificate> certificate = certificateDao.findCertificate(CIVIC_REGISTRATION_NUMBER,
                singletonList(FK7263), new LocalDate("2013-04-01"), new LocalDate("2013-04-15"));

        assertEquals(2, certificate.size());
    }

    @Test
    public void testGetDocument() {

        Certificate certificate = CertificateFactory.buildCertificate("1", new LocalDate("2013-04-25"), new LocalDate("2013-05-25"));
        certificate.setSignedDate(new LocalDate("2013-04-24"));
        certificateDao.store(certificate);

        entityManager.flush();
        entityManager.clear();

        Certificate storedCertificate = certificateDao.getCertificate("1");

        assertEquals(new LocalDate("2013-04-24"), storedCertificate.getSignedDate());
        assertEquals(new LocalDate("2013-04-25"), storedCertificate.getValidFromDate());
        assertEquals(new LocalDate("2013-05-25"), storedCertificate.getValidToDate());

        String document = storedCertificate.getDocument();
        assertEquals("<RegisterMedicalCertificate xmlns=\"urn:riv:insuranceprocess:healthreporting:RegisterMedicalCertificateResponder:3\"><lakarutlatande></lakarutlatande></RegisterMedicalCertificate>", document);
    }

    @Test
    public void testStore() {

        assertNull(certificateDao.getCertificate(CERTIFICATE_ID));

        Certificate certificate = buildCertificate();
        certificateDao.store(certificate);

        assertNotNull(certificateDao.getCertificate(CERTIFICATE_ID));
    }

    @Test(expected = InvalidCertificateIdentifierException.class)
    public void testSetCertificateStatusForNonExistingCertificate() {

        certificateDao.updateStatus("12345", "asd", CertificateState.IN_PROGRESS, "fk", null);
    }

    @Test
    public void testSetCertificateStatusForDifferentPatients() {

        // store a certificate for reference patient
        Certificate certificate = buildCertificate();
        entityManager.persist(certificate);

        assertEquals(0, certificate.getStates().size());

        try {
            certificateDao.updateStatus(CERTIFICATE_ID, "another patient", RECEIVED, "fk", null);
            fail("Exception expected.");
        } catch (InvalidCertificateIdentifierException e) {
            // Empty
        }

        assertEquals(0, certificate.getStates().size());
        certificateDao.updateStatus(CERTIFICATE_ID, CIVIC_REGISTRATION_NUMBER, RECEIVED, "fk", null);
        assertEquals(1, certificate.getStates().size());
    }

    public void testSetCertificateStatus() {

        // store a certificate for patient 19101112-1314
        Certificate certificate = new CertificateBuilder("no1", "<diagnosis>")
                .civicRegistrationNumber("19101112-1314").build();
        entityManager.persist(certificate);

        assertEquals(0, certificate.getStates().size());

        certificateDao.updateStatus("no1", "19101112-1314", DELETED, "fk", null);

        assertEquals(1, certificate.getStates().size());
        assertEquals(DELETED, certificate.getStates().get(0).getState());
        assertEquals("fk", certificate.getStates().get(0).getTarget());
        assertNull(certificate.getStates().get(0).getTimestamp());
    }
}
