package se.inera.certificate.integration.validator;

import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import iso.v21090.dt.v1.II;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.*;
import se.inera.ifv.insuranceprocess.healthreporting.v2.EnhetType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.HosPersonalType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.PatientType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.VardgivareType;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static com.google.common.collect.Iterables.find;
import static java.util.Arrays.asList;

/**
 * @author andreaskaltenbach
 */
public class LakarutlatandeValidator {

    public static final String ICD_10 = "ICD-10";
    public static final String ARBETSPLATS_CODE_OID = "1.2.752.29.4.71";
    private LakarutlatandeType lakarutlatande;
    private List<String> validationErrors = new ArrayList<>();

    private static final List<String> PATIENT_ID_OIDS = asList("1.2.752.129.2.1.3.1", "1.2.752.129.2.1.3.3");
    private static final String HOS_PERSONAL_OID = "1.2.752.129.2.1.4.1";
    private static final String ENHET_OID = "1.2.752.129.2.1.4.1";

    public static final String PERSON_NUMBER_REGEX = "[0-9]{8}[-+][0-9]{4}";

    public LakarutlatandeValidator(LakarutlatandeType lakarutlatande) {
        this.lakarutlatande = lakarutlatande;
    }

    public void validate() {

        if (lakarutlatande.getLakarutlatandeId() == null || lakarutlatande.getLakarutlatandeId().isEmpty()) {
            validationErrors.add("No Lakarutlatande Id found!");
        }

        // Check skickat datum - mandatory
        if (lakarutlatande.getSkickatDatum() == null) {
            validationErrors.add("No or wrong skickatDatum found!");
        }

        validatePatient(lakarutlatande.getPatient());
        validateSkapadAvHosPersonal(lakarutlatande.getSkapadAvHosPersonal());
        validateFunktionstillstandAktivitet();
        validateNonSmittskydd();

        FunktionstillstandType aktivitet = findFunktionsTillstandType(TypAvFunktionstillstand.AKTIVITET);
        if (aktivitet == null || aktivitet.getArbetsformaga() == null) {
            validationErrors.add("No arbetsformaga element found for field 8a!");
        } else {
            validateArbetsformogaNedsatting(aktivitet);
        }

        validateRessatt();
        validateKommentar();
        validateSignering();

        if (!validationErrors.isEmpty()) {
            throw new ValidationException(validationErrors);
        }
    }

    private void validatePatient(PatientType patient) {

        if (patient == null) {
            validationErrors.add("No Patient element found!");
            return;
        }

        // Check patient id - mandatory
        if (patient.getPersonId().getExtension() == null
                || patient.getPersonId().getExtension().isEmpty()) {
            validationErrors.add("No Patient Id found!");
        }

        // Check patient o.i.d.
        if (patient.getPersonId().getRoot() == null || !PATIENT_ID_OIDS.contains(patient.getPersonId().getRoot())) {
            validationErrors.add(String.format("Wrong o.i.d. for Patient Id! Should be %s", Joiner.on(" or ").join(PATIENT_ID_OIDS)));
        }

        // Check format of patient id (has to be a valid personnummer)
        String personNumber = patient.getPersonId().getExtension();
        if (personNumber == null || !Pattern.matches(PERSON_NUMBER_REGEX, personNumber)) {
            validationErrors.add("Wrong format for person-id! Valid format is YYYYMMDD-XXXX or YYYYMMDD+XXXX.");
        }

        // Get namn for patient - mandatory
        if (patient.getFullstandigtNamn() == null || patient.getFullstandigtNamn().isEmpty()) {
            validationErrors.add("No Patient fullstandigtNamn elements found or set!");
        }
    }

