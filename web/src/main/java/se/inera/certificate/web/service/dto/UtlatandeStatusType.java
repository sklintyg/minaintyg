package se.inera.certificate.web.service.dto;

import static org.springframework.util.Assert.hasText;
import static org.springframework.util.Assert.notNull;

import org.joda.time.LocalDateTime;

/**
 * A status object describing one status of a utlatande.
 */
public class UtlatandeStatusType {

    private final StatusType type;

    private final String target;

    private final LocalDateTime timestamp;

    public UtlatandeStatusType(StatusType type, String target, LocalDateTime timestamp) {
        notNull(type, "'type' must not be null");
        hasText(target, "'target' must not be empty");
        notNull(timestamp, "'timestamp' must not be null");
        this.type = type;
        this.target = target;
        this.timestamp = timestamp;
    }

    public StatusType getType() {
        return type;
    }

    public String getTarget() {
        return target;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public enum StatusType {
        CANCELLED,
        SENT;
    }
}
