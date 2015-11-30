package se.inera.certificate.web.service.dto;

import static org.springframework.util.Assert.hasText;
import static org.springframework.util.Assert.notNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.joda.time.LocalDateTime;
import se.inera.intyg.common.support.model.Status;

/**
 * Meta data describing a utlatande.
 */
public class UtlatandeMetaData {

    /** The id of the utlatande. */
    private final String id;

    /** The type of utlatande. */
    private final String type;

    /** The name of the HoS-personal who signed the utlatande. */
    private final String issuerName;

    /** The name of the vardenhet at which the utlatande was signed. */
    private final String facilityName;

    /** The date and time at which the utlatande was signed. */
    private final LocalDateTime signDate;

    /** Tells if the utlatande is available or not. */
    private String available;

    /** A short text describing the content of the utlatande. */
    private final String complemantaryInfo;

    /** A list of statuses of the utlatande. */
    private final List<Status> statuses;

    // CHECKSTYLE:OFF ParameterNumber
    public UtlatandeMetaData(String id, String type, String issuerName, String facilityName, LocalDateTime signDate, String available,
            String complemantaryInfo, List<Status> statuses) {
        hasText(id, "'id' must not be empty");
        hasText(type, "'type' must not be empty");
        hasText(issuerName, "'issuerName' must not be empty");
        hasText(facilityName, "'facilityName' must not be empty");
        notNull(signDate, "'signDate' must not be null");
        // Should these be mandatory?
        // hasText(available, "'available' must not be empty");
        // hasText(complemantaryInfo, "'complemantaryInfo' must not be empty");
        this.id = id;
        this.type = type;
        this.issuerName = issuerName;
        this.facilityName = facilityName;
        this.signDate = signDate;
        this.available = available;
        this.complemantaryInfo = complemantaryInfo;
        if (statuses != null) {
            this.statuses = new ArrayList<>(statuses);
            Collections.sort(this.statuses, STATUS_COMPARATOR);
        } else {
            this.statuses = Collections.emptyList();
        }
    }
    // CHECKSTYLE:ON ParameterNumber

    /**
     * Compare status newest first.
     */
    private static final Comparator<Status> STATUS_COMPARATOR = new Comparator<Status>() {
        @Override
        public int compare(Status o1, Status o2) {
            return o2.getTimestamp().compareTo(o1.getTimestamp());
        }
    };

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getIssuerName() {
        return issuerName;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public LocalDateTime getSignDate() {
        return signDate;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public String getComplemantaryInfo() {
        return complemantaryInfo;
    }

    public List<Status> getStatuses() {
        return Collections.unmodifiableList(statuses);
    }
}
