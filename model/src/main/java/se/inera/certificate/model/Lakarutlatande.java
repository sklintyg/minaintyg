package se.inera.certificate.model;

import static se.inera.certificate.model.Aktivitetskod.ARBETSLIVSINRIKTAD_REHABILITERING_AR_AKTUELL;
import static se.inera.certificate.model.Aktivitetskod.ARBETSLIVSINRIKTAD_REHABILITERING_AR_EJ_AKTUELL;
import static se.inera.certificate.model.Aktivitetskod.AVSTANGNING_ENLIGT_SM_L_PGA_SMITTA;
import static se.inera.certificate.model.Aktivitetskod.FORANDRAT_RESSATT_TILL_ARBETSPLATSEN_AR_AKTUELLT;
import static se.inera.certificate.model.Aktivitetskod.FORANDRAT_RESSATT_TILL_ARBETSPLATSEN_AR_EJ_AKTUELLT;
import static se.inera.certificate.model.Aktivitetskod.GAR_EJ_ATT_BEDOMMA_OM_ARBETSLIVSINRIKTAD_REHABILITERING_AR_AKTUELL;
import static se.inera.certificate.model.Aktivitetskod.KONTAKT_MED_FORSAKRINGSKASSAN_AR_AKTUELL;
import static se.inera.certificate.model.Aktivitetskod.OVRIGT;
import static se.inera.certificate.model.Aktivitetskod.PATIENTEN_BEHOVER_FA_KONTAKT_MED_ARBETSFORMEDLINGEN;
import static se.inera.certificate.model.Aktivitetskod.PATIENTEN_BEHOVER_FA_KONTAKT_MED_FORETAGSHALSOVARDEN;
import static se.inera.certificate.model.Aktivitetskod.PLANERAD_ELLER_PAGAENDE_ANNAN_ATGARD;
import static se.inera.certificate.model.Aktivitetskod.PLANERAD_ELLER_PAGAENDE_BEHANDLING_ELLER_ATGARD_INOM_SJUKVARDEN;
import static se.inera.certificate.model.util.Iterables.find;
import static se.inera.certificate.model.util.Strings.emptyToNull;
import static se.inera.certificate.model.util.Strings.join;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import se.inera.certificate.model.util.Predicate;

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

    private String sjukdomsforlopp;

    private Ovrigt ovrigt;

    private List<Status> status;

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

    public String getSjukdomsforlopp() {
        return sjukdomsforlopp;
    }

    public void setSjukdomsforlopp(String sjukdomsforlopp) {
        this.sjukdomsforlopp = sjukdomsforlopp;
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

    public LocalDate getValidFromDate() {

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

    public LocalDate getValidToDate() {

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

    public Aktivitet getAktivitet(final Aktivitetskod aktivitetsKod) {
        if (aktiviteter == null) {
            return null;
        }

        return find(aktiviteter, new Predicate<Aktivitet>() {
            @Override
            public boolean apply(Aktivitet aktivitet) {
                return aktivitet.getAktivitetskod() == aktivitetsKod;
            }
        }, null);
    }


    public Vardkontakt getVardkontakt(final Vardkontakttyp vardkontaktTyp) {
        return find(vardkontakter, new Predicate<Vardkontakt>() {
            @Override
            public boolean apply(Vardkontakt vardkontakt) {
                return vardkontakt.getVardkontakttyp() == vardkontaktTyp;
            }
        }, null);
    }

    public Referens getReferens(final Referenstyp referensTyp) {
        return find(referenser, new Predicate<Referens>() {
            @Override
            public boolean apply(Referens referens) {
                return referens.getReferenstyp() == referensTyp;
            }
        }, null);
    }

    public Aktivitet getForandratRessattAktuellt() {
        return getAktivitet(FORANDRAT_RESSATT_TILL_ARBETSPLATSEN_AR_AKTUELLT);
    }

    public Aktivitet getForandratRessattEjAktuellt() {
        return getAktivitet(FORANDRAT_RESSATT_TILL_ARBETSPLATSEN_AR_EJ_AKTUELLT);
    }

    public Aktivitet getKontaktMedForsakringskassanAktuell() {
        return getAktivitet(KONTAKT_MED_FORSAKRINGSKASSAN_AR_AKTUELL);
    }

    public Aktivitet getArbetsinriktadRehabiliteringAktuell() {
        return getAktivitet(ARBETSLIVSINRIKTAD_REHABILITERING_AR_AKTUELL);
    }

    public Aktivitet getArbetsinriktadRehabiliteringEjAktuell() {
        return getAktivitet(ARBETSLIVSINRIKTAD_REHABILITERING_AR_EJ_AKTUELL);
    }

    public Aktivitet getArbetsinriktadRehabiliteringEjBedombar() {
        return getAktivitet(GAR_EJ_ATT_BEDOMMA_OM_ARBETSLIVSINRIKTAD_REHABILITERING_AR_AKTUELL);
    }

    public Aktivitet getAvstangningEnligtSmittskyddslagen() {
        return getAktivitet(AVSTANGNING_ENLIGT_SM_L_PGA_SMITTA);
    }

    public Aktivitet getRekommenderarKontaktMedArbetsformedlingen() {
        return getAktivitet(PATIENTEN_BEHOVER_FA_KONTAKT_MED_ARBETSFORMEDLINGEN);
    }

    public Aktivitet getRekommenderarKontaktMedForetagshalsovarden() {
        return getAktivitet(PATIENTEN_BEHOVER_FA_KONTAKT_MED_FORETAGSHALSOVARDEN);
    }

    public Aktivitet getRekommenderarOvrigt() {
        return getAktivitet(OVRIGT);
    }

    public Aktivitet getAtgardInomSjukvarden() {
        return getAktivitet(PLANERAD_ELLER_PAGAENDE_BEHANDLING_ELLER_ATGARD_INOM_SJUKVARDEN);
    }

    public Aktivitet getAnnanAtgard() {
        return getAktivitet(PLANERAD_ELLER_PAGAENDE_ANNAN_ATGARD);
    }

    public String getForskrivarkodOchArbetsplatskod() {
        List<String> parts = new ArrayList<>();
        if (skapadAv != null) {
            parts.add(skapadAv.getForskrivarkod());
        }
        if (vardenhet != null) {
            parts.add(vardenhet.getArbetsplatskod());
        }

        return emptyToNull(join(" - ", parts));
    }

    public List<Status> getStatus() {
        return status;
    }

    public void setStatus(List<Status> status) {
        this.status = status;
    }
}
