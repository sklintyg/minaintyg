package se.inera.intyg.minaintyg.web.exception;

/**
 * Created by eriklupander on 2017-04-25.
 */
public class PersonNotFoundException extends RuntimeException {

    public PersonNotFoundException(String message) {
        super(message);
    }
}
