package se.inera.certificate.model;

import org.joda.time.LocalDateTime;

/**
 * @author andreaskaltenbach
 */
public class Sysselsattning {

    private Kod sysselsattningsTyp;
    private LocalDateTime datum;

    public Kod getSysselsattningsTyp() {
        return sysselsattningsTyp;
    }

    public void setSysselsattningsTyp(Kod sysselsattningsTyp) {
        this.sysselsattningsTyp = sysselsattningsTyp;
    }

    public LocalDateTime getDatum() {
        return datum;
    }

    public void setDatum(LocalDateTime datum) {
        this.datum = datum;
    }
}
