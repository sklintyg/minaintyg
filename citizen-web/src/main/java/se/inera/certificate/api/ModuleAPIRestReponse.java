package se.inera.certificate.api;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ModuleAPIRestReponse implements Serializable {
    private String result;
    private String errorCode;

    public ModuleAPIRestReponse(String result, String errorCode) {
        super();
        this.result = result;
        this.errorCode = errorCode;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
