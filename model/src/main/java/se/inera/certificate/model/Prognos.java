package se.inera.certificate.model;

/**
 * @author andreaskaltenbach
 */
public class Prognos {

    private Kod prognosKod;
    private String beskrivning;

    public Kod getPrognosKod() {
        return prognosKod;
    }

    public void setPrognosKod(Kod prognosKod) {
        this.prognosKod = prognosKod;
    }

    public String getBeskrivning() {
        return beskrivning;
    }

    public void setBeskrivning(String beskrivning) {
        this.beskrivning = beskrivning;
    }
}
