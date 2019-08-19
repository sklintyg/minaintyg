/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.intyg.minaintyg.web.service;

import java.util.List;
import java.util.Optional;
import se.inera.intyg.clinicalprocess.healthcond.certificate.listrelationsforcertificate.v1.IntygRelations;
import se.inera.intyg.common.support.modules.support.api.dto.CertificateResponse;
import se.inera.intyg.minaintyg.web.api.SendToRecipientResult;
import se.inera.intyg.minaintyg.web.exception.ExternalWebServiceCallFailedException;
import se.inera.intyg.minaintyg.web.service.dto.UtlatandeMetaData;
import se.inera.intyg.minaintyg.web.service.dto.UtlatandeRecipient;
import se.inera.intyg.schemas.contract.Personnummer;

public interface CertificateService {

    Optional<CertificateResponse> getUtlatande(String type, String intygTypeVersion, Personnummer civicRegistrationNumber,
        String certificateId)
        throws ExternalWebServiceCallFailedException;

    /**
     * Retrives a list of certificates for the given civicRegistrationNumber.
     */
    List<UtlatandeMetaData> getCertificates(Personnummer civicRegistrationNumber, boolean arkiverade);

    /**
     * Request to send a specific certificate to a specific recipient.
     */
    List<SendToRecipientResult> sendCertificate(Personnummer civicRegistrationNumber, String certificateId,
        List<String> recipientIds);

    /**
     * Retrieves a list of possible recipients for the given certificate type.
     *
     * @param certificateType the type of certificate
     * @return a List of {@link UtlatandeRecipient}
     */
    List<UtlatandeRecipient> getRecipientsForCertificate(String certificateType);

    /**
     * Retrieve a list of all recipients known to Intygstjänsten.
     *
     * @return a List of {@link UtlatandeRecipient}
     */
    List<UtlatandeRecipient> getAllRecipients();

    /**
     * Set a certificate as archived.
     */
    UtlatandeMetaData archiveCertificate(String certificateId, Personnummer civicRegistrationNumber);

    /**
     * Restore a certificate from an archived state.
     */
    UtlatandeMetaData restoreCertificate(String certificateId, Personnummer civicRegistrationNumber);

    /**
     * Get all labels for intyg as json.
     */
    String getQuestions(String intygsTyp, String version);

    /**
     * Get relation for one or many intyg ids.
     */
    List<IntygRelations> getRelationsForCertificates(List<String> intygIds);
}
