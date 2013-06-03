package se.inera.certificate.integration.converter;

import se.inera.certificate.model.Aktivitet;
import se.inera.certificate.model.Aktivitetsbegransning;
import se.inera.certificate.model.Arbetsformaga;
import se.inera.certificate.model.ArbetsformagaNedsattning;
import se.inera.certificate.model.BedomtTillstand;
import se.inera.certificate.model.Funktionsnedsattning;
import se.inera.certificate.model.HosPersonal;
import se.inera.certificate.model.Lakarutlatande;
import se.inera.certificate.model.Patient;
import se.inera.certificate.model.Prognosangivelse;
import se.inera.certificate.model.Referens;
import se.inera.certificate.model.Sysselsattning;
import se.inera.certificate.model.Vardenhet;
import se.inera.certificate.model.Vardgivare;
import se.inera.certificate.model.Vardkontakt;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.AktivitetType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.Aktivitetskod;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.ArbetsformagaNedsattningType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.ArbetsformagaType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.FunktionstillstandType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.LakarutlatandeType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.MedicinsktTillstandType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.Nedsattningsgrad;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.ReferensType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.Referenstyp;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.SysselsattningType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.TypAvSysselsattning;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.VardkontaktType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.Vardkontakttyp;
import se.inera.ifv.insuranceprocess.healthreporting.v2.EnhetType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.HosPersonalType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.PatientType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.VardgivareType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author andreaskaltenbach
 */
public class LakarutlatandeTypeToLakarutlatandeConverter {

    public static final String FK_7263 = "fk7263";

    private LakarutlatandeTypeToLakarutlatandeConverter() {
    }

    /**
     * Converts a JAXB {@link LakarutlatandeType} to a {@link Lakarutlatande}.
     */
    public static Lakarutlatande convert(LakarutlatandeType source) {
        Lakarutlatande lakarutlatande = new Lakarutlatande();

        lakarutlatande.setId(source.getLakarutlatandeId());
        lakarutlatande.setTyp(FK_7263);
        lakarutlatande.setKommentar(source.getKommentar());
        lakarutlatande.setSigneringsDatum(source.getSigneringsdatum());
        lakarutlatande.setSkickatDatum(source.getSkickatDatum());
        lakarutlatande.setVardenhet(convert(source.getSkapadAvHosPersonal().getEnhet()));
        lakarutlatande.setPatient(convert(source.getPatient()));
        lakarutlatande.setSkapadAv(convert(source.getSkapadAvHosPersonal()));
        lakarutlatande.setBedomtTillstand(convert(source.getMedicinsktTillstand()));

        if (source.getBedomtTillstand() != null) {
            lakarutlatande.setSjukdomsfarlopp(source.getBedomtTillstand().getBeskrivning());
        }

        List<Aktivitetsbegransning> aktivitetsbegransningar = new ArrayList<>();
        List<Funktionsnedsattning> funktionsnedsattningar = new ArrayList<>();
        for (FunktionstillstandType funktionstillstand : source.getFunktionstillstand()) {
            switch (funktionstillstand.getTypAvFunktionstillstand()) {
                case AKTIVITET:
                    aktivitetsbegransningar.add(convertToAktivitetsbegransning(funktionstillstand));
                    break;
                case KROPPSFUNKTION:
                    funktionsnedsattningar.add(convertToFunktionsnedsattning(funktionstillstand));
                    break;
                default:
                    break;
            }
        }
        lakarutlatande.setAktivitetsbegransningar(aktivitetsbegransningar);
        lakarutlatande.setFunktionsnedsattningar(funktionsnedsattningar);

        lakarutlatande.setAktiviteter(convertAktiviteter(source.getAktivitet()));
        lakarutlatande.setReferenser(convertReferenser(source.getReferens()));
        lakarutlatande.setVardkontakter(convertVardkontakter(source.getVardkontakt()));

        return lakarutlatande;
    }

