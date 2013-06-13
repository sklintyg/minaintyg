package se.inera.certificate.integration;

import org.apache.cxf.annotations.SchemaValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.w3.wsaddressing10.AttributedURIType;

import se.inera.certificate.service.CertificateService;
import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificate.v1.rivtabp20.SendMedicalCertificateResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificateresponder.v1.SendMedicalCertificateRequestType;
import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificateresponder.v1.SendMedicalCertificateResponseType;

@Transactional
@SchemaValidation
public class SendMedicalCertificateResponderImpl implements SendMedicalCertificateResponderInterface {

    @Autowired
    private CertificateService certificateService;

    @Override
    public SendMedicalCertificateResponseType sendMedicalCertificate(AttributedURIType logicalAddress, SendMedicalCertificateRequestType parameters) {
        SendMedicalCertificateResponseType response = new SendMedicalCertificateResponseType();

        String certificateId = parameters.getSend().getLakarutlatande().getLakarutlatandeId();
        String civicRegistrationNumber = parameters.getSend().getLakarutlatande().getPatient().getPersonId().getExtension();

        try {
            certificateService.sendCertificate(civicRegistrationNumber, certificateId, "FK");
            response.setResult(ResultOfCallUtil.okResult());
        } catch (IllegalArgumentException e) {
            response.setResult(ResultOfCallUtil.applicationErrorResult("Kunde inte skicka." + e.getMessage()));
        }
        
        return response;
    }
}
