package se.inera.certificate.web.service.dto;

import static org.springframework.util.Assert.hasText;
import static org.springframework.util.Assert.notNull;

/**
 * Object returned by services containing a utlatande and meta data describing the utlatande.
 */
public class UtlatandeWithMeta {

    /** An utlatande in the external model. */
    private final String utlatande;

    /** Meta data for the utlatande. */
    private final UtlatandeMetaData meta;

    public UtlatandeWithMeta(String utlatande, UtlatandeMetaData meta) {
        hasText(utlatande, "'utlatande' must not be empty");
        notNull(meta, "'meta' must not be null");
        this.utlatande = utlatande;
        this.meta = meta;
    }

    public String getUtlatande() {
        return utlatande;
    }

    public UtlatandeMetaData getMeta() {
        return meta;
    }
}
