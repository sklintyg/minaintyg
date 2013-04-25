package se.inera.certificate.integration.certificates.config;

import org.springframework.beans.factory.annotation.Autowired;
import se.inera.certificate.integration.certificates.CertificateSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Collects all additional JAXB types which have to be registered for the CXF endpoints.
 *
 * Visits all CertificateSupport implementations and collects all their additional context classes.
 *
 * @see se.inera.certificate.integration.certificates.CertificateSupport#additionalContextClasses()
 *
 * @author andreaskaltenbach
 */
public class AdditionalContextClassCollector {

    @Autowired
    private List<CertificateSupport> supportedCertificates;

    public Class<?>[] getCertificateContextClasses() {
        List<Class<?>> additionalCertificateClasses = new ArrayList<>();
        for (CertificateSupport certificateSupport : supportedCertificates) {
            additionalCertificateClasses.addAll(certificateSupport.additionalContextClasses());
        }
        return additionalCertificateClasses.toArray(new Class<?>[additionalCertificateClasses.size()]);
    }
}
