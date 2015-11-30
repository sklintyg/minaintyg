package se.inera.intyg.minaintyg.web.api;

import java.io.Serializable;


@SuppressWarnings("serial")
public class ConsentResponse implements Serializable {
    private Boolean result;

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public ConsentResponse(Boolean result) {
        super();
        this.result = result;
    }
}
