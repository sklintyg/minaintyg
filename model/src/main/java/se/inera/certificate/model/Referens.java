package se.inera.certificate.model;

import org.joda.time.LocalDate;

/**
 * @author andreaskaltenbach
 */
public class Referens {

    private Referenstyp referenstyp;
    private LocalDate datum;

    public Referenstyp getReferenstyp() {
        return referenstyp;
    }

    public void setReferenstyp(Referenstyp referenstyp) {
        this.referenstyp = referenstyp;
    }

    public LocalDate getDatum() {
        return datum;
    }

    public void setDatum(LocalDate datum) {
        this.datum = datum;
    }
}
