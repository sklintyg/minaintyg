package se.inera.certificate.model;

import java.util.ArrayList;
import java.util.List;

import static se.inera.certificate.model.util.Iterables.find;
import static se.inera.certificate.model.util.Strings.emptyToNull;
import static se.inera.certificate.model.util.Strings.join;

import org.joda.time.LocalDateTime;
import se.inera.certificate.model.util.Predicate;

/**
 * @author andreaskaltenbach
 */
public class Utlatande {

    private Id id;

    private Kod typ;

    private List<String> kommentars;

    private LocalDateTime signeringsDatum;

    private LocalDateTime skickatDatum;

    private Patient patient;

    private HosPersonal skapadAv;

    private List<Aktivitet> aktiviteter;

    private List<Observation> observations;

    private List<Vardkontakt> vardkontakter;

    private List<Referens> referenser;

    private Ovrigt ovrigt;

    private List<Status> status;

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    public Kod getTyp() {
        return typ;
    }

    public void setTyp(Kod typ) {
        this.typ = typ;
    }

    public List<String> getKommentars() {
        return kommentars;
    }

    public void setKommentars(List<String> kommentar) {
        this.kommentars = kommentar;
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

    public List<Observation> getObservations() {
        return observations;
    }

    public void setObservations(List<Observation> observations) {
        this.observations = observations;
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

    /*public LocalDate getValidFromDate() {

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
    }        */

    public Aktivitet getAktivitet(final Kod aktivitetsKod) {
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


    public Vardkontakt getVardkontakt(final Kod vardkontaktTyp) {
        return find(vardkontakter, new Predicate<Vardkontakt>() {
            @Override
            public boolean apply(Vardkontakt vardkontakt) {
                return vardkontakt.getVardkontakttyp() == vardkontaktTyp;
            }
        }, null);
    }

    public Referens getReferens(final Kod referensTyp) {
        return find(referenser, new Predicate<Referens>() {
            @Override
            public boolean apply(Referens referens) {
                return referens.getReferenstyp() == referensTyp;
            }
        }, null);
    }

    public String getForskrivarkodOchArbetsplatskod() {
        List<String> parts = new ArrayList<>();
        if (skapadAv != null) {
            parts.add(skapadAv.getForskrivarkod());

            if (skapadAv.getVardenhet() != null) {
                parts.add(skapadAv.getVardenhet().getArbetsplatskod().getExtension());
            }
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
