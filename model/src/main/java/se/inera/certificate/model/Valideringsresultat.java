package se.inera.certificate.model;

import java.util.List;

/**
 * @author andreaskaltenbach
 */
public class Valideringsresultat {

    private List<String> fel;

    public Valideringsresultat() {}

    public Valideringsresultat(List<String> fel) {
        this.fel = fel;
    }

    public List<String> getFel() {
        return fel;
    }

    public void setFel(List<String> fel) {
        this.fel = fel;
    }
}