    private void validateSkapadAvHosPersonal(HosPersonalType hosPersonal) {

        if (hosPersonal == null) {
            validationErrors.add("No SkapadAvHosPersonal element found!");
            return;
        }

        // Check lakar id - mandatory
        if (hosPersonal.getPersonalId().getExtension() == null
                || hosPersonal.getPersonalId().getExtension().isEmpty()) {
            validationErrors.add("No personal-id found!");
        }

        // Check lakar id o.i.d.
        if (hosPersonal.getPersonalId().getRoot() == null
                || !hosPersonal.getPersonalId().getRoot().equals(HOS_PERSONAL_OID)) {
            validationErrors.add("Wrong o.i.d. for personalId! Should be " + HOS_PERSONAL_OID);
        }

        // Check lakarnamn - mandatory
        if (hosPersonal.getFullstandigtNamn() == null || hosPersonal.getFullstandigtNamn().isEmpty()) {
            validationErrors.add("No skapadAvHosPersonal fullstandigtNamn found.");
        }

        validateHosPersonalEnhet(hosPersonal.getEnhet());
    }

    private void validateHosPersonalEnhet(EnhetType enhet) {

        if (enhet == null) {
            validationErrors.add("No enhet element found!");
            return;
        }

        // Check enhets id - mandatory
        if (enhet.getEnhetsId().getExtension() == null
                || enhet.getEnhetsId().getExtension().isEmpty()) {
            validationErrors.add("No enhets-id found!");
        }

        // Check enhets o.i.d
        if (enhet.getEnhetsId().getRoot() == null
                || !enhet.getEnhetsId().getRoot().equals(ENHET_OID)) {
            validationErrors.add("Wrong o.i.d. for enhetsId! Should be " + ENHET_OID);
        }

        // Check enhetsnamn - mandatory
        if (enhet.getEnhetsnamn() == null || enhet.getEnhetsnamn().length() < 1) {
            validationErrors.add("No enhetsnamn found!");
        }

        // Check enhetsadress - mandatory
        if (enhet.getPostadress() == null || enhet.getPostadress().isEmpty()) {
            validationErrors.add("No postadress found for enhet!");
        }
        if (enhet.getPostnummer() == null || enhet.getPostnummer().isEmpty()) {
            validationErrors.add("No postnummer found for enhet!");
        }
        if (enhet.getPostort() == null || enhet.getPostort().isEmpty()) {
            validationErrors.add("No postort found for enhet!");
        }
        if (enhet.getTelefonnummer() == null || enhet.getTelefonnummer().isEmpty()) {
            validationErrors.add("No telefonnummer found for enhet!");
        }

        validateVardgivare(enhet.getVardgivare());

        validateArbetsplatskod(enhet.getArbetsplatskod());
    }

    private void validateArbetsplatskod(II arbetsplatskod) {
        // Fält 17 - arbetsplatskod - Check that we got an element
        if (arbetsplatskod == null) {
            validationErrors.add("No Arbetsplatskod element found!");
            return;
        }

        // Fält 17 arbetsplatskod id
        if (arbetsplatskod.getExtension() == null || arbetsplatskod.getExtension().isEmpty()) {
            validationErrors.add("Arbetsplatskod for enhet not found!");
        }
        // Fält 17 arbetsplatskod o.i.d.
        if (arbetsplatskod.getRoot() == null || !arbetsplatskod.getRoot().equalsIgnoreCase(ARBETSPLATS_CODE_OID)) {
            validationErrors.add("Wrong o.i.d. for arbetsplatskod! Should be " + ARBETSPLATS_CODE_OID);
        }

    }

    private void validateVardgivare(VardgivareType vardgivare) {
        if (vardgivare == null) {
            validationErrors.add("No vardgivare element found!");
            return;
        }

        // Check vardgivare id - mandatory
        if (vardgivare.getVardgivareId() == null || vardgivare.getVardgivareId().getExtension() == null
                || vardgivare.getVardgivareId().getExtension().isEmpty()) {
            validationErrors.add("No vardgivare-id found!");
        }
        // Check vardgivare o.i.d.
        if (vardgivare.getVardgivareId() == null || vardgivare.getVardgivareId().getRoot() == null
                || !vardgivare.getVardgivareId().getRoot().equals(HOS_PERSONAL_OID)) {
            validationErrors.add("Wrong o.i.d. for vardgivareId! Should be " + HOS_PERSONAL_OID);
        }

        // Check vardgivarename - mandatory
        if (vardgivare.getVardgivarnamn() == null || vardgivare.getVardgivarnamn().isEmpty()) {
            validationErrors.add("No vardgivarenamn found!");
        }


    }

