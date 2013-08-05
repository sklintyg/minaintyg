package se.inera.certificate.integration.converter;

import java.util.ArrayList;
import java.util.List;

import se.inera.certificate.model.CertificateState;
import se.inera.certificate.model.Status;
import se.inera.ifv.insuranceprocess.certificate.v1.CertificateStatusType;

/**
 * @author andreaskaltenbach
 */
public class StatusConverter {

    private StatusConverter() {
    }

    public static List<Status> toStatus(List<CertificateStatusType> source) {
        if (source == null || source.isEmpty()) return null;

        List<Status> statusList = new ArrayList<>();
        for (CertificateStatusType state : source) {
            statusList.add(toStatus(state));
        }
        return statusList;

    }

    private static Status toStatus(CertificateStatusType source) {
        Status status = new Status();
        status.setTarget(source.getTarget());
        status.setTimestamp(source.getTimestamp());
        status.setType(CertificateState.valueOf(source.getType().name()));
        return status;
    }

}
