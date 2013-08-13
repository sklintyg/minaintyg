package se.inera.certificate.model;

import java.util.List;

/**
 * @author andreaskaltenbach
 */
public class Observation {

    private Kod observationsKategori;
    private Kod observationsKod;
    private PartialInterval observationsPeriod;
    private String beskrivning;
    private Prognos prognos;
    private List<PhysicalQuantity> varde;

    public Kod getObservationsKategori() {
        return observationsKategori;
    }

    public void setObservationsKategori(Kod observationsKategori) {
        this.observationsKategori = observationsKategori;
    }

    public Kod getObservationsKod() {
        return observationsKod;
    }

    public void setObservationsKod(Kod observationsKod) {
        this.observationsKod = observationsKod;
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

    public List<PhysicalQuantity> getVarde() {
        return varde;
    }

    public void setVarde(List<PhysicalQuantity> varde) {
        this.varde = varde;
    }
}
