package se.inera.certificate.integration.stub;

import static se.inera.certificate.integration.ResultOfCallUtil.failResult;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

import org.apache.cxf.annotations.SchemaValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.w3.wsaddressing10.AttributedURIType;

import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificatequestion.v1.rivtabp20.SendMedicalCertificateQuestionResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificatequestionresponder.v1.ObjectFactory;
import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificatequestionresponder.v1.SendMedicalCertificateQuestionResponseType;
import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificatequestionresponder.v1.SendMedicalCertificateQuestionType;

/**
 * @author par.wenaker
 */
@Transactional
@SchemaValidation
public class SendMedicalCertificateQuestionResponderStub implements SendMedicalCertificateQuestionResponderInterface {

    private Logger logger = LoggerFactory.getLogger(SendMedicalCertificateQuestionResponderStub.class);

    private final JAXBContext jaxbContext;

    @Autowired
    private FkMedicalCertificatesStore fkMedicalCertificatesStore;

    public SendMedicalCertificateQuestionResponderStub() throws JAXBException {
        jaxbContext = JAXBContext.newInstance(SendMedicalCertificateQuestionType.class);
    }
    
    @Override
    public SendMedicalCertificateQuestionResponseType sendMedicalCertificateQuestion(AttributedURIType logicalAddress, SendMedicalCertificateQuestionType request) {
        
        SendMedicalCertificateQuestionResponseType response = new SendMedicalCertificateQuestionResponseType();
        
        try {
            String id = request.getQuestion().getLakarutlatande().getLakarutlatandeId();
            
            marshalCertificate(request);
            logger.info("STUB Received request");
            fkMedicalCertificatesStore.makulera(id);
        } catch (JAXBException e) {
            response.setResult(failResult("Unable to marshal certificate information"));
            return response;
        }
        return response;
    }
    
    private String marshalCertificate(SendMedicalCertificateQuestionType request) throws JAXBException {

        StringWriter stringWriter = new StringWriter();

        JAXBElement<SendMedicalCertificateQuestionType> jaxbElement = new ObjectFactory().createSendMedicalCertificateQuestion(request);

        jaxbContext.createMarshaller().marshal(jaxbElement, stringWriter);

        return stringWriter.toString();
    }
    
}
