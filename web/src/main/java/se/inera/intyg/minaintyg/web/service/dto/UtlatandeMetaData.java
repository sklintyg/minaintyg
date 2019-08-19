/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.intyg.minaintyg.web.service.dto;

import static org.springframework.util.Assert.hasText;
import static org.springframework.util.Assert.notNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.modules.support.api.dto.CertificateRelation;

/**
 * Meta data describing a utlatande.
 */
public class UtlatandeMetaData {

    /**
     * The id of the utlatande.
     */
    private final String id;

    /**
     * The type of utlatande.
     */
    private final String type;

    /**
     * The type version of utlatande.
     */
    private final String typeVersion;

    /**
     * The name of the HoS-personal who signed the utlatande.
     */
    private final String issuerName;

    /**
     * The name of the vardenhet at which the utlatande was signed.
     */
    private final String facilityName;

    /**
     * The date and time at which the utlatande was signed.
     */
    private final LocalDateTime signDate;

    /**
     * Tells if the utlatande is available or not.
     */
    private String available;

    /**
     * A short text describing the content of the utlatande.
     */
    private final String complemantaryInfo;

    /**
     * A list of statuses of the utlatande.
     */
    private final List<Status> statuses;

    /**
     * A list of relations where this certificate is the parent.
     */
    private final List<CertificateRelation> relations;

    /**
     * Compare status newest first.
     */
    private static final Comparator<Status> STATUS_COMPARATOR = (o1, o2) -> o2.getTimestamp().compareTo(o1.getTimestamp());

    // CHECKSTYLE:OFF ParameterNumber
    public UtlatandeMetaData(String id, String type, String typeVersion, String issuerName, String facilityName, LocalDateTime signDate,
        String available,
        String complemantaryInfo, List<Status> statuses, List<CertificateRelation> relations) {
        hasText(id, "'id' must not be empty");
        hasText(type, "'type' must not be empty");
        hasText(typeVersion, "'typeVersion' must not be empty");
        hasText(issuerName, "'issuerName' must not be empty");
        hasText(facilityName, "'facilityName' must not be empty");
        notNull(signDate, "'signDate' must not be null");
        this.id = id;
        this.type = type;
        this.typeVersion = typeVersion;
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

        if (relations != null) {
            this.relations = new ArrayList<>(relations);
        } else {
            this.relations = Collections.emptyList();
        }
    }
    // CHECKSTYLE:ON ParameterNumber

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getTypeVersion() {
        return typeVersion;
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

    public List<CertificateRelation> getRelations() {
        return relations;
    }

}
