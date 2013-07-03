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
package se.inera.certificate.service;

import java.util.List;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import se.inera.certificate.exception.CertificateRevokedException;
import se.inera.certificate.exception.InvalidCertificateException;
import se.inera.certificate.exception.MissingConsentException;
import se.inera.certificate.model.CertificateState;
import se.inera.certificate.model.Lakarutlatande;
import se.inera.certificate.model.dao.Certificate;

/**
 * @author andreaskaltenbach
 */
public interface CertificateService {

    /**
     * Returns the list of certificates for the patient and filter criteria.
     *
     * @param civicRegistrationNumber the patient's civic registration number
     * @param certificateTypes optional certificate type filter. If empty or null, all certificate types will be returned
     * @param fromDate optional from date filter
     * @param toDate optional to date filter
     * @return list of matching certificates or empty list if no such certificates can be found
     * @throws MissingConsentException if the patient has not given consent for accessing her certificates
     */
    List<Certificate> listCertificates(String civicRegistrationNumber, List<String> certificateTypes, LocalDate fromDate, LocalDate toDate) throws MissingConsentException;

    /**
     * Returns the certificate for the given patient and certificate ID.
     *
     * @param civicRegistrationNumber the patient's civic registration number
     * @param certificateId the certificate ID
     * @return the certificate information or null if the requested certificate does not exist
     * @throws MissingConsentException if the patient has not given consent for accessing her certificates
     */
    Certificate getCertificate(String civicRegistrationNumber, String certificateId) throws MissingConsentException;

    Certificate storeCertificate(Lakarutlatande lakarutlatande);

    void setCertificateState(String civicRegistrationNumber, String certificateId, String target, CertificateState state, LocalDateTime timestamp);

    /**
     * Sends the certificate to the destined target.
     * @throws InvalidCertificateException if the certificate does not exist
     * @throws CertificateRevokedException if the certificate has been revoked
     */
    void sendCertificate(String civicRegistrationNumber, String certificateId, String target) throws InvalidCertificateException, CertificateRevokedException;

    Lakarutlatande getLakarutlatande(Certificate certificate);

    /**
     * Revokes the certifictae.
     * @return the revoked certificate.
     * @throws InvalidCertificateException if the certificate does not exist
     * @throws CertificateRevokedException if the certificate has been revoked
     */
    Certificate revokeCertificate(String civicRegistrationNumber, String certificateId) throws InvalidCertificateException, CertificateRevokedException;
}