    private AktivitetType findAktivitetWithCode(final Aktivitetskod aktivitetskod) {
        return find(lakarutlatande.getAktivitet(), new Predicate<AktivitetType>() {
            @Override
            public boolean apply(AktivitetType aktivitet) {
                return aktivitet.getAktivitetskod() == aktivitetskod;
            }
        }, null);
    }

    private void validateFunktionstillstandAktivitet() {
        // Must be set as this element contains a lot of mandatory information
        if (findFunktionsTillstandType(TypAvFunktionstillstand.AKTIVITET) == null) {
            validationErrors.add("No funktionstillstand - aktivitet element found!");
        }
    }

    private VardkontaktType findVardkontaktTyp(final Vardkontakttyp vardkontaktTyp) {

        return find(lakarutlatande.getVardkontakt(), new Predicate<VardkontaktType>() {
            @Override
            public boolean apply(VardkontaktType vardkontakt) {
                return vardkontakt.getVardkontakttyp() == vardkontaktTyp;

            }
        }, null);
    }

    private ReferensType findReferensTyp(final Referenstyp referenstyp) {

        return find(lakarutlatande.getReferens(), new Predicate<ReferensType>() {
            @Override
            public boolean apply(ReferensType referens) {
                return referens.getReferenstyp() == referenstyp;
            }
        }, null);
    }


    private FunktionstillstandType findFunktionsTillstandType(final TypAvFunktionstillstand funktionstillstandsTyp) {

        return find(lakarutlatande.getFunktionstillstand(), new Predicate<FunktionstillstandType>() {
            @Override
            public boolean apply(FunktionstillstandType funktionstillstand) {
                return funktionstillstand.getTypAvFunktionstillstand() == funktionstillstandsTyp;
            }
        }, null);
    }


    private void validateNonSmittskydd() {

        // Many fields are optional if smittskydd is checked, if not set validate these below
        boolean inSmittskydd = findAktivitetWithCode(Aktivitetskod.AVSTANGNING_ENLIGT_SM_L_PGA_SMITTA) != null;

        if (inSmittskydd) {
            return;
        }

        // Fält 2 - Check that we got a medicinsktTillstand element
        if (lakarutlatande.getMedicinsktTillstand() == null) {
            validationErrors.add("No medicinsktTillstand element found!");
            return;
        }

        // Fält 2 - Medicinskt tillstånd kod - mandatory
        MedicinsktTillstandType medTillstand = lakarutlatande.getMedicinsktTillstand();
        if (medTillstand.getTillstandskod() == null || medTillstand.getTillstandskod().getCode() == null
                || medTillstand.getTillstandskod().getCode().isEmpty()) {
            validationErrors.add("No tillstandskod in medicinsktTillstand found!");
        }

        // Fält 2 - Medicinskt tillstånd kodsystemnamn - mandatory
        if (medTillstand.getTillstandskod() == null
                || medTillstand.getTillstandskod().getCodeSystemName() == null
                || !medTillstand.getTillstandskod().getCodeSystemName().equalsIgnoreCase(ICD_10)) {
            validationErrors.add("Wrong code system name for medicinskt tillstand - tillstandskod (diagnoskod)! Should be " + ICD_10);
        }

        validateKroppsfunktion();
        validateArbetsformoga();
    }

