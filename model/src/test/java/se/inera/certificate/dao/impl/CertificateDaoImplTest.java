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
import se.inera.certificate.model.Certificate;
import se.inera.certificate.model.CertificateMetaData;
import se.inera.certificate.model.builder.CertificateMetaDataBuilder;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:persistence-config.xml" })
@ActiveProfiles("dev")
@Transactional
public class CertificateDaoImplTest {

    private static final String CERTIFICATE_ID = "02468";
    private static final String CIVIC_REGISTRATION_NUMBER = "19220101-1234";
    private static final String FK7263 = "fk7263";

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

        persistCertificateMetaData(
                new CertificateMetaDataBuilder(CERTIFICATE_ID)
                        .civicRegistrationNumber(CIVIC_REGISTRATION_NUMBER)
                        .certificateType(FK7263));

        List<CertificateMetaData> metaData = certificateDao.findCertificateMetaData(CIVIC_REGISTRATION_NUMBER, null, null, null);
        assertEquals(1, metaData.size());
    }

    private void persistCertificateMetaData(CertificateMetaDataBuilder builder) {
        entityManager.persist(builder.build());
    }

    @Test
    public void testFindCertificateMetaDataWithEmptyTypeForUserWithOneCertificate() {
        persistCertificateMetaData(new CertificateMetaDataBuilder(CERTIFICATE_ID)
                .civicRegistrationNumber(CIVIC_REGISTRATION_NUMBER)
                .certificateType(FK7263));

        List<CertificateMetaData> metaData = certificateDao.findCertificateMetaData(CIVIC_REGISTRATION_NUMBER, Collections.<String>emptyList(), null, null);
        assertEquals(1, metaData.size());
    }

    @Test
    public void testFindCertificateMetaDataWithCertificateTypeFilter() {

        String otherCertificateType = "other";

        // create an FK7263 and another certificate
        persistCertificateMetaData(new CertificateMetaDataBuilder(CERTIFICATE_ID)
                .civicRegistrationNumber(CIVIC_REGISTRATION_NUMBER)
                .certificateType(FK7263));
        persistCertificateMetaData(new CertificateMetaDataBuilder("otherCertificateId")
                .civicRegistrationNumber(CIVIC_REGISTRATION_NUMBER)
                .certificateType(otherCertificateType));

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

        persistCertificateMetaData(new CertificateMetaDataBuilder(String.valueOf(certificateId++))
                .civicRegistrationNumber(CIVIC_REGISTRATION_NUMBER)
                .certificateType(FK7263)
                .validity("2013-04-13", "2013-05-13"));
        persistCertificateMetaData(new CertificateMetaDataBuilder(String.valueOf(certificateId++))
                .civicRegistrationNumber(CIVIC_REGISTRATION_NUMBER)
                .certificateType(FK7263)
                .validity("2013-03-13", "2013-04-12"));
        persistCertificateMetaData(new CertificateMetaDataBuilder(String.valueOf(certificateId++))
                .civicRegistrationNumber(CIVIC_REGISTRATION_NUMBER)
                .certificateType(FK7263)
                .validity("2013-05-13", "2013-06-13"));


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
        Certificate certificate = new Certificate("12345", "Ett dokument");
        CertificateMetaData certificateMetaData = new CertificateMetaData(certificate);
        certificateDao.store(certificateMetaData);
        entityManager.flush();
    }
}
