package se.inera.certificate.exception;

/**
 * Exception thrown when performing an operation on a revoked certificate.
 *
 * @author andreaskaltenbach
 */
public class CertificateRevokedException extends RuntimeException {

    private String certificateId;

    public CertificateRevokedException(String certificateId) {
        super();
        this.certificateId = certificateId;
    }

    @Override
    public String getMessage() {
        return "Certificate '" + certificateId + "' has been revoked.";
    }
}
