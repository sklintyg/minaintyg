package se.inera.certificate.model;

/**
 * @author andreaskaltenbach
 */
public class Vardenhet {

    private String id;
    private String namn;

    private Vardgivare vardgivare;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
