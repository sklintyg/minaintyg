/*
 * Copyright (C) 2015 Inera AB (http://www.inera.se)
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

package se.inera.intyg.minaintyg.web.web.service.dto;

import static org.springframework.util.Assert.hasText;
import static org.springframework.util.Assert.notNull;

import java.util.List;

import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.model.common.internal.Utlatande;


/**
 * Object returned by services containing a utlatande and meta data describing the utlatande.
 */
public class UtlatandeWithMeta {

    private final Utlatande utlatande;
    private final String document;

    private final List<Status> statuses;

    public UtlatandeWithMeta(Utlatande utlatande, String document, List<Status> statuses) {
        notNull(utlatande, "'utlatande' must not be null");
        hasText(document, "'document' must not be empty");
        notNull(statuses, "'statuses' must not be null");
        this.utlatande = utlatande;
        this.document = document;
        this.statuses = statuses;
    }

    public Utlatande getUtlatande() {
        return utlatande;
    }

    public String getDocument() {
        return document;
    }

    public List<Status> getStatuses() {
        return statuses;
    }

}
