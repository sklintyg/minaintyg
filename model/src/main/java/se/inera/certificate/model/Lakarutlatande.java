package se.inera.certificate.model;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import java.util.List;

/**
 * @author andreaskaltenbach
 */
public class Lakarutlatande {

    private String id;

    private String typ;

    private String kommentar;

    private LocalDateTime signeringsDatum;

    private LocalDateTime skickatDatum;

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

    public LocalDateTime getSigneringsDatum() {
        return signeringsDatum;
    }

    public void setSigneringsDatum(LocalDateTime signeringsDatum) {
        this.signeringsDatum = signeringsDatum;
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

    public LocalDateTime getSkickatDatum() {
        return skickatDatum;
    }

    public void setSkickatDatum(LocalDateTime skickatDatum) {
        this.skickatDatum = skickatDatum;
    }

    public LocalDate calculateValidFromDate() {

        if (aktivitetsbegransningar == null) {
            return null;
        }

        LocalDate fromDate = null;

        for (Aktivitetsbegransning aktivitetsbegransning : aktivitetsbegransningar) {
            LocalDate aktivitetsbegransningFromDate = aktivitetsbegransning.calculateValidFromDate();
            if (fromDate == null || fromDate.isAfter(aktivitetsbegransningFromDate)) {
                fromDate = aktivitetsbegransningFromDate;
            }
        }
        return fromDate;
    }

    public LocalDate calculateValidToDate() {

        if (aktivitetsbegransningar == null) {
            return null;
        }

        LocalDate toDate = null;

        for (Aktivitetsbegransning aktivitetsbegransning : aktivitetsbegransningar) {
            LocalDate aktivitetsbegransningToDate = aktivitetsbegransning.calculateValidToDate();
            if (toDate == null || toDate.isBefore(aktivitetsbegransningToDate)) {
                toDate = aktivitetsbegransningToDate;
            }
        }
        return toDate;
    }


}
