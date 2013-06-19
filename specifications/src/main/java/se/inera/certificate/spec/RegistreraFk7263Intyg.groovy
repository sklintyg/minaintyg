package se.inera.certificate.spec

import org.springframework.core.io.ClassPathResource
import se.inera.certificate.spec.util.WsClientFixture
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificate.v3.rivtabp20.RegisterMedicalCertificateResponderInterface
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificate.v3.rivtabp20.RegisterMedicalCertificateResponderService
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateResponseType
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateType

import javax.xml.bind.JAXBContext
import javax.xml.bind.Unmarshaller
import javax.xml.transform.stream.StreamSource

/**
 *
 * @author andreaskaltenbach
 */
public class RegistreraFk7263Intyg extends WsClientFixture {

    private RegisterMedicalCertificateResponderService registerMedicalCertificateService = new RegisterMedicalCertificateResponderService()
    private RegisterMedicalCertificateResponderInterface registerMedicalCertificateResponder = registerMedicalCertificateService.registerMedicalCertificateResponderPort

    public RegistreraFk7263Intyg() {
        setEndpoint(registerMedicalCertificateResponder, "register-certificate/v3.0")
    }

    String personnummer
    String intyg

    RegisterMedicalCertificateResponseType response

    public void execute() {
        // read request template from file
        JAXBContext jaxbContext = JAXBContext.newInstance(RegisterMedicalCertificateType.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        RegisterMedicalCertificateType request = unmarshaller.unmarshal(new StreamSource(new ClassPathResource("fk7263_template.xml").getInputStream()), RegisterMedicalCertificateType.class).getValue()

        request.getLakarutlatande().getPatient().getPersonId().setExtension(personnummer)
        request.getLakarutlatande().setLakarutlatandeId(intyg)

        response = registerMedicalCertificateResponder.registerMedicalCertificate(null, request);
    }

    public String svar() {
        resultAsString(response)
    }
}
