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
