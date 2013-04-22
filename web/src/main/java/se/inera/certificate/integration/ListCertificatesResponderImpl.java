package se.inera.certificate.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.w3.wsaddressing10.AttributedURIType;
import se.inera.certificate.integration.converter.ModelConverter;
import se.inera.certificate.model.CertificateMetaData;
import se.inera.certificate.service.CertificateService;
import se.inera.ifv.insuranceprocess.healthreporting.listcertificates.v1.rivtabp20.ListCertificatesResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.listcertificatesresponder.v1.ListCertificatesRequestType;
import se.inera.ifv.insuranceprocess.healthreporting.listcertificatesresponder.v1.ListCertificatesResponseType;

import java.util.List;

import static se.inera.certificate.integration.ResultOfCallUtil.okResult;

/**
 * @author andreaskaltenbach
 */
public class ListCertificatesResponderImpl implements ListCertificatesResponderInterface {

    @Autowired
    CertificateService certificateService;

    @Override
    public ListCertificatesResponseType listCertificates(AttributedURIType logicalAddress, ListCertificatesRequestType parameters) {

        ListCertificatesResponseType response = new ListCertificatesResponseType();

        List<CertificateMetaData> certificates = certificateService.listCertificates(parameters.getNationalIdentityNumber(), parameters.getCertificateType());

        for (CertificateMetaData certificate : certificates) {
            response.getMeta().add(ModelConverter.toCertificateMetaType(certificate));
        }

        response.setResult(okResult());

        return response;
    }


}
