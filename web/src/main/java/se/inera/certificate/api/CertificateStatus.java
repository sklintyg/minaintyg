package se.inera.certificate.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import se.inera.intyg.common.support.model.Status;

@SuppressWarnings("serial")
public class CertificateStatus implements Serializable {

    private Boolean cancelled;
    private List<Status> statuses = new ArrayList<>();

    public Boolean getCancelled() {
        return cancelled;
    }

    public void setCancelled(Boolean cancelled) {
        this.cancelled = cancelled;
    }

    public List<Status> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<Status> statuses) {
        this.statuses = statuses;
    }
}
