package se.inera.certificate.integration.converter;

import iso.v21090.dt.v1.CD;
import iso.v21090.dt.v1.II;

import java.util.List;

import se.inera.certificate.model.Aktivitet;
import se.inera.certificate.model.Aktivitetsbegransning;
import se.inera.certificate.model.Arbetsformaga;
import se.inera.certificate.model.ArbetsformagaNedsattning;
import se.inera.certificate.model.BedomtTillstand;
import se.inera.certificate.model.Funktionsnedsattning;
import se.inera.certificate.model.HosPersonal;
import se.inera.certificate.model.Lakarutlatande;
import se.inera.certificate.model.Patient;
import se.inera.certificate.model.Referens;
import se.inera.certificate.model.Sysselsattning;
import se.inera.certificate.model.Vardenhet;
import se.inera.certificate.model.Vardgivare;
import se.inera.certificate.model.Vardkontakt;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.AktivitetType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.Aktivitetskod;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.ArbetsformagaNedsattningType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.ArbetsformagaType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.ArbetsuppgiftType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.BedomtTillstandType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.FunktionstillstandType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.LakarutlatandeType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.MedicinsktTillstandType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.Nedsattningsgrad;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.Prognosangivelse;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.ReferensType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.Referenstyp;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.SysselsattningType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.TypAvFunktionstillstand;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.TypAvSysselsattning;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.VardkontaktType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.Vardkontakttyp;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.EnhetType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.HosPersonalType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.PatientType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.VardgivareType;

public final class LakarutlatandeToRegisterMedicalCertificate {

    private LakarutlatandeToRegisterMedicalCertificate() {
    }

    public static RegisterMedicalCertificateType getJaxbObject(Lakarutlatande lakarutlatande) {
        try {
            RegisterMedicalCertificateType register = new RegisterMedicalCertificateType();
            register.setLakarutlatande(new LakarutlatandeType());
            register.getLakarutlatande().setLakarutlatandeId(lakarutlatande.getId());
            register.getLakarutlatande().setTypAvUtlatande("Läkarintyg enligt 3 kap, 8 § lagen (1962:381) om allmän försäkring");
            register.getLakarutlatande().setKommentar(lakarutlatande.getKommentar());
            register.getLakarutlatande().setSigneringsdatum(lakarutlatande.getSigneringsDatum());
            register.getLakarutlatande().setSkickatDatum(lakarutlatande.getSkickatDatum());
            register.getLakarutlatande().setPatient(toJaxb(lakarutlatande.getPatient()));
            register.getLakarutlatande().setSkapadAvHosPersonal(toJaxb(lakarutlatande.getSkapadAv()));
            register.getLakarutlatande().getSkapadAvHosPersonal().setEnhet(toJaxb(lakarutlatande.getVardenhet()));
            register.getLakarutlatande().setBedomtTillstand(sjukdomsforloppToJaxb(lakarutlatande.getSjukdomsforlopp()));
            register.getLakarutlatande().setMedicinsktTillstand(toJaxb(lakarutlatande.getBedomtTillstand()));
            addAktivitet(register.getLakarutlatande().getAktivitet(), lakarutlatande.getAktiviteter());
            addReferens(register.getLakarutlatande().getReferens(), lakarutlatande.getReferenser());
            addVardkontakt(register.getLakarutlatande().getVardkontakt(), lakarutlatande.getVardkontakter());
            addFunktionstillstand(register.getLakarutlatande().getFunktionstillstand(), lakarutlatande.getFunktionsnedsattningar());
            addAktivitestbegransningar(register.getLakarutlatande().getFunktionstillstand(), lakarutlatande.getAktivitetsbegransningar());
            return register;
        } catch (Exception e) {
            // TODO: Kasta annat undantag! /PW
            throw new RuntimeException(e);
        }
    }

