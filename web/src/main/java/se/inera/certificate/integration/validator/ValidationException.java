package se.inera.certificate.integration.validator;

import com.google.common.base.Joiner;

import java.util.List;

/**
 * @author andreaskaltenbach
 */
public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(List<String> messages) {
        super(Joiner.on("\n").join(messages));
    }
}
