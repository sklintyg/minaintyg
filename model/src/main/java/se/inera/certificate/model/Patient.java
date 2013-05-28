package se.inera.certificate.model;

/**
 * @author andreaskaltenbach
 */
public class Patient {

    private String id;
    private String fornamn;
    private String mellannamn;
    private String efternamn;
    private String tilltalsnamn;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFornamn() {
        return fornamn;
    }

    public void setFornamn(String fornamn) {
        this.fornamn = fornamn;
    }

    public String getMellannamn() {
        return mellannamn;
    }

    public void setMellannamn(String mellannamn) {
        this.mellannamn = mellannamn;
    }

    public String getEfternamn() {
        return efternamn;
    }

    public void setEfternamn(String efternamn) {
        this.efternamn = efternamn;
    }

    public String getTilltalsnamn() {
        return tilltalsnamn;
    }

    public void setTilltalsnamn(String tilltalsnamn) {
        this.tilltalsnamn = tilltalsnamn;
    }
}
