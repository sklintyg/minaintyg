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
package se.inera.certificate.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.w3.wsaddressing10.AttributedURIType;
import se.inera.certificate.integration.certificates.CertificateSupport;
import se.inera.certificate.integration.converter.ModelConverter;
import se.inera.certificate.model.CertificateMetaData;
import se.inera.certificate.service.CertificateService;
import se.inera.ifv.insuranceprocess.healthreporting.getcertificate.v1.rivtabp20.GetCertificateResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.getcertificateresponder.v1.CertificateType;
import se.inera.ifv.insuranceprocess.healthreporting.getcertificateresponder.v1.GetCertificateRequestType;
import se.inera.ifv.insuranceprocess.healthreporting.getcertificateresponder.v1.GetCertificateResponseType;

import java.util.ArrayList;
import java.util.List;

import static se.inera.certificate.integration.ResultOfCallUtil.applicationErrorResult;
import static se.inera.certificate.integration.ResultOfCallUtil.failResult;
import static se.inera.certificate.integration.ResultOfCallUtil.okResult;

/**
 * @author andreaskaltenbach
 */
@Transactional
public class GetCertificateResponderImpl implements GetCertificateResponderInterface {

    @Autowired
    private CertificateService certificateService;

    @Autowired
    List<CertificateSupport> supportedCertificates = new ArrayList<>();

    @Override
    public GetCertificateResponseType getCertificate(AttributedURIType logicalAddress, GetCertificateRequestType parameters) {

        CertificateMetaData metaData = certificateService.getCertificate(parameters.getNationalIdentityNumber(), parameters.getCertificateId());

        GetCertificateResponseType response = new GetCertificateResponseType();

        if (metaData != null) {
            response.setMeta(ModelConverter.toCertificateMetaType(metaData));
            attachCertificateDocument(metaData, response);
        }
        else {
            response.setResult(failResult(String.format("Unknown certificate ID: %s", parameters.getCertificateId())));
        }

        return response;
    }

    private void attachCertificateDocument(CertificateMetaData metaData, GetCertificateResponseType response) {
        CertificateType certificateType = new CertificateType();

        CertificateSupport certificateSupport = retrieveCertificateSupportForCertificateType(metaData.getType());
        if (certificateSupport == null) {
            // given certificate type is not supported
            response.setResult(applicationErrorResult(String.format("Unsupported certificate type: %s", metaData.getType())));
        }
        else {
            // certificate type is supported and we use a CertificateSupport implementation to get the JAXB element from the certificate string
            certificateType.getAny().add(certificateSupport.deserializeCertificate(metaData.getCertificate().getDocument()));
            response.setCertificate(certificateType);
            response.setResult(okResult());
        }
    }

    private CertificateSupport retrieveCertificateSupportForCertificateType(String certificateType) {
        for (CertificateSupport certificateSupport : supportedCertificates) {
            if (certificateSupport.certificateType().equals(certificateType)) {
                return certificateSupport;
            }
        }
        return null;
    }


}
