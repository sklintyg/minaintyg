package se.inera.certificate.integration.validator;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.*;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateType;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.util.Iterator;

/**
 * @author andreaskaltenbach
 */
public class LakarutlatandeValidatorTest {

    private static Unmarshaller UNMARSHALLER;

    @BeforeClass
    public static void setupJaxb() throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(RegisterMedicalCertificateType.class);
        UNMARSHALLER = jaxbContext.createUnmarshaller();
    }

    private LakarutlatandeType lakarutlatande() throws IOException, JAXBException {
        // read request from file
        JAXBElement<RegisterMedicalCertificateType> request = UNMARSHALLER.unmarshal(new StreamSource(new ClassPathResource("register-medical-certificate/register-medical-certificate-valid.xml").getInputStream()), RegisterMedicalCertificateType.class);
        return request.getValue().getLakarutlatande();
    }

    @Test
    public void testHappyCase() throws Exception {
        new LakarutlatandeValidator(lakarutlatande()).validate();
    }

    @Test( expected = ValidationException.class )
    public void testMissingLakarautlatandeId() throws Exception {
        LakarutlatandeType lakarutlatande = lakarutlatande();
        lakarutlatande.setLakarutlatandeId(null);

        new LakarutlatandeValidator(lakarutlatande).validate();
    }

    @Test( expected = ValidationException.class )
    public void testMissingSkickatDatum() throws Exception {
        LakarutlatandeType lakarutlatande = lakarutlatande();
        lakarutlatande.setSkickatDatum(null);

        new LakarutlatandeValidator(lakarutlatande).validate();
    }

    @Test( expected = ValidationException.class )
    public void testMissingPatient() throws Exception {
        LakarutlatandeType lakarutlatande = lakarutlatande();
        lakarutlatande.setPatient(null);

        new LakarutlatandeValidator(lakarutlatande).validate();
    }

    @Test( expected = ValidationException.class )
    public void testMissingPatientId() throws Exception {
        LakarutlatandeType lakarutlatande = lakarutlatande();
        lakarutlatande.getPatient().getPersonId().setExtension(null);

        new LakarutlatandeValidator(lakarutlatande).validate();
    }

    @Test( expected = ValidationException.class )
    public void testWrongPatientIdCodeSystem() throws Exception {
        LakarutlatandeType lakarutlatande = lakarutlatande();
        lakarutlatande.getPatient().getPersonId().setRoot("<strange>");

        new LakarutlatandeValidator(lakarutlatande).validate();
    }

    @Test( expected = ValidationException.class )
    public void testMissingPatientName() throws Exception {
        LakarutlatandeType lakarutlatande = lakarutlatande();
        lakarutlatande.getPatient().setFullstandigtNamn(null);

        new LakarutlatandeValidator(lakarutlatande).validate();
    }

    @Test( expected = ValidationException.class )
    public void testMissingSkapadAvHosPersonal() throws Exception {
        LakarutlatandeType lakarutlatande = lakarutlatande();
        lakarutlatande.setSkapadAvHosPersonal(null);

        new LakarutlatandeValidator(lakarutlatande).validate();
    }

    @Test( expected = ValidationException.class )
    public void testMissingSkapadAvHosPersonalId() throws Exception {
        LakarutlatandeType lakarutlatande = lakarutlatande();
        lakarutlatande.getSkapadAvHosPersonal().getPersonalId().setExtension(null);

        new LakarutlatandeValidator(lakarutlatande).validate();
    }

    @Test( expected = ValidationException.class )
    public void testWrongSkapadAvHosPersonalCodeSystem() throws Exception {
        LakarutlatandeType lakarutlatande = lakarutlatande();
        lakarutlatande.getSkapadAvHosPersonal().getPersonalId().setRoot("<strange>");

        new LakarutlatandeValidator(lakarutlatande).validate();
    }

    @Test( expected = ValidationException.class )
    public void testMissingSkapadAvHosPersonalName() throws Exception {
        LakarutlatandeType lakarutlatande = lakarutlatande();
        lakarutlatande.getSkapadAvHosPersonal().setFullstandigtNamn(null);

        new LakarutlatandeValidator(lakarutlatande).validate();
    }

    @Test( expected = ValidationException.class )
    public void testMissingEnhet() throws Exception {
        LakarutlatandeType lakarutlatande = lakarutlatande();
        lakarutlatande.getSkapadAvHosPersonal().setEnhet(null);

        new LakarutlatandeValidator(lakarutlatande).validate();
    }

    @Test( expected = ValidationException.class )
    public void testMissingEnhetExtension() throws Exception {
        LakarutlatandeType lakarutlatande = lakarutlatande();
        lakarutlatande.getSkapadAvHosPersonal().getEnhet().getEnhetsId().setExtension(null);

        new LakarutlatandeValidator(lakarutlatande).validate();
    }

    @Test( expected = ValidationException.class )
    public void testMissingEnhetRoot() throws Exception {
        LakarutlatandeType lakarutlatande = lakarutlatande();
        lakarutlatande.getSkapadAvHosPersonal().getEnhet().getEnhetsId().setRoot("<strange>");

        new LakarutlatandeValidator(lakarutlatande).validate();
    }

    @Test( expected = ValidationException.class )
    public void testMissingEnhetName() throws Exception {
        LakarutlatandeType lakarutlatande = lakarutlatande();
        lakarutlatande.getSkapadAvHosPersonal().getEnhet().setEnhetsnamn(null);

        new LakarutlatandeValidator(lakarutlatande).validate();
    }

    @Test( expected = ValidationException.class )
    public void testMissingEnhetPostadress() throws Exception {
        LakarutlatandeType lakarutlatande = lakarutlatande();
        lakarutlatande.getSkapadAvHosPersonal().getEnhet().setPostadress(null);

        new LakarutlatandeValidator(lakarutlatande).validate();
    }

    @Test( expected = ValidationException.class )
    public void testMissingEnhetPostnummer() throws Exception {
        LakarutlatandeType lakarutlatande = lakarutlatande();
        lakarutlatande.getSkapadAvHosPersonal().getEnhet().setPostnummer(null);

        new LakarutlatandeValidator(lakarutlatande).validate();
    }

    @Test( expected = ValidationException.class )
    public void testMissingEnhetPostort() throws Exception {
        LakarutlatandeType lakarutlatande = lakarutlatande();
        lakarutlatande.getSkapadAvHosPersonal().getEnhet().setPostort(null);

        new LakarutlatandeValidator(lakarutlatande).validate();
    }

    @Test( expected = ValidationException.class )
    public void testMissingEnhetTelefonnummer() throws Exception {
        LakarutlatandeType lakarutlatande = lakarutlatande();
        lakarutlatande.getSkapadAvHosPersonal().getEnhet().setTelefonnummer(null);

        new LakarutlatandeValidator(lakarutlatande).validate();
    }

    @Test( expected = ValidationException.class )
    public void testMissingVardgivare() throws Exception {
        LakarutlatandeType lakarutlatande = lakarutlatande();
        lakarutlatande.getSkapadAvHosPersonal().getEnhet().setVardgivare(null);

        new LakarutlatandeValidator(lakarutlatande).validate();
    }

    @Test( expected = ValidationException.class )
    public void testMissingVardgivareId() throws Exception {
        LakarutlatandeType lakarutlatande = lakarutlatande();
        lakarutlatande.getSkapadAvHosPersonal().getEnhet().getVardgivare().getVardgivareId().setExtension(null);

        new LakarutlatandeValidator(lakarutlatande).validate();
    }

    @Test( expected = ValidationException.class )
    public void testMissingVardgivareRoot() throws Exception {
        LakarutlatandeType lakarutlatande = lakarutlatande();
        lakarutlatande.getSkapadAvHosPersonal().getEnhet().getVardgivare().getVardgivareId().setRoot("<strange>");

        new LakarutlatandeValidator(lakarutlatande).validate();
    }

    @Test( expected = ValidationException.class )
    public void testMissingVardgivareName() throws Exception {
        LakarutlatandeType lakarutlatande = lakarutlatande();
        lakarutlatande.getSkapadAvHosPersonal().getEnhet().getVardgivare().setVardgivarnamn(null);

        new LakarutlatandeValidator(lakarutlatande).validate();
    }

    @Test( expected = ValidationException.class )
    public void testMissingAktivitet() throws Exception {
        LakarutlatandeType lakarutlatande = lakarutlatande();


        Iterator<FunktionstillstandType> iter = lakarutlatande.getFunktionstillstand().iterator();

        while (iter.hasNext()) {
            FunktionstillstandType funktionstillstand = iter.next();
            if (funktionstillstand.getTypAvFunktionstillstand() == TypAvFunktionstillstand.AKTIVITET) {
                iter.remove();
            }
        }

        new LakarutlatandeValidator(lakarutlatande).validate();
    }

    @Test( expected = ValidationException.class )
    public void testMissingMedicinsktTillstand() throws Exception {
        LakarutlatandeType lakarutlatande = lakarutlatande();
        lakarutlatande.setMedicinsktTillstand(null);

        new LakarutlatandeValidator(lakarutlatande).validate();
    }

    @Test( expected = ValidationException.class )
    public void testMissingMedicinsktTillstandCode() throws Exception {
        LakarutlatandeType lakarutlatande = lakarutlatande();
        lakarutlatande.getMedicinsktTillstand().getTillstandskod().setCode(null);

        new LakarutlatandeValidator(lakarutlatande).validate();
    }

    @Test( expected = ValidationException.class )
    public void testEmptyMedicinsktTillstandCode() throws Exception {
        LakarutlatandeType lakarutlatande = lakarutlatande();
        lakarutlatande.getMedicinsktTillstand().getTillstandskod().setCode("");

        new LakarutlatandeValidator(lakarutlatande).validate();
    }

    @Test( expected = ValidationException.class )
    public void testMissingMedicinsktTillstandCodeSystemName() throws Exception {
        LakarutlatandeType lakarutlatande = lakarutlatande();
        lakarutlatande.getMedicinsktTillstand().getTillstandskod().setCodeSystemName(null);

        new LakarutlatandeValidator(lakarutlatande).validate();
    }

    @Test( expected = ValidationException.class )
    public void testIllegalMedicinsktTillstandCodeSystemName() throws Exception {
        LakarutlatandeType lakarutlatande = lakarutlatande();
        lakarutlatande.getMedicinsktTillstand().getTillstandskod().setCodeSystemName("<strange>");

        new LakarutlatandeValidator(lakarutlatande).validate();
    }

    @Test( expected = ValidationException.class )
    public void testMissingKroppsfunktion() throws Exception {
        LakarutlatandeType lakarutlatande = lakarutlatande();

        Iterator<FunktionstillstandType> iter = lakarutlatande.getFunktionstillstand().iterator();

        while (iter.hasNext()) {
            FunktionstillstandType funktionstillstand = iter.next();
            if (funktionstillstand.getTypAvFunktionstillstand() == TypAvFunktionstillstand.KROPPSFUNKTION) {
                iter.remove();
            }
        }

        new LakarutlatandeValidator(lakarutlatande).validate();
    }

    @Test( expected = ValidationException.class )
    public void testMissingKroppsfunktionBeskrivning() throws Exception {
        LakarutlatandeType lakarutlatande = lakarutlatande();
        kroppsfunktion(lakarutlatande).setBeskrivning(null);


        new LakarutlatandeValidator(lakarutlatande).validate();
    }

    @Test( expected = ValidationException.class )
    public void testEmptyKroppsfunktionBeskrivning() throws Exception {
        LakarutlatandeType lakarutlatande = lakarutlatande();
        kroppsfunktion(lakarutlatande).setBeskrivning("");


        new LakarutlatandeValidator(lakarutlatande).validate();
    }


    @Test( expected = ValidationException.class )
    public void testMissingVardkontaktOrReferens() throws Exception {
        LakarutlatandeType lakarutlatande = lakarutlatande();

        // remove all vardkontakter
        lakarutlatande.getVardkontakt().clear();

        // remove all referense
        lakarutlatande.getReferens().clear();

        new LakarutlatandeValidator(lakarutlatande).validate();
    }

    @Test( expected = ValidationException.class )
    public void testMissingUndersokningTid() throws Exception {
        LakarutlatandeType lakarutlatande = lakarutlatande();
        undersokning(lakarutlatande).setVardkontaktstid(null);

        new LakarutlatandeValidator(lakarutlatande).validate();
    }

    @Test( expected = ValidationException.class )
    public void testMissingTelefonkontaktTid() throws Exception {
        LakarutlatandeType lakarutlatande = lakarutlatande();
        telefonkontakt(lakarutlatande).setVardkontaktstid(null);

        new LakarutlatandeValidator(lakarutlatande).validate();
    }

    @Test( expected = ValidationException.class )
    public void testMissingJournalReferensDate() throws Exception {
        LakarutlatandeType lakarutlatande = lakarutlatande();
        referensJournal(lakarutlatande).setDatum(null);

        new LakarutlatandeValidator(lakarutlatande).validate();
    }

    @Test( expected = ValidationException.class )
    public void testMissingAnnatReferensDate() throws Exception {
        LakarutlatandeType lakarutlatande = lakarutlatande();
        referensAnnat(lakarutlatande).setDatum(null);

        new LakarutlatandeValidator(lakarutlatande).validate();
    }

    @Test( expected = ValidationException.class )
    public void testMissingArbetsformoga() throws Exception {
        LakarutlatandeType lakarutlatande = lakarutlatande();
        aktivitet(lakarutlatande).setArbetsformaga(null);

        new LakarutlatandeValidator(lakarutlatande).validate();
    }

    @Test( expected = ValidationException.class )
    public void testMissingSysseksattning() throws Exception {
        LakarutlatandeType lakarutlatande = lakarutlatande();
        aktivitet(lakarutlatande).getArbetsformaga().getSysselsattning().clear();

        new LakarutlatandeValidator(lakarutlatande).validate();
    }

    @Test( expected = ValidationException.class )
    public void testMissingArbetsbeskrivning() throws Exception {
        LakarutlatandeType lakarutlatande = lakarutlatande();
        for (SysselsattningType sysselsattning : aktivitet(lakarutlatande).getArbetsformaga().getSysselsattning()) {

            sysselsattning.setTypAvSysselsattning(TypAvSysselsattning.NUVARANDE_ARBETE);
        }
        aktivitet(lakarutlatande).getArbetsformaga().setArbetsuppgift(null);

        new LakarutlatandeValidator(lakarutlatande).validate();
    }

    @Test( expected = ValidationException.class )
    public void testEmptyArbetsbeskrivning() throws Exception {
        LakarutlatandeType lakarutlatande = lakarutlatande();
        for (SysselsattningType sysselsattning : aktivitet(lakarutlatande).getArbetsformaga().getSysselsattning()) {

            sysselsattning.setTypAvSysselsattning(TypAvSysselsattning.NUVARANDE_ARBETE);
        }
        aktivitet(lakarutlatande).getArbetsformaga().getArbetsuppgift().setTypAvArbetsuppgift("");

        new LakarutlatandeValidator(lakarutlatande).validate();
    }

    @Test( expected = ValidationException.class )
    public void testMissingArbetsformogaNedsattning() throws Exception {
        LakarutlatandeType lakarutlatande = lakarutlatande();
        aktivitet(lakarutlatande).getArbetsformaga().getArbetsformagaNedsattning().clear();

        new LakarutlatandeValidator(lakarutlatande).validate();
    }

    @Test( expected = ValidationException.class )
    public void testMissingNedsattning_1_4_VaraktighetFrom() throws Exception {
        LakarutlatandeType lakarutlatande = lakarutlatande();
        aktivitet(lakarutlatande).getArbetsformaga().getArbetsformagaNedsattning().get(0).setVaraktighetFrom(null);

        new LakarutlatandeValidator(lakarutlatande).validate();
    }

    @Test( expected = ValidationException.class )
    public void testMissingNedsattning_1_4_VaraktighetTom() throws Exception {
        LakarutlatandeType lakarutlatande = lakarutlatande();
        aktivitet(lakarutlatande).getArbetsformaga().getArbetsformagaNedsattning().get(0).setVaraktighetTom(null);

        new LakarutlatandeValidator(lakarutlatande).validate();
    }

    @Test( expected = ValidationException.class )
    public void testMissingNedsattning_1_2_VaraktighetFrom() throws Exception {
        LakarutlatandeType lakarutlatande = lakarutlatande();
        aktivitet(lakarutlatande).getArbetsformaga().getArbetsformagaNedsattning().get(1).setVaraktighetFrom(null);

        new LakarutlatandeValidator(lakarutlatande).validate();
    }

    @Test( expected = ValidationException.class )
    public void testMissingNedsattning_1_2_VaraktighetTom() throws Exception {
        LakarutlatandeType lakarutlatande = lakarutlatande();
        aktivitet(lakarutlatande).getArbetsformaga().getArbetsformagaNedsattning().get(1).setVaraktighetTom(null);

        new LakarutlatandeValidator(lakarutlatande).validate();
    }

    @Test( expected = ValidationException.class )
    public void testMissingNedsattning_3_4_VaraktighetFrom() throws Exception {
        LakarutlatandeType lakarutlatande = lakarutlatande();
        aktivitet(lakarutlatande).getArbetsformaga().getArbetsformagaNedsattning().get(2).setVaraktighetFrom(null);

        new LakarutlatandeValidator(lakarutlatande).validate();
    }

    @Test( expected = ValidationException.class )
    public void testMissingNedsattning_3_4_VaraktighetTom() throws Exception {
        LakarutlatandeType lakarutlatande = lakarutlatande();
        aktivitet(lakarutlatande).getArbetsformaga().getArbetsformagaNedsattning().get(2).setVaraktighetTom(null);

        new LakarutlatandeValidator(lakarutlatande).validate();
    }

    @Test( expected = ValidationException.class )
    public void testMissingNedsattning_Hel_VaraktighetFrom() throws Exception {
        LakarutlatandeType lakarutlatande = lakarutlatande();
        aktivitet(lakarutlatande).getArbetsformaga().getArbetsformagaNedsattning().get(3).setVaraktighetFrom(null);

        new LakarutlatandeValidator(lakarutlatande).validate();
    }

    @Test( expected = ValidationException.class )
    public void testMissingNedsattning_Hel_VaraktighetTom() throws Exception {
        LakarutlatandeType lakarutlatande = lakarutlatande();
        aktivitet(lakarutlatande).getArbetsformaga().getArbetsformagaNedsattning().get(3).setVaraktighetTom(null);

        new LakarutlatandeValidator(lakarutlatande).validate();
    }

    @Test( expected = ValidationException.class )
    public void testMissingPrognos() throws Exception {
        LakarutlatandeType lakarutlatande = lakarutlatande();

        aktivitet(lakarutlatande).getArbetsformaga().getPrognosangivelse();
        aktivitet(lakarutlatande).getArbetsformaga().getArbetsformagaNedsattning().get(3).setVaraktighetTom(null);

        new LakarutlatandeValidator(lakarutlatande).validate();
    }

    @Test( expected = ValidationException.class )
    public void testDoubleRessatt() throws Exception {
        LakarutlatandeType lakarutlatande = lakarutlatande();

        // set two activities with conflicting activity code
        lakarutlatande.getAktivitet().get(0).setAktivitetskod(Aktivitetskod.FORANDRAT_RESSATT_TILL_ARBETSPLATSEN_AR_AKTUELLT);
        lakarutlatande.getAktivitet().get(1).setAktivitetskod(Aktivitetskod.FORANDRAT_RESSATT_TILL_ARBETSPLATSEN_AR_EJ_AKTUELLT);

        new LakarutlatandeValidator(lakarutlatande).validate();
    }

    @Test( expected = ValidationException.class )
    public void testMissingComment() throws Exception {
        LakarutlatandeType lakarutlatande = lakarutlatande();
        lakarutlatande.setKommentar(null);

        new LakarutlatandeValidator(lakarutlatande).validate();
    }

    @Test( expected = ValidationException.class )
    public void testEmptyComment() throws Exception {
        LakarutlatandeType lakarutlatande = lakarutlatande();
        lakarutlatande.setKommentar("");


        new LakarutlatandeValidator(lakarutlatande).validate();
    }

    @Test( expected = ValidationException.class )
    public void testMissingSigneringsdatum() throws Exception {
        LakarutlatandeType lakarutlatande = lakarutlatande();
        lakarutlatande.setSigneringsdatum(null);


        new LakarutlatandeValidator(lakarutlatande).validate();
    }


    @Test( expected = ValidationException.class )
    public void testMissingArbetsplatskod() throws Exception {
        LakarutlatandeType lakarutlatande = lakarutlatande();
        lakarutlatande.getSkapadAvHosPersonal().getEnhet().setArbetsplatskod(null);


        new LakarutlatandeValidator(lakarutlatande).validate();
    }


    @Test( expected = ValidationException.class )
    public void testMissingArbetsplatskodExtension() throws Exception {
        LakarutlatandeType lakarutlatande = lakarutlatande();
        lakarutlatande.getSkapadAvHosPersonal().getEnhet().getArbetsplatskod().setExtension(null);


        new LakarutlatandeValidator(lakarutlatande).validate();
    }

    @Test( expected = ValidationException.class )
    public void testEmptyArbetsplatskodExtension() throws Exception {
        LakarutlatandeType lakarutlatande = lakarutlatande();
        lakarutlatande.getSkapadAvHosPersonal().getEnhet().getArbetsplatskod().setExtension("");


        new LakarutlatandeValidator(lakarutlatande).validate();
    }

    @Test( expected = ValidationException.class )
    public void testMissingArbetsplatskodRoot() throws Exception {
        LakarutlatandeType lakarutlatande = lakarutlatande();
        lakarutlatande.getSkapadAvHosPersonal().getEnhet().getArbetsplatskod().setRoot(null);


        new LakarutlatandeValidator(lakarutlatande).validate();
    }

    @Test( expected = ValidationException.class )
    public void testWrongArbetsplatskodRoot() throws Exception {
        LakarutlatandeType lakarutlatande = lakarutlatande();
        lakarutlatande.getSkapadAvHosPersonal().getEnhet().getArbetsplatskod().setRoot("<strange>");


        new LakarutlatandeValidator(lakarutlatande).validate();
    }

    private FunktionstillstandType aktivitet(LakarutlatandeType lakarutlatande) {
        for (FunktionstillstandType funktionstillstand : lakarutlatande.getFunktionstillstand()) {
            if (funktionstillstand.getTypAvFunktionstillstand() == TypAvFunktionstillstand.AKTIVITET) {
                return funktionstillstand;
            }
        }
        return null;
    }

    private FunktionstillstandType kroppsfunktion(LakarutlatandeType lakarutlatande) {
        for (FunktionstillstandType funktionstillstand : lakarutlatande.getFunktionstillstand()) {
            if (funktionstillstand.getTypAvFunktionstillstand() == TypAvFunktionstillstand.KROPPSFUNKTION) {
                return funktionstillstand;
            }
        }
        return null;
    }

    private ReferensType referensJournal(LakarutlatandeType lakarutlatande) {
        for (ReferensType referens : lakarutlatande.getReferens()) {
            if (referens.getReferenstyp() == Referenstyp.JOURNALUPPGIFTER) {
                return referens;
            }
        }
        return null;
    }

    private ReferensType referensAnnat(LakarutlatandeType lakarutlatande) {
        for (ReferensType referens : lakarutlatande.getReferens()) {
            if (referens.getReferenstyp() == Referenstyp.ANNAT) {
                return referens;
            }
        }
        return null;
    }

    private VardkontaktType telefonkontakt(LakarutlatandeType lakarutlatande) {
        for (VardkontaktType vardkontakt : lakarutlatande.getVardkontakt()) {
            if (vardkontakt.getVardkontakttyp() == Vardkontakttyp.MIN_TELEFONKONTAKT_MED_PATIENTEN) {
                return vardkontakt;
            }
        }
        return null;
    }

    private VardkontaktType undersokning(LakarutlatandeType lakarutlatande) {
        for (VardkontaktType vardkontakt : lakarutlatande.getVardkontakt()) {
            if (vardkontakt.getVardkontakttyp() == Vardkontakttyp.MIN_UNDERSOKNING_AV_PATIENTEN) {
                return vardkontakt;
            }
        }
        return null;
    }
}
