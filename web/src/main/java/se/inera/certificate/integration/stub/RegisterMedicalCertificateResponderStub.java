package se.inera.certificate.integration.stub;

import static se.inera.certificate.integration.ResultOfCallUtil.failResult;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.cxf.annotations.SchemaValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.w3.wsaddressing10.AttributedURIType;

import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificate.v3.rivtabp20.RegisterMedicalCertificateResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.ObjectFactory;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateResponseType;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateType;

/**
 * @author par.wenaker
 */
@Transactional
@SchemaValidation
public class RegisterMedicalCertificateResponderStub implements RegisterMedicalCertificateResponderInterface {

    private Logger logger = LoggerFactory.getLogger(RegisterMedicalCertificateResponderStub.class);

    @Override
    public RegisterMedicalCertificateResponseType registerMedicalCertificate(AttributedURIType logicalAddress, RegisterMedicalCertificateType request) {

        RegisterMedicalCertificateResponseType response = new RegisterMedicalCertificateResponseType();

        try {
            marshalCertificate(request);
            logger.info("STUB Received request");
        } catch (JAXBException e) {
            response.setResult(failResult("Unable to marshal certificate information"));
            return response;
        }
        return response;
    }

    private String marshalCertificate(RegisterMedicalCertificateType request) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(RegisterMedicalCertificateType.class);
        Marshaller marshaller = jaxbContext.createMarshaller();

        StringWriter stringWriter = new StringWriter();

        JAXBElement<RegisterMedicalCertificateType> jaxbElement = new ObjectFactory().createRegisterMedicalCertificate(request);

        marshaller.marshal(jaxbElement, stringWriter);

        return stringWriter.toString();
    }
}