    private static BedomtTillstand convert(MedicinsktTillstandType medicinsktTillstand) {
        BedomtTillstand bedomtTillstand = new BedomtTillstand();
        bedomtTillstand.setBeskrivning(medicinsktTillstand.getBeskrivning());

        if (medicinsktTillstand.getTillstandskod() != null) {
            bedomtTillstand.setTillstandskod(medicinsktTillstand.getTillstandskod().getCode());
        }
        return bedomtTillstand;
    }

    private static List<Vardkontakt> convertVardkontakter(List<VardkontaktType> source) {
        List<Vardkontakt> vardkontakter = new ArrayList<>();
        for (VardkontaktType vardkontakt : source) {
            vardkontakter.add(convert(vardkontakt));
        }
        return vardkontakter;
    }

    private static Vardkontakt convert(VardkontaktType source) {
        Vardkontakt vardkontakt = new Vardkontakt();
        vardkontakt.setVardkontakttyp(convert(source.getVardkontakttyp()));
        vardkontakt.setVardkontaktstid(source.getVardkontaktstid());
        return vardkontakt;
    }

    private static se.inera.certificate.model.Vardkontakttyp convert(Vardkontakttyp source) {
        switch (source) {
            case MIN_TELEFONKONTAKT_MED_PATIENTEN:
                return se.inera.certificate.model.Vardkontakttyp.MIN_TELEFONKONTAKT_MED_PATIENTEN;
            case MIN_UNDERSOKNING_AV_PATIENTEN:
                return se.inera.certificate.model.Vardkontakttyp.MIN_UNDERSOKNING_AV_PATIENTEN;
            default:
                return null;
        }
    }

    private static List<Referens> convertReferenser(List<ReferensType> source) {
        List<Referens> referenser = new ArrayList<>();
        for (ReferensType referens : source) {
            referenser.add(convert(referens));
        }
        return referenser;
    }

    private static Referens convert(ReferensType source) {
        Referens referens = new Referens();
        referens.setReferenstyp(convert(source.getReferenstyp()));
        referens.setDatum(source.getDatum());
        return referens;
    }

    private static se.inera.certificate.model.Referenstyp convert(Referenstyp source) {
        switch (source) {
            case JOURNALUPPGIFTER:
                return se.inera.certificate.model.Referenstyp.JOURNALUPPGIFTER;
            case ANNAT:
                return se.inera.certificate.model.Referenstyp.ANNAT;
            default:
                return null;
        }
    }

    private static List<Aktivitet> convertAktiviteter(List<AktivitetType> source) {
        List<Aktivitet> aktiviteter = new ArrayList<>();
        for (AktivitetType aktivitet : source) {
            aktiviteter.add(convert(aktivitet));
        }
        return aktiviteter;
    }

    private static Aktivitet convert(AktivitetType source) {
        Aktivitet aktivitet = new Aktivitet();
        aktivitet.setBeskrivning(source.getBeskrivning());
        aktivitet.setAktivitetskod(convert(source.getAktivitetskod()));
        return aktivitet;
    }

