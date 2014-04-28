package se.inera.certificate.web.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import se.inera.certificate.api.CertificateMeta;
import se.inera.certificate.clinicalprocess.healthcond.certificate.v1.CertificateMetaType;
import se.inera.certificate.clinicalprocess.healthcond.certificate.v1.CertificateStatusType;
import se.inera.certificate.clinicalprocess.healthcond.certificate.v1.StatusType;

public final class ClinicalProcessMetaConverter {

    private ClinicalProcessMetaConverter() {

    }

    private static final Comparator<? super CertificateMetaType> DESCENDING_DATE = new Comparator<CertificateMetaType>() {
        @Override
        public int compare(CertificateMetaType m1, CertificateMetaType m2) {
            return m2.getSignDate().compareTo(m1.getSignDate());
        }
    };

    public static CertificateMeta toCertificateMeta(CertificateMetaType meta) {
        CertificateMetaBuilder builder = new CertificateMetaBuilder();

        builder.id(meta.getCertificateId())
                .type(meta.getCertificateType())
                .caregiverName(meta.getIssuerName())
                .careunitName(meta.getFacilityName())
                .sentDate(meta.getSignDate().toString())
                .archived(!Boolean.parseBoolean(meta.getAvailable()))
                .complemenaryInfo(meta.getComplemantaryInfo());

        boolean isCancelled = false;
        if (meta.getStatus() != null) {
            for (CertificateStatusType statusType : meta.getStatus()) {
                if (statusType.getType().equals(StatusType.SENT) || statusType.getType().equals(StatusType.CANCELLED)) {
                    builder.addStatus(statusType.getType().toString(), statusType.getTarget(), statusType.getTimestamp());
                }

                if (statusType.getType().equals(StatusType.CANCELLED)) {
                    isCancelled = true;
                }
            }
        }

        builder.cancelled(isCancelled);

        return builder.build();
    }

    public static List<CertificateMeta> toCertificateMeta(List<CertificateMetaType> metas) {
        List<CertificateMeta> result = new ArrayList<>();

        // Copy and sort metadata
        List<CertificateMetaType> input = new ArrayList<>(metas);
        Collections.sort(input, DESCENDING_DATE);

        for (CertificateMetaType certificateMetaType : input) {
            result.add(toCertificateMeta(certificateMetaType));
        }

        return result;
    }
}
