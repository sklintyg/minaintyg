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
package se.inera.intyg.minaintyg.web.api;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import se.inera.intyg.common.support.common.enumerations.RelationKod;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.modules.support.api.dto.CertificateRelation;
import se.inera.intyg.minaintyg.web.service.dto.CertificateEvent;
import se.inera.intyg.minaintyg.web.service.dto.CertificateEventType;

/**
 * DTO representing metadata of a signed certificate in the MI rest API.
 */
public class CertificateMeta {

    private static final String EVENT_TYPE_ERSATT = "ERSATT";
    private static final String EVENT_TYPE_ERSATTER = "ERSATTER";

    private String id;
    private Boolean selected;
    private String type;
    private String typeVersion;
    private String caregiverName;
    private String careunitName;
    private LocalDateTime sentDate;
    private Boolean archived;
    private String complementaryInfo;
    private List<Status> statuses = new ArrayList<>();
    private List<CertificateRelation> relations = new ArrayList<>();

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

    public String getTypeVersion() {
        return typeVersion;
    }

    public void setTypeVersion(String typeVersion) {
        this.typeVersion = typeVersion;
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

    public LocalDateTime getSentDate() {
        return sentDate;
    }

    public void setSentDate(LocalDateTime sentDate) {
        this.sentDate = sentDate;
    }

    public Boolean getArchived() {
        return archived;
    }

    public void setArchived(Boolean archived) {
        this.archived = archived;
    }

    public String getComplementaryInfo() {
        return complementaryInfo;
    }

    public void setComplementaryInfo(String complementaryInfo) {
        this.complementaryInfo = complementaryInfo;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public List<Status> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<Status> statuses) {
        this.statuses = statuses;
    }

    public void setRelations(List<CertificateRelation> relations) {
        this.relations = relations;
    }

    /**
     * Builds a list of {@link CertificateEvent} based on the statuses and relations of this certificate.
     */
    public List<CertificateEvent> getEvents() {
        Stream<CertificateEvent> s1 = Stream.of(getReplacedBy()).filter(Objects::nonNull).map(
            r -> new CertificateEvent(
                CertificateEventType.RELATION, EVENT_TYPE_ERSATT, r.getFromIntygsId(), r.getSkapad(), getType(), getTypeVersion()
            ));
        Stream<CertificateEvent> s2 = Stream.of(getReplaces()).filter(Objects::nonNull).map(
            r -> new CertificateEvent(
                CertificateEventType.RELATION, EVENT_TYPE_ERSATTER, r.getToIntygsId(), r.getSkapad(), getType(), getTypeVersion()
            ));
        Stream<CertificateEvent> relationStream = Stream.concat(s1, s2);
        Stream<CertificateEvent> statusStream = statuses.stream()
            .map(s -> new CertificateEvent(CertificateEventType.STATUS, s.getType().name(), s.getTarget(), s.getTimestamp()));

        return Stream.concat(relationStream, statusStream)
            .sorted((e1, e2) -> e2.getTimestamp().compareTo(e1.getTimestamp()))
            .collect(Collectors.toList());
    }

    /**
     * Finds a ERSATT or KOMPLT relation where this certificate is the to-relation. If not found, null is returned.
     */
    public CertificateRelation getReplacedBy() {
        return relations.stream()
            .filter(cr -> cr.getToIntygsId().equals(id))
            .filter(cr -> cr.getRelationKod() == RelationKod.ERSATT || cr.getRelationKod() == RelationKod.KOMPLT)
            .findFirst()
            .orElse(null);
    }

    /**
     * Returns true if there exists a replacing relation on this certmeta.
     *
     * @return true or false.
     */
    public boolean getIsReplaced() {
        return getReplacedBy() != null;
    }

    /**
     * Finds a ERSATT or KOMPLT relation where this certificate is the from-relation. If not found, null is returned.
     */
    public CertificateRelation getReplaces() {
        return relations.stream()
            .filter(cr -> cr.getFromIntygsId().equals(id))
            .filter(cr -> cr.getRelationKod() == RelationKod.ERSATT || cr.getRelationKod() == RelationKod.KOMPLT)
            .findFirst()
            .orElse(null);
    }
}
