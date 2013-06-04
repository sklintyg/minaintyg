package se.inera.certificate.integration.converter;

import static se.inera.certificate.integration.v1.Aktivitetskod.ARBETSLIVSINRIKTAD_REHABILITERING_AR_AKTUELL;
import static se.inera.certificate.integration.v1.Aktivitetskod.ARBETSLIVSINRIKTAD_REHABILITERING_AR_EJ_AKTUELL;
import static se.inera.certificate.integration.v1.Aktivitetskod.AVSTANGNING_ENLIGT_SM_L_PGA_SMITTA;
import static se.inera.certificate.integration.v1.Aktivitetskod.FORANDRAT_RESSATT_TILL_ARBETSPLATSEN_AR_AKTUELLT;
import static se.inera.certificate.integration.v1.Aktivitetskod.FORANDRAT_RESSATT_TILL_ARBETSPLATSEN_AR_EJ_AKTUELLT;
import static se.inera.certificate.integration.v1.Aktivitetskod.GAR_EJ_ATT_BEDOMMA_OM_ARBETSLIVSINRIKTAD_REHABILITERING_AR_AKTUELL;
import static se.inera.certificate.integration.v1.Aktivitetskod.KONTAKT_MED_FORSAKRINGSKASSAN_AR_AKTUELL;
import static se.inera.certificate.integration.v1.Aktivitetskod.OVRIGT;
import static se.inera.certificate.integration.v1.Aktivitetskod.PATIENTEN_BEHOVER_FA_KONTAKT_MED_ARBETSFORMEDLINGEN;
import static se.inera.certificate.integration.v1.Aktivitetskod.PATIENTEN_BEHOVER_FA_KONTAKT_MED_FORETAGSHALSOVARDEN;
import static se.inera.certificate.integration.v1.Aktivitetskod.PLANERAD_ELLER_PAGAENDE_ANNAN_ATGARD;
import static se.inera.certificate.integration.v1.Aktivitetskod.PLANERAD_ELLER_PAGAENDE_BEHANDLING_ELLER_ATGARD_INOM_SJUKVARDEN;
import static se.inera.certificate.integration.v1.Referenstyp.ANNAT;
import static se.inera.certificate.integration.v1.Referenstyp.JOURNALUPPGIFTER;
import static se.inera.certificate.integration.v1.Vardkontakttyp.MIN_TELEFONKONTAKT_MED_PATIENTEN;
import static se.inera.certificate.integration.v1.Vardkontakttyp.MIN_UNDERSOKNING_AV_PATIENTEN;
import se.inera.certificate.integration.v1.AktivitetType;
import se.inera.certificate.integration.v1.AktivitetsbegransningType;
import se.inera.certificate.integration.v1.Aktivitetskod;
import se.inera.certificate.integration.v1.ArbetsformagaNedsattningType;
import se.inera.certificate.integration.v1.ArbetsformagaType;
import se.inera.certificate.integration.v1.BedomtTillstandType;
import se.inera.certificate.integration.v1.FunktionsnedsattningType;
import se.inera.certificate.integration.v1.HosPersonalType;
import se.inera.certificate.integration.v1.Lakarutlatande;
import se.inera.certificate.integration.v1.Nedsattningsgrad;
import se.inera.certificate.integration.v1.Prognosangivelse;
import se.inera.certificate.integration.v1.ReferensType;
import se.inera.certificate.integration.v1.Referenstyp;
import se.inera.certificate.integration.v1.Sysselsattning;
import se.inera.certificate.integration.v1.VardenhetType;
import se.inera.certificate.integration.v1.VardgivareType;
import se.inera.certificate.integration.v1.VardkontaktType;
import se.inera.certificate.integration.v1.Vardkontakttyp;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.FunktionstillstandType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.LakarutlatandeType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.MedicinsktTillstandType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.SysselsattningType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.EnhetType;

/**
 * @author andreaskaltenbach
 */
