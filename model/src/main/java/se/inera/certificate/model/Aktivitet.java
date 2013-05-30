package se.inera.certificate.model;

/**
 * @author andreaskaltenbach
 */
public class Aktivitet {
    private Aktivitetskod aktivitetskod;
    private String beskrivning;

    public Aktivitetskod getAktivitetskod() {
        return aktivitetskod;
    }

    public void setAktivitetskod(Aktivitetskod aktivitetskod) {
        this.aktivitetskod = aktivitetskod;
    }

    public String getBeskrivning() {
        return beskrivning;
    }

    public void setBeskrivning(String beskrivning) {
        this.beskrivning = beskrivning;
    }
}
