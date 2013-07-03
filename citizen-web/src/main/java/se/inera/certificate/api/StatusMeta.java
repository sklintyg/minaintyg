package se.inera.certificate.api;

import org.joda.time.LocalDateTime;

import java.io.Serializable;

public class StatusMeta implements Serializable {

    private String type;

    private String target;

    private LocalDateTime timestamp;

    public String getType() {
        return type;
    }

    public void setType(String type) {
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