public final class LakarutlatandeTypeToLakarutlatandeConverter {

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
            lakarutlatande.setSjukdomsforlopp(source.getBedomtTillstand().getBeskrivning());
        }

        for (FunktionstillstandType funktionstillstand : source.getFunktionstillstand()) {
            switch (funktionstillstand.getTypAvFunktionstillstand()) {
                case AKTIVITET:
                    lakarutlatande.getAktivitetsbegransnings().add(convertToAktivitetsbegransning(funktionstillstand));
                    break;
                case KROPPSFUNKTION:
                    lakarutlatande.getFunktionsnedsattnings().add(convertToFunktionsnedsattning(funktionstillstand));
                    break;
                default:
                    break;
            }
        }

        for (se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.AktivitetType aktivitetType : source.getAktivitet()) {
            lakarutlatande.getAktivitets().add(convert(aktivitetType));
        }

        for (se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.ReferensType referensType : source.getReferens()) {
            lakarutlatande.getReferens().add(convert(referensType));
        }

        for (se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.VardkontaktType vardkontaktType : source.getVardkontakt()) {
            lakarutlatande.getVardkontakts().add(convert(vardkontaktType));
        }

        return lakarutlatande;
    }

    private static BedomtTillstandType convert(MedicinsktTillstandType medicinsktTillstand) {
        BedomtTillstandType bedomtTillstand = new BedomtTillstandType();
        bedomtTillstand.setBeskrivning(medicinsktTillstand.getBeskrivning());

        if (medicinsktTillstand.getTillstandskod() != null) {
            bedomtTillstand.setTillstandskod(medicinsktTillstand.getTillstandskod().getCode());
        }
        return bedomtTillstand;
    }

    private static VardkontaktType convert(se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.VardkontaktType source) {
        VardkontaktType vardkontakt = new VardkontaktType();
        vardkontakt.setVardkontakttyp(convert(source.getVardkontakttyp()));
        vardkontakt.setVardkontaktstid(source.getVardkontaktstid());
        return vardkontakt;
    }

    private static Vardkontakttyp convert(se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.Vardkontakttyp source) {
        switch (source) {
            case MIN_TELEFONKONTAKT_MED_PATIENTEN:
                return MIN_TELEFONKONTAKT_MED_PATIENTEN;
            case MIN_UNDERSOKNING_AV_PATIENTEN:
                return MIN_UNDERSOKNING_AV_PATIENTEN;
            default:
                return null;
        }
    }

    private static ReferensType convert(se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.ReferensType source) {
        ReferensType referens = new ReferensType();
        referens.setReferenstyp(convert(source.getReferenstyp()));
        referens.setDatum(source.getDatum());
        return referens;
    }

    private static Referenstyp convert(se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.Referenstyp source) {
        switch (source) {
            case JOURNALUPPGIFTER:
                return JOURNALUPPGIFTER;
            case ANNAT:
                return ANNAT;
            default:
                return null;
        }
    }

    private static AktivitetType convert(se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.AktivitetType source) {
        AktivitetType aktivitet = new AktivitetType();
        aktivitet.setBeskrivning(source.getBeskrivning());
        aktivitet.setAktivitetskod(convert(source.getAktivitetskod()));
        return aktivitet;
    }

    private static Aktivitetskod convert(se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.Aktivitetskod aktivitetskod) {
        switch (aktivitetskod) {
            case ARBETSLIVSINRIKTAD_REHABILITERING_AR_AKTUELL:
                return ARBETSLIVSINRIKTAD_REHABILITERING_AR_AKTUELL;
            case ARBETSLIVSINRIKTAD_REHABILITERING_AR_EJ_AKTUELL:
                return ARBETSLIVSINRIKTAD_REHABILITERING_AR_EJ_AKTUELL;
            case GAR_EJ_ATT_BEDOMMA_OM_ARBETSLIVSINRIKTAD_REHABILITERING_AR_AKTUELL:
                return GAR_EJ_ATT_BEDOMMA_OM_ARBETSLIVSINRIKTAD_REHABILITERING_AR_AKTUELL;
            case FORANDRAT_RESSATT_TILL_ARBETSPLATSEN_AR_AKTUELLT:
                return FORANDRAT_RESSATT_TILL_ARBETSPLATSEN_AR_AKTUELLT;
            case FORANDRAT_RESSATT_TILL_ARBETSPLATSEN_AR_EJ_AKTUELLT:
                return FORANDRAT_RESSATT_TILL_ARBETSPLATSEN_AR_EJ_AKTUELLT;
            case PLANERAD_ELLER_PAGAENDE_BEHANDLING_ELLER_ATGARD_INOM_SJUKVARDEN:
                return PLANERAD_ELLER_PAGAENDE_BEHANDLING_ELLER_ATGARD_INOM_SJUKVARDEN;
            case PLANERAD_ELLER_PAGAENDE_ANNAN_ATGARD:
                return PLANERAD_ELLER_PAGAENDE_ANNAN_ATGARD;
            case KONTAKT_MED_FORSAKRINGSKASSAN_AR_AKTUELL:
                return KONTAKT_MED_FORSAKRINGSKASSAN_AR_AKTUELL;
            case PATIENTEN_BEHOVER_FA_KONTAKT_MED_FORETAGSHALSOVARDEN:
                return PATIENTEN_BEHOVER_FA_KONTAKT_MED_FORETAGSHALSOVARDEN;
            case AVSTANGNING_ENLIGT_SM_L_PGA_SMITTA:
                return AVSTANGNING_ENLIGT_SM_L_PGA_SMITTA;
            case OVRIGT:
                return OVRIGT;
            case PATIENTEN_BEHOVER_FA_KONTAKT_MED_ARBETSFORMEDLINGEN:
                return PATIENTEN_BEHOVER_FA_KONTAKT_MED_ARBETSFORMEDLINGEN;
            default:
                return null;
        }
    }

    private static FunktionsnedsattningType convertToFunktionsnedsattning(FunktionstillstandType source) {
        FunktionsnedsattningType funktionsnedsattning = new FunktionsnedsattningType();
        funktionsnedsattning.setBeskrivning(source.getBeskrivning());
        return funktionsnedsattning;
    }

    private static AktivitetsbegransningType convertToAktivitetsbegransning(FunktionstillstandType funktionstillstand) {
        AktivitetsbegransningType aktivitetsbegransning = new AktivitetsbegransningType();
        aktivitetsbegransning.setBeskrivning(funktionstillstand.getBeskrivning());
        aktivitetsbegransning.setArbetsformaga(convert(funktionstillstand.getArbetsformaga()));
        return aktivitetsbegransning;
    }

    private static ArbetsformagaType convert(se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.ArbetsformagaType source) {
        ArbetsformagaType arbetsformaga = new ArbetsformagaType();

        arbetsformaga.setMotivering(source.getMotivering());

        if (source.getArbetsuppgift() != null) {
            arbetsformaga.setArbetsuppgift(source.getArbetsuppgift().getTypAvArbetsuppgift());
        }
        arbetsformaga.setPrognosangivelse(convert(source.getPrognosangivelse()));

        for (SysselsattningType sysselsattningType : source.getSysselsattning()) {
            arbetsformaga.getSysselsattnings().add(convert(sysselsattningType.getTypAvSysselsattning()));
        }

        for (se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.ArbetsformagaNedsattningType arbetsformagaNedsattningType : source.getArbetsformagaNedsattning()) {
            arbetsformaga.getArbetsformagaNedsattnings().add(convert(arbetsformagaNedsattningType));
        }

        return arbetsformaga;
    }

    private static ArbetsformagaNedsattningType convert(se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.ArbetsformagaNedsattningType source) {
        ArbetsformagaNedsattningType nedsattning = new ArbetsformagaNedsattningType();
        nedsattning.setVaraktighetFrom(source.getVaraktighetFrom());
        nedsattning.setVaraktighetTom(source.getVaraktighetTom());
        nedsattning.setNedsattningsgrad(convert(source.getNedsattningsgrad()));
        return nedsattning;
    }

    private static Nedsattningsgrad convert(se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.Nedsattningsgrad nedsattningsgrad) {
        switch (nedsattningsgrad) {
            case HELT_NEDSATT:
                return Nedsattningsgrad.HELT_NEDSATT;
            case NEDSATT_MED_3_4:
                return Nedsattningsgrad.NEDSATT_MED_3_4;
            case NEDSATT_MED_1_2:
                return Nedsattningsgrad.NEDSATT_MED_1_2;
            case NEDSATT_MED_1_4:
                return Nedsattningsgrad.NEDSATT_MED_1_4;
            default:
                return null;
        }
    }

    private static Sysselsattning convert(se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.TypAvSysselsattning source) {
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

    private static HosPersonalType convert(se.inera.ifv.insuranceprocess.healthreporting.v2.HosPersonalType source) {
        HosPersonalType hosPersonal = new HosPersonalType();
        hosPersonal.setId(source.getPersonalId().getExtension());
        hosPersonal.setNamn(source.getFullstandigtNamn());
        return hosPersonal;
    }

    private static se.inera.certificate.integration.v1.PatientType convert(se.inera.ifv.insuranceprocess.healthreporting.v2.PatientType source) {
        se.inera.certificate.integration.v1.PatientType patient = new se.inera.certificate.integration.v1.PatientType();
        patient.setId(source.getPersonId().getExtension());
        patient.setFullstandigtNamn(source.getFullstandigtNamn());
        return patient;
    }

    private static VardenhetType convert(EnhetType enhet) {
        VardenhetType vardenhet = new VardenhetType();
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

    private static VardgivareType convert(se.inera.ifv.insuranceprocess.healthreporting.v2.VardgivareType source) {
        VardgivareType vardgivare = new VardgivareType();
        vardgivare.setId(source.getVardgivareId().getExtension());
        vardgivare.setNamn(source.getVardgivarnamn());
        return vardgivare;
    }
}
