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

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.List;

import static se.inera.certificate.integration.ResultOfCallUtil.applicationErrorResult;
import static se.inera.certificate.integration.ResultOfCallUtil.failResult;
import static se.inera.certificate.integration.ResultOfCallUtil.okResult;

import org.apache.cxf.annotations.SchemaValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.w3.wsaddressing10.AttributedURIType;
import se.inera.certificate.exception.MissingConsentException;
import se.inera.certificate.integration.certificates.CertificateSupport;
import se.inera.certificate.integration.converter.LakarutlatandeToRegisterMedicalCertificate;
import se.inera.certificate.integration.converter.ModelConverter;
import se.inera.certificate.model.Certificate;
import se.inera.certificate.model.Lakarutlatande;
import se.inera.certificate.service.CertificateService;
import se.inera.ifv.insuranceprocess.healthreporting.getcertificate.v1.rivtabp20.GetCertificateResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.getcertificateresponder.v1.CertificateType;
import se.inera.ifv.insuranceprocess.healthreporting.getcertificateresponder.v1.GetCertificateRequestType;
import se.inera.ifv.insuranceprocess.healthreporting.getcertificateresponder.v1.GetCertificateResponseType;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateType;

/**
 * @author andreaskaltenbach
 */
@Transactional
@SchemaValidation
public class GetCertificateResponderImpl implements GetCertificateResponderInterface {

    @Autowired
    private CertificateService certificateService;

    @Autowired
    private List<CertificateSupport> supportedCertificates = new ArrayList<>();

    @Override
    public GetCertificateResponseType getCertificate(AttributedURIType logicalAddress, GetCertificateRequestType parameters) {
        GetCertificateResponseType response = new GetCertificateResponseType();

        try {
            Certificate certificate = certificateService.getCertificate(parameters.getNationalIdentityNumber(), parameters.getCertificateId());
            if (certificate != null) {
                response.setMeta(ModelConverter.toCertificateMetaType(certificate));
                attachCertificateDocument(certificate, response);
            } else {
                response.setResult(failResult(String.format("Unknown certificate ID: %s", parameters.getCertificateId())));
            }
        } catch (MissingConsentException ex) {
            response.setResult(failResult(String.format("Missing consent for patient %s", parameters.getNationalIdentityNumber())));
        }

        return response;
    }

    private void attachCertificateDocument(Certificate certificate, GetCertificateResponseType response) {
        CertificateType certificateType = new CertificateType();

        CertificateSupport certificateSupport = retrieveCertificateSupportForCertificateType(certificate.getType());
        if (certificateSupport == null) {
            // given certificate type is not supported
            response.setResult(applicationErrorResult(String.format("Unsupported certificate type: %s", certificate.getType())));
        } else {
            Lakarutlatande lakarutlatande = certificateService.getLakarutlatande(certificate);

            // TODO - this is FK7263-specific and has to be done by the FK7263 module
            RegisterMedicalCertificateType registerMedicalCertificateType = LakarutlatandeToRegisterMedicalCertificate.getJaxbObject(lakarutlatande);
            certificateType.getAny().add(wrapJaxb(registerMedicalCertificateType));

            response.setCertificate(certificateType);
            response.setResult(okResult());
        }
    }

    private JAXBElement<?> wrapJaxb(RegisterMedicalCertificateType ws) {
        JAXBElement<?> jaxbElement = new JAXBElement(
                new QName("urn:riv:insuranceprocess:healthreporting:RegisterMedicalCertificateResponder:3", "RegisterMedicalCertificate"),
                RegisterMedicalCertificateType.class, ws);
        return jaxbElement;
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
