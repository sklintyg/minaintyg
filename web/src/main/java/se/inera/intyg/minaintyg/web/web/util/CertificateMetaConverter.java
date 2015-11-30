package se.inera.intyg.minaintyg.web.web.util;

import java.util.ArrayList;
import java.util.List;

import se.inera.intyg.minaintyg.web.api.CertificateMeta;
import se.inera.intyg.common.support.model.CertificateState;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.minaintyg.web.web.service.dto.UtlatandeMetaData;

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
            for (Status statusType : meta.getStatuses()) {
                result.getStatuses().add(statusType);

                if (statusType.getType().equals(CertificateState.CANCELLED)) {
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
