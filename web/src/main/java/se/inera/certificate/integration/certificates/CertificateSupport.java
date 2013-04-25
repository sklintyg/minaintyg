package se.inera.certificate.integration.certificates;

import javax.xml.bind.JAXBElement;
import java.util.List;

/**
 * Implements support for one particular certificate type.
 *
 * @author andreaskaltenbach
 */
public interface CertificateSupport {

    /**
     * Returns the supported certificate type (e.g. 'fk723').
     */
    String certificateType();

    /**
     * Returns a list of additional JAXB classes which are required to represent the supported certificate type.
     */
    List<Class<?>> additionalContextClasses();

    /**
     * Returns the serialized representation of the certificate
     * @param certificate the certificate as JAXB element
     */
    String serializeCertificate(JAXBElement certificate);

    /**
     * Deserializes the certificate JAXB element from the given certificate data.
     *
     * @param data serialized certificate information
     * @return the deserialized certificate
     */
    JAXBElement deserializeCertificate(String data);
}
