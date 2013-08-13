package se.inera.certificate.integration.converter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import iso.v21090.dt.v1.PQ;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.inera.certificate.common.v1.AktivitetType;
import se.inera.certificate.common.v1.ArbetsuppgiftType;
import se.inera.certificate.common.v1.ObservationType;
import se.inera.certificate.common.v1.OvrigtType;
import se.inera.certificate.common.v1.PatientType;
import se.inera.certificate.common.v1.PrognosType;
import se.inera.certificate.common.v1.ReferensType;
import se.inera.certificate.common.v1.SysselsattningType;
import se.inera.certificate.common.v1.VardkontaktType;
import se.inera.certificate.integration.converter.util.IsoTypeConverter;
import se.inera.certificate.model.Aktivitet;
import se.inera.certificate.model.Arbetsuppgift;
import se.inera.certificate.model.HosPersonal;
import se.inera.certificate.model.LocalDateInterval;
import se.inera.certificate.model.Observation;
import se.inera.certificate.model.Ovrigt;
import se.inera.certificate.model.PartialInterval;
import se.inera.certificate.model.Patient;
import se.inera.certificate.model.PhysicalQuantity;
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
public final class UtlatandeJaxbToUtlatandeConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(UtlatandeJaxbToUtlatandeConverter.class);

    private static final Marshaller marshaller;

    static {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(OvrigtType.class);
            marshaller = jaxbContext.createMarshaller();
        } catch (JAXBException e) {
            throw new RuntimeException("Failed to create JAXB context", e);
        }
    }

    private UtlatandeJaxbToUtlatandeConverter() {
    }

    /**
     * Converts a JAXB {@link se.inera.certificate.common.v1.Utlatande} to a {@link se.inera.certificate.model.Utlatande}.
     */
    public static Utlatande convert(se.inera.certificate.common.v1.Utlatande source) {
        Utlatande utlatande = new Utlatande();

        utlatande.setId(IsoTypeConverter.toId(source.getUtlatandeId()));
        utlatande.setTyp(IsoTypeConverter.toKod(source.getTypAvUtlatande()));
        utlatande.setKommentars(source.getKommentars());
        utlatande.setSigneringsDatum(source.getSigneringsdatum());
        utlatande.setSkickatDatum(source.getSkickatdatum());

        utlatande.setPatient(convert(source.getPatient()));

        utlatande.setSkapadAv(convert(source.getSkapadAv()));

        utlatande.setObservations(convertObservations(source.getObservations()));

        utlatande.setAktiviteter(convertAktiviteter(source.getAktivitets()));
        utlatande.setReferenser(convertReferenser(source.getReferens()));
        utlatande.setVardkontakter(convertVardkontakter(source.getVardkontakts()));

        utlatande.setOvrigt(convert(source.getOvrigt()));

        utlatande.setStatus(StatusConverter.toStatus(source.getStatuses()));

        return utlatande;
    }

    private static Ovrigt convert(OvrigtType source) {
        if (source == null) return null;

        Ovrigt ovrigt = new Ovrigt();
        ovrigt.setData(ovrigtTypeAsXmlString(source));

        return ovrigt;
    }

    private static JAXBElement<?> wrapJaxb(OvrigtType ovrigt) {
        JAXBElement<?> jaxbElement = new JAXBElement<OvrigtType>(
                new QName("urn:intyg:common-model:1", "ovrigt"),
                OvrigtType.class, ovrigt);
        return jaxbElement;
    }

    private static String ovrigtTypeAsXmlString(OvrigtType ovrigt) {
        try {
            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(wrapJaxb(ovrigt), stringWriter);
            return stringWriter.toString();
        } catch (JAXBException e) {
            LOGGER.error("Failed to marshall Ovrigt element", e);
            throw new RuntimeException("Failed to marshall Ovrigt element", e);
        }
    }

    private static List<Observation> convertObservations(List<ObservationType> source) {
        List<Observation> observations = new ArrayList<>();
        for (ObservationType observationType : source) {
            Observation observation = convert(observationType);
            if (observation != null) {
                observations.add(observation);
            }
        }
        return observations;
    }

    private static Observation convert(ObservationType source) {
        if (source == null) return null;

        Observation observation = new Observation();

        observation.setObservationsKategori(IsoTypeConverter.toKod(source.getObservationskategori()));
        observation.setObservationsKod(IsoTypeConverter.toKod(source.getObservationskod()));

        if (source.getObservationsperiod() != null) {
            PartialInterval observationsPeriod = new PartialInterval(source.getObservationsperiod().getFrom(), source.getObservationsperiod().getTom());
            observation.setObservationsPeriod(observationsPeriod);
        }


        observation.setVarde(convertVarde(source.getVardes()));

        observation.setPrognos(convertPrognoser(source.getPrognos()));
        observation.setBeskrivning(source.getBeskrivning());

        return observation;
    }

    private static List<PhysicalQuantity> convertVarde(List<PQ> source) {

        List<PhysicalQuantity> vardes = new ArrayList<>();
        for (PQ varde : source) {
            vardes.add(new PhysicalQuantity(varde.getValue(), varde.getUnit()));
        }
        return vardes;
    }

    private static List<Prognos> convertPrognoser(List<PrognosType> source) {
        List<Prognos> prognoser = new ArrayList<>();
        for (PrognosType prognos : source) {
            prognoser.add(convert(prognos));
        }
        return prognoser;
    }

    private static Prognos convert(PrognosType source) {
        if (source == null) return null;

        Prognos prognos = new Prognos();
        prognos.setPrognosKod(IsoTypeConverter.toKod(source.getPrognoskod()));
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
        vardkontakt.setVardkontakttyp(IsoTypeConverter.toKod(source.getVardkontakttyp()));

        LocalDateInterval vardkontaktTid = new LocalDateInterval(source.getVardkontakttid().getFrom(), source.getVardkontakttid().getTom());
        vardkontakt.setVardkontaktstid(vardkontaktTid);

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
        referens.setReferenstyp(IsoTypeConverter.toKod(source.getReferenstyp()));
        referens.setDatum(source.getReferensdatum());
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
        aktivitet.setAktivitetskod(IsoTypeConverter.toKod(source.getAktivitetskod()));
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
        if (source == null) return null;

        HosPersonal hosPersonal = new HosPersonal();
        hosPersonal.setId(IsoTypeConverter.toId(source.getPersonalId()));
        hosPersonal.setNamn(source.getFullstandigtNamn());
        hosPersonal.setForskrivarkod(source.getForskrivarkod());

        hosPersonal.setVardenhet(convert(source.getEnhet()));

        return hosPersonal;
    }

    private static Patient convert(PatientType source) {
        if (source == null) return null;

        Patient patient = new Patient();
        patient.setId(IsoTypeConverter.toId(source.getPersonId()));

        if (!source.getFornamns().isEmpty()) {
            patient.setFornamns(source.getFornamns());
        }
        if (!source.getEfternamns().isEmpty()) {
            patient.setEfternamns(source.getEfternamns());
        }
        if (!source.getMellannamns().isEmpty()) {
            patient.setMellannamns(source.getMellannamns());
        }

        patient.setSysselsattnings(convert(source.getSysselsattnings()));

        if (!source.getArbetsuppgifts().isEmpty()) {
            List<Arbetsuppgift> arbetsuppgifts = new ArrayList<>();
            for (ArbetsuppgiftType sourceArbetsuppgift : source.getArbetsuppgifts()) {
                Arbetsuppgift arbetsuppgift = new Arbetsuppgift();
                arbetsuppgift.setTypAvArbetsuppgift(sourceArbetsuppgift.getTypAvArbetsuppgift());
                arbetsuppgifts.add(arbetsuppgift);
            }
            patient.setArbetsuppgifts(arbetsuppgifts);
        }

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
        sysselsattning.setSysselsattningsTyp(IsoTypeConverter.toKod(source.getTypAvSysselsattning()));
        sysselsattning.setDatum(source.getDatum());
        return sysselsattning;
    }

    private static Vardenhet convert(EnhetType source) {
        if (source == null) return null;

        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setId(IsoTypeConverter.toId(source.getEnhetsId()));
        vardenhet.setNamn(source.getEnhetsnamn());
        vardenhet.setArbetsplatskod(IsoTypeConverter.toId(source.getArbetsplatskod()));
        vardenhet.setPostadress(source.getPostadress());
        vardenhet.setPostnummer(source.getPostnummer());
        vardenhet.setPostort(source.getPostort());
        vardenhet.setEpost(source.getEpost());
        vardenhet.setTelefonnummer(source.getTelefonnummer());
        vardenhet.setVardgivare(convert(source.getVardgivare()));
        return vardenhet;
    }

    private static Vardgivare convert(VardgivareType source) {
        if (source == null) return null;

        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setId(IsoTypeConverter.toId(source.getVardgivareId()));
        vardgivare.setNamn(source.getVardgivarnamn());
        return vardgivare;
    }
}
