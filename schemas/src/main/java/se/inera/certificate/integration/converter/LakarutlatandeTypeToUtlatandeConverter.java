package se.inera.certificate.integration.converter;

import iso.v21090.dt.v1.CD;
import iso.v21090.dt.v1.II;
import iso.v21090.dt.v1.PQ;
import org.joda.time.Partial;
import se.inera.certificate.common.v1.AktivitetType;
import se.inera.certificate.common.v1.ArbetsuppgiftType;
import se.inera.certificate.common.v1.DateInterval;
import se.inera.certificate.common.v1.ObservationType;
import se.inera.certificate.common.v1.PartialDateInterval;
import se.inera.certificate.common.v1.PatientType;
import se.inera.certificate.common.v1.PrognosType;
import se.inera.certificate.common.v1.ReferensType;
import se.inera.certificate.common.v1.SysselsattningType;
import se.inera.certificate.common.v1.Utlatande;
import se.inera.certificate.common.v1.VardkontaktType;
import se.inera.certificate.model.Kod;
import se.inera.certificate.model.codes.Aktivitetskoder;
import se.inera.certificate.model.codes.ObservationsKoder;
import se.inera.certificate.model.codes.Prognoskoder;
import se.inera.certificate.model.codes.Referenstypkoder;
import se.inera.certificate.model.codes.Sysselsattningskoder;
import se.inera.certificate.model.codes.Vardkontakttypkoder;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.ArbetsformagaNedsattningType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.ArbetsformagaType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.BedomtTillstandType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.FunktionstillstandType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.LakarutlatandeType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.MedicinsktTillstandType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.Prognosangivelse;

import java.util.ArrayList;
import java.util.List;

import static se.inera.certificate.integration.converter.util.IsoTypeConverter.toCD;
import static se.inera.certificate.model.codes.ObservationsKoder.DIAGNOS;

/**
 * @author andreaskaltenbach
 */
public final class LakarutlatandeTypeToUtlatandeConverter {

    public static final String FK_7263 = "fk7263";

    private LakarutlatandeTypeToUtlatandeConverter() {
    }

    /**
     * Converts a JAXB {@link LakarutlatandeType} to a {@link se.inera.certificate.common.v1.Utlatande}.
     */
    public static Utlatande convert(LakarutlatandeType source) {
        Utlatande utlatande = new Utlatande();


        II id = new II();
        id.setExtension(source.getLakarutlatandeId());
        utlatande.setUtlatandeId(id);

        CD type = new CD();
        type.setCode(FK_7263);
        utlatande.setTypAvUtlatande(type);

        utlatande.setSigneringsdatum(source.getSigneringsdatum());
        utlatande.setSkickatdatum(source.getSkickatDatum());

        if (source.getKommentar() != null && !source.getKommentar().isEmpty()) {
            utlatande.getKommentars().add(source.getKommentar());
        }

        utlatande.setSkapadAv(source.getSkapadAvHosPersonal());
        utlatande.setPatient(convert(source.getPatient()));

        utlatande.getObservations().add(convert(source.getMedicinsktTillstand()));

        if (source.getBedomtTillstand() != null) {
            utlatande.getObservations().add(convert(source.getBedomtTillstand()));
        }

        for (FunktionstillstandType funktionstillstand : source.getFunktionstillstand()) {

            utlatande.getObservations().addAll(convert(funktionstillstand));

            if (funktionstillstand.getArbetsformaga() != null) {

                if (funktionstillstand.getArbetsformaga().getArbetsuppgift() != null) {
                    utlatande.getPatient().getArbetsuppgifts().add(convert(funktionstillstand.getArbetsformaga().getArbetsuppgift()));
                }
                if (funktionstillstand.getArbetsformaga().getSysselsattning() != null) {
                    for (se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.SysselsattningType sysselsattning : funktionstillstand.getArbetsformaga().getSysselsattning()) {
                        utlatande.getPatient().getSysselsattnings().add(convert(sysselsattning, source.getPatient()));
                    }
                }
            }
        }

        for (se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.AktivitetType aktivitetType : source.getAktivitet()) {
            utlatande.getAktivitets().add(convert(aktivitetType));
        }

        for (se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.ReferensType referensType : source.getReferens()) {
            utlatande.getReferens().add(convert(referensType));
        }

        for (se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.VardkontaktType vardkontaktType : source.getVardkontakt()) {
            utlatande.getVardkontakts().add(convert(vardkontaktType));
        }

        return utlatande;
    }

