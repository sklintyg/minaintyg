package se.inera.certificate.integration.exception;

import se.inera.ifv.insuranceprocess.healthreporting.v2.ResultOfCall;

/**
 * Exception throws if a call to another RIV-TA web service does return with a result code
 * INFO or ERROR.
 * The returned {@link ResultOfCall} is available within the exception to allow proper exception handling
 * for the executing client.
 *
 * @author andreaskaltenbach
 */
public class ExternalWebServiceCallFailedException extends RuntimeException {

    private final ResultOfCall resultOfCall;

    public ExternalWebServiceCallFailedException(ResultOfCall resultOfCall) {
        this.resultOfCall = resultOfCall;
    }

    @Override
    public String getMessage() {
        return "Failed to invoke internal web service. Result of call is " + resultOfCall;
    }

    public ResultOfCall getResultOfCall() {
        return resultOfCall;
    }
}
