package se.inera.certificate.integration;

import java.io.StringReader;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

import org.apache.cxf.annotations.SchemaValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.w3.wsaddressing10.AttributedURIType;

import se.inera.certificate.integration.certificates.CertificateSupport;
import se.inera.certificate.model.Certificate;
import se.inera.certificate.service.CertificateService;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificate.v3.rivtabp20.RegisterMedicalCertificateResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateType;
import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificate.v1.rivtabp20.SendMedicalCertificateResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificateresponder.v1.SendMedicalCertificateRequestType;
import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificateresponder.v1.SendMedicalCertificateResponseType;

@Transactional
@SchemaValidation
public class SendMedicalCertificateResponderImpl implements SendMedicalCertificateResponderInterface {

    @Autowired
    private CertificateService certificateService;

    @Autowired
    private RegisterMedicalCertificateResponderInterface registerMedicalCertificateResponder;

    @Autowired
    private List<CertificateSupport> supportedCertificates;

    @Override
    public SendMedicalCertificateResponseType sendMedicalCertificate(AttributedURIType logicalAddress, SendMedicalCertificateRequestType parameters) {
        SendMedicalCertificateResponseType response = new SendMedicalCertificateResponseType();

        String certificateId = parameters.getSend().getLakarutlatande().getLakarutlatandeId();
        String civicRegistrationNumber = parameters.getSend().getLakarutlatande().getPatient().getPersonId().getExtension();

        Certificate certificate = certificateService.getCertificate(civicRegistrationNumber, certificateId);

        // TODO: Hur hanterar vi olika typer av intyg och destinationer? / PW
        if (certificate.getType().equalsIgnoreCase("fk7263")) {
            CertificateSupport certificateSupport = retrieveCertificateSupportForCertificateType(certificate.getType());
            registerMedicalCertificateResponder.registerMedicalCertificate(logicalAddress, getJaxbObject(certificateSupport, certificate));
            response.setResult(ResultOfCallUtil.okResult());
        } else {
            response.setResult(ResultOfCallUtil.applicationErrorResult("Metoden är inte implementerad"));
        }

        return response;
    }

    private RegisterMedicalCertificateType getJaxbObject(CertificateSupport certificateSupport, Certificate certificate) {
        try {
            @SuppressWarnings("unchecked")
            JAXBElement<RegisterMedicalCertificateType> elem = (JAXBElement<RegisterMedicalCertificateType>)
            certificateSupport.getJaxbContext().createUnmarshaller().unmarshal(new StringReader(certificate.getDocument()));
            return elem.getValue();
        } catch (JAXBException e) {
            // TODO: Kasta annat undantag! /PW
            throw new RuntimeException(e);
        }
    }

    // TODO: Move to Util... Duplicated code / PW
    private CertificateSupport retrieveCertificateSupportForCertificateType(String certificateType) {
        for (CertificateSupport certificateSupport : supportedCertificates) {
            if (certificateSupport.certificateType().equalsIgnoreCase(certificateType)) {
                return certificateSupport;
            }
        }
        return null;
    }

}
