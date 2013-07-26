package se.inera.certificate.integration.converter;

import java.util.List;

import static se.inera.certificate.integration.converter.util.IsoTypeConverter.toCD;
import static se.inera.certificate.model.codes.ObservationsKoder.AKTIVITET;
import static se.inera.certificate.model.codes.ObservationsKoder.DIAGNOS;
import static se.inera.certificate.model.codes.ObservationsKoder.KROPPSFUNKTION;
import static se.inera.certificate.model.codes.ObservationsKoder.SJUKDOMSFORLOPP;

import iso.v21090.dt.v1.CD;
import iso.v21090.dt.v1.II;
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
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.ArbetsformagaNedsattningType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.BedomtTillstandType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.FunktionstillstandType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.LakarutlatandeType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.MedicinsktTillstandType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.Prognosangivelse;

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

            utlatande.getObservations().add(convert(funktionstillstand));

            if (funktionstillstand.getArbetsformaga() != null) {

                if (funktionstillstand.getArbetsformaga().getArbetsuppgift() != null) {
                    utlatande.getPatient().getArbetsuppgifts().add(convert(funktionstillstand.getArbetsformaga().getArbetsuppgift()));
                }
                if (funktionstillstand.getArbetsformaga().getSysselsattning() != null) {
                    for (se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.SysselsattningType sysselsattning : funktionstillstand.getArbetsformaga().getSysselsattning()) {
                        utlatande.getPatient().getSysselsattnings().add(convert(sysselsattning));
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

    private static SysselsattningType convert(se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.SysselsattningType source) {
        SysselsattningType sysselsattning = new SysselsattningType();
        CD sysselsattningsType = new CD();
        sysselsattningsType.setCode(source.getTypAvSysselsattning().value());
        sysselsattning.setTypAvSysselsattning(sysselsattningsType);
        return sysselsattning;
    }

    private static ArbetsuppgiftType convert(se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.ArbetsuppgiftType source) {
        ArbetsuppgiftType arbetsuppgift = new ArbetsuppgiftType();
        arbetsuppgift.setTypAvArbetsuppgift(source.getTypAvArbetsuppgift());
        return arbetsuppgift;
    }

    private static ObservationType convert(BedomtTillstandType bedomtTillstand) {
        // TODO - bedomtTillstand will always result in a code-less observation with only a description. We need to set the observation category
        ObservationType observation = new ObservationType();
        observation.setBeskrivning(bedomtTillstand.getBeskrivning());
        observation.setObservationskategori(toCD(SJUKDOMSFORLOPP));
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

        CD vardkontaktType = new CD();
        vardkontaktType.setCode(source.getVardkontakttyp().value());
        vardkontakt.setVardkontakttyp(vardkontaktType);

        // TODO - do we really need a timespan with from and to date? In FK7263 case, we set fromDate=endDate
        DateInterval vardkontaktTid= new DateInterval();
        vardkontaktTid.setFrom(source.getVardkontaktstid());
        vardkontaktTid.setTom(source.getVardkontaktstid());
        vardkontakt.setVardkontakttid(vardkontaktTid);
        return vardkontakt;
    }

    private static ReferensType convert(se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.ReferensType source) {
        ReferensType referens = new ReferensType();

        CD referensType = new CD();
        referensType.setCode(source.getReferenstyp().value());
        referens.setReferenstyp(referensType);

        referens.setReferensdatum(source.getDatum());

        return referens;
    }

    private static AktivitetType convert(se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.AktivitetType source) {
        AktivitetType aktivitet = new AktivitetType();
        aktivitet.setBeskrivning(source.getBeskrivning());

        CD aktivitetsCode = new CD();
        aktivitetsCode.setCode(source.getAktivitetskod().value());
        aktivitet.setAktivitetskod(aktivitetsCode);
        return aktivitet;
    }

    private static ObservationType convert(FunktionstillstandType source) {

        ObservationType observation = new ObservationType();

        switch (source.getTypAvFunktionstillstand()) {
            case AKTIVITET:
                observation.setObservationskategori(toCD(AKTIVITET));
                break;
            case KROPPSFUNKTION:
                observation.setObservationskategori(toCD(KROPPSFUNKTION));
                break;
        }
        observation.setBeskrivning(source.getBeskrivning());


        if (source.getArbetsformaga() != null) {
            //TODO - how to map arbetsförmåga motivering?
            String motivering = source.getArbetsformaga().getMotivering();

            // TODO - are nedsättningsgrader mapped correctly?!?
            List<ArbetsformagaNedsattningType> nedsattningList = source.getArbetsformaga().getArbetsformagaNedsattning();
            for (ArbetsformagaNedsattningType nedsattning : nedsattningList) {
                ObservationType nedsattningObservation = new ObservationType();

                CD nedsattningskod = new CD();
                nedsattningskod.setCode(nedsattning.getNedsattningsgrad().value());
                nedsattningObservation.setObservationskod(nedsattningskod);

                PartialDateInterval observationsperiod = new PartialDateInterval();
                observationsperiod.setFrom(new Partial(nedsattning.getVaraktighetFrom()));
                observationsperiod.setTom(new Partial(nedsattning.getVaraktighetTom()));
                nedsattningObservation.setObservationsperiod(observationsperiod);
            }

            observation.setPrognos(convert(source.getArbetsformaga().getPrognosangivelse()));
        }

        return observation;
    }

    /*
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
    }  */

    private static PrognosType convert(Prognosangivelse source) {
        if (source == null) {
            return null;
        }

        PrognosType prognos = new PrognosType();
        CD prognosCode = new CD();
        prognosCode.setCode(source.value());
        prognos.setPrognoskod(prognosCode);

        return prognos;
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
