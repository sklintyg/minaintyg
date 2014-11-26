package se.inera.certificate.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class CertificateStatus implements Serializable {

    private Boolean cancelled;
    private List<StatusMeta> statuses = new ArrayList<>();

    public Boolean getCancelled() {
        return cancelled;
    }

    public void setCancelled(Boolean cancelled) {
        this.cancelled = cancelled;
    }

    public List<StatusMeta> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<StatusMeta> statuses) {
        this.statuses = statuses;
    }
}
