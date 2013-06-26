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
import se.inera.certificate.exception.MissingConsentException;
import se.inera.certificate.integration.converter.ModelConverter;
import se.inera.certificate.model.Certificate;
import se.inera.certificate.service.CertificateService;
import se.inera.ifv.insuranceprocess.healthreporting.listcertificates.v1.rivtabp20.ListCertificatesResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.listcertificatesresponder.v1.ListCertificatesRequestType;
import se.inera.ifv.insuranceprocess.healthreporting.listcertificatesresponder.v1.ListCertificatesResponseType;

import java.util.List;

import static se.inera.certificate.integration.ResultOfCallUtil.okResult;
import static se.inera.certificate.integration.ResultOfCallUtil.failResult;

/**
 * @author andreaskaltenbach
 */
@Transactional
public class ListCertificatesResponderImpl implements ListCertificatesResponderInterface {

    @Autowired
    private CertificateService certificateService;

    @Override
    public ListCertificatesResponseType listCertificates(AttributedURIType logicalAddress, ListCertificatesRequestType parameters) {

        ListCertificatesResponseType response = new ListCertificatesResponseType();

        try {
            List<Certificate> certificates = certificateService.listCertificates(
                    parameters.getNationalIdentityNumber(), parameters.getCertificateType(), parameters.getFromDate(), parameters.getToDate());
            for (Certificate certificate : certificates) {
                response.getMeta().add(ModelConverter.toCertificateMetaType(certificate));
            }
            response.setResult(okResult());

        } catch (MissingConsentException ex) {
            response.setResult(failResult(String.format("Missing consent for patient %s", parameters.getNationalIdentityNumber())));
        }

        return response;
    }
}
