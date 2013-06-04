package se.inera.certificate.integration.converter;

import se.inera.certificate.integration.v1.AktivitetsbegransningType;
import se.inera.certificate.integration.v1.ArbetsformagaType;
import se.inera.certificate.integration.v1.BedomtTillstandType;
import se.inera.certificate.integration.v1.FunktionsnedsattningType;
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
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.LakarutlatandeType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author andreaskaltenbach
 */
public class LakarutlatandeJaxbToLakarutlatandeConverter {

    private LakarutlatandeJaxbToLakarutlatandeConverter() {
    }

    /**
     * Converts a JAXB {@link LakarutlatandeType} to a {@link Lakarutlatande}.
     */
    public static Lakarutlatande convert(se.inera.certificate.integration.v1.Lakarutlatande value) {
        Lakarutlatande lakarutlatande = new Lakarutlatande();

        lakarutlatande.setId(value.getId());
        lakarutlatande.setTyp(value.getTyp());
        lakarutlatande.setKommentar(value.getKommentar());
        lakarutlatande.setSigneringsDatum(value.getSigneringsDatum());
        lakarutlatande.setSkickatDatum(value.getSkickatDatum());
        lakarutlatande.setVardenhet(convert(value.getVardenhet()));
        lakarutlatande.setPatient(convert(value.getPatient()));
        lakarutlatande.setSkapadAv(convert(value.getSkapadAv()));
        lakarutlatande.setBedomtTillstand(convert(value.getBedomtTillstand()));

        lakarutlatande.setSjukdomsfarlopp(value.getSjukdomsforlopp());

        lakarutlatande.setAktivitetsbegransningar(convertAktivitetsbegransning(value.getAktivitetsbegransnings()));
        lakarutlatande.setFunktionsnedsattningar(convertFunktionsnedsattning(value.getFunktionsnedsattnings()));

        lakarutlatande.setAktiviteter(convertAktiviteter(value.getAktivitets()));
        lakarutlatande.setReferenser(convertReferenser(value.getReferens()));
        lakarutlatande.setVardkontakter(convertVardkontakter(value.getVardkontakts()));

        return lakarutlatande;
    }

    private static List<Aktivitetsbegransning> convertAktivitetsbegransning(List<AktivitetsbegransningType> source) {
        List<Aktivitetsbegransning> begransningar = new ArrayList<>();
        for (AktivitetsbegransningType aktivitetsbegransning : source) {
            begransningar.add(convert(aktivitetsbegransning));
        }
        return begransningar;
    }

    private static Aktivitetsbegransning convert(AktivitetsbegransningType source) {
        Aktivitetsbegransning aktivitetsbegransning = new Aktivitetsbegransning();
        aktivitetsbegransning.setBeskrivning(source.getBeskrivning());
        Arbetsformaga arbetsformaga = convert(source.getArbetsformaga());
        aktivitetsbegransning.setArbetsformaga(arbetsformaga);
        return aktivitetsbegransning;
    }

    private static Arbetsformaga convert(ArbetsformagaType source) {
        Arbetsformaga arbetsformaga = new Arbetsformaga();
        arbetsformaga.setArbetsuppgift(source.getArbetsuppgift());
        arbetsformaga.setArbetsformagaNedsattningar(convertArbetsformaganedsattningar(source.getArbetsformagaNedsattnings()));
        arbetsformaga.setPrognosangivelse(convert(source.getPrognosangivelse()));
        arbetsformaga.setSysselsattningar(convertSysselsattnings(source.getSysselsattnings()));
        arbetsformaga.setMotivering(source.getMotivering());
        return arbetsformaga;
    }

    private static List<Sysselsattning> convertSysselsattnings(List<se.inera.certificate.integration.v1.Sysselsattning> source) {
        List<Sysselsattning> sysselsattnings = new ArrayList<>();
        for (se.inera.certificate.integration.v1.Sysselsattning sysselsattning : source) {
            sysselsattnings.add(convert(sysselsattning));
        }
        return sysselsattnings;
    }

