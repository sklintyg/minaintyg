package se.inera.certificate.model;

import org.joda.time.LocalDateTime;

/**
 * @author andreaskaltenbach
 */
public class LocalDateTimeInterval {

    private LocalDateTime start;
    private LocalDateTime end;

    public LocalDateTimeInterval() {}

    public LocalDateTimeInterval(LocalDateTime start, LocalDateTime end) {
        this.start = start;
        this.end = end;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }
}