    private static void addFunktionstillstand(List<FunktionstillstandType> target, List<Funktionsnedsattning> source) {
        if (isNull(source)) {
            return;
        }
        for (Funktionsnedsattning nedsattning: source) {
            target.add(toJaxb(nedsattning));
        }
    }

    private static FunktionstillstandType toJaxb(Funktionsnedsattning nedsattning) {
        FunktionstillstandType funktionstillstandType = new FunktionstillstandType();
        funktionstillstandType.setTypAvFunktionstillstand(TypAvFunktionstillstand.KROPPSFUNKTION);
        funktionstillstandType.setBeskrivning(nedsattning.getBeskrivning());
        return funktionstillstandType;
    }

    private static void addAktivitestbegransningar(List<FunktionstillstandType> target, List<Aktivitetsbegransning> source) {
        if (isNull(source)) {
            return;
        }
        for (Aktivitetsbegransning begransning: source) {
            target.add(toJaxb(begransning));
        }
    }

    private static FunktionstillstandType toJaxb(Aktivitetsbegransning source) {
        FunktionstillstandType funktionstillstandType = new FunktionstillstandType();
        funktionstillstandType.setTypAvFunktionstillstand(TypAvFunktionstillstand.AKTIVITET);
        funktionstillstandType.setBeskrivning(source.getBeskrivning());
        funktionstillstandType.setArbetsformaga(taJaxb(source.getArbetsformaga()));
        return funktionstillstandType;
    }

    private static ArbetsformagaType taJaxb(Arbetsformaga source) {
        ArbetsformagaType arbetsformagaType = new ArbetsformagaType();
        arbetsformagaType.setMotivering(source.getMotivering());
        arbetsformagaType.setPrognosangivelse(toJaxb(source.getPrognosangivelse()));
        arbetsformagaType.setArbetsuppgift(getArbetsuppgift(source));
        addArbetsformagaNedsattning(arbetsformagaType.getArbetsformagaNedsattning(), source.getArbetsformagaNedsattningar());
        addSysselsattning(arbetsformagaType.getSysselsattning(), source.getSysselsattningar());
        return arbetsformagaType;
    }

    private static void addSysselsattning(List<SysselsattningType> target, List<Sysselsattning> source) {
        if (source == null) {
            return;
        }
        for (Sysselsattning sysselsattning: source) {
            target.add(toJaxb(sysselsattning));
        }
    }

    private static SysselsattningType toJaxb(Sysselsattning source) {
        SysselsattningType sysselsattningType = new SysselsattningType();
        switch (source) {
        case ARBETSLOSHET: sysselsattningType.setTypAvSysselsattning(TypAvSysselsattning.ARBETSLOSHET); break;
        case FORALDRALEDIGHET: sysselsattningType.setTypAvSysselsattning(TypAvSysselsattning.FORALDRALEDIGHET); break;
        case NUVARANDE_ARBETE: sysselsattningType.setTypAvSysselsattning(TypAvSysselsattning.NUVARANDE_ARBETE); break;
        default: throw new IllegalArgumentException("Can not convert " + source);
        }
        return sysselsattningType;
    }

    private static void addArbetsformagaNedsattning(List<ArbetsformagaNedsattningType> target, List<ArbetsformagaNedsattning> source) {
        if (source == null) {
            return;
        }
        for (ArbetsformagaNedsattning nedsattning: source) {
            target.add(toJaxb(nedsattning));
        }
    }

    private static ArbetsformagaNedsattningType toJaxb(ArbetsformagaNedsattning source) {
        ArbetsformagaNedsattningType arbetsformagaNedsattningType = new ArbetsformagaNedsattningType();
        arbetsformagaNedsattningType.setNedsattningsgrad(toJaxb(source.getNedsattningsgrad()));
        arbetsformagaNedsattningType.setVaraktighetFrom(source.getVaraktighetFrom());
        arbetsformagaNedsattningType.setVaraktighetTom(source.getVaraktighetTom());
        return arbetsformagaNedsattningType;
    }

