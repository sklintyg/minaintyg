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

import org.joda.time.LocalDateTime;

import se.inera.certificate.api.ModuleAPIResponse;
import se.inera.certificate.exception.ExternalWebServiceCallFailedException;
import se.inera.certificate.web.service.dto.UtlatandeMetaData;
import se.inera.certificate.web.service.dto.UtlatandeRecipient;
import se.inera.certificate.web.service.dto.UtlatandeWithMeta;
import se.inera.ifv.insuranceprocess.certificate.v1.StatusType;

import java.util.List;

public interface CertificateService {

    UtlatandeWithMeta getUtlatande(String civicRegistrationNumber, String certificateId) throws ExternalWebServiceCallFailedException;

    /**
     * Retrives a list of certificates for the given civicRegistrationNumber.
     *
     * @param civicRegistrationNumber
     * @return
     */
    List<UtlatandeMetaData> getCertificates(String civicRegistrationNumber);

    /**
     * Sets a new status for the certificate.
     *
     * @param id
     * @param target
     * @param type
     * @return Partially populated CertificateMeta object with id and new status and status description
     */
    UtlatandeMetaData setCertificateStatus(String civicRegistrationNumber, String id, LocalDateTime timestamp, String target, StatusType type);

    /**
     * Request to send a specific certificate to a specific recipient.
     *
     * @param civicRegistrationNumber
     * @param id
     * @param target
     * @return
     */
    ModuleAPIResponse sendCertificate(String civicRegistrationNumber, String id, String target);

    /**
     * Retrieves a list of possible recipients for the given certificate type.
     *
     * @param certificateType
     *            the type of certificate to check
     * @return a List of {@link UtlatandeRecipient}
     */
    List<UtlatandeRecipient> getRecipientsForCertificate(String certificateType);

    /**
     * Set archived stat for a certificate
     * @param id
     * @param civicRegistrationNumber
     * @param archivedState
     * @param timestamp
     */
    UtlatandeMetaData setArchived(String id, String civicRegistrationNumber, String archivedState);
}
