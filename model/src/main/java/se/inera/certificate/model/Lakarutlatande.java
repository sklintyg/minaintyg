package se.inera.certificate.model;

import org.joda.time.LocalDateTime;

/**
 * @author andreaskaltenbach
 */
public class Lakarutlatande {

    private String id;

    private String typ;

    private String kommentar;

    private LocalDateTime signeringsdatum;

    private Patient patient;

    private HosPersonal skapadAv;

    private Vardenhet vardenhet;

    private String sjukdomsfarlopp;

    private Ovrigt ovrigt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public String getKommentar() {
        return kommentar;
    }

    public void setKommentar(String kommentar) {
        this.kommentar = kommentar;
    }

    public LocalDateTime getSigneringsdatum() {
        return signeringsdatum;
    }

    public void setSigneringsdatum(LocalDateTime signeringsdatum) {
        this.signeringsdatum = signeringsdatum;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public HosPersonal getSkapadAv() {
        return skapadAv;
    }

    public void setSkapadAv(HosPersonal skapadAv) {
        this.skapadAv = skapadAv;
    }

    public Vardenhet getVardenhet() {
        return vardenhet;
    }

    public void setVardenhet(Vardenhet vardenhet) {
        this.vardenhet = vardenhet;
    }

    public String getSjukdomsfarlopp() {
        return sjukdomsfarlopp;
    }

    public void setSjukdomsfarlopp(String sjukdomsfarlopp) {
        this.sjukdomsfarlopp = sjukdomsfarlopp;
    }

    public Ovrigt getOvrigt() {
        return ovrigt;
    }

    public void setOvrigt(Ovrigt ovrigt) {
        this.ovrigt = ovrigt;
    }

}
