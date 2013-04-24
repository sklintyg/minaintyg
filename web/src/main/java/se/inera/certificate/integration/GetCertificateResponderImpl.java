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
import org.w3.wsaddressing10.AttributedURIType;
import se.inera.certificate.integration.converter.ModelConverter;
import se.inera.certificate.model.CertificateMetaData;
import se.inera.certificate.service.CertificateService;
import se.inera.ifv.insuranceprocess.healthreporting.getcertificate.v1.rivtabp20.GetCertificateResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.getcertificateresponder.v1.GetCertificateRequestType;
import se.inera.ifv.insuranceprocess.healthreporting.getcertificateresponder.v1.GetCertificateResponseType;

import static se.inera.certificate.integration.ResultOfCallUtil.okResult;

/**
 * @author andreaskaltenbach
 */
public class GetCertificateResponderImpl implements GetCertificateResponderInterface {

    @Autowired
    private CertificateService certificateService;

    @Override
    public GetCertificateResponseType getCertificate(AttributedURIType logicalAddress, GetCertificateRequestType parameters) {

        CertificateMetaData certificate = certificateService.getCertificate(parameters.getNationalIdentityNumber(), parameters.getCertificateId());

        GetCertificateResponseType response = new GetCertificateResponseType();
        response.setMeta(ModelConverter.toCertificateMetaType(certificate));

        response.setResult(okResult());

        return response;
    }
}