    private static se.inera.certificate.model.Aktivitetskod convert(Aktivitetskod aktivitetskod) {
        switch (aktivitetskod) {
            case ARBETSLIVSINRIKTAD_REHABILITERING_AR_AKTUELL:
                return se.inera.certificate.model.Aktivitetskod.ARBETSLIVSINRIKTAD_REHABILITERING_AR_AKTUELL;
            case ARBETSLIVSINRIKTAD_REHABILITERING_AR_EJ_AKTUELL:
                return se.inera.certificate.model.Aktivitetskod.ARBETSLIVSINRIKTAD_REHABILITERING_AR_EJ_AKTUELL;
            case GAR_EJ_ATT_BEDOMMA_OM_ARBETSLIVSINRIKTAD_REHABILITERING_AR_AKTUELL:
                return se.inera.certificate.model.Aktivitetskod.GAR_EJ_ATT_BEDOMMA_OM_ARBETSLIVSINRIKTAD_REHABILITERING_AR_AKTUELL;
            case FORANDRAT_RESSATT_TILL_ARBETSPLATSEN_AR_AKTUELLT:
                return se.inera.certificate.model.Aktivitetskod.FORANDRAT_RESSATT_TILL_ARBETSPLATSEN_AR_AKTUELLT;
            case FORANDRAT_RESSATT_TILL_ARBETSPLATSEN_AR_EJ_AKTUELLT:
                return se.inera.certificate.model.Aktivitetskod.FORANDRAT_RESSATT_TILL_ARBETSPLATSEN_AR_EJ_AKTUELLT;
            case PLANERAD_ELLER_PAGAENDE_BEHANDLING_ELLER_ATGARD_INOM_SJUKVARDEN:
                return se.inera.certificate.model.Aktivitetskod.PLANERAD_ELLER_PAGAENDE_BEHANDLING_ELLER_ATGARD_INOM_SJUKVARDEN;
            case PLANERAD_ELLER_PAGAENDE_ANNAN_ATGARD:
                return se.inera.certificate.model.Aktivitetskod.PLANERAD_ELLER_PAGAENDE_ANNAN_ATGARD;
            case KONTAKT_MED_FORSAKRINGSKASSAN_AR_AKTUELL:
                return se.inera.certificate.model.Aktivitetskod.KONTAKT_MED_FORSAKRINGSKASSAN_AR_AKTUELL;
            case PATIENTEN_BEHOVER_FA_KONTAKT_MED_FORETAGSHALSOVARDEN:
                return se.inera.certificate.model.Aktivitetskod.PATIENTEN_BEHOVER_FA_KONTAKT_MED_FORETAGSHALSOVARDEN;
            case AVSTANGNING_ENLIGT_SM_L_PGA_SMITTA:
                return se.inera.certificate.model.Aktivitetskod.AVSTANGNING_ENLIGT_SM_L_PGA_SMITTA;
            case OVRIGT:
                return se.inera.certificate.model.Aktivitetskod.OVRIGT;
            case PATIENTEN_BEHOVER_FA_KONTAKT_MED_ARBETSFORMEDLINGEN:
                return se.inera.certificate.model.Aktivitetskod.PATIENTEN_BEHOVER_FA_KONTAKT_MED_ARBETSFORMEDLINGEN;
            default:
                return null;
        }
    }

    private static Funktionsnedsattning convertToFunktionsnedsattning(FunktionstillstandType source) {
        Funktionsnedsattning funktionsnedsattning = new Funktionsnedsattning();
        funktionsnedsattning.setBeskrivning(source.getBeskrivning());
        return funktionsnedsattning;
    }

    private static Aktivitetsbegransning convertToAktivitetsbegransning(FunktionstillstandType funktionstillstand) {
        Aktivitetsbegransning aktivitetsbegransning = new Aktivitetsbegransning();
        aktivitetsbegransning.setBeskrivning(funktionstillstand.getBeskrivning());
        aktivitetsbegransning.setArbetsformaga(convert(funktionstillstand.getArbetsformaga()));
        return aktivitetsbegransning;
    }

    private static Arbetsformaga convert(ArbetsformagaType source) {
        Arbetsformaga arbetsformaga = new Arbetsformaga();

        arbetsformaga.setMotivering(source.getMotivering());

        if (source.getArbetsuppgift() != null) {
            arbetsformaga.setArbetsuppgift(source.getArbetsuppgift().getTypAvArbetsuppgift());
        }
        arbetsformaga.setPrognosangivelse(convert(source.getPrognosangivelse()));
        arbetsformaga.setSysselsattningar(convert(source.getSysselsattning()));
        arbetsformaga.setArbetsformagaNedsattningar(convertNedsattning(source.getArbetsformagaNedsattning()));
        return arbetsformaga;
    }

    private static List<ArbetsformagaNedsattning> convertNedsattning(List<ArbetsformagaNedsattningType> source) {
        List<ArbetsformagaNedsattning> arbetsformagaNedsattningar = new ArrayList<>();
        for (ArbetsformagaNedsattningType arbetsformagaNedsattning : source) {
            arbetsformagaNedsattningar.add(convert(arbetsformagaNedsattning));
        }
        return arbetsformagaNedsattningar;
    }

