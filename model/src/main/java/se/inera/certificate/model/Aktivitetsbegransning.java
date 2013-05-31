package se.inera.certificate.model;

import org.joda.time.LocalDate;

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

    public LocalDate calculateValidFromDate() {
        if (arbetsformaga == null) {
            return null;
        }

        LocalDate fromDate = null;
        for (ArbetsformagaNedsattning arbetsformagaNedsattning : arbetsformaga.getArbetsformagaNedsattningar()) {
            if (fromDate == null || fromDate.isAfter(arbetsformagaNedsattning.getVaraktighetFrom())) {
                fromDate = arbetsformagaNedsattning.getVaraktighetFrom();
            }
        }
        return fromDate;
    }

    public LocalDate calculateValidToDate() {
        if (arbetsformaga == null) {
            return null;
        }

        LocalDate toDate = null;
        for (ArbetsformagaNedsattning arbetsformagaNedsattning : arbetsformaga.getArbetsformagaNedsattningar()) {
            if (toDate == null || toDate.isBefore(arbetsformagaNedsattning.getVaraktighetTom())) {
                toDate = arbetsformagaNedsattning.getVaraktighetTom();
            }
        }
        return toDate;
    }
}
