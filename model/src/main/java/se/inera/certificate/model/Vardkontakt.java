package se.inera.certificate.model;

/**
 * @author andreaskaltenbach
 */
public class Vardkontakt {
    private Kod vardkontakttyp;
    private LocalDateInterval vardkontaktstid;

    public Kod getVardkontakttyp() {
        return vardkontakttyp;
    }

    public void setVardkontakttyp(Kod vardkontakttyp) {
        this.vardkontakttyp = vardkontakttyp;
    }

    public LocalDateInterval getVardkontaktstid() {
        return vardkontaktstid;
    }

    public void setVardkontaktstid(LocalDateInterval vardkontaktstid) {
        this.vardkontaktstid = vardkontaktstid;
    }
}
