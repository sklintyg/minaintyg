package se.inera.certificate.web.service.dto;

import se.inera.certificate.model.Status;
import se.inera.certificate.model.common.internal.Utlatande;

import java.util.List;

import static org.springframework.util.Assert.hasText;
import static org.springframework.util.Assert.notNull;


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
