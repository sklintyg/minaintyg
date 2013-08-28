package se.inera.certificate.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.Partial;

/**
 * Wrapper class that holds meta information about a certificate, such as the list of statuses
 * @author marced
 */
@SuppressWarnings("serial")
public class CertificateContentMeta implements Serializable {

    private String id;
    private String type;
    private String patientId;
    private Partial fromDate;
    private Partial tomDate;
    private List<StatusMeta> statuses = new ArrayList<>();

    public Partial getFromDate() {
        return fromDate;
    }

    public void setFromDate(Partial fromDate) {
        this.fromDate = fromDate;
    }

    public Partial getTomDate() {
        return tomDate;
    }

    public void setTomDate(Partial tomDate) {
        this.tomDate = tomDate;
    }

    public List<StatusMeta> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<StatusMeta> statuses) {
        this.statuses = statuses;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }
}
