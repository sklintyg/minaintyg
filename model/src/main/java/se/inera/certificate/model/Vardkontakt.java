package se.inera.certificate.model;

import org.joda.time.LocalDate;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * @author andreaskaltenbach
 */
public class Vardkontakt {
    private Vardkontakttyp vardkontakttyp;
    private LocalDate vardkontaktstid;

    public Vardkontakttyp getVardkontakttyp() {
        return vardkontakttyp;
    }

    public void setVardkontakttyp(Vardkontakttyp vardkontakttyp) {
        this.vardkontakttyp = vardkontakttyp;
    }

    public LocalDate getVardkontaktstid() {
        return vardkontaktstid;
    }

    public void setVardkontaktstid(LocalDate vardkontaktstid) {
        this.vardkontaktstid = vardkontaktstid;
    }
}
