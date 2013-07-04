package se.inera.certificate.integration.validator;

import static java.util.Arrays.asList;

import com.google.common.base.Joiner;
import iso.v21090.dt.v1.II;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.LakarutlatandeType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.MedicinsktTillstandType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.EnhetType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.HosPersonalType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.PatientType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.VardgivareType;

import java.util.ArrayList;
import java.util.List;

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

    public LakarutlatandeValidator(LakarutlatandeType lakarutlatande) {
        this.lakarutlatande = lakarutlatande;
    }

    public void validate() {

        validatePatient(lakarutlatande.getPatient());
        validateSkapadAvHosPersonal(lakarutlatande.getSkapadAvHosPersonal());
        validateMedicinsktTillstand(lakarutlatande.getMedicinsktTillstand());

        if (!validationErrors.isEmpty()) {
            throw new ValidationException(validationErrors);
        }
    }

    private void validatePatient(PatientType patient) {

        // Check patient o.i.d.
        if (patient.getPersonId().getRoot() == null || !PATIENT_ID_OIDS.contains(patient.getPersonId().getRoot())) {
            validationErrors.add(String.format("Wrong o.i.d. for Patient Id! Should be %s", Joiner.on(" or ").join(PATIENT_ID_OIDS)));
        }
    }

    private void validateSkapadAvHosPersonal(HosPersonalType hosPersonal) {

        // Check lakar id o.i.d.
        if (hosPersonal.getPersonalId().getRoot() == null
                || !hosPersonal.getPersonalId().getRoot().equals(HOS_PERSONAL_OID)) {
            validationErrors.add("Wrong o.i.d. for personalId! Should be " + HOS_PERSONAL_OID);
        }

        validateHosPersonalEnhet(hosPersonal.getEnhet());
    }

    private void validateHosPersonalEnhet(EnhetType enhet) {

        // Check enhets o.i.d
        if (enhet.getEnhetsId().getRoot() == null
                || !enhet.getEnhetsId().getRoot().equals(ENHET_OID)) {
            validationErrors.add("Wrong o.i.d. for enhetsId! Should be " + ENHET_OID);
        }

        validateVardgivare(enhet.getVardgivare());

        validateArbetsplatskod(enhet.getArbetsplatskod());
    }

    private void validateArbetsplatskod(II arbetsplatskod) {
        // Fält 17 arbetsplatskod o.i.d.
        if (arbetsplatskod.getRoot() == null || !arbetsplatskod.getRoot().equalsIgnoreCase(ARBETSPLATS_CODE_OID)) {
            validationErrors.add("Wrong o.i.d. for arbetsplatskod! Should be " + ARBETSPLATS_CODE_OID);
        }
    }

    private void validateVardgivare(VardgivareType vardgivare) {
        // Check vardgivare o.i.d.
        if (vardgivare.getVardgivareId() == null || vardgivare.getVardgivareId().getRoot() == null
                || !vardgivare.getVardgivareId().getRoot().equals(HOS_PERSONAL_OID)) {
            validationErrors.add("Wrong o.i.d. for vardgivareId! Should be " + HOS_PERSONAL_OID);
        }
    }

    private void validateMedicinsktTillstand(MedicinsktTillstandType medicinsktTillstand) {
        // Fält 2 - Medicinskt tillstånd kodsystemnamn - mandatory
        if (medicinsktTillstand != null) {
            if (medicinsktTillstand.getTillstandskod() == null
                    || !ICD_10.equalsIgnoreCase(medicinsktTillstand.getTillstandskod().getCodeSystemName())) {
                validationErrors.add("Wrong code system name for medicinskt tillstand - tillstandskod (diagnoskod)! Should be " + ICD_10);
            }
        }
    }
}
