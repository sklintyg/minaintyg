package se.inera.certificate.model.dao;

import com.google.common.collect.Ordering;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDateTime;
import se.inera.certificate.model.CertificateState;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * @author andreaskaltenbach
 */
@Embeddable
public class CertificateStateHistoryEntry {

    @Column(name = "TARGET", nullable = false)
    private String target;

    @Column(name = "STATE")
    @Enumerated(EnumType.STRING)
    private CertificateState state;

    @Column(name = "TIMESTAMP")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    private LocalDateTime timestamp;

    private final static Ordering<LocalDateTime> ORDERING_DESC_TIME_NULL_LAST = Ordering.<LocalDateTime>natural().reverse().nullsFirst();

    final static Ordering<CertificateStateHistoryEntry> byTimestampDesc = new Ordering<CertificateStateHistoryEntry>() {
        @Override
        public int compare(CertificateStateHistoryEntry left, CertificateStateHistoryEntry right) {
            return ORDERING_DESC_TIME_NULL_LAST.compare(left.timestamp, right.timestamp);
        }
    };

    public CertificateStateHistoryEntry() {
    }

    public CertificateStateHistoryEntry(String target, CertificateState state, LocalDateTime timestamp) {
        this.target = target;
        this.state = state;
        if (timestamp != null) {
            this.timestamp = timestamp;
        } else {
            this.timestamp = new LocalDateTime();
        }
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public CertificateState getState() {
        return state;
    }

    public void setState(CertificateState state) {
        this.state = state;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "CertificateStateHistoryEntry{" +
                "target='" + target + '\'' +
                ", state=" + state +
                ", timestamp=" + timestamp +
                '}';
    }
}
