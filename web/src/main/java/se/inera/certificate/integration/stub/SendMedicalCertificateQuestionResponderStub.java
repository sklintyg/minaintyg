package se.inera.certificate.integration.stub;

import static se.inera.certificate.integration.ResultOfCallUtil.failResult;

import java.io.StringWriter;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.cxf.annotations.SchemaValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.w3.wsaddressing10.AttributedURIType;

import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificatequestion.v1.rivtabp20.SendMedicalCertificateQuestionResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificatequestionresponder.v1.SendMedicalCertificateQuestionResponseType;
import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificatequestionresponder.v1.SendMedicalCertificateQuestionType;
import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificatequestionresponder.v1.ObjectFactory;

/**
 * @author par.wenaker
 */
@Transactional
@SchemaValidation
public class SendMedicalCertificateQuestionResponderStub implements SendMedicalCertificateQuestionResponderInterface {

    Logger logger = LoggerFactory.getLogger(SendMedicalCertificateQuestionResponderStub.class);

    @Override
    public SendMedicalCertificateQuestionResponseType sendMedicalCertificateQuestion(AttributedURIType logicalAddress, SendMedicalCertificateQuestionType request) {
        
        SendMedicalCertificateQuestionResponseType response = new SendMedicalCertificateQuestionResponseType();
        
        // unmarshal the certificate document
        String document;
        try {
            document = marshalCertificate(request);
            logger.info("STUB Received request");
        } catch (JAXBException e) {
            response.setResult(failResult("Unable to marshal certificate information"));
            return response;
        }
        return response;
    }
    
    private String marshalCertificate(SendMedicalCertificateQuestionType request) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(SendMedicalCertificateQuestionType.class);
        Marshaller marshaller = jaxbContext.createMarshaller();

        StringWriter stringWriter = new StringWriter();

        JAXBElement<SendMedicalCertificateQuestionType> jaxbElement = new ObjectFactory().createSendMedicalCertificateQuestion(request);

        marshaller.marshal(jaxbElement, stringWriter);

        return stringWriter.toString();
    }
    
}
