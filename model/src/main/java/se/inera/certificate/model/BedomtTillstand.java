package se.inera.certificate.model;

/**
 * @author andreaskaltenbach
 */
public class BedomtTillstand {

    private String beskrivning;
    private String tillstandskod;

    public String getBeskrivning() {
        return beskrivning;
    }

    public void setBeskrivning(String beskrivning) {
        this.beskrivning = beskrivning;
    }

    public String getTillstandskod() {
        return tillstandskod;
    }

    public void setTillstandskod(String tillstandskod) {
        this.tillstandskod = tillstandskod;
    }
}
