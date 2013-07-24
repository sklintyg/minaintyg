package se.inera.certificate.model;

/**
 * @author andreaskaltenbach
 */
public class Vardgivare {

    private Id id;
    private String namn;

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    public String getNamn() {
        return namn;
    }

    public void setNamn(String namn) {
        this.namn = namn;
    }
}
