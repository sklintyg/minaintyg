package se.inera.certificate.model;

/**
 * @author andreaskaltenbach
 */
public class Aktivitet {
    private Kod aktivitetskod;
    private String beskrivning;

    public Kod getAktivitetskod() {
        return aktivitetskod;
    }

    public void setAktivitetskod(Kod aktivitetskod) {
        this.aktivitetskod = aktivitetskod;
    }

    public String getBeskrivning() {
        return beskrivning;
    }

    public void setBeskrivning(String beskrivning) {
        this.beskrivning = beskrivning;
    }
}
