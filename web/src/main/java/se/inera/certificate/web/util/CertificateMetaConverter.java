package se.inera.certificate.web.util;

import java.util.ArrayList;
import java.util.List;

import se.inera.certificate.api.CertificateMeta;
import se.inera.certificate.api.StatusMeta;
import se.inera.certificate.web.service.dto.UtlatandeMetaData;
import se.inera.certificate.web.service.dto.UtlatandeStatusType;
import se.inera.certificate.web.service.dto.UtlatandeStatusType.StatusType;

/**
 * Converts meta data from the internal {@link UtlatandeMetaData} to the REST service model {@link CertificateMeta}.
 */
public final class CertificateMetaConverter {

    private CertificateMetaConverter() {
    }

    public static CertificateMeta toCertificateMeta(UtlatandeMetaData meta) {
        CertificateMeta result = new CertificateMeta();

        result.setId(meta.getId());
        result.setSelected(false);
        result.setType(meta.getType());
        result.setCaregiverName(meta.getIssuerName());
        result.setCareunitName(meta.getFacilityName());
        result.setSentDate(meta.getSignDate().toString());
        result.setArchived(!Boolean.parseBoolean(meta.getAvailable()));
        result.setComplementaryInfo(meta.getComplemantaryInfo());

        boolean cancelled = false;
        if (meta.getStatuses() != null) {
            for (UtlatandeStatusType statusType : meta.getStatuses()) {
                StatusMeta statusMeta = new StatusMeta();
                statusMeta.setType(statusType.getType().name());
                statusMeta.setTarget(statusType.getTarget());
                statusMeta.setTimestamp(statusType.getTimestamp());
                result.getStatuses().add(statusMeta);

                if (statusType.getType().equals(StatusType.CANCELLED)) {
                    cancelled = true;
                }
            }
        }
        result.setCancelled(cancelled);

        return result;
    }

    public static List<CertificateMeta> toCertificateMeta(List<UtlatandeMetaData> metas) {
        List<CertificateMeta> result = new ArrayList<>();

        for (UtlatandeMetaData certificateMetaType : metas) {
            result.add(toCertificateMeta(certificateMetaType));
        }

        return result;
    }
}
