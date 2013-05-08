package se.inera.certificate.integration.certificates;

public class CertificateSupportException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CertificateSupportException(String message, Throwable cause) {
        super(message, cause);
    }

    public CertificateSupportException(String message) {
        super(message);
    }

    public CertificateSupportException(Throwable cause) {
        super(cause);
    }

}
