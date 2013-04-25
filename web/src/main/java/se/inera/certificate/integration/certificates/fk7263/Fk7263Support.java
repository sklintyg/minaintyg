package se.inera.certificate.integration.certificates.fk7263;

import org.springframework.stereotype.Component;
import se.inera.certificate.integration.certificates.CertificateSupport;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.ObjectFactory;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateType;

import javax.xml.bind.JAXBElement;
import java.util.ArrayList;
import java.util.List;

/**
 * CertificateSupport implementation for FK7263 certificates.
 *
 * @author andreaskaltenbach
 */
@Component
public class Fk7263Support implements CertificateSupport {

    @Override
    public String certificateType() {
        return "fk7263";
    }

    @Override
    public List<Class<?>> additionalContextClasses() {
        List<Class<?>> classes = new ArrayList<>();
        classes.add(RegisterMedicalCertificateType.class);
        return classes;
    }

    @Override
    public String serializeCertificate(JAXBElement certificate) {
        // TODO - serialize certificate to String
        return "";
    }

    @Override
    public JAXBElement deserializeCertificate(String data) {
        return new ObjectFactory().createRegisterMedicalCertificate(new RegisterMedicalCertificateType());
    }
}
