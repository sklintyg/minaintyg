package se.inera.certificate.model;

import org.joda.time.LocalDateTime;

import java.util.List;

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

    private List<Aktivitet> aktiviteter;

    private List<Funktionsnedsattning> funktionsnedsattningar;

    private List<Aktivitetsbegransning> aktivitetsbegransningar;

    private BedomtTillstand bedomtTillstand;

    private List<Vardkontakt> vardkontakter;

    private List<Referens> referenser;

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

    public List<Aktivitet> getAktiviteter() {
        return aktiviteter;
    }

    public void setAktiviteter(List<Aktivitet> aktiviteter) {
        this.aktiviteter = aktiviteter;
    }

    public List<Funktionsnedsattning> getFunktionsnedsattningar() {
        return funktionsnedsattningar;
    }

    public void setFunktionsnedsattningar(List<Funktionsnedsattning> funktionsnedsattningar) {
        this.funktionsnedsattningar = funktionsnedsattningar;
    }

    public List<Aktivitetsbegransning> getAktivitetsbegransningar() {
        return aktivitetsbegransningar;
    }

    public void setAktivitetsbegransningar(List<Aktivitetsbegransning> aktivitetsbegransningar) {
        this.aktivitetsbegransningar = aktivitetsbegransningar;
    }

    public BedomtTillstand getBedomtTillstand() {
        return bedomtTillstand;
    }

    public void setBedomtTillstand(BedomtTillstand bedomtTillstand) {
        this.bedomtTillstand = bedomtTillstand;
    }

    public List<Vardkontakt> getVardkontakter() {
        return vardkontakter;
    }

    public void setVardkontakter(List<Vardkontakt> vardkontakter) {
        this.vardkontakter = vardkontakter;
    }

    public List<Referens> getReferenser() {
        return referenser;
    }

    public void setReferenser(List<Referens> referenser) {
        this.referenser = referenser;
    }
}
