package se.inera.certificate.model;

import org.joda.time.LocalDateTime;

/**
 * @author andreaskaltenbach
 */
public class Status {

    private CertificateState type;

    private String target;

    private LocalDateTime timestamp;

    public CertificateState getType() {
        return type;
    }

    public void setType(CertificateState type) {
        this.type = type;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
