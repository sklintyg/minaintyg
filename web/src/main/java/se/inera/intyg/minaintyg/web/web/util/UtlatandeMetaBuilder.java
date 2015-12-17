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

package se.inera.intyg.minaintyg.web.web.util;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDateTime;

import se.inera.intyg.common.support.model.CertificateState;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.minaintyg.web.web.service.dto.UtlatandeMetaData;

/**
 * Builder object for creating immutable {@link UtlatandeMetaData} objects.
 */
public class UtlatandeMetaBuilder {

    private String id;

    private String type;

    private String issuerName;

    private String facilityName;

    private LocalDateTime signDate;

    private String available;

    private String additionalInfo;

    private List<Status> statuses = new ArrayList<>();

    public UtlatandeMetaData build() {
        return new UtlatandeMetaData(id, type, issuerName, facilityName, signDate, available, additionalInfo, statuses);
    }

    public UtlatandeMetaBuilder id(String id) {
        this.id = id;

        return this;
    }

    public UtlatandeMetaBuilder type(String type) {
        this.type = type;

        return this;
    }

    public UtlatandeMetaBuilder issuerName(String issuerName) {
        this.issuerName = issuerName;

        return this;
    }

    public UtlatandeMetaBuilder facilityName(String facilityName) {
        this.facilityName = facilityName;

        return this;
    }

    public UtlatandeMetaBuilder signDate(LocalDateTime signDate) {
        this.signDate = signDate;

        return this;
    }

    public UtlatandeMetaBuilder available(String available) {
        this.available = available;

        return this;
    }

    public UtlatandeMetaBuilder additionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;

        return this;
    }

    public UtlatandeMetaBuilder addStatus(CertificateState type, String target, LocalDateTime timestamp) {
        return addStatus(new Status(type, target, timestamp));
    }

    public UtlatandeMetaBuilder addStatus(Status status) {
        this.statuses.add(status);

        return this;
    }

    /**
     * Utility method for creating a {@link UtlatandeMetaBuilder} from a {@link Utlatande} object populated with.
     *
     * <ul>
     * <li>id
     * <li>type
     * <li>issuerName
     * <li>facilityName
     * <li>signDate
     * </ul>
     *
     * @param utlatande
     *            The utlatande to extract meta data from.
     *
     * @return A prepopulated builder.
     */
    public static UtlatandeMetaBuilder fromUtlatande(Utlatande utlatande) {
        UtlatandeMetaBuilder builder = new UtlatandeMetaBuilder();
        String id = utlatande.getId();
        builder.id(id)
                .type(utlatande.getTyp())
                .issuerName(utlatande.getGrundData().getSkapadAv().getFullstandigtNamn())
                .facilityName(utlatande.getGrundData().getSkapadAv().getVardenhet().getEnhetsnamn())
                .signDate(utlatande.getGrundData().getSigneringsdatum());

        return builder;
    }
}
