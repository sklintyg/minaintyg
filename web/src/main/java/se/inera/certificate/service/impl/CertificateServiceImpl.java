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

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.inera.certificate.dao.CertificateDao;
import se.inera.certificate.exception.MissingConsentException;
import se.inera.certificate.model.Certificate;
import se.inera.certificate.model.CertificateState;
import se.inera.certificate.model.CertificateStateHistoryEntry;
import se.inera.certificate.service.CertificateService;
import se.inera.certificate.service.ConsentService;

import java.util.List;

/**
 * @author andreaskaltenbach
 */
@Service
public class CertificateServiceImpl implements CertificateService {

    public static final String MI = "MI";
    @Autowired
    private CertificateDao certificateDao;

    @Autowired
    private ConsentService consentService;

    @Override
    public List<Certificate> listCertificates(String civicRegistrationNumber, List<String> certificateTypes, LocalDate fromDate, LocalDate toDate) {

        if (!consentService.isConsent(civicRegistrationNumber)) {
            throw new MissingConsentException(civicRegistrationNumber);
        }

        return certificateDao.findCertificate(civicRegistrationNumber, certificateTypes, fromDate, toDate);
    }

    @Override
    public Certificate getCertificate(String civicRegistrationNumber, String id) {

        if (!consentService.isConsent(civicRegistrationNumber)) {
            throw new MissingConsentException(civicRegistrationNumber);
        }

        return certificateDao.getCertificate(id);
    }

    @Override
    public void storeCertificate(Certificate certificate) {

        // add initial RECEIVED state using current time as receiving timestamp
        CertificateStateHistoryEntry state = new CertificateStateHistoryEntry(MI, CertificateState.RECEIVED, new LocalDateTime());
        certificate.getStates().add(state);

        certificateDao.store(certificate);
    }

    @Override
    public void setCertificateState(String civicRegistrationNumber, String certificateId, String target, CertificateState state, LocalDateTime timestamp) {
        certificateDao.updateStatus(certificateId, civicRegistrationNumber, state, target, timestamp);
    }
}
