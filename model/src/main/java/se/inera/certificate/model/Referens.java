package se.inera.certificate.model;

import org.joda.time.LocalDate;

/**
 * @author andreaskaltenbach
 */
public class Referens {

    private Kod referenstyp;
    private LocalDate datum;

    public Kod getReferenstyp() {
        return referenstyp;
    }

    public void setReferenstyp(Kod referenstyp) {
        this.referenstyp = referenstyp;
    }

    public LocalDate getDatum() {
        return datum;
    }

    public void setDatum(LocalDate datum) {
        this.datum = datum;
    }
}
