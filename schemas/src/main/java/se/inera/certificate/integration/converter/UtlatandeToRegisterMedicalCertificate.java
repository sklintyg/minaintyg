package se.inera.certificate.integration.converter;

import org.joda.time.LocalDate;
import se.inera.certificate.model.Aktivitet;
import se.inera.certificate.model.ArbetsformagaNedsattning;
import se.inera.certificate.model.HosPersonal;
import se.inera.certificate.model.Kod;
import se.inera.certificate.model.Observation;
import se.inera.certificate.model.Patient;
import se.inera.certificate.model.Referens;
import se.inera.certificate.model.Sysselsattning;
import se.inera.certificate.model.Utlatande;
import se.inera.certificate.model.Vardenhet;
import se.inera.certificate.model.Vardgivare;
import se.inera.certificate.model.Vardkontakt;
import se.inera.certificate.model.codes.Aktivitetskoder;
import se.inera.certificate.model.codes.ObservationsKoder;
import se.inera.certificate.model.codes.Prognoskoder;
import se.inera.certificate.model.codes.Referenstypkoder;
import se.inera.certificate.model.codes.Sysselsattningskoder;
import se.inera.certificate.model.codes.Vardkontakttypkoder;
import se.inera.certificate.model.util.Strings;
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

import java.util.ArrayList;
import java.util.List;

import static se.inera.certificate.integration.converter.util.IsoTypeConverter.toCD;
import static se.inera.certificate.integration.converter.util.IsoTypeConverter.toII;
import static se.inera.certificate.model.util.Iterables.addAll;

public final class UtlatandeToRegisterMedicalCertificate {

    private static final String FK7263 = "Läkarintyg enligt 3 kap, 8 § lagen (1962:381) om allmän försäkring";

    private UtlatandeToRegisterMedicalCertificate() {
    }

    public static RegisterMedicalCertificateType getJaxbObject(Utlatande utlatande) {
        RegisterMedicalCertificateType register = new RegisterMedicalCertificateType();
        register.setLakarutlatande(new LakarutlatandeType());
        register.getLakarutlatande().setLakarutlatandeId(toII(utlatande.getId()).getExtension());
        register.getLakarutlatande().setTypAvUtlatande(FK7263);

        if (utlatande.getKommentars() != null && !utlatande.getKommentars().isEmpty()) {
            register.getLakarutlatande().setKommentar(utlatande.getKommentars().get(0));
        }

        register.getLakarutlatande().setSigneringsdatum(utlatande.getSigneringsDatum());
        register.getLakarutlatande().setSkickatDatum(utlatande.getSkickatDatum());
        register.getLakarutlatande().setPatient(toJaxb(utlatande.getPatient()));
        register.getLakarutlatande().setSkapadAvHosPersonal(toJaxb(utlatande.getSkapadAv()));

        Observation sjukdomsforlopp = utlatande.findObservationByKod(ObservationsKoder.FORLOPP);
        if (sjukdomsforlopp != null) {
            register.getLakarutlatande().setBedomtTillstand(sjukdomsforloppToJaxb(sjukdomsforlopp.getBeskrivning()));
        }

        Observation diagnos = utlatande.findObservationByKategori(ObservationsKoder.DIAGNOS);
        if (diagnos != null) {
            register.getLakarutlatande().setMedicinsktTillstand(toMedicinsktTillstand(diagnos));
        }

        // Add collections with wrapped nullchecks method
        addAll(register.getLakarutlatande().getAktivitet(), convert(utlatande.getAktiviteter()));
        addAll(register.getLakarutlatande().getReferens(), convertReferenser(utlatande.getReferenser()));
        addAll(register.getLakarutlatande().getVardkontakt(), convertVardkontakter(utlatande.getVardkontakter()));

        Observation kroppsfunktion = utlatande.findObservationByKategori(ObservationsKoder.KROPPSFUNKTIONER);
        if (kroppsfunktion != null) {
            register.getLakarutlatande().getFunktionstillstand().add(toFunktionstillstand(kroppsfunktion, TypAvFunktionstillstand.KROPPSFUNKTION));
        }

        Observation aktivitet = utlatande.findObservationByKategori(ObservationsKoder.AKTIVITETER_OCH_DELAKTIGHET);
        if (aktivitet != null) {

            // add arbetsformaga to aktivitetsbegransing
            FunktionstillstandType aktivitetsbegransing = toFunktionstillstand(aktivitet, TypAvFunktionstillstand.AKTIVITET);
            aktivitetsbegransing.setArbetsformaga(toArbetsformaga(utlatande, aktivitet));

            register.getLakarutlatande().getFunktionstillstand().add(aktivitetsbegransing);

        }
        return register;
    }

