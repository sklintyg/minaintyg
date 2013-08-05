package se.inera.certificate.integration.converter;

import se.inera.certificate.model.dao.CertificateStateHistoryEntry;
import se.inera.ifv.insuranceprocess.certificate.v1.CertificateStatusType;
import se.inera.ifv.insuranceprocess.certificate.v1.StatusType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author andreaskaltenbach
 */
public class CertificateStateHistoryEntryConverter {

    private CertificateStateHistoryEntryConverter() {
    }

    public static List<CertificateStatusType> toCertificateStatusType(List<CertificateStateHistoryEntry> source) {
        if (source == null || source.isEmpty()) return Collections.emptyList();

        List<CertificateStatusType> states = new ArrayList<>();
        for (CertificateStateHistoryEntry state : source) {
            states.add(toCertificateStatusType(state));
        }
        return states;
    }

    private static CertificateStatusType toCertificateStatusType(CertificateStateHistoryEntry source) {
        CertificateStatusType status = new CertificateStatusType();
        status.setTarget(source.getTarget());
        status.setTimestamp(source.getTimestamp());
        status.setType(StatusType.valueOf(source.getState().name()));
        return status;
    }
}
