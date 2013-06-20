package se.inera.certificate.exception;

/**
 * Exception thrown whenever a certificate with unknown certificate ID is tried to access.
 *
 * @author andreaskaltenbach
 */
public class InvalidCertificateException extends RuntimeException {

    private String certificateId;
    private String civicRegistrationNumber;

    public InvalidCertificateException(String certificateId, String civicRegistrationNumber) {
        super();
        this.certificateId = certificateId;
        this.civicRegistrationNumber = civicRegistrationNumber;
    }

    @Override
    public String getMessage() {
        return "Certificate '" + certificateId + "' does not exist for user + '" + civicRegistrationNumber + "'.";
    }
}