    private static FunktionstillstandType toFunktionstillstand(Observation observation, TypAvFunktionstillstand typAvFunktionstillstand) {
        FunktionstillstandType funktionstillstandType = new FunktionstillstandType();
        funktionstillstandType.setTypAvFunktionstillstand(typAvFunktionstillstand);
        funktionstillstandType.setBeskrivning(observation.getBeskrivning());
        return funktionstillstandType;
    }

    private static ArbetsformagaType toArbetsformaga(Utlatande utlatande, Observation aktivitetsbegransing) {

        ArbetsformagaType arbetsformagaType = new ArbetsformagaType();

        List<Observation> arbetsformagas = utlatande.getObservationsByKod(ObservationsKoder.ARBETSFORMAGA);

        if(arbetsformagas != null && arbetsformagas.size() > 0) {
            Observation firstObservation = arbetsformagas.get(0);
            arbetsformagaType.setMotivering(firstObservation.getBeskrivning());
            if(firstObservation.getPrognos() != null) {
                Kod prognosKod = firstObservation.getPrognos().getPrognosKod();
                if(prognosKod != null) {
                    String fk7263String = Prognoskoder.mapToFk7263.get(prognosKod);
                    arbetsformagaType.setPrognosangivelse(Prognosangivelse.fromValue(fk7263String));
                }
            }
        }

        if (utlatande.getPatient() != null) {

            Patient patient = utlatande.getPatient();

            // attach arbetsuppgift if available
            if (patient.getArbetsuppgifts() != null && !patient.getArbetsuppgifts().isEmpty()) {
                ArbetsuppgiftType arbetsuppgift = new ArbetsuppgiftType();
                arbetsuppgift.setTypAvArbetsuppgift(patient.getArbetsuppgifts().get(0).getTypAvArbetsuppgift());
                arbetsformagaType.setArbetsuppgift(arbetsuppgift);
            }

            if (patient.getSysselsattnings() != null) {
                arbetsformagaType.getSysselsattning().addAll(convertSysselsattnings(patient.getSysselsattnings()));
            }
        }

        for (Observation arbetsformaga : arbetsformagas) {
            ArbetsformagaNedsattningType nedsattningType = new ArbetsformagaNedsattningType();

            if (arbetsformaga.getVarde() != null && arbetsformaga.getVarde() != null && !arbetsformaga.getVarde().isEmpty()) {
                switch (arbetsformaga.getVarde().get(0).getQuantity().intValue()) {
                case 25:
                    nedsattningType.setNedsattningsgrad(Nedsattningsgrad.NEDSATT_MED_1_4);
                    break;
                case 50:
                    nedsattningType.setNedsattningsgrad(Nedsattningsgrad.NEDSATT_MED_1_2);
                    break;
                case 75:
                    nedsattningType.setNedsattningsgrad(Nedsattningsgrad.NEDSATT_MED_3_4);
                    break;
                case 100:
                    nedsattningType.setNedsattningsgrad(Nedsattningsgrad.HELT_NEDSATT);
                    break;
                default:
                    throw new IllegalStateException("Wrong nedsättningsgrad " + arbetsformaga.getVarde().get(0).getQuantity());
                }
            }

            if (arbetsformaga.getObservationsPeriod() != null) {
                nedsattningType.setVaraktighetFrom(new LocalDate(arbetsformaga.getObservationsPeriod().getFrom()));
                nedsattningType.setVaraktighetTom(new LocalDate(arbetsformaga.getObservationsPeriod().getTom()));
            }

            arbetsformagaType.getArbetsformagaNedsattning().add(nedsattningType);
        }
        return arbetsformagaType;
    }

