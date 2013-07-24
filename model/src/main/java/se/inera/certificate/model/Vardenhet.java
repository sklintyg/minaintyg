package se.inera.certificate.model;

/**
 * @author andreaskaltenbach
 */
public class Vardenhet {

    private Id id;
    private Id arbetsplatskod;
    private String namn;
    private String postadress;
    private String postnummer;
    private String postort;
    private String telefonnummer;
    private String epost;

    private Vardgivare vardgivare;

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    public Id getArbetsplatskod() {
        return arbetsplatskod;
    }

    public void setArbetsplatskod(Id arbetsplatskod) {
        this.arbetsplatskod = arbetsplatskod;
    }

    public String getNamn() {
        return namn;
    }

    public void setNamn(String namn) {
        this.namn = namn;
    }

    public Vardgivare getVardgivare() {
        return vardgivare;
    }

    public void setVardgivare(Vardgivare vardgivare) {
        this.vardgivare = vardgivare;
    }

    public String getPostadress() {
        return postadress;
    }

    public void setPostadress(String postadress) {
        this.postadress = postadress;
    }

    public String getPostnummer() {
        return postnummer;
    }

    public void setPostnummer(String postnummer) {
        this.postnummer = postnummer;
    }

    public String getPostort() {
        return postort;
    }

    public void setPostort(String postort) {
        this.postort = postort;
    }

    public String getTelefonnummer() {
        return telefonnummer;
    }

    public void setTelefonnummer(String telefonnummer) {
        this.telefonnummer = telefonnummer;
    }

    public String getEpost() {
        return epost;
    }

    public void setEpost(String epost) {
        this.epost = epost;
    }
}
