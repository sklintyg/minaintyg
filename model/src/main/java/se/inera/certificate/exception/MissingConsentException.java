package se.inera.certificate.exception;

/**
 * @author andreaskaltenbach
 */
public class MissingConsentException extends RuntimeException {

    public MissingConsentException(String civicRegistrationNumber) {
        super(String.format("Consent required from user %s.", civicRegistrationNumber));
    }
}
