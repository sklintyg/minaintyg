package se.inera.certificate.model;

import org.joda.time.LocalDate;

/**
 * @author andreaskaltenbach
 */
public class Sysselsattning {

    private Kod sysselsattningsTyp;
    private LocalDate datum;

    public Kod getSysselsattningsTyp() {
        return sysselsattningsTyp;
    }

    public void setSysselsattningsTyp(Kod sysselsattningsTyp) {
        this.sysselsattningsTyp = sysselsattningsTyp;
    }

    public LocalDate getDatum() {
        return datum;
    }

    public void setDatum(LocalDate datum) {
        this.datum = datum;
    }
}
