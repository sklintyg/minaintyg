package se.inera.certificate.exception;

import se.inera.certificate.model.Certificate;

/**
 * @author andreaskaltenbach
 */
public class InvalidCertificateIdentifierException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidCertificateIdentifierException(String certificateId, String civicRegistrationNumber) {
        super(String.format("No certificate with ID %s available for patient %s", certificateId, civicRegistrationNumber));
    }

    public InvalidCertificateIdentifierException(Certificate certificate) {
        this(certificate.getId(), certificate.getCivicRegistrationNumber());
    }
}
