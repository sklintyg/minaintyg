package se.inera.certificate.api;

import java.io.Serializable;

@SuppressWarnings("serial")
public class CertificateMeta implements Serializable {
    
    private String id;
    private Boolean selected;
    private String type;
    private String caregiverName;
    private String careunitName;
    private String sentDate;
    private String status;
    private Boolean deleted;
    private String fromDate;
    private String tomDate;
    
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
    
    public String getStatus() {
            return status;
    }
    
    public void setStatus(String status) {
            this.status = status;
    }
    
    public Boolean getDeleted() {
            return deleted;
    }
    
    public void setDeleted(Boolean deleted) {
            this.deleted = deleted;
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

}