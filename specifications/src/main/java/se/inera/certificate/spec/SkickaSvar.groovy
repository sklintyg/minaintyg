package se.inera.certificate.spec

import static se.inera.ifv.insuranceprocess.healthreporting.v2.ResultCodeEnum.OK

import javax.xml.bind.JAXBContext
import javax.xml.bind.Unmarshaller
import javax.xml.transform.stream.StreamSource

import org.apache.cxf.endpoint.Client
import org.apache.cxf.frontend.ClientProxy
import org.apache.cxf.message.Message
import org.joda.time.LocalDateTime
import org.springframework.core.io.ClassPathResource

import riv.insuranceprocess.healthreporting.medcertqa._1.Amnetyp
import se.inera.certificate.spec.util.WsClientFixture
import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificateanswer.v1.rivtabp20.SendMedicalCertificateAnswerResponderInterface
import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificateanswer.v1.rivtabp20.SendMedicalCertificateAnswerResponderService
import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificateanswerresponder.v1.AnswerToFkType
import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificateanswerresponder.v1.SendMedicalCertificateAnswerResponseType
import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificateanswerresponder.v1.SendMedicalCertificateAnswerType
import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificatequestionresponder.v1.SendMedicalCertificateQuestionType


class SkickaSvar extends WsClientFixture {

    private SendMedicalCertificateAnswerResponderService sendService = new SendMedicalCertificateAnswerResponderService();
    private SendMedicalCertificateAnswerResponderInterface sendResponder = sendService.sendMedicalCertificateAnswerResponderPort

    String vårdReferens
    String fkReferens
    String ämne
    String fråga
    String frågeTidpunkt
    String svar
    String svarsTidpunkt
	String signeringsTidpunkt
	String lakarutlatandeId
	String personnr
	String namn

    public SkickaSvar() {
		String certificateAnswerUrl = System.getProperty("fk.certificateAnswerUrl", "http://localhost:8080/inera-certificate/send-certificate-answer-stub")
        Client client = ClientProxy.getClient(sendResponder)
        client.getRequestContext().put(Message.ENDPOINT_ADDRESS, certificateAnswerUrl)
    }

    public String resultat() {
        // read request template from file
        JAXBContext jaxbContext = JAXBContext.newInstance(SendMedicalCertificateQuestionType.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        AnswerToFkType answer = unmarshaller.unmarshal(new StreamSource(new ClassPathResource("SendMedicalCertificateAnswer_template.xml").getInputStream()), AnswerToFkType.class).getValue()
        answer.setAvsantTidpunkt(LocalDateTime.now());
		if (ämne) answer.setAmne(Amnetyp.fromValue(ämne))
        if (vårdReferens) answer.setVardReferensId(vårdReferens);
        if (fkReferens) answer.setFkReferensId(fkReferens);
		if (fråga) answer.getFraga().setMeddelandeText(fråga);
		if (frågeTidpunkt) answer.getFraga().setSigneringsTidpunkt(LocalDateTime.parse(frågeTidpunkt));
		if (svar) answer.getSvar().setMeddelandeText(svar)
		if (svarsTidpunkt) answer.getSvar().setSigneringsTidpunkt(LocalDateTime.parse(svarsTidpunkt))
		if (lakarutlatandeId) answer.getLakarutlatande().setLakarutlatandeId(lakarutlatandeId)
		if (signeringsTidpunkt) answer.getLakarutlatande().setSigneringsTidpunkt(LocalDateTime.parse(signeringsTidpunkt))
		if (personnr) answer.getLakarutlatande().getPatient().getPersonId().setExtension(personnr)
		if (namn) answer.getLakarutlatande().getPatient().setFullstandigtNamn(namn)

		SendMedicalCertificateAnswerType parameters = new SendMedicalCertificateAnswerType();
		parameters.setAnswer(answer)

        SendMedicalCertificateAnswerResponseType sendResponse = sendResponder.sendMedicalCertificateAnswer(logicalAddress, parameters);
        resultAsString(sendResponse)
    }
}