    private static SysselsattningType convert(se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.SysselsattningType source,
                                              se.inera.ifv.insuranceprocess.healthreporting.v2.PatientType patient) {

        SysselsattningType sysselsattning = new SysselsattningType();
        CD sysselsattningsType = null;

        switch(source.getTypAvSysselsattning()) {
            case ARBETSLOSHET:
                sysselsattningsType = toCD(Sysselsattningskoder.ARBETSLOSHET);
                break;
            case NUVARANDE_ARBETE:
                sysselsattningsType = toCD(Sysselsattningskoder.NUVARANDE_ARBETE);
                break;
            case FORALDRALEDIGHET:
                String personnr = patient.getPersonId().getExtension();
                int v = Integer.parseInt(personnr.substring(personnr.indexOf('-')).substring(3,4)) % 2;
                switch (v) {
                    case 0:
                        sysselsattningsType = toCD(Sysselsattningskoder.MAMMALEDIG);
                        break;
                    case 1:
                        sysselsattningsType = toCD(Sysselsattningskoder.PAPPALEDIG);
                        break;
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown SysselsattningsType: " + source.getTypAvSysselsattning() );
        }
        sysselsattning.setTypAvSysselsattning(sysselsattningsType);
        return sysselsattning;
    }

    private static ArbetsuppgiftType convert(se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.ArbetsuppgiftType source) {
        ArbetsuppgiftType arbetsuppgift = new ArbetsuppgiftType();
        arbetsuppgift.setTypAvArbetsuppgift(source.getTypAvArbetsuppgift());
        return arbetsuppgift;
    }

    private static ObservationType convert(BedomtTillstandType bedomtTillstand) {
        ObservationType observation = new ObservationType();
        observation.setBeskrivning(bedomtTillstand.getBeskrivning());
        observation.setObservationskod(toCD(ObservationsKoder.FORLOPP));
        return observation;
    }

    private static ObservationType convert(MedicinsktTillstandType medicinsktTillstand) {
        if (medicinsktTillstand == null) {
            return null;
        }
        ObservationType observation = new ObservationType();
        observation.setBeskrivning(medicinsktTillstand.getBeskrivning());

        if (medicinsktTillstand.getTillstandskod() != null) {
            observation.setObservationskategori(toCD(DIAGNOS));
            observation.setObservationskod(medicinsktTillstand.getTillstandskod());
        }
        return observation;
    }

    private static VardkontaktType convert(se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.VardkontaktType source) {
        VardkontaktType vardkontakt = new VardkontaktType();

        switch (source.getVardkontakttyp()) {
            case MIN_UNDERSOKNING_AV_PATIENTEN:
                vardkontakt.setVardkontakttyp(toCD(Vardkontakttypkoder.MIN_UNDERSOKNING_AV_PATIENTEN));
                break;
            case MIN_TELEFONKONTAKT_MED_PATIENTEN:
                vardkontakt.setVardkontakttyp(toCD(Vardkontakttypkoder.MIN_TELEFONKONTAKT_MED_PATIENTEN));
                break;
            default:
                throw new IllegalArgumentException("Unknown VardkontaktType: " + source.getVardkontakttyp() );
        }

        // TODO - do we really need a timespan with from and to date? In FK7263 case, we set fromDate=endDate
        DateInterval vardkontaktTid = new DateInterval();
        vardkontaktTid.setFrom(source.getVardkontaktstid());
        vardkontaktTid.setTom(source.getVardkontaktstid());
        vardkontakt.setVardkontakttid(vardkontaktTid);
        return vardkontakt;
    }

    private static ReferensType convert(se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.ReferensType source) {
        ReferensType referens = new ReferensType();

        switch (source.getReferenstyp()) {
            case JOURNALUPPGIFTER:
                referens.setReferenstyp(toCD(Referenstypkoder.JOURNALUPPGIFT));
                break;
            case ANNAT:
                referens.setReferenstyp(toCD(Referenstypkoder.ANNAT));
                break;
            default:
                throw new IllegalArgumentException("Unknown ReferensType: " + source.getReferenstyp() );
        }
        referens.setReferensdatum(source.getDatum());

        return referens;
    }

    private static AktivitetType convert(se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.AktivitetType source) {
        AktivitetType aktivitet = new AktivitetType();

        CD aktivitetsCode = null;

        switch (source.getAktivitetskod()) {
            case PLANERAD_ELLER_PAGAENDE_BEHANDLING_ELLER_ATGARD_INOM_SJUKVARDEN:
                aktivitetsCode = toCD(Aktivitetskoder.PLANERAD_ELLER_PAGAENDE_BEHANDLING_ELLER_ATGARD_INOM_SJUKVARDEN);
                break;
            case PLANERAD_ELLER_PAGAENDE_ANNAN_ATGARD:
                aktivitetsCode = toCD(Aktivitetskoder.PLANERAD_ELLER_PAGAENDE_ANNAN_ATGARD);
                break;
            case ARBETSLIVSINRIKTAD_REHABILITERING_AR_AKTUELL:
                aktivitetsCode = toCD(Aktivitetskoder.ARBETSLIVSINRIKTAD_REHABILITERING_AR_AKTUELL);
                break;
            case ARBETSLIVSINRIKTAD_REHABILITERING_AR_EJ_AKTUELL:
                aktivitetsCode = toCD(Aktivitetskoder.ARBETSLIVSINRIKTAD_REHABILITERING_AR_INTE_AKTUELL);
                break;
            case GAR_EJ_ATT_BEDOMMA_OM_ARBETSLIVSINRIKTAD_REHABILITERING_AR_AKTUELL:
                aktivitetsCode = toCD(Aktivitetskoder.GAR_EJ_ATT_BEDOMA_OM_ARBETSLIVSINRIKTAD_REHABILITERING_AR_AKTUELL);
                break;
            case PATIENTEN_BEHOVER_FA_KONTAKT_MED_FORETAGSHALSOVARDEN:
                aktivitetsCode = toCD(Aktivitetskoder.PATIENTEN_BOR_FA_KONTAKT_MED_FORETAGSHALSOVARDEN);
                break;
            case PATIENTEN_BEHOVER_FA_KONTAKT_MED_ARBETSFORMEDLINGEN:
                aktivitetsCode = toCD(Aktivitetskoder.PATIENTEN_BOR_FA_KONTAKT_MED_ARBETSFORMEDLINGEN);
                break;
            case FORANDRAT_RESSATT_TILL_ARBETSPLATSEN_AR_AKTUELLT:
                aktivitetsCode = toCD(Aktivitetskoder.FORANDRA_RESSATT_TILL_ARBETSPLATSEN_AR_AKTUELLT);
                break;
            case FORANDRAT_RESSATT_TILL_ARBETSPLATSEN_AR_EJ_AKTUELLT:
                aktivitetsCode = toCD(Aktivitetskoder.FORANDRA_RESSATT_TILL_ARBETSPLATSEN_AR_EJ_AKTUELLT);
                break;
            case KONTAKT_MED_FORSAKRINGSKASSAN_AR_AKTUELL:
                aktivitetsCode = toCD(Aktivitetskoder.KONTAKT_MED_FK_AR_AKTUELL);
                break;
            case AVSTANGNING_ENLIGT_SM_L_PGA_SMITTA:
                aktivitetsCode = toCD(Aktivitetskoder.AVSTANGNING_ENLIGT_SML_PGA_SMITTA);
                break;
            case OVRIGT:
                aktivitetsCode = toCD(Aktivitetskoder.OVRIGT);
                break;
            default:
                throw new IllegalArgumentException("Unknown Aktivitetskod: " + source.getAktivitetskod() );
        }

        aktivitet.setBeskrivning(source.getBeskrivning());
        aktivitet.setAktivitetskod(aktivitetsCode);
        return aktivitet;
    }

    private static List<ObservationType> convert(ArbetsformagaType source) {

        PrognosType prognos = null;
        if(source.getPrognosangivelse() != null) {
            prognos = new PrognosType();
            switch (source.getPrognosangivelse()) {
                case ATERSTALLAS_DELVIS:
                    prognos.setPrognoskod(toCD(Prognoskoder.ATERSTALLAS_DELVIS));
                    break;
                case ATERSTALLAS_HELT:
                    prognos.setPrognoskod(toCD(Prognoskoder.ATERSTALLAS_HELT));
                    break;
                case DET_GAR_INTE_ATT_BEDOMMA:
                    prognos.setPrognoskod(toCD(Prognoskoder.DET_GAR_INTE_ATT_BEDOMA));
                    break;
                case INTE_ATERSTALLAS:
                    prognos.setPrognoskod(toCD(Prognoskoder.INTE_ATERSTALLAS));
                    break;
            }
            prognos.setBeskrivning(source.getMotivering());
        }

        List<ObservationType> observations = new ArrayList<>();

        for (ArbetsformagaNedsattningType nedsattning : source.getArbetsformagaNedsattning()) {
            ObservationType arbetsformaga = convert(nedsattning);
            arbetsformaga.setPrognos(prognos);
            observations.add(arbetsformaga);
        }
        return observations;
    }

    private static ObservationType convert(ArbetsformagaNedsattningType source) {
        ObservationType nedsattning = new ObservationType();
        nedsattning.setObservationskod(toCD(ObservationsKoder.ARBETSFORMAGA));

        PQ varde = new PQ();
        varde.setUnit("percent");
        switch (source.getNedsattningsgrad()) {
            case HELT_NEDSATT:
                varde.setValue(0.0);
                break;
            case NEDSATT_MED_3_4:
                varde.setValue(25.0);
                break;
            case NEDSATT_MED_1_2:
                varde.setValue(50.0);
                break;
            case NEDSATT_MED_1_4:
                varde.setValue(75.0);
                break;
        }
        nedsattning.getVardes().add(varde);

        PartialDateInterval observationsperiod = new PartialDateInterval();
        observationsperiod.setFrom(new Partial(source.getVaraktighetFrom()));
        observationsperiod.setTom(new Partial(source.getVaraktighetTom()));
        nedsattning.setObservationsperiod(observationsperiod);

        return nedsattning;
    }

    private static List<ObservationType> convert(FunktionstillstandType source) {

        List<ObservationType> observations = new ArrayList<>();

        ObservationType observation = new ObservationType();

        switch (source.getTypAvFunktionstillstand()) {
            case AKTIVITET:
                observation.setObservationskategori(toCD(ObservationsKoder.AKTIVITETER_OCH_DELAKTIGHET));
                break;
            case KROPPSFUNKTION:
                observation.setObservationskategori(toCD(ObservationsKoder.KROPPSFUNKTIONER));
                break;
            default:
                throw new IllegalArgumentException("Unknown FunktionstillstandType: " + source.getTypAvFunktionstillstand());
        }
        observation.setBeskrivning(source.getBeskrivning());
        observations.add(observation);


        if (source.getArbetsformaga() != null) {
            observations.addAll(convert(source.getArbetsformaga()));
        }

        return observations;
    }

    private static CD convert(Prognosangivelse source) {
        if (source == null) {
            return null;
        }

        return toCD(new Kod(source.value()));
    }

    private static PatientType convert(se.inera.ifv.insuranceprocess.healthreporting.v2.PatientType source) {
        PatientType patient = new PatientType();
        patient.setPersonId(source.getPersonId());
        // // TODO - sort out what should happen with fullständigt namn vs. förnamn/efternamn
        patient.getFornamns().add("");
        patient.getEfternamns().add(source.getFullstandigtNamn());
        return patient;
    }
}
