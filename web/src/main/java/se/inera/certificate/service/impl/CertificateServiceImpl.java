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

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import se.inera.certificate.exception.CertificateRevokedException;
import se.inera.certificate.exception.InvalidCertificateException;
import se.inera.certificate.exception.MissingConsentException;
import se.inera.certificate.model.CertificateState;
import se.inera.certificate.model.Utlatande;
import se.inera.certificate.model.dao.Certificate;
import se.inera.certificate.model.dao.CertificateDao;
import se.inera.certificate.model.dao.CertificateStateHistoryEntry;
import se.inera.certificate.service.CertificateSenderService;
import se.inera.certificate.service.CertificateService;
import se.inera.certificate.service.ConsentService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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

    @Autowired
    private CertificateSenderService senderService;

    @Override
    public List<Certificate> listCertificates(String civicRegistrationNumber, List<String> certificateTypes, LocalDate fromDate, LocalDate toDate) {
        assertConsent(civicRegistrationNumber);
        return fixDeletedStatus(certificateDao.findCertificate(civicRegistrationNumber, certificateTypes, fromDate, toDate));
    }

    @Override
    public Certificate getCertificate(String civicRegistrationNumber, String id) {
        assertConsent(civicRegistrationNumber);
        return getCertificateInternal(civicRegistrationNumber, id);
    }

    private Certificate getCertificateInternal(String civicRegistrationNumber, String id) {
        return fixDeletedStatus(certificateDao.getCertificate(civicRegistrationNumber, id));
    }

    @Override
    @Transactional
    public Certificate storeCertificate(Utlatande utlatande) {

        // turn a lakarutlatande into a certificate entity
        Certificate certificate = createCertificate(utlatande);

        // add initial RECEIVED state using current time as receiving timestamp
        CertificateStateHistoryEntry state = new CertificateStateHistoryEntry(MI, CertificateState.RECEIVED, new LocalDateTime());
        certificate.getStates().add(state);

        certificateDao.store(certificate);

        return certificate;
    }

    @Override
    @Transactional
    public void sendCertificate(String civicRegistrationNumber, String certificateId, String target) {
        Certificate certificate = getCertificateInternal(civicRegistrationNumber, certificateId);

        if (certificate == null) {
            throw new InvalidCertificateException(certificateId, civicRegistrationNumber);
        }
        if (certificate.isRevoked()) {
            throw new CertificateRevokedException(certificateId);
        }

        senderService.sendCertificate(certificate, target);
        setCertificateState(civicRegistrationNumber, certificateId, target, CertificateState.SENT, null);
    }

    private String toJson(Utlatande utlatande) {
        try {
            return objectMapper.writeValueAsString(utlatande);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize lakarutlatande.", e);
        }
    }

    private Certificate createCertificate(Utlatande utlatande) {
        Certificate certificate = new Certificate(utlatande.getId().getExtension(), toJson(utlatande));

        certificate.setType(utlatande.getTyp().getCode());
        certificate.setSigningDoctorName(utlatande.getSkapadAv().getNamn());
        certificate.setSignedDate(utlatande.getSigneringsDatum());

        if (utlatande.getSkapadAv() != null && utlatande.getSkapadAv().getVardenhet() != null) {
            certificate.setCareUnitName(utlatande.getSkapadAv().getVardenhet().getNamn());
        }

        certificate.setCivicRegistrationNumber(utlatande.getPatient().getId().getExtension());
        certificate.setValidFromDate(utlatande.getValidFromDate());
        certificate.setValidToDate(utlatande.getValidToDate());

        return certificate;
    }

    @Override
    public void setCertificateState(String civicRegistrationNumber, String certificateId, String target, CertificateState state, LocalDateTime timestamp) {
        certificateDao.updateStatus(certificateId, civicRegistrationNumber, state, target, timestamp);
    }

    @Override
    public Utlatande getLakarutlatande(Certificate certificate) {
        try {
            return objectMapper.readValue(certificate.getDocument(), Utlatande.class);
        } catch (IOException e) {
            throw new IllegalStateException("Could not parse document for " + certificate.getId(), e);
        }
    }

    @Override
    public Certificate revokeCertificate(String civicRegistrationNumber, String certificateId) {
        Certificate certificate = getCertificateInternal(civicRegistrationNumber, certificateId);

        if (certificate == null) {
            throw new InvalidCertificateException(certificateId, civicRegistrationNumber);
        }

        if (certificate.isRevoked()) {
            throw new CertificateRevokedException(certificateId);
        }
        setCertificateState(civicRegistrationNumber, certificateId, "FK", CertificateState.CANCELLED, null);
        return certificate;
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