    private static Nedsattningsgrad toJaxb(se.inera.certificate.model.Nedsattningsgrad source) {
        Nedsattningsgrad nedsattningsgrad;
        switch(source) {
        case HELT_NEDSATT: nedsattningsgrad = Nedsattningsgrad.HELT_NEDSATT; break;
        case NEDSATT_MED_1_2: nedsattningsgrad = Nedsattningsgrad.NEDSATT_MED_1_2; break;
        case NEDSATT_MED_1_4: nedsattningsgrad = Nedsattningsgrad.NEDSATT_MED_1_4; break;
        case NEDSATT_MED_3_4: nedsattningsgrad = Nedsattningsgrad.NEDSATT_MED_3_4; break;
        default: throw new IllegalArgumentException("Can not convert " + source);
        }
        return nedsattningsgrad;
    }

    private static ArbetsuppgiftType getArbetsuppgift(Arbetsformaga source) {
        ArbetsuppgiftType arbetsuppgiftType = new ArbetsuppgiftType();
        arbetsuppgiftType.setTypAvArbetsuppgift(source.getArbetsuppgift());
        return arbetsuppgiftType;
    }

    private static Prognosangivelse toJaxb(se.inera.certificate.model.Prognosangivelse source) {
        if (source == null) {
            return null;
        }
        Prognosangivelse prognosangivelse;
        switch (source) {
        case ATERSTALLAS_DELVIS: prognosangivelse = Prognosangivelse.ATERSTALLAS_DELVIS; break;
        case ATERSTALLAS_HELT: prognosangivelse = Prognosangivelse.ATERSTALLAS_HELT; break;
        case DET_GAR_INTE_ATT_BEDOMMA: prognosangivelse = Prognosangivelse.DET_GAR_INTE_ATT_BEDOMMA; break;
        case INTE_ATERSTALLAS: prognosangivelse = Prognosangivelse.INTE_ATERSTALLAS; break;
        default: throw new IllegalArgumentException("Can not convert " + source);
        }
        return prognosangivelse;
    }

    private static void addVardkontakt(List<VardkontaktType> target, List<Vardkontakt> source) {
        if (isNull(source)) {
            return;
        }
        for (Vardkontakt vardkontakt: source) {
            target.add(toJaxb(vardkontakt));
        }
    }

    private static VardkontaktType toJaxb(Vardkontakt source) {
        VardkontaktType vardkontaktType = new VardkontaktType();
        vardkontaktType.setVardkontaktstid(source.getVardkontaktstid());
        switch (source.getVardkontakttyp()) {
        case MIN_TELEFONKONTAKT_MED_PATIENTEN: vardkontaktType.setVardkontakttyp(Vardkontakttyp.MIN_TELEFONKONTAKT_MED_PATIENTEN); break;
        case MIN_UNDERSOKNING_AV_PATIENTEN: vardkontaktType.setVardkontakttyp(Vardkontakttyp.MIN_UNDERSOKNING_AV_PATIENTEN); break;
        default: throw new IllegalArgumentException("Can not convert " + source.getVardkontakttyp());
        }
        return vardkontaktType;
    }

    private static void addReferens(List<ReferensType> target, List<Referens> source) {
        if (isNull(source)) {
            return;
        }
        for (Referens referens: source) {
            target.add(toJaxb(referens));
        }
    }

    private static ReferensType toJaxb(Referens source) {
        ReferensType referensType = new ReferensType();
        referensType.setDatum(source.getDatum());
        switch (source.getReferenstyp()) {
        case JOURNALUPPGIFTER: referensType.setReferenstyp(Referenstyp.JOURNALUPPGIFTER); break;
        case ANNAT: referensType.setReferenstyp(Referenstyp.ANNAT); break;
        default: throw new IllegalArgumentException("Can not convert " + source.getReferenstyp());
        }
        return referensType;
    }