    private static Sysselsattning convert(se.inera.certificate.integration.v1.Sysselsattning source) {
        switch (source) {
        case NUVARANDE_ARBETE: return Sysselsattning.NUVARANDE_ARBETE;
        case FORALDRALEDIGHET: return Sysselsattning.FORALDRALEDIGHET;
        case ARBETSLOSHET: return Sysselsattning.ARBETSLOSHET;
        }
        throw new IllegalArgumentException("Can not convert " + source);
    }

    private static Prognosangivelse convert(se.inera.certificate.integration.v1.Prognosangivelse source) {
        switch(source){
        case ATERSTALLAS_HELT: return Prognosangivelse.ATERSTALLAS_HELT;
        case ATERSTALLAS_DELVIS: return Prognosangivelse.ATERSTALLAS_DELVIS;
        case INTE_ATERSTALLAS: return Prognosangivelse.INTE_ATERSTALLAS;
        case DET_GAR_INTE_ATT_BEDOMMA: return Prognosangivelse.DET_GAR_INTE_ATT_BEDOMMA;        
        }
        throw new IllegalArgumentException("Can not convert " + source);
    }

    private static List<ArbetsformagaNedsattning> convertArbetsformaganedsattningar(List<se.inera.certificate.integration.v1.ArbetsformagaNedsattningType> source) {
        List<ArbetsformagaNedsattning> nedsattningar = new ArrayList<>();
        for (se.inera.certificate.integration.v1.ArbetsformagaNedsattningType arbetsformagaNedsattningType : source) {
            ArbetsformagaNedsattning arbetsformagaNedsattning = new ArbetsformagaNedsattning();
            arbetsformagaNedsattning.setNedsattningsgrad(convert(arbetsformagaNedsattningType.getNedsattningsgrad()));
            arbetsformagaNedsattning.setVaraktighetFrom(arbetsformagaNedsattningType.getVaraktighetFrom());
            arbetsformagaNedsattning.setVaraktighetTom(arbetsformagaNedsattningType.getVaraktighetTom());
            nedsattningar .add(arbetsformagaNedsattning);
        }
        return nedsattningar;
    }

    private static List<Funktionsnedsattning> convertFunktionsnedsattning(List<FunktionsnedsattningType> source) {
        List<Funktionsnedsattning> nedsattningar = new ArrayList<>();
        for (FunktionsnedsattningType funktionsnedsattningType : source) {
            nedsattningar.add(convert(funktionsnedsattningType));
        }
        return nedsattningar ;
    }
    
    private static Funktionsnedsattning convert(FunktionsnedsattningType source) {
        Funktionsnedsattning funktionsnedsattning = new Funktionsnedsattning();
        funktionsnedsattning.setBeskrivning(source.getBeskrivning());
        return funktionsnedsattning;
    }

    private static BedomtTillstand convert(BedomtTillstandType source) {
        BedomtTillstand tillstand = new BedomtTillstand();
        tillstand.setBeskrivning(source.getBeskrivning());
        tillstand.setTillstandskod(source.getTillstandskod());
        return tillstand;
    }

    private static List<Vardkontakt> convertVardkontakter(List<se.inera.certificate.integration.v1.VardkontaktType> list) {
        List<Vardkontakt> vardkontakter = new ArrayList<>();
        for (se.inera.certificate.integration.v1.VardkontaktType vardkontakt : list) {
            vardkontakter.add(convert(vardkontakt));
        }
        return vardkontakter;
    }

    private static Vardkontakt convert(se.inera.certificate.integration.v1.VardkontaktType source) {
        Vardkontakt vardkontakt = new Vardkontakt();
        vardkontakt.setVardkontakttyp(convert(source.getVardkontakttyp()));
        vardkontakt.setVardkontaktstid(source.getVardkontaktstid());
        return vardkontakt;
    }

