package se.inera.certificate.integration.certificates;

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
}
