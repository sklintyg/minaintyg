package se.inera.certificate.web.util;

import java.util.Collections;
import java.util.Comparator;

import org.joda.time.LocalDateTime;

import se.inera.certificate.api.CertificateMeta;
import se.inera.certificate.api.StatusMeta;

public class CertificateMetaBuilder {

    private CertificateMeta meta;

    public CertificateMetaBuilder() {
        meta = new CertificateMeta();
    }

    public CertificateMeta build() {
        Collections.sort(meta.getStatuses(), STATUS_COMPARATOR);
        return meta;
    }

    public CertificateMetaBuilder id(String id) {
        meta.setId(id);

        return this;
    }

    public CertificateMetaBuilder selected(Boolean selected) {
        meta.setSelected(selected);

        return this;
    }

    public CertificateMetaBuilder type(String type) {
        meta.setType(type);

        return this;
    }

    public CertificateMetaBuilder caregiverName(String caregiverName) {
        meta.setCaregiverName(caregiverName);

        return this;
    }

    public CertificateMetaBuilder careunitName(String careunitName) {
        meta.setCareunitName(careunitName);

        return this;
    }

    public CertificateMetaBuilder sentDate(String sentDate) {
        meta.setSentDate(sentDate);

        return this;
    }

    public CertificateMetaBuilder archived(Boolean archived) {
        meta.setArchived(archived);

        return this;
    }

    public CertificateMetaBuilder cancelled(Boolean cancelled) {
        meta.setCancelled(cancelled);

        return this;
    }

    public CertificateMetaBuilder complemenaryInfo(String complementaryInfo) {
        meta.setComplementaryInfo(complementaryInfo);

        return this;
    }

    public CertificateMetaBuilder addStatus(String type, String target, LocalDateTime timestamp) {
        StatusMeta status = new StatusMeta();
        status.setType(type);
        status.setTarget(target);
        status.setTimestamp(timestamp);

        meta.getStatuses().add(status);

        return this;
    }

    /**
     * Compare status newest first.
     */
    private static final Comparator<StatusMeta> STATUS_COMPARATOR = new Comparator<StatusMeta>() {
        @Override
        public int compare(StatusMeta o1, StatusMeta o2) {
            return o2.getTimestamp().compareTo(o1.getTimestamp());
        }
    };
}
