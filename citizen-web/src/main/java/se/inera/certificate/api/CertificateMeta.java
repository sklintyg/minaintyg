package se.inera.certificate.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class CertificateMeta implements Serializable {

    private String id;
    private Boolean selected;
    private String type;
    private String caregiverName;
    private String careunitName;
    private String sentDate;
    private Boolean archived;
    private Boolean cancelled;
    private String fromDate;
    private String tomDate;
    private List<StatusMeta> statuses = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCaregiverName() {
        return caregiverName;
    }

    public void setCaregiverName(String caregiverName) {
        this.caregiverName = caregiverName;
    }

    public String getCareunitName() {
        return careunitName;
    }

    public void setCareunitName(String careunitName) {
        this.careunitName = careunitName;
    }

    public String getSentDate() {
        return sentDate;
    }

    public void setSentDate(String sentDate) {
        this.sentDate = sentDate;
    }

    public Boolean getArchived() {
        return archived;
    }

    public void setArchived(Boolean archived) {
        this.archived = archived;
    }

    public Boolean getCancelled() {
        return cancelled;
    }

    public void setCancelled(Boolean cancelled) {
        this.cancelled = cancelled;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setTomDate(String tomDate) {
        this.tomDate = tomDate;
    }

    public String getTomDate() {
        return tomDate;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public List<StatusMeta> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<StatusMeta> statuses) {
        this.statuses = statuses;
    }
}
