package se.inera.certificate.integration.stub;

import static se.inera.certificate.integration.util.ResultOfCallUtil.failResult;
import static se.inera.certificate.integration.util.ResultOfCallUtil.okResult;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

import org.apache.cxf.annotations.SchemaValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.w3.wsaddressing10.AttributedURIType;

import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificate.v3.rivtabp20.RegisterMedicalCertificateResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.ObjectFactory;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateResponseType;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateType;

import com.google.common.base.Throwables;

/**
 * @author par.wenaker
 */
@Transactional
@SchemaValidation
public class RegisterMedicalCertificateResponderStub implements RegisterMedicalCertificateResponderInterface {

    private Logger logger = LoggerFactory.getLogger(RegisterMedicalCertificateResponderStub.class);

    private JAXBContext jaxbContext;

    @Autowired
    private FkMedicalCertificatesStore fkMedicalCertificatesStore;

    public RegisterMedicalCertificateResponderStub() {
        try {
            jaxbContext = JAXBContext.newInstance(RegisterMedicalCertificateType.class);
        } catch (JAXBException e) {
            Throwables.propagate(e);
        }
    }

    @Override
    public RegisterMedicalCertificateResponseType registerMedicalCertificate(AttributedURIType logicalAddress, RegisterMedicalCertificateType request) {

        RegisterMedicalCertificateResponseType response = new RegisterMedicalCertificateResponseType();

        try {
            String id = request.getLakarutlatande().getLakarutlatandeId();

            Map<String, String> props = new HashMap<>();
            props.put("Personnummer", request.getLakarutlatande().getPatient().getPersonId().getExtension());
            props.put("Makulerad", "NEJ");

            marshalCertificate(request);
            logger.info("STUB Received request");
            fkMedicalCertificatesStore.addCertificate(id, props);
        } catch (JAXBException e) {
            response.setResult(failResult("Unable to marshal certificate information"));
            return response;
        }
        response.setResult(okResult());
        return response;
    }

    private String marshalCertificate(RegisterMedicalCertificateType request) throws JAXBException {

        StringWriter stringWriter = new StringWriter();

        JAXBElement<RegisterMedicalCertificateType> jaxbElement = new ObjectFactory().createRegisterMedicalCertificate(request);

        jaxbContext.createMarshaller().marshal(jaxbElement, stringWriter);

        return stringWriter.toString();
    }

}