    private static void addAktivitet(List<AktivitetType> target, List<Aktivitet> source) {
        if (isNull(source)) {
            return;
        }
        for (Aktivitet aktivitet: source) {
            target.add(toJaxb(aktivitet));
        }
    }

    private static boolean isNull(Object o) {
        return o == null;
    }

    private static AktivitetType toJaxb(Aktivitet source) {
        AktivitetType aktivitet;
        switch (source.getAktivitetskod()) {
        case ARBETSLIVSINRIKTAD_REHABILITERING_AR_AKTUELL: aktivitet = createAktivitetType(Aktivitetskod.ARBETSLIVSINRIKTAD_REHABILITERING_AR_AKTUELL); break;
        case ARBETSLIVSINRIKTAD_REHABILITERING_AR_EJ_AKTUELL: aktivitet = createAktivitetType(Aktivitetskod.ARBETSLIVSINRIKTAD_REHABILITERING_AR_EJ_AKTUELL); break;
        case AVSTANGNING_ENLIGT_SM_L_PGA_SMITTA: aktivitet = createAktivitetType(Aktivitetskod.AVSTANGNING_ENLIGT_SM_L_PGA_SMITTA); break;
        case FORANDRAT_RESSATT_TILL_ARBETSPLATSEN_AR_AKTUELLT: aktivitet = createAktivitetType(Aktivitetskod.FORANDRAT_RESSATT_TILL_ARBETSPLATSEN_AR_AKTUELLT); break;
        case FORANDRAT_RESSATT_TILL_ARBETSPLATSEN_AR_EJ_AKTUELLT: aktivitet = createAktivitetType(Aktivitetskod.FORANDRAT_RESSATT_TILL_ARBETSPLATSEN_AR_EJ_AKTUELLT); break;
        case GAR_EJ_ATT_BEDOMMA_OM_ARBETSLIVSINRIKTAD_REHABILITERING_AR_AKTUELL: aktivitet = createAktivitetType(Aktivitetskod.GAR_EJ_ATT_BEDOMMA_OM_ARBETSLIVSINRIKTAD_REHABILITERING_AR_AKTUELL); break;
        case KONTAKT_MED_FORSAKRINGSKASSAN_AR_AKTUELL: aktivitet = createAktivitetType(Aktivitetskod.KONTAKT_MED_FORSAKRINGSKASSAN_AR_AKTUELL); break;
        case OVRIGT: aktivitet = createAktivitetType(Aktivitetskod.OVRIGT, source.getBeskrivning()); break;
        case PATIENTEN_BEHOVER_FA_KONTAKT_MED_ARBETSFORMEDLINGEN: aktivitet = createAktivitetType(Aktivitetskod.PATIENTEN_BEHOVER_FA_KONTAKT_MED_ARBETSFORMEDLINGEN); break;
        case PATIENTEN_BEHOVER_FA_KONTAKT_MED_FORETAGSHALSOVARDEN: aktivitet = createAktivitetType(Aktivitetskod.PATIENTEN_BEHOVER_FA_KONTAKT_MED_FORETAGSHALSOVARDEN); break;
        case PLANERAD_ELLER_PAGAENDE_ANNAN_ATGARD: aktivitet = createAktivitetType(Aktivitetskod.PLANERAD_ELLER_PAGAENDE_ANNAN_ATGARD, source.getBeskrivning()); break;
        case PLANERAD_ELLER_PAGAENDE_BEHANDLING_ELLER_ATGARD_INOM_SJUKVARDEN: aktivitet = createAktivitetType(Aktivitetskod.PLANERAD_ELLER_PAGAENDE_BEHANDLING_ELLER_ATGARD_INOM_SJUKVARDEN, source.getBeskrivning()); break;
        default:
            throw new IllegalArgumentException("Can not convert " + source.getAktivitetskod());
        }
        return aktivitet;
    }

