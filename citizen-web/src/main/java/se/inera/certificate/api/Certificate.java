package se.inera.certificate.api;

public class Certificate {

    private final Object utlatande;

    private final CertificateMeta meta;

    public Certificate(Object utlatande, CertificateMeta meta) {
        this.utlatande = utlatande;
        this.meta = meta;
    }

    public Object getUtlatande() {
        return utlatande;
    }

    public CertificateMeta getMeta() {
        return meta;
    }
}