    private static List<SysselsattningType> convertSysselsattnings(List<Sysselsattning> source) {
        List<SysselsattningType> sysselsattningTypes = new ArrayList<>();
        for (Sysselsattning sysselsattning : source) {
            SysselsattningType sysselsattningType = convert(sysselsattning);
            if (sysselsattningType != null) {
                sysselsattningTypes.add(sysselsattningType);
            }
        }
        return sysselsattningTypes;
    }

    private static SysselsattningType convert(Sysselsattning source) {

        if (source == null || source.getSysselsattningsTyp() == null) {
            return null;
        }

        SysselsattningType sysselsattningTyp = null;
        try {
            Kod sysselsattning = source.getSysselsattningsTyp();
            String fk7263String = Sysselsattningskoder.mapToFk7263.get(sysselsattning);
            if(fk7263String != null) {
                sysselsattningTyp = new SysselsattningType();
                sysselsattningTyp.setTypAvSysselsattning(TypAvSysselsattning.fromValue(fk7263String));
            }
        } catch (IllegalArgumentException ex) {
            // unknown typAvSysselsattning that is not relevant in the Fk7263 context
            return null;
        }

        return sysselsattningTyp;
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
        switch (source) {
        case HELT_NEDSATT:
            nedsattningsgrad = Nedsattningsgrad.HELT_NEDSATT;
            break;
        case NEDSATT_MED_1_2:
            nedsattningsgrad = Nedsattningsgrad.NEDSATT_MED_1_2;
            break;
        case NEDSATT_MED_1_4:
            nedsattningsgrad = Nedsattningsgrad.NEDSATT_MED_1_4;
            break;
        case NEDSATT_MED_3_4:
            nedsattningsgrad = Nedsattningsgrad.NEDSATT_MED_3_4;
            break;
        default:
            throw new IllegalArgumentException("Can not convert 'Nedsattningsgrad' " + source);
        }
        return nedsattningsgrad;
    }

    private static List<VardkontaktType> convertVardkontakter(List<Vardkontakt> source) {
        if (source == null) {
            return null;
        }

        List<VardkontaktType> vardkontaktTypes = new ArrayList<>();
        for (Vardkontakt vardkontakt : source) {
            VardkontaktType vardkontaktType = toVardkontakt(vardkontakt);
            if (vardkontaktType != null) {
                vardkontaktTypes.add(vardkontaktType);
            }
        }
        return vardkontaktTypes;
    }

    private static VardkontaktType toVardkontakt(Vardkontakt source) {
        if (source == null || source.getVardkontakttyp() == null) {
            return null;
        }

        VardkontaktType vardkontaktType = null;

        try {
            Kod vardkontakttyp = source.getVardkontakttyp();
            String fk7263String = Vardkontakttypkoder.mapToFk7263.get(vardkontakttyp);
            if(fk7263String != null) {
                vardkontaktType = new VardkontaktType();
                vardkontaktType.setVardkontaktstid(source.getVardkontaktstid().getStart());
                vardkontaktType.setVardkontakttyp(Vardkontakttyp.fromValue(fk7263String));
            }
        } catch (IllegalArgumentException ex) {
            // unknown vardkontakttyp that is not relevant in the Fk7263 context
            return null;
        }

        return vardkontaktType;
    }

    private static List<ReferensType> convertReferenser(List<Referens> source) {
        if (source == null) {
            return null;
        }

        List<ReferensType> referensTypes = new ArrayList<>();
        for (Referens referens : source) {
            ReferensType referensType = toReferens(referens);
            if (referensType != null) {
                referensTypes.add(referensType);
            }
        }
        return referensTypes;
    }

    private static ReferensType toReferens(Referens source) {
        if (source == null || source.getReferenstyp() == null) {
            return null;
        }

        ReferensType referensType = null;

        try {
            Kod referenstyp = source.getReferenstyp();
            String fk7363String = Referenstypkoder.mapToFk7263.get(referenstyp);
            if(fk7363String != null) {
                referensType = new ReferensType();
                referensType.setDatum(source.getDatum());
                referensType.setReferenstyp(Referenstyp.fromValue(fk7363String));
            }
        } catch (IllegalArgumentException ex) {
            // unknown referenstyp that is not relevant in the Fk7263 context
            return null;
        }

        return referensType;
    }

