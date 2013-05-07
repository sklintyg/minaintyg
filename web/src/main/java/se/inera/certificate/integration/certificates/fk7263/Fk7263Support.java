package se.inera.certificate.integration.certificates.fk7263;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.springframework.stereotype.Component;

import se.inera.certificate.integration.certificates.CertificateSupport;
import se.inera.certificate.integration.certificates.CertificateSupportException;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateType;

/**
 * CertificateSupport implementation for FK7263 certificates.
 *
 * @author andreaskaltenbach
 */
@Component
public class Fk7263Support implements CertificateSupport {

    private final JAXBContext jaxbContext;

    public Fk7263Support() throws CertificateSupportException {
        try {
            jaxbContext = JAXBContext.newInstance(RegisterMedicalCertificateType.class);
        } catch (JAXBException e) {
            throw new CertificateSupportException("Failed to create JAXBContext!", e);
        }
    }

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
    public JAXBContext getJaxbContext() {
        return jaxbContext;
    }

}
