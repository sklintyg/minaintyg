package se.inera.certificate.model;

import org.joda.time.LocalDate;

/**
 * @author andreaskaltenbach
 */
public class ArbetsformagaNedsattning {

    private LocalDate varaktighetTom;
    private LocalDate varaktighetFrom;

    private Nedsattningsgrad nedsattningsgrad;

    public LocalDate getVaraktighetTom() {
        return varaktighetTom;
    }

    public void setVaraktighetTom(LocalDate varaktighetTom) {
        this.varaktighetTom = varaktighetTom;
    }

    public LocalDate getVaraktighetFrom() {
        return varaktighetFrom;
    }

    public void setVaraktighetFrom(LocalDate varaktighetFrom) {
        this.varaktighetFrom = varaktighetFrom;
    }

    public Nedsattningsgrad getNedsattningsgrad() {
        return nedsattningsgrad;
    }

    public void setNedsattningsgrad(Nedsattningsgrad nedsattningsgrad) {
        this.nedsattningsgrad = nedsattningsgrad;
    }
}
