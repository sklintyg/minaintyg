package se.inera.certificate.api;

public class Certificate {

    private final Object utlatande;

    private final CertificateStatus meta;

    public Certificate(Object utlatande, CertificateStatus meta) {
        this.utlatande = utlatande;
        this.meta = meta;
    }

    public Object getUtlatande() {
        return utlatande;
    }

    public CertificateStatus getMeta() {
        return meta;
    }
}
