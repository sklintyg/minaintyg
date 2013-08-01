/**
 * Copyright (C) 2013 Inera AB (http://www.inera.se)
 *
 * This file is part of Inera Certificate Web (http://code.google.com/p/inera-certificate-web).
 *
 * Inera Certificate Web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Inera Certificate Web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.certificate.web.service;

import java.util.List;

import org.joda.time.LocalDateTime;
import se.inera.certificate.api.CertificateMeta;
import se.inera.certificate.api.ModuleAPIResponse;
import se.inera.certificate.integration.exception.ExternalWebServiceCallFailedException;
import se.inera.certificate.model.Utlatande;
import se.inera.ifv.insuranceprocess.certificate.v1.StatusType;

public interface CertificateService {

    Utlatande getUtlatande(String civicRegistrationNumber, String certificateId) throws ExternalWebServiceCallFailedException;

    /**
     * Retrives a list of certificates for the given civicRegistrationNumber.
     * @param civicRegistrationNumber
     * @return
     */
    List<CertificateMeta> getCertificates(String civicRegistrationNumber);

    /**
     * Sets a new status for the certificate.
     * @param id
     * @param target
     * @param type
     * @return Partially populated CertificateMeta object with id and new status and status description
     */
    CertificateMeta setCertificateStatus(String civicRegistrationNumber, String id, LocalDateTime timestamp, String target, StatusType type);

    /**
     * Request to send a specific certificate to a specific recipient.
     * @param civicRegistrationNumber
     * @param id
     * @param target
     * @return
     */
    ModuleAPIResponse sendCertificate(String civicRegistrationNumber, String id, String target);
}
