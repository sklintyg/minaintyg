package se.inera.certificate.model;

public class ModelException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ModelException(String message, Throwable cause) {
        super(message, cause);
    }

    public ModelException(String message) {
        super(message);
    }
}
