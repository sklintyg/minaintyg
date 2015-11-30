package se.inera.certificate.web.util;

import se.inera.intyg.common.support.model.CertificateState;
import se.inera.certificate.web.service.dto.UtlatandeMetaData;
import se.riv.clinicalprocess.healthcond.certificate.v1.CertificateMetaType;
import se.riv.clinicalprocess.healthcond.certificate.v1.StatusType;
import se.riv.clinicalprocess.healthcond.certificate.v1.UtlatandeStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * Converts meta data from {@link CertificateMetaType} to the internal {@link UtlatandeMetaData} type.
 */
public final class ClinicalProcessMetaConverter {

    private ClinicalProcessMetaConverter() {
    }

    private static final Comparator<? super CertificateMetaType> DESCENDING_DATE = new Comparator<CertificateMetaType>() {
        @Override
        public int compare(CertificateMetaType m1, CertificateMetaType m2) {
            return m2.getSignDate().compareTo(m1.getSignDate());
        }
    };

    public static UtlatandeMetaData toUtlatandeMetaData(CertificateMetaType meta) {
        UtlatandeMetaBuilder builder = new UtlatandeMetaBuilder();

        builder.id(meta.getCertificateId())
                .type(meta.getCertificateType())
                .issuerName(meta.getIssuerName())
                .facilityName(meta.getFacilityName())
                .signDate(meta.getSignDate())
                .available(meta.getAvailable())
                .additionalInfo(meta.getComplemantaryInfo());

        if (meta.getStatus() != null) {
            for (UtlatandeStatus statusType : meta.getStatus()) {
                if (statusType.getType() == StatusType.SENT || statusType.getType() == StatusType.CANCELLED) {
                    CertificateState internalStatusType = CertificateState.valueOf(statusType.getType().name());
                    builder.addStatus(internalStatusType, statusType.getTarget(), statusType.getTimestamp());
                }
            }
        }

        return builder.build();
    }

    public static List<UtlatandeMetaData> toUtlatandeMetaData(List<CertificateMetaType> metas) {
        List<UtlatandeMetaData> result = new ArrayList<>();

        // Copy and sort metadata
        List<CertificateMetaType> input = new ArrayList<>(metas);
        Collections.sort(input, DESCENDING_DATE);

        for (CertificateMetaType certificateMetaType : input) {
            result.add(toUtlatandeMetaData(certificateMetaType));
        }

        return result;
    }
}