    private void validateKroppsfunktion() {

        // Fält 4 - vänster Check that we got a funktionstillstand - kroppsfunktion element
        FunktionstillstandType inKroppsFunktion = findFunktionsTillstandType(TypAvFunktionstillstand.KROPPSFUNKTION);
        if (inKroppsFunktion == null) {
            validationErrors.add("No funktionstillstand - kroppsfunktion element found!");
            return;
        }

        // Fält 4 - vänster Funktionstillstand - kroppsfunktion
        // beskrivning - mandatory
        if (inKroppsFunktion.getBeskrivning() == null || inKroppsFunktion.getBeskrivning().length() < 1) {
            validationErrors.add("No beskrivning in funktionstillstand - kroppsfunktion found!");
        }

        // Fält 4 - höger översta kryssrutan
        VardkontaktType inUndersokning = findVardkontaktTyp(Vardkontakttyp.MIN_UNDERSOKNING_AV_PATIENTEN);

        // Fält 4 - höger näst översta kryssrutan
        VardkontaktType telefonkontakt = findVardkontaktTyp(Vardkontakttyp.MIN_TELEFONKONTAKT_MED_PATIENTEN);

        // Fält 4 - höger näst nedersta kryssrutan
        ReferensType journal = findReferensTyp(Referenstyp.JOURNALUPPGIFTER);

        // Fält 4 - höger nedersta kryssrutan
        ReferensType inAnnat = findReferensTyp(Referenstyp.ANNAT);

        // Fält 4 - höger Check that we at least got one field set
        if (inUndersokning == null && telefonkontakt == null && journal == null && inAnnat == null) {
            validationErrors.add("No vardkontakt or referens element found ! At least one must be set!");
            return;
        }

        // Fält 4 - höger - 1:a kryssrutan Check that we got a date if choice is set
        if (inUndersokning != null && inUndersokning.getVardkontaktstid() == null) {
            validationErrors.add("No or wrong date for vardkontakt - min undersokning av patienten found!");
        }

        // Fält 4 - höger - 2:a kryssrutan Check that we got a date if choice is set
        if (telefonkontakt != null && telefonkontakt.getVardkontaktstid() == null) {
            validationErrors.add("No or wrong date for vardkontakt - telefonkontakt found!");
        }
        // Fält 4 - höger - 3:e kryssrutan Check that we got a date if choice is set
        if (journal != null && journal.getDatum() == null) {
            validationErrors.add("No or wrong date for referens - journal found!");
        }

        // Fält 4 - höger - 4:e kryssrutan Check that we got a date if choice is set
        if (inAnnat != null && inAnnat.getDatum() == null) {
            validationErrors.add("No or wrong date for referens - annat found!");
        }


    }

    private SysselsattningType findTypAvSysselsattning(List<SysselsattningType> sysselsattnings, final TypAvSysselsattning sysselsattningsTyp) {

        return find(sysselsattnings, new Predicate<SysselsattningType>() {
            @Override
            public boolean apply(SysselsattningType sysselsattning) {
                return sysselsattning.getTypAvSysselsattning() == sysselsattningsTyp;
            }
        }, null);
    }

    private void validateArbetsformoga() {
        // Fält 8a - Check that we got a arbetsformaga element

        FunktionstillstandType aktivitet = findFunktionsTillstandType(TypAvFunktionstillstand.AKTIVITET);
        if (aktivitet == null || aktivitet.getArbetsformaga() == null) {
            validationErrors.add("No arbetsformaga element found for field 8a!");
            return;
        }

        // Fält 8a
        SysselsattningType inArbete = findTypAvSysselsattning(aktivitet.getArbetsformaga().getSysselsattning(), TypAvSysselsattning.NUVARANDE_ARBETE);
        SysselsattningType inArbetslos = findTypAvSysselsattning(aktivitet.getArbetsformaga().getSysselsattning(), TypAvSysselsattning.ARBETSLOSHET);
        SysselsattningType inForaldraledig = findTypAvSysselsattning(aktivitet.getArbetsformaga().getSysselsattning(), TypAvSysselsattning.FORALDRALEDIGHET);

        // Fält 8a - Check that we at least got one choice
        if (inArbete == null && inArbetslos == null && inForaldraledig == null) {
            validationErrors.add("No sysselsattning element found for field 8a! Nuvarande arbete, arbestloshet or foraldraledig should be set.");
            return;
        }

        ArbetsuppgiftType arbetsBeskrivning = aktivitet.getArbetsformaga().getArbetsuppgift();
        // Fält 8a - Check that we got a arbetsuppgift element if arbete is set
        if (inArbete != null && arbetsBeskrivning == null) {
            validationErrors.add("No arbetsuppgift element found when arbete set in field 8a!.");
            return;
        }

        // Fält 8a - 1:a kryssrutan - beskrivning
        if (inArbete != null && (arbetsBeskrivning.getTypAvArbetsuppgift() == null || arbetsBeskrivning.getTypAvArbetsuppgift().isEmpty())) {
            validationErrors.add("No typAvArbetsuppgift found when arbete set in field 8a!.");
            return;
        }
    }

