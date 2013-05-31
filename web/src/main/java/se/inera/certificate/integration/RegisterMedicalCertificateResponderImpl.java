package se.inera.certificate.integration;

import org.apache.cxf.annotations.SchemaValidation;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.w3.wsaddressing10.AttributedURIType;
import se.inera.certificate.integration.converter.LakarutlatandeTypeToLakarutlatandeConverter;
import se.inera.certificate.model.Lakarutlatande;
import se.inera.certificate.service.CertificateService;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificate.v3.rivtabp20.RegisterMedicalCertificateResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateResponseType;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateType;

import static se.inera.certificate.integration.ResultOfCallUtil.okResult;

/**
 * @author andreaskaltenbach
 */
@Transactional
@SchemaValidation
public class RegisterMedicalCertificateResponderImpl implements RegisterMedicalCertificateResponderInterface {

    @Autowired
    private CertificateService certificateService;

    @Override
    public RegisterMedicalCertificateResponseType registerMedicalCertificate(AttributedURIType logicalAddress, RegisterMedicalCertificateType request) {

        Lakarutlatande lakarutlatande = LakarutlatandeTypeToLakarutlatandeConverter.convert(request.getLakarutlatande());
        certificateService.storeCertificate(lakarutlatande);

        RegisterMedicalCertificateResponseType response = new RegisterMedicalCertificateResponseType();
        response.setResult(okResult());
        return response;
    }
}
