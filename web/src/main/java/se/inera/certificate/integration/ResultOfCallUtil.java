package se.inera.certificate.integration;

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
}
