package se.inera.certificate.integration.converter;

import java.util.ArrayList;
import java.util.List;

import static se.inera.certificate.integration.converter.util.IsoTypeConverter.toId;
import static se.inera.certificate.integration.converter.util.IsoTypeConverter.toKod;
import static se.inera.certificate.integration.converter.util.IsoTypeConverter.toLocalDate;
import static se.inera.certificate.integration.converter.util.IsoTypeConverter.toLocalDateTime;
import static se.inera.certificate.integration.converter.util.IsoTypeConverter.toLocalDateTimeInterval;

import se.inera.certificate.common.v1.AktivitetType;
import se.inera.certificate.common.v1.ObservationType;
import se.inera.certificate.common.v1.PatientType;
import se.inera.certificate.common.v1.PrognosType;
import se.inera.certificate.common.v1.ReferensType;
import se.inera.certificate.common.v1.SysselsattningType;
import se.inera.certificate.common.v1.VardkontaktType;
import se.inera.certificate.model.Aktivitet;
import se.inera.certificate.model.HosPersonal;
import se.inera.certificate.model.Observation;
import se.inera.certificate.model.Patient;
import se.inera.certificate.model.Prognos;
import se.inera.certificate.model.Referens;
import se.inera.certificate.model.Sysselsattning;
import se.inera.certificate.model.Utlatande;
import se.inera.certificate.model.Vardenhet;
import se.inera.certificate.model.Vardgivare;
import se.inera.certificate.model.Vardkontakt;
import se.inera.ifv.insuranceprocess.healthreporting.v2.EnhetType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.HosPersonalType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.VardgivareType;

/**
 * @author andreaskaltenbach
 */
public final class LakarutlatandeJaxbToLakarutlatandeConverter {

    private LakarutlatandeJaxbToLakarutlatandeConverter() {
    }

    /**
     * Converts a JAXB {@link se.inera.certificate.common.v1.Utlatande} to a {@link se.inera.certificate.model.Utlatande}.
     */
    public static Utlatande convert(se.inera.certificate.common.v1.Utlatande source) {
        Utlatande utlatande = new Utlatande();

        utlatande.setId(toId(source.getUtlatandeId()));
        utlatande.setTyp(toKod(source.getTypAvUtlatande()));
        utlatande.setKommentars(source.getKommentars());
        utlatande.setSigneringsDatum(toLocalDateTime(source.getSigneringsdatum()));

        //TODO - convert skickatDatum
        //utlatande.setSkickatDatum(source.getSkickatDatum());

        utlatande.setPatient(convert(source.getPatient()));

        utlatande.setSkapadAv(convert(source.getSkapadAvHosPersonal()));

        utlatande.setObservations(convertObservations(source.getObservations()));

        utlatande.setAktiviteter(convertAktiviteter(source.getAktivitets()));
        utlatande.setReferenser(convertReferenser(source.getReferens()));
        utlatande.setVardkontakter(convertVardkontakter(source.getVardkontakts()));

        return utlatande;
    }

    /*
    private static Arbetsformaga convert(ArbetsformagaType source) {
        Arbetsformaga arbetsformaga = new Arbetsformaga();
        arbetsformaga.setArbetsuppgift(source.getArbetsuppgift());
        arbetsformaga.setArbetsformagaNedsattningar(convertArbetsformaganedsattningar(source.getArbetsformagaNedsattnings()));
        arbetsformaga.setPrognosangivelse(convert(source.getPrognosangivelse()));
        arbetsformaga.setMotivering(source.getMotivering());
        return arbetsformaga;
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
    }  */

    private static List<Observation> convertObservations(List<ObservationType> source) {
        List<Observation> observations = new ArrayList<>();
        for (ObservationType observation : source) {
            observations.add(convert(observation));
        }
        return observations;
    }


    private static Observation convert(ObservationType source) {
        Observation observation = new Observation();

        observation.setObservationsKategori(toKod(source.getObservationskategori()));
        observation.setObservatonsKod(toKod(source.getObservationskod()));
        observation.setObservationsTid(toLocalDateTimeInterval(source.getObservationsperiod()));

        observation.setPrognos(convert(source.getPrognos()));

        return observation;
    }

    private static Prognos convert(PrognosType source) {
        Prognos prognos = new Prognos();
        prognos.setPrognosKod(toKod(source.getPrognoskod()));
        return prognos;
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
        vardkontakt.setVardkontakttyp(toKod(source.getVardkontakttyp()));
        vardkontakt.setVardkontaktstid(toLocalDateTimeInterval(source.getVardkontakttid()));
        return vardkontakt;
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
        referens.setReferenstyp(toKod(source.getReferenstyp()));
        referens.setDatum(toLocalDate(source.getReferensdatum()));
        return referens;
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
        aktivitet.setAktivitetskod(toKod(source.getAktivitetskod()));
        return aktivitet;
    }
     /*
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
    }       */

    private static HosPersonal convert(HosPersonalType source) {
        HosPersonal hosPersonal = new HosPersonal();
        hosPersonal.setId(toId(source.getPersonalId()));
        hosPersonal.setNamn(source.getFullstandigtNamn());
        hosPersonal.setForskrivarkod(source.getForskrivarkod());

        hosPersonal.setVardenhet(convert(source.getEnhet()));

        return hosPersonal;
    }

    private static Patient convert(PatientType source) {
        Patient patient = new Patient();
        patient.setId(toId(source.getPersonId()));
        patient.setFornamns(source.getFornamns());
        patient.setEfternamns(source.getEfternamns());
        patient.setMellannamns(source.getMellannamns());

        patient.setSysselsattnings(convert(source.getSysselsattnings()));

        return patient;
    }

    private static List<Sysselsattning> convert(List<SysselsattningType> source) {
        List<Sysselsattning> sysselsattnings = new ArrayList<>();
        for (SysselsattningType sysselsattning : source) {
            sysselsattnings.add(convert(sysselsattning));
        }
        return sysselsattnings;
    }

    private static Sysselsattning convert(SysselsattningType source) {
        Sysselsattning sysselsattning = new Sysselsattning();
        sysselsattning.setSysselsattningsTyp(toKod(source.getTypAvSysselsattning()));
        sysselsattning.setDatum(toLocalDateTime(source.getDatum()));
        return sysselsattning;
    }

    private static Vardenhet convert(EnhetType source) {
        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setId(toId(source.getEnhetsId()));
        vardenhet.setNamn(source.getEnhetsnamn());
        vardenhet.setArbetsplatskod(toId(source.getArbetsplatskod()));
        vardenhet.setPostadress(source.getPostadress());
        vardenhet.setPostnummer(source.getPostnummer());
        vardenhet.setPostort(source.getPostort());
        vardenhet.setEpost(source.getEpost());
        vardenhet.setTelefonnummer(source.getTelefonnummer());
        vardenhet.setVardgivare(convert(source.getVardgivare()));
        return vardenhet;
    }

    private static Vardgivare convert(VardgivareType source) {
        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setId(toId(source.getVardgivareId()));
        vardgivare.setNamn(source.getVardgivarnamn());
        return vardgivare;
    }
}