    private ArbetsformagaNedsattningType findArbetsformaga(List<ArbetsformagaNedsattningType> arbetsformagaNedsattning, final Nedsattningsgrad nedsattningsgrad) {

        return find(arbetsformagaNedsattning, new Predicate<ArbetsformagaNedsattningType>() {
            @Override
            public boolean apply(ArbetsformagaNedsattningType nedsattning) {
                return nedsattning.getNedsattningsgrad() == nedsattningsgrad;
            }
        }, null);
    }


    private void validateArbetsformogaNedsatting(FunktionstillstandType aktivitet) {

        validateNedsatting(aktivitet);
        validatePrognos(aktivitet);
    }


    private void validateNedsatting(FunktionstillstandType aktivitet) {
        // Fält 8b - kryssruta 1
        ArbetsformagaNedsattningType nedsatt14del = findArbetsformaga(aktivitet.getArbetsformaga()
                .getArbetsformagaNedsattning(), Nedsattningsgrad.NEDSATT_MED_1_4);

        // Fält 8b - kryssruta 2
        ArbetsformagaNedsattningType nedsatthalften = findArbetsformaga(aktivitet.getArbetsformaga()
                .getArbetsformagaNedsattning(), Nedsattningsgrad.NEDSATT_MED_1_2);

        // Fält 8b - kryssruta 3
        ArbetsformagaNedsattningType nedsatt34delar = findArbetsformaga(aktivitet.getArbetsformaga()
                .getArbetsformagaNedsattning(), Nedsattningsgrad.NEDSATT_MED_3_4);

        // Fält 8b - kryssruta 4
        ArbetsformagaNedsattningType heltNedsatt = findArbetsformaga(aktivitet.getArbetsformaga()
                .getArbetsformagaNedsattning(), Nedsattningsgrad.HELT_NEDSATT);

        // Check that we at least got one choice
        if (nedsatt14del == null && nedsatthalften == null && nedsatt34delar == null && heltNedsatt == null) {
            validationErrors.add("No arbetsformaganedsattning element found 8b!.");
            return;
        }
        // Fält 8b - kryssruta 1 - varaktighet From
        if (nedsatt14del != null && nedsatt14del.getVaraktighetFrom() == null) {
            validationErrors.add("No or wrong date for nedsatt 1/4 from date found!");
        }
        // Fält 8b - kryssruta 1 - varaktighet Tom
        if (nedsatt14del != null && nedsatt14del.getVaraktighetTom() == null) {
            validationErrors.add("No or wrong date for nedsatt 1/4 tom date found!");
        }
        // Fält 8b - kryssruta 2 - varaktighet From
        if (nedsatthalften != null && nedsatthalften.getVaraktighetFrom() == null) {
            validationErrors.add("No or wrong date for nedsatt 1/2 from date found!");
        }
        // Fält 8b - kryssruta 2 - varaktighet Tom
        if (nedsatthalften != null && nedsatthalften.getVaraktighetTom() == null) {
            validationErrors.add("No or wrong date for nedsatt 1/2 tom date found!");
        }
        // Fält 8b - kryssruta 3 - varaktighet From
        if (nedsatt34delar != null && nedsatt34delar.getVaraktighetFrom() == null) {
            validationErrors.add("No or wrong date for nedsatt 3/4 from date found!");
        }
        // Fält 8b - kryssruta 3 - varaktighet Tom
        if (nedsatt34delar != null && nedsatt34delar.getVaraktighetTom() == null) {
            validationErrors.add("No or wrong date for nedsatt 3/4 tom date found!");
        }
        // Fält 8b - kryssruta 4 - varaktighet From
        if (heltNedsatt != null && heltNedsatt.getVaraktighetFrom() == null) {
            validationErrors.add("No or wrong date for helt nedsatt from date found!");
        }
        // Fält 8b - kryssruta 4 - varaktighet Tom
        if (heltNedsatt != null && heltNedsatt.getVaraktighetTom() == null) {
            validationErrors.add("No or wrong date for helt nedsatt tom date found!");
        }
    }

