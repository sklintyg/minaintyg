package se.inera.certificate.integration.converter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static se.inera.certificate.integration.converter.util.IsoTypeConverter.toCD;
import static se.inera.certificate.integration.converter.util.IsoTypeConverter.toII;
import static se.inera.certificate.model.util.Iterables.addAll;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import riv.insuranceprocess.certificate._1.CertificateStatusType;
import riv.insuranceprocess.certificate._1.StatusType;
import se.inera.certificate.common.v1.AktivitetType;
import se.inera.certificate.common.v1.ArbetsuppgiftType;
import se.inera.certificate.common.v1.DateInterval;
import se.inera.certificate.common.v1.ObservationType;
import se.inera.certificate.common.v1.OvrigtType;
import se.inera.certificate.common.v1.PartialDateInterval;
import se.inera.certificate.common.v1.PatientType;
import se.inera.certificate.common.v1.PrognosType;
import se.inera.certificate.common.v1.ReferensType;
import se.inera.certificate.common.v1.SysselsattningType;
import se.inera.certificate.common.v1.VardkontaktType;
import se.inera.certificate.model.Aktivitet;
import se.inera.certificate.model.Arbetsuppgift;
import se.inera.certificate.model.HosPersonal;
import se.inera.certificate.model.Observation;
import se.inera.certificate.model.Ovrigt;
import se.inera.certificate.model.Patient;
import se.inera.certificate.model.Prognos;
import se.inera.certificate.model.Referens;
import se.inera.certificate.model.Status;
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
public class UtlatandeToUtlatandeJaxbConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(UtlatandeToUtlatandeJaxbConverter.class);

    private Utlatande source;

    private static final Unmarshaller unmarshaller;

    static {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(OvrigtType.class);
            unmarshaller = jaxbContext.createUnmarshaller();
        } catch (JAXBException e) {
            throw new RuntimeException("Failed to create JAXB context", e);
        }
    }


    public UtlatandeToUtlatandeJaxbConverter(Utlatande source) {
        this.source = source;
    }

    public se.inera.certificate.common.v1.Utlatande convert() {
        se.inera.certificate.common.v1.Utlatande utlatande = new se.inera.certificate.common.v1.Utlatande();

        utlatande.setUtlatandeId(toII(source.getId()));
        utlatande.setTypAvUtlatande(toCD(source.getTyp()));

        addAll(utlatande.getKommentars(), source.getKommentars());
        utlatande.setSigneringsdatum(source.getSigneringsDatum());
        utlatande.setSkickatdatum(source.getSkickatDatum());

        utlatande.setPatient(convert(source.getPatient()));
        utlatande.setSkapadAv(convert(source.getSkapadAv()));

        addAll(utlatande.getAktivitets(), convertAktiviteter(source.getAktiviteter()));
        addAll(utlatande.getObservations(), convertObservations(source.getObservations()));
        addAll(utlatande.getVardkontakts(), convertVardkontakter(source.getVardkontakter()));
        addAll(utlatande.getReferens(), convertReferenser(source.getReferenser()));
        addAll(utlatande.getStatuses(), convertStatus(source.getStatus()));

        utlatande.setOvrigt(convert(source.getOvrigt()));

        return utlatande;
    }

    private List<CertificateStatusType> convertStatus(List<Status> source) {
        if (source == null) return null;

        List<CertificateStatusType> statusList = new ArrayList<>();
        for (Status status : source) {
            statusList.add(convert(status));
        }
        return statusList;
    }

    private CertificateStatusType convert(Status source) {
        CertificateStatusType certificateStatusType = new CertificateStatusType();
        certificateStatusType.setTarget(source.getTarget());
        certificateStatusType.setTimestamp(source.getTimestamp());
        certificateStatusType.setType(StatusType.valueOf(source.getType().name()));
        return certificateStatusType;
    }

    private List<ReferensType> convertReferenser(List<Referens> source) {
        if (source == null) return null;

        List<ReferensType> referenser = new ArrayList<>();
        for (Referens referens : source) {
            referenser.add(convert(referens));
        }
        return referenser;
    }

    private ReferensType convert(Referens source) {
        if (source == null) return null;

        ReferensType referens = new ReferensType();
        referens.setReferenstyp(toCD(source.getReferenstyp()));
        referens.setReferensdatum(source.getDatum());
        return referens;
    }

    private List<VardkontaktType> convertVardkontakter(List<Vardkontakt> source) {
        if (source == null) return null;

        List<VardkontaktType> vardkontakter = new ArrayList<>();
        for (Vardkontakt vardkontakt : source) {
            vardkontakter.add(convert(vardkontakt));
        }
        return vardkontakter;
    }

    private VardkontaktType convert(Vardkontakt source) {
        VardkontaktType vardkontakt = new VardkontaktType();
        vardkontakt.setVardkontakttyp(toCD(source.getVardkontakttyp()));

        if (source.getVardkontaktstid() != null) {
            DateInterval vardkontakttid = new DateInterval();
            vardkontakttid.setFrom(source.getVardkontaktstid().getStart());
            vardkontakttid.setTom(source.getVardkontaktstid().getEnd());
            vardkontakt.setVardkontakttid(vardkontakttid);
        }
        return vardkontakt;
    }

    private List<ObservationType> convertObservations(List<Observation> source) {
        if (source == null) return null;

        List<ObservationType> observations = new ArrayList<>();
        for (Observation observation : source) {
            observations.add(convert(observation));
        }
        return observations;
    }

    private ObservationType convert(Observation source) {
        ObservationType observation = new ObservationType();
        observation.setObservationskategori(toCD(source.getObservationsKategori()));
        observation.setObservationskod(toCD(source.getObservatonsKod()));

        if (source.getObservationsPeriod() != null) {
            PartialDateInterval interval = new PartialDateInterval();
            interval.setFrom(source.getObservationsPeriod().getFrom());
            interval.setTom(source.getObservationsPeriod().getTom());
            observation.setObservationsperiod(interval);
        }

        observation.setBeskrivning(source.getBeskrivning());
        observation.setPrognos(convert(source.getPrognos()));

        return observation;
    }

    private PrognosType convert(Prognos source) {
        if (source == null) return null;

        PrognosType prognos = new PrognosType();
        prognos.setPrognoskod(toCD(source.getPrognosKod()));
        return prognos;
    }

    private List<AktivitetType> convertAktiviteter(List<Aktivitet> source) {
        List<AktivitetType> aktiviteter = new ArrayList<>();

        for (Aktivitet aktivitet : source) {
            aktiviteter.add(convert(aktivitet));
        }
        return aktiviteter;
    }

    private AktivitetType convert(Aktivitet source) {
        AktivitetType aktivitet = new AktivitetType();
        aktivitet.setAktivitetskod(toCD(source.getAktivitetskod()));
        aktivitet.setBeskrivning(source.getBeskrivning());
        return aktivitet;
    }

    private HosPersonalType convert(HosPersonal source) {
        if (source == null) return null;

        HosPersonalType hosPersonal = new HosPersonalType();
        hosPersonal.setPersonalId(toII(source.getId()));
        hosPersonal.setFullstandigtNamn(source.getNamn());
        hosPersonal.setForskrivarkod(source.getForskrivarkod());
        hosPersonal.setEnhet(convert(source.getVardenhet()));
        return hosPersonal;
    }

    private EnhetType convert(Vardenhet source) {
        if (source == null) return null;

        EnhetType enhet = new EnhetType();
        enhet.setEnhetsId(toII(source.getId()));
        enhet.setArbetsplatskod(toII(source.getArbetsplatskod()));
        enhet.setEnhetsnamn(source.getNamn());
        enhet.setPostadress(source.getPostadress());
        enhet.setPostnummer(source.getPostnummer());
        enhet.setPostort(source.getPostort());
        enhet.setTelefonnummer(source.getTelefonnummer());
        enhet.setEpost(source.getEpost());
        enhet.setVardgivare(convert(source.getVardgivare()));
        return enhet;
    }

    private VardgivareType convert(Vardgivare source) {
        if (source == null) return null;

        VardgivareType vardgivare = new VardgivareType();
        vardgivare.setVardgivareId(toII(source.getId()));
        vardgivare.setVardgivarnamn(source.getNamn());
        return vardgivare;
    }

    private PatientType convert(Patient source) {
        if (source == null) return null;

        PatientType patient = new PatientType();
        patient.setPersonId(toII(source.getId()));

        addAll(patient.getFornamns(), source.getFornamns());
        addAll(patient.getMellannamns(), source.getMellannamns());
        addAll(patient.getEfternamns(), source.getEfternamns());
        addAll(patient.getSysselsattnings(), convertSysselsattnings(source.getSysselsattnings()));
        addAll(patient.getArbetsuppgifts(), convertArbetsuppgifts(source.getArbetsuppgifts()));
        return patient;
    }

    private List<ArbetsuppgiftType> convertArbetsuppgifts(List<Arbetsuppgift> source) {
        List<ArbetsuppgiftType> arbetsuppgifts = new ArrayList<>();
        for (Arbetsuppgift arbetsuppgift : source) {
            arbetsuppgifts.add(convert(arbetsuppgift));
        }
        return arbetsuppgifts;
    }

    private ArbetsuppgiftType convert(Arbetsuppgift source) {
        ArbetsuppgiftType arbetsuppgift = new ArbetsuppgiftType();
        arbetsuppgift.setTypAvArbetsuppgift(source.getTypAvArbetsuppgift());
        return arbetsuppgift;
    }

    private List<SysselsattningType> convertSysselsattnings(List<Sysselsattning> source) {
        List<SysselsattningType> sysselsattnings = new ArrayList<>();
        for (Sysselsattning sysselsattning : source) {
            sysselsattnings.add(convert(sysselsattning));
        }
        return sysselsattnings;

    }

    private SysselsattningType convert(Sysselsattning source) {
        SysselsattningType sysselsattning = new SysselsattningType();
        sysselsattning.setTypAvSysselsattning(toCD(source.getSysselsattningsTyp()));
        sysselsattning.setDatum(source.getDatum());
        return sysselsattning;
    }

    private OvrigtType convert(Ovrigt source) {
        if (source == null) return null;

        try {
            JAXBElement<OvrigtType> ovrigt = unmarshaller.unmarshal(
                    new StreamSource(new StringReader(source.getData())), OvrigtType.class);
            return ovrigt.getValue();
        } catch (JAXBException e) {
            LOGGER.error("Failed to unmarshall Ovrigt element", e);
            throw new RuntimeException("Failed to unmarshall Ovrigt element", e);
        }
    }
}
