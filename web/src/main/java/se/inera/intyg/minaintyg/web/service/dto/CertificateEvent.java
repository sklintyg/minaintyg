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

import java.time.LocalDateTime;

/**
 * Created by eriklupander on 2017-10-13.
 */
public class CertificateEvent {

    private CertificateEventType eventType;
    private String type;
    private String target;
    private LocalDateTime timestamp;
    private String intygsTyp;

    public CertificateEvent(CertificateEventType eventType, String type, String target, LocalDateTime timestamp) {
        this.eventType = eventType;
        this.type = type;
        this.target = target;
        this.timestamp = timestamp;
    }

    public CertificateEvent(CertificateEventType eventType, String type, String target, LocalDateTime timestamp, String intygsTyp) {
        this.eventType = eventType;
        this.type = type;
        this.target = target;
        this.timestamp = timestamp;
        this.intygsTyp = intygsTyp;
    }

    public CertificateEventType getEventType() {
        return eventType;
    }

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

    public String getIntygsTyp() {
        return intygsTyp;
    }

    public void setIntygsTyp(String intygsTyp) {
        this.intygsTyp = intygsTyp;
    }
}