    private void validatePrognos(FunktionstillstandType aktivitet) {

        // TODO - do we really reach this function - according to the XML schema, only max one prognos can be sent!!!

        // Fält 10 - Prognosangivelse - optional
        boolean inArbetsformagaAterstallasHelt = false;
        boolean inArbetsformagaAterstallasDelvis = false;
        boolean inArbetsformagaEjAterstallas = false;
        boolean inArbetsformagaGarEjAttBedomma = false;

        if (aktivitet.getArbetsformaga().getPrognosangivelse() != null) {
            inArbetsformagaAterstallasHelt = aktivitet.getArbetsformaga().getPrognosangivelse() == Prognosangivelse.ATERSTALLAS_HELT;
            inArbetsformagaAterstallasDelvis = aktivitet.getArbetsformaga().getPrognosangivelse() == Prognosangivelse.ATERSTALLAS_DELVIS;
            inArbetsformagaEjAterstallas = aktivitet.getArbetsformaga().getPrognosangivelse() == Prognosangivelse.INTE_ATERSTALLAS;
            inArbetsformagaGarEjAttBedomma = aktivitet.getArbetsformaga().getPrognosangivelse() == Prognosangivelse.DET_GAR_INTE_ATT_BEDOMMA;
        }

        // If we got more then one prognoselement these will not be read as
        // only the first is set!
        int inPrognosCount = 0;
        if (inArbetsformagaAterstallasHelt) {
            inPrognosCount++;
        }
        if (inArbetsformagaAterstallasDelvis) {
            inPrognosCount++;
        }
        if (inArbetsformagaEjAterstallas) {
            inPrognosCount++;
        }
        if (inArbetsformagaGarEjAttBedomma) {
            inPrognosCount++;
        }

        // Fält 10 - Prognosangivelse - Check that we only got one choice
        if (inPrognosCount >= 2) {
            validationErrors.add("Only one prognosangivelse should be set for field 10.");
        }
    }


    private void validateRessatt() {
        // Fält 11 - optional
        AktivitetType inForandratRessatt = findAktivitetWithCode(Aktivitetskod.FORANDRAT_RESSATT_TILL_ARBETSPLATSEN_AR_AKTUELLT);
        AktivitetType inEjForandratRessatt = findAktivitetWithCode(Aktivitetskod.FORANDRAT_RESSATT_TILL_ARBETSPLATSEN_AR_EJ_AKTUELLT);

        // Fält 11 - If set only one should be set
        if (inForandratRessatt != null && inEjForandratRessatt != null) {
            validationErrors.add("Only one forandrat ressatt could be set for field 11.");
        }
    }

    private void validateKommentar() {

        // Fält 13 - Upplysningar - optional
        // If field 4 annat satt or field 10 går ej att bedömma is set then
        // field 13 should contain data.
        String kommentar = lakarutlatande.getKommentar();
        ReferensType annat = findReferensTyp(Referenstyp.ANNAT);

        FunktionstillstandType aktivitetFunktion = findFunktionsTillstandType(TypAvFunktionstillstand.AKTIVITET);
        boolean garEjAttBedomma = aktivitetFunktion != null && aktivitetFunktion.getArbetsformaga() != null && aktivitetFunktion.getArbetsformaga().getPrognosangivelse() == Prognosangivelse.DET_GAR_INTE_ATT_BEDOMMA;

        if ((annat != null || garEjAttBedomma) && (kommentar == null || kommentar.isEmpty())) {
            validationErrors.add("Upplysningar should contain data as field 4 or fields 10 is checked.");
        }
    }

    private void validateSignering() {
        // Fält 14 - Signeringstidpunkt
        if (lakarutlatande.getSigneringsdatum() == null) {
            validationErrors.add("Signeringsdatum must be set (14)");
        }
    }
}
