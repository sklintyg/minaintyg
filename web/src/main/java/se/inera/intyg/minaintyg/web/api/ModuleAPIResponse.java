package se.inera.intyg.minaintyg.web.api;

import java.io.Serializable;

/**
 * Generic Response Result holder for module API requests.
 * Subclasses can extend with custom properties as needed.
 * @author marced
 *
 */
@SuppressWarnings("serial")
public class ModuleAPIResponse implements Serializable {
    private String resultCode;
    private String errorCode;

    public ModuleAPIResponse(String result, String errorCode) {
        super();
        this.resultCode = result;
        this.errorCode = errorCode;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String result) {
        this.resultCode = result;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
