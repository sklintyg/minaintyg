package se.inera.certificate.exception;

import se.inera.certificate.model.CertificateMetaData;

/**
 * @author andreaskaltenbach
 */
public class InvalidCertificateIdentifierException extends RuntimeException {

    public InvalidCertificateIdentifierException(String certificateId, String civicRegistrationNumber) {
        super(String.format("No certificate with ID %s available for patient %s", certificateId, civicRegistrationNumber));
    }

    public InvalidCertificateIdentifierException(CertificateMetaData metaData) {
        this(metaData.getId(), metaData.getCivicRegistrationNumber());
    }
}
