package se.inera.certificate.model;

/**
 * @author andreaskaltenbach
 */
public class Observation {

    private Kod observationsKategori;
    private Kod observatonsKod;
    private PartialInterval observationsPeriod;
    private String beskrivning;
    private Prognos prognos;

    public Kod getObservationsKategori() {
        return observationsKategori;
    }

    public void setObservationsKategori(Kod observationsKategori) {
        this.observationsKategori = observationsKategori;
    }

    public Kod getObservatonsKod() {
        return observatonsKod;
    }

    public void setObservatonsKod(Kod observatonsKod) {
        this.observatonsKod = observatonsKod;
    }

    public PartialInterval getObservationsPeriod() {
        return observationsPeriod;
    }

    public void setObservationsPeriod(PartialInterval observationsPeriod) {
        this.observationsPeriod = observationsPeriod;
    }

    public String getBeskrivning() {
        return beskrivning;
    }

    public void setBeskrivning(String beskrivning) {
        this.beskrivning = beskrivning;
    }

    public Prognos getPrognos() {
        return prognos;
    }

    public void setPrognos(Prognos prognos) {
        this.prognos = prognos;
    }
}
