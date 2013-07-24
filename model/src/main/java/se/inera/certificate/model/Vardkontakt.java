package se.inera.certificate.model;

/**
 * @author andreaskaltenbach
 */
public class Vardkontakt {
    private Kod vardkontakttyp;
    private LocalDateTimeInterval vardkontaktstid;

    public Kod getVardkontakttyp() {
        return vardkontakttyp;
    }

    public void setVardkontakttyp(Kod vardkontakttyp) {
        this.vardkontakttyp = vardkontakttyp;
    }

    public LocalDateTimeInterval getVardkontaktstid() {
        return vardkontaktstid;
    }

    public void setVardkontaktstid(LocalDateTimeInterval vardkontaktstid) {
        this.vardkontaktstid = vardkontaktstid;
    }
}
