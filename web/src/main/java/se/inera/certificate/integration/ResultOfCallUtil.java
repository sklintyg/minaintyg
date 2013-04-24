package se.inera.certificate.integration;

import se.inera.ifv.insuranceprocess.healthreporting.v2.ErrorIdEnum;
import se.inera.ifv.insuranceprocess.healthreporting.v2.ResultCodeEnum;
import se.inera.ifv.insuranceprocess.healthreporting.v2.ResultOfCall;

/**
 * @author andreaskaltenbach
 */
public final class ResultOfCallUtil {

    private ResultOfCallUtil() { }

    public static ResultOfCall okResult() {
        ResultOfCall result = new ResultOfCall();
        result.setResultCode(ResultCodeEnum.OK);
        return result;
    }

    public static ResultOfCall failResult(String errorText) {
        return failResult(ErrorIdEnum.VALIDATION_ERROR, errorText);
    }

    public static ResultOfCall applicationErrorResult(String errorText) {
        return failResult(ErrorIdEnum.APPLICATION_ERROR, errorText);
    }

    private static ResultOfCall failResult(ErrorIdEnum errorType, String errorText) {
        ResultOfCall result = new ResultOfCall();
        result.setResultCode(ResultCodeEnum.ERROR);
        result.setErrorId(errorType);
        result.setErrorText(errorText);
        return result;
    }
}
