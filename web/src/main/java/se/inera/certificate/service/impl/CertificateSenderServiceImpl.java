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

import static se.inera.ifv.insuranceprocess.healthreporting.v2.ResultCodeEnum.OK;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import se.inera.certificate.integration.exception.ExternalWebServiceCallFailedException;
import se.inera.certificate.integration.converter.UtlatandeToRegisterMedicalCertificate;
import se.inera.certificate.model.Utlatande;
import se.inera.certificate.model.dao.Certificate;
import se.inera.certificate.service.CertificateSenderService;
import se.inera.certificate.service.CertificateService;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificate.v3.rivtabp20.RegisterMedicalCertificateResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateResponseType;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateType;

/**
 * @author andreaskaltenbach
 */
@Service
public class CertificateSenderServiceImpl implements CertificateSenderService {

    @Autowired
    private RegisterMedicalCertificateResponderInterface registerMedicalCertificateResponder;

    @Autowired
    private CertificateService certificateService;

    @Override
    public void sendCertificate(Certificate certificate, String target) {
        if (certificate.getType().equalsIgnoreCase("fk7263")) {

            Utlatande utlatande = certificateService.getLakarutlatande(certificate);
            RegisterMedicalCertificateType jaxbObject = UtlatandeToRegisterMedicalCertificate.getJaxbObject(utlatande);

            RegisterMedicalCertificateResponseType response = registerMedicalCertificateResponder.registerMedicalCertificate(null, jaxbObject);
            if (response.getResult().getResultCode() != OK) {
                throw new ExternalWebServiceCallFailedException(response.getResult());
            }

        } else {
            throw new IllegalArgumentException("Can not send certificate of type " + certificate.getType() + " to " + target);
        }
    }
}