    private static AktivitetType createAktivitetType(Aktivitetskod kod, String beskrivning) {
        AktivitetType aktivitet = new AktivitetType();
        aktivitet.setAktivitetskod(kod);
        aktivitet.setBeskrivning(beskrivning);
        return aktivitet;
    }

    private static AktivitetType createAktivitetType(Aktivitetskod kod) {
        AktivitetType aktivitet = new AktivitetType();
        aktivitet.setAktivitetskod(kod);
        return aktivitet;
    }

    private static MedicinsktTillstandType toJaxb(BedomtTillstand bedomtTillstand) {
        MedicinsktTillstandType tillstand = new MedicinsktTillstandType();
        tillstand.setBeskrivning(bedomtTillstand.getBeskrivning());
        CD cd = new CD();
        cd.setCode(bedomtTillstand.getTillstandskod());
        cd.setCodeSystem("ICD-10");
        return tillstand;
    }


    private static BedomtTillstandType sjukdomsforloppToJaxb(String source) {
        BedomtTillstandType tillstand = new BedomtTillstandType();
        tillstand.setBeskrivning(source);
        return tillstand;
    }


    private static EnhetType toJaxb(Vardenhet source) {
        EnhetType enhet = new EnhetType();
        enhet.setEnhetsnamn(source.getNamn());
        enhet.setPostadress(source.getPostadress());
        enhet.setPostnummer(source.getPostnummer());
        enhet.setPostort(source.getPostort());
        enhet.setTelefonnummer(source.getTelefonnummer());
        enhet.setEpost(source.getEpost());

        enhet.setArbetsplatskod(arbetsplatskodToJaxb(source.getArbetsplatskod()));
        enhet.setEnhetsId(enhetsidToJaxb(source.getId()));

        enhet.setVardgivare(toJaxb(source.getVardgivare()));
        return enhet;
    }

    private static VardgivareType toJaxb(Vardgivare source) {
        VardgivareType vardgivare = new VardgivareType();
        vardgivare.setVardgivarnamn(source.getNamn());
        vardgivare.setVardgivareId(vardgivareidToJaxb(source.getId()));
        return vardgivare;
    }

    private static II vardgivareidToJaxb(String id) {
        return ii("1.2.752.129.2.1.4.1", id);
    }

    private static II enhetsidToJaxb(String id) {
        return ii("1.2.752.129.2.1.4.1", id);
    }

    private static II arbetsplatskodToJaxb(String arbetsplatskod) {
        return ii("1.2.752.29.4.71", arbetsplatskod);
    }

    private static HosPersonalType toJaxb(HosPersonal skapadAv) {
        HosPersonalType personal = new HosPersonalType();
        personal.setForskrivarkod(skapadAv.getForskrivarkod());
        personal.setFullstandigtNamn(skapadAv.getNamn());
        personal.setPersonalId(hosPersonalIdToJaxb(skapadAv.getId()));
        return personal;
    }

    private static II hosPersonalIdToJaxb(String id) {
        return ii("1.2.752.129.2.1.4.1", id);
    }

    private static PatientType toJaxb(Patient source) {
        PatientType patient = new PatientType();
        patient.setFullstandigtNamn(extractFullstandigtNamn(source));
        patient.setPersonId(personnummerToII(source.getId()));
        return patient;
    }

    private static II ii(String root, String extension) {
        II ii = new II();
        ii.setRoot(root);
        ii.setExtension(extension);
        return ii;
    }
    private static II personnummerToII(String id) {
        // TODO Always sets as personnummer, inte samordningsnummer
        return ii("1.2.752.129.2.1.3.1", id);
    }

    private static String extractFullstandigtNamn(Patient source) {
        if (notEmpty(source.getFullstandigtNamn())) {
            return source.getFullstandigtNamn();
        }
        return source.getFornamn() + " " + source.getEfternamn();
    }

    private static boolean notEmpty(String source) {
        return source != null && source.length() > 0;
    }

}
