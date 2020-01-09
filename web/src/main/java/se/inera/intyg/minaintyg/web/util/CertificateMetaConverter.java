/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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
package se.inera.intyg.minaintyg.web.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import se.inera.intyg.clinicalprocess.healthcond.certificate.listrelationsforcertificate.v1.IntygRelations;
import se.inera.intyg.common.support.common.enumerations.RelationKod;
import se.inera.intyg.common.support.model.CertificateState;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.support.modules.support.api.dto.CertificateMetaData;
import se.inera.intyg.common.support.modules.support.api.dto.CertificateRelation;
import se.inera.intyg.minaintyg.web.api.CertificateMeta;
import se.inera.intyg.minaintyg.web.service.dto.UtlatandeMetaData;

/**
 * Converts meta data from the internal {@link UtlatandeMetaData} and {@link CertificateMetaData} to the REST service
 * model {@link CertificateMeta}.
 */
public final class CertificateMetaConverter {

    private CertificateMetaConverter() {
    }

    public static CertificateMeta toCertificateMetaFromUtlatandeMeta(UtlatandeMetaData meta) {
        CertificateMeta result = new CertificateMeta();

        result.setId(meta.getId());
        result.setSelected(false);
        result.setType(meta.getType());
        result.setTypeVersion(meta.getTypeVersion());
        result.setCaregiverName(meta.getIssuerName());
        result.setCareunitName(meta.getFacilityName());
        result.setSentDate(meta.getSignDate());
        result.setArchived(!Boolean.parseBoolean(meta.getAvailable()));
        result.setComplementaryInfo(meta.getComplemantaryInfo());
        result.getStatuses().addAll(meta.getStatuses());
        result.setRelations(meta.getRelations());
        return result;
    }

    public static List<CertificateMeta> toCertificateMetaFromUtlatandeMetaList(List<UtlatandeMetaData> metas) {
        List<CertificateMeta> result = new ArrayList<>();

        for (UtlatandeMetaData certificateMetaType : metas) {
            result.add(toCertificateMetaFromUtlatandeMeta(certificateMetaType));
        }

        return result;
    }

    /**
     * Converts CertificateMetaData types to Rest CertificateMeta, optionally filtering statuses to include only
     * selected statuses.
     *
     * @param statusFilter - List of CertificateState types to keep
     */
    public static CertificateMeta toCertificateMetaFromCertMetaData(Utlatande utlatande, CertificateMetaData metaData,
        List<IntygRelations> relations, List<CertificateState> statusFilter) {
        CertificateMeta result = new CertificateMeta();

        result.setId(metaData.getCertificateId());
        result.setSelected(false);
        result.setType(utlatande.getTyp());
        result.setTypeVersion(utlatande.getTextVersion());
        result.setCaregiverName(metaData.getIssuerName());
        result.setCareunitName(metaData.getFacilityName());
        result.setSentDate(metaData.getSignDate());
        result.setArchived(!metaData.isAvailable());
        result.setComplementaryInfo(metaData.getAdditionalInfo());

        for (Status status : metaData.getStatus()) {
            // Obey any status filter restrictions
            if (statusFilter == null || statusFilter.contains(status.getType())) {
                result.getStatuses().add(status);
            }
        }

        result.setRelations(relations.stream()
            .filter(ir -> ir.getIntygsId().getExtension().equals(result.getId()))
            .flatMap(ir -> ir.getRelation().stream())
            .filter(relation -> !relation.isFranIntygMakulerat())
            .map(r -> new CertificateRelation(r.getFranIntygsId().getExtension(), r.getTillIntygsId().getExtension(),
                RelationKod.fromValue(r.getTyp().getCode()), r.getSkapad()))
            .collect(Collectors.toList()));

        return result;
    }

}