    private static ArbetsformagaNedsattning convert(ArbetsformagaNedsattningType source) {
        ArbetsformagaNedsattning nedsattning = new ArbetsformagaNedsattning();
        nedsattning.setVaraktighetFrom(source.getVaraktighetFrom());
        nedsattning.setVaraktighetTom(source.getVaraktighetTom());
        nedsattning.setNedsattningsgrad(convert(source.getNedsattningsgrad()));
        return nedsattning;
    }

    private static se.inera.certificate.model.Nedsattningsgrad convert(Nedsattningsgrad nedsattningsgrad) {
        switch (nedsattningsgrad) {
            case HELT_NEDSATT:
                return se.inera.certificate.model.Nedsattningsgrad.HELT_NEDSATT;
            case NEDSATT_MED_3_4:
                return se.inera.certificate.model.Nedsattningsgrad.NEDSATT_MED_3_4;
            case NEDSATT_MED_1_2:
                return se.inera.certificate.model.Nedsattningsgrad.NEDSATT_MED_1_2;
            case NEDSATT_MED_1_4:
                return se.inera.certificate.model.Nedsattningsgrad.NEDSATT_MED_1_4;
            default:
                return null;
        }
    }

    private static List<Sysselsattning> convert(List<SysselsattningType> source) {
        List<Sysselsattning> sysselsattningar = new ArrayList<>();
        for (SysselsattningType sysselsattning : source) {
            sysselsattningar.add(convert(sysselsattning.getTypAvSysselsattning()));
        }
        return sysselsattningar;
    }

    private static Sysselsattning convert(TypAvSysselsattning source) {
        switch (source) {
            case NUVARANDE_ARBETE:
                return Sysselsattning.NUVARANDE_ARBETE;
            case ARBETSLOSHET:
                return Sysselsattning.ARBETSLOSHET;
            case FORALDRALEDIGHET:
                return Sysselsattning.FORALDRALEDIGHET;
            default:
                return null;
        }
    }

    private static Prognosangivelse convert(se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.Prognosangivelse source) {
        switch (source) {
            case ATERSTALLAS_HELT:
                return Prognosangivelse.ATERSTALLAS_HELT;
            case ATERSTALLAS_DELVIS:
                return Prognosangivelse.ATERSTALLAS_DELVIS;
            case INTE_ATERSTALLAS:
                return Prognosangivelse.INTE_ATERSTALLAS;
            case DET_GAR_INTE_ATT_BEDOMMA:
                return Prognosangivelse.DET_GAR_INTE_ATT_BEDOMMA;
            default:
                return null;
        }
    }

    private static HosPersonal convert(HosPersonalType source) {
        HosPersonal hosPersonal = new HosPersonal();
        hosPersonal.setId(source.getPersonalId().getExtension());
        hosPersonal.setNamn(source.getFullstandigtNamn());
        return hosPersonal;
    }

    private static Patient convert(PatientType source) {
        Patient patient = new Patient();
        patient.setId(source.getPersonId().getExtension());
        // TODO - how should we split names
        patient.setFornamn(source.getFullstandigtNamn());
        return patient;
    }

    private static Vardenhet convert(EnhetType enhet) {
        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setId(enhet.getEnhetsId().getExtension());
        if (enhet.getArbetsplatskod() != null) {
            vardenhet.setArbetsplatskod(enhet.getArbetsplatskod().getExtension());
        }
        vardenhet.setNamn(enhet.getEnhetsnamn());

        vardenhet.setPostadress(enhet.getPostadress());
        vardenhet.setPostnummer(enhet.getPostnummer());
        vardenhet.setPostort(enhet.getPostort());
        vardenhet.setTelefonnummer(enhet.getTelefonnummer());
        vardenhet.setEpost(enhet.getEpost());

        vardenhet.setVardgivare(convert(enhet.getVardgivare()));
        return vardenhet;
    }

    private static Vardgivare convert(VardgivareType source) {
        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setId(source.getVardgivareId().getExtension());
        vardgivare.setNamn(source.getVardgivarnamn());
        return vardgivare;
    }
}
