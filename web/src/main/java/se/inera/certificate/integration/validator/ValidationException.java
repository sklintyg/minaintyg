package se.inera.certificate.integration.validator;

import static se.inera.certificate.model.util.Strings.join;

import java.util.List;

/**
 * @author andreaskaltenbach
 */
public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(List<String> messages) {
        super(join("\n", messages));
    }
}
