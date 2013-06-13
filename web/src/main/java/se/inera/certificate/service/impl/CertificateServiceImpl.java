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
package se.inera.certificate.service.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.inera.certificate.dao.CertificateDao;
import se.inera.certificate.exception.MissingConsentException;
import se.inera.certificate.model.Certificate;
import se.inera.certificate.model.CertificateState;
import se.inera.certificate.model.CertificateStateHistoryEntry;
import se.inera.certificate.model.Lakarutlatande;
import se.inera.certificate.service.CertificateService;
import se.inera.certificate.service.ConsentService;

/**
 * @author andreaskaltenbach
 */
@Service
public class CertificateServiceImpl implements CertificateService {

    @Autowired
    private ObjectMapper objectMapper;

    private static final Comparator<CertificateStateHistoryEntry> SORTER = new Comparator<CertificateStateHistoryEntry>() {

        @Override
        public int compare(CertificateStateHistoryEntry e1, CertificateStateHistoryEntry e2) {
            return e2.getTimestamp().compareTo(e1.getTimestamp());
        }
    };

    public static final String MI = "MI";

    @Autowired
    private CertificateDao certificateDao;

    @Autowired
    private ConsentService consentService;

    @Override
    public List<Certificate> listCertificates(String civicRegistrationNumber, List<String> certificateTypes, LocalDate fromDate, LocalDate toDate) {
        assertConsent(civicRegistrationNumber);
        return fixDeletedStatus(certificateDao.findCertificate(civicRegistrationNumber, certificateTypes, fromDate, toDate));
    }

    @Override
    public Certificate getCertificate(String civicRegistrationNumber, String id) {
        assertConsent(civicRegistrationNumber);
        return fixDeletedStatus(certificateDao.getCertificate(id));
    }

    @Override
    @Transactional
    public Certificate storeCertificate(Lakarutlatande lakarutlatande) {

        // turn a lakarutlatande into a certificate entity
        Certificate certificate = createCertificate(lakarutlatande);

        // add initial RECEIVED state using current time as receiving timestamp
        CertificateStateHistoryEntry state = new CertificateStateHistoryEntry(MI, CertificateState.RECEIVED, new LocalDateTime());
        certificate.getStates().add(state);

        certificateDao.store(certificate);

        return certificate;
    }

    @Override
    @Transactional
    public void sendCertificate(String civicRegistrationNumber, String certificateId, String target) {
        // TODO Actually call send implementation and then set status sent if all ok
        setCertificateState(civicRegistrationNumber, certificateId, target, CertificateState.SENT, null);
    }

    private String toJson(Lakarutlatande lakarutlatande) {
        try {
            return objectMapper.writeValueAsString(lakarutlatande);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize lakarutlatande.", e);
        }
    }

    private Certificate createCertificate(Lakarutlatande lakarutlatande) {
        Certificate certificate = new Certificate(lakarutlatande.getId(), toJson(lakarutlatande));

        certificate.setType(lakarutlatande.getTyp());
        certificate.setSigningDoctorName(lakarutlatande.getSkapadAv().getNamn());
        certificate.setSignedDate(lakarutlatande.getSigneringsDatum());
        certificate.setCareUnitName(lakarutlatande.getVardenhet().getNamn());
        certificate.setCivicRegistrationNumber(lakarutlatande.getPatient().getId());
        certificate.setValidFromDate(lakarutlatande.getValidFromDate());
        certificate.setValidToDate(lakarutlatande.getValidToDate());

        return certificate;
    }

    @Override
    public void setCertificateState(String civicRegistrationNumber, String certificateId, String target, CertificateState state, LocalDateTime timestamp) {
        certificateDao.updateStatus(certificateId, civicRegistrationNumber, state, target, timestamp);
    }

    private void assertConsent(String civicRegistrationNumber) {
        if (!consentService.isConsent(civicRegistrationNumber)) {
            throw new MissingConsentException(civicRegistrationNumber);
        }
    }

    private List<Certificate> fixDeletedStatus(List<Certificate> certificates) {
        for (Certificate certificate : certificates) {
            fixDeletedStatus(certificate);
        }
        return certificates;
    }

    private Certificate fixDeletedStatus(Certificate certificate) {
        if (certificate != null) {
            List<CertificateStateHistoryEntry> states = certificate.getStates();
            Collections.sort(states, SORTER);
            certificate.setDeleted(isDeleted(states));
        }
        return certificate;
    }

    private Boolean isDeleted(List<CertificateStateHistoryEntry> entries) {
        for (CertificateStateHistoryEntry entry : entries) {
            switch (entry.getState()) {
                case DELETED:
                    return true;
                case RESTORED:
                    return false;
                default:
            }
        }
        return false;
    }
}
