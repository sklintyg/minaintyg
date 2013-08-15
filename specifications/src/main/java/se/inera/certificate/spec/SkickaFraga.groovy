package se.inera.certificate.spec

import static se.inera.ifv.insuranceprocess.healthreporting.v2.ResultCodeEnum.OK
import iso.v21090.dt.v1.II

import javax.xml.bind.JAXBContext
import javax.xml.bind.Unmarshaller
import javax.xml.transform.stream.StreamSource

import org.apache.cxf.endpoint.Client
import org.apache.cxf.frontend.ClientProxy
import org.apache.cxf.message.Message
import org.joda.time.LocalDateTime
import org.springframework.core.io.ClassPathResource

import riv.insuranceprocess.healthreporting.medcertqa._1.Amnetyp
import riv.insuranceprocess.healthreporting.medcertqa._1.InnehallType
import riv.insuranceprocess.healthreporting.medcertqa._1.LakarutlatandeEnkelType
import riv.insuranceprocess.healthreporting.medcertqa._1.VardAdresseringsType
import se.inera.certificate.spec.util.WsClientFixture
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateType
import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificatequestion.v1.rivtabp20.SendMedicalCertificateQuestionResponderInterface
import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificatequestion.v1.rivtabp20.SendMedicalCertificateQuestionResponderService
import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificatequestionresponder.v1.QuestionToFkType
import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificatequestionresponder.v1.SendMedicalCertificateQuestionResponseType
import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificatequestionresponder.v1.SendMedicalCertificateQuestionType
import se.inera.ifv.insuranceprocess.healthreporting.v2.EnhetType
import se.inera.ifv.insuranceprocess.healthreporting.v2.HosPersonalType
import se.inera.ifv.insuranceprocess.healthreporting.v2.PatientType
import se.inera.ifv.insuranceprocess.healthreporting.v2.VardgivareType

/**
 *
 * @author andreaskaltenbach
 */
class SkickaFraga extends WsClientFixture {

    private SendMedicalCertificateQuestionResponderService sendService = new SendMedicalCertificateQuestionResponderService();
    private SendMedicalCertificateQuestionResponderInterface sendResponder = sendService.sendMedicalCertificateQuestionResponderPort

    String vårdReferens
    String ämne
	String vårdAddress
    String fråga
    String frågeTidpunkt
	String signeringsTidpunkt
	String lakarutlatandeId
	String personnr
	String namn

    public SkickaFraga() {
		String certificateQuestionUrl = System.getProperty("fk.certificateQuestionUrl", "http://localhost:8080/inera-certificate/send-certificate-question-stub")
        Client client = ClientProxy.getClient(sendResponder)
        client.getRequestContext().put(Message.ENDPOINT_ADDRESS, certificateQuestionUrl)
    }

    public String resultat() {
        // read request template from file
        JAXBContext jaxbContext = JAXBContext.newInstance(SendMedicalCertificateQuestionType.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        QuestionToFkType question = unmarshaller.unmarshal(new StreamSource(new ClassPathResource("SendMedicalCertificateQuestion_template.xml").getInputStream()), RegisterMedicalCertificateType.class).getValue()
		question.setAmne(Amnetyp.fromValue(ämne))
        question.setVardReferensId(vårdReferens);
        question.setAvsantTidpunkt(LocalDateTime.now());
        question.getFraga().setMeddelandeText(frågeTidpunkt);
        question.getFraga().setSigneringsTidpunkt(LocalDateTime.parse(signeringsTidpunkt));

        question.setLakarutlatande(new LakarutlatandeEnkelType());
		question.getLakarutlatande().setLakarutlatandeId(lakarutlatandeId)
		question.getLakarutlatande().setSigneringsTidpunkt(LocalDateTime.parse(signeringsTidpunkt))
		question.getLakarutlatande().setPatient(new PatientType())
		question.getLakarutlatande().getPatient().setPersonId(new II())
		question.getLakarutlatande().getPatient().getPersonId().setExtension(personnr)
		question.getLakarutlatande().getPatient().getPersonId().setRoot("1.2.752.129.2.1.3.1")
		question.getLakarutlatande().getPatient().setFullstandigtNamn(namn)

        SendMedicalCertificateQuestionType parameters = new SendMedicalCertificateQuestionType();
        parameters.setQuestion(question);

        SendMedicalCertificateQuestionResponseType sendResponse = sendResponder.sendMedicalCertificateQuestion(logicalAddress, parameters);
        resultAsString(sendResponse)
    }
}
