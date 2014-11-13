package se.inera.certificate.exception;

import se.inera.certificate.clinicalprocess.healthcond.certificate.v1.ResultType;

public class ResultTypeErrorException extends RuntimeException {

    private static final long serialVersionUID = -412823459027541760L;

    private final ResultType resultType;

    public ResultTypeErrorException(ResultType resultType) {
        this.resultType = resultType;
    }

    @Override
    public String getMessage() {
        return "Failed to invoke internal web service. Result type is " + resultType;
    }

    public ResultType getResultType() {
        return resultType;
    }
}
