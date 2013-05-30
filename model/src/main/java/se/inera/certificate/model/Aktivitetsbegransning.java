package se.inera.certificate.model;

/**
 * @author andreaskaltenbach
 */
public class Aktivitetsbegransning {

    private String beskrivning;

    private Arbetsformaga arbetsformaga;

    public String getBeskrivning() {
        return beskrivning;
    }

    public void setBeskrivning(String beskrivning) {
        this.beskrivning = beskrivning;
    }

    public Arbetsformaga getArbetsformaga() {
        return arbetsformaga;
    }

    public void setArbetsformaga(Arbetsformaga arbetsformaga) {
        this.arbetsformaga = arbetsformaga;
    }
}