    private static List<AktivitetType> convert(List<Aktivitet> source) {
        if (source == null) {
            return null;
        }

        List<AktivitetType> aktivitets = new ArrayList<>();
        for (Aktivitet aktivitet : source) {
            AktivitetType aktivitetType = convert(aktivitet);
            if (aktivitetType != null) {
                aktivitets.add(aktivitetType);
            }
        }
        return aktivitets;
    }

    private static AktivitetType convert(Aktivitet source) {
        if (source == null || source.getAktivitetskod() == null) {
            return null;
        }

        AktivitetType aktivitet = null;
        try {
            Kod aktivitetskod = source.getAktivitetskod();
            String asfk7263String = Aktivitetskoder.mapToFk7263.get(aktivitetskod);
            if (asfk7263String != null) {
                aktivitet = new AktivitetType();
                aktivitet.setBeskrivning(source.getBeskrivning());
                aktivitet.setAktivitetskod(Aktivitetskod.fromValue(asfk7263String));
            }
        } catch (IllegalArgumentException ex) {
            // unknown aktivitetkod that is not relevant in the Fk7263 context
            return null;
        }

        return aktivitet;
    }

    private static MedicinsktTillstandType toMedicinsktTillstand(Observation diagnos) {
        MedicinsktTillstandType tillstand = new MedicinsktTillstandType();
        tillstand.setBeskrivning(diagnos.getBeskrivning());
        tillstand.setTillstandskod(toCD(diagnos.getObservationsKod()));
        return tillstand;
    }

    private static BedomtTillstandType sjukdomsforloppToJaxb(String source) {
        BedomtTillstandType tillstand = new BedomtTillstandType();
        tillstand.setBeskrivning(source);
        return tillstand;
    }

    private static EnhetType toJaxb(Vardenhet source) {
        if (source == null) {
            return null;
        }

        EnhetType enhet = new EnhetType();
        enhet.setEnhetsnamn(source.getNamn());
        enhet.setPostadress(source.getPostadress());
        enhet.setPostnummer(source.getPostnummer());
        enhet.setPostort(source.getPostort());
        enhet.setTelefonnummer(source.getTelefonnummer());
        enhet.setEpost(source.getEpost());

        enhet.setArbetsplatskod(toII(source.getArbetsplatskod()));
        enhet.setEnhetsId(toII(source.getId()));

        enhet.setVardgivare(toJaxb(source.getVardgivare()));
        return enhet;
    }

    private static VardgivareType toJaxb(Vardgivare source) {
        if (source == null) {
            return null;
        }

        VardgivareType vardgivare = new VardgivareType();
        vardgivare.setVardgivarnamn(source.getNamn());
        vardgivare.setVardgivareId(toII(source.getId()));
        return vardgivare;
    }

    private static HosPersonalType toJaxb(HosPersonal skapadAv) {
        HosPersonalType personal = new HosPersonalType();
        personal.setForskrivarkod(skapadAv.getForskrivarkod());
        personal.setFullstandigtNamn(skapadAv.getNamn());
        personal.setPersonalId(toII(skapadAv.getId()));

        personal.setEnhet(toJaxb(skapadAv.getVardenhet()));

        return personal;
    }

    private static PatientType toJaxb(Patient source) {
        PatientType patient = new PatientType();
        patient.setFullstandigtNamn(extractFullstandigtNamn(source));
        patient.setPersonId(toII(source.getId()));
        return patient;
    }

    private static String extractFullstandigtNamn(Patient source) {
        List<String> names = new ArrayList<>();

        if (source.getFornamns() != null) {
            names.addAll(source.getFornamns());
        }

        if (source.getMellannamns() != null) {
            names.addAll(source.getMellannamns());
        }

        if (source.getEfternamns() != null) {
            names.addAll(source.getEfternamns());
        }

        return Strings.join(" ", names);
    }
}