    private static se.inera.certificate.model.Vardkontakttyp convert(se.inera.certificate.integration.v1.Vardkontakttyp source) {
        switch (source) {
            case MIN_TELEFONKONTAKT_MED_PATIENTEN:
                return se.inera.certificate.model.Vardkontakttyp.MIN_TELEFONKONTAKT_MED_PATIENTEN;
            case MIN_UNDERSOKNING_AV_PATIENTEN:
                return se.inera.certificate.model.Vardkontakttyp.MIN_UNDERSOKNING_AV_PATIENTEN;
            default:
                return null;
        }
    }

    private static List<Referens> convertReferenser(List<se.inera.certificate.integration.v1.ReferensType> list) {
        List<Referens> referenser = new ArrayList<>();
        for (se.inera.certificate.integration.v1.ReferensType referens : list) {
            referenser.add(convert(referens));
        }
        return referenser;
    }

    private static Referens convert(se.inera.certificate.integration.v1.ReferensType source) {
        Referens referens = new Referens();
        referens.setReferenstyp(convert(source.getReferenstyp()));
        referens.setDatum(source.getDatum());
        return referens;
    }

    private static se.inera.certificate.model.Referenstyp convert(se.inera.certificate.integration.v1.Referenstyp source) {
        switch (source) {
            case JOURNALUPPGIFTER:
                return se.inera.certificate.model.Referenstyp.JOURNALUPPGIFTER;
            case ANNAT:
                return se.inera.certificate.model.Referenstyp.ANNAT;
            default:
                return null;
        }
    }

    private static List<Aktivitet> convertAktiviteter(List<se.inera.certificate.integration.v1.AktivitetType> list) {
        List<Aktivitet> aktiviteter = new ArrayList<>();
        for (se.inera.certificate.integration.v1.AktivitetType aktivitet : list) {
            aktiviteter.add(convert(aktivitet));
        }
        return aktiviteter;
    }

    private static Aktivitet convert(se.inera.certificate.integration.v1.AktivitetType source) {
        Aktivitet aktivitet = new Aktivitet();
        aktivitet.setBeskrivning(source.getBeskrivning());
        aktivitet.setAktivitetskod(convert(source.getAktivitetskod()));
        return aktivitet;
    }

    private static se.inera.certificate.model.Aktivitetskod convert(se.inera.certificate.integration.v1.Aktivitetskod aktivitetskod) {
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

    private static se.inera.certificate.model.Nedsattningsgrad convert(se.inera.certificate.integration.v1.Nedsattningsgrad nedsattningsgrad) {
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

    private static HosPersonal convert(se.inera.certificate.integration.v1.HosPersonalType source) {
        HosPersonal hosPersonal = new HosPersonal();
        hosPersonal.setId(source.getId());
        hosPersonal.setNamn(source.getNamn());
        return hosPersonal;
    }

    private static Patient convert(se.inera.certificate.integration.v1.PatientType source) {
        Patient patient = new Patient();
        patient.setId(source.getId());
        patient.setFornamn(source.getFornamn());
        patient.setEfternamn(source.getEfternamn());
        patient.setMellannamn(source.getMellannamn());
        patient.setTilltalsnamn(source.getTilltalsnamn());
        return patient;
    }

    private static Vardenhet convert(se.inera.certificate.integration.v1.VardenhetType enhet) {
        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setId(enhet.getId());
        vardenhet.setNamn(enhet.getNamn());
        vardenhet.setArbetsplatskod(enhet.getArbetsplatskod());
        vardenhet.setPostadress(enhet.getPostadress());
        vardenhet.setPostnummer(enhet.getPostnummer());
        vardenhet.setPostort(enhet.getPostort());
        vardenhet.setEpost(enhet.getEpost());
        vardenhet.setTelefonnummer(enhet.getTelefonnummer());
        vardenhet.setVardgivare(convert(enhet.getVardgivare()));
        return vardenhet;
    }

    private static Vardgivare convert(se.inera.certificate.integration.v1.VardgivareType source) {
        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setId(source.getId());
        vardgivare.setNamn(source.getNamn());
        return vardgivare;
    }
    
}
