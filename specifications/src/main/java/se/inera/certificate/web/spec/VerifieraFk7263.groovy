package se.inera.certificate.web.spec

import org.codehaus.groovy.runtime.StackTraceUtils
import se.inera.certificate.spec.Browser

class VerifieraFk7263 {

    public VerifieraFk7263() {

    }

    String getCurrentMethodName(){
        def marker = new Throwable()
        return StackTraceUtils.sanitize(marker).stackTrace[1].methodName
    }

    String getStringResult(field) {
        def result = ''
        Browser.drive {
            if (!page."$field".isDisplayed()) {
                result = "notshown"
            } else {
                result = page."$field".text().trim()
            }
        }
        result
    }

    // Patient
    String patientnamn() {
        getStringResult(getCurrentMethodName())
    }

    String personnummer() {
        getStringResult(getCurrentMethodName())
    }

    // Utfärdare
    String vardenhet() {
        getStringResult(getCurrentMethodName())
    }

    String vardgivare() {
        getStringResult(getCurrentMethodName())
    }

    // Fält 1
    String smittskyddJa() {
        getStringResult(getCurrentMethodName())
    }

    String smittskyddNej() {
        getStringResult(getCurrentMethodName())
    }

    // Fält 2
    String diagnosKod() {
        getStringResult(getCurrentMethodName())
    }

    String diagnosBeskrivning() {
        getStringResult(getCurrentMethodName())
    }

    // Fält 3
    String sjukdomsforlopp() {
        getStringResult(getCurrentMethodName())
    }

    // Fält 4
    String funktionsnedsattning() {
        getStringResult(getCurrentMethodName())
    }

    // Fält 4b
    String undersokningAvPatienten() {
        getStringResult(getCurrentMethodName())
    }

    String telefonkontaktMedPatienten() {
        getStringResult(getCurrentMethodName())
    }

    String journaluppgifter() {
        getStringResult(getCurrentMethodName())
    }

    String annanReferens() {
        getStringResult(getCurrentMethodName())
    }

    // Fält 5
    String aktivitetsbegransning() {
        getStringResult(getCurrentMethodName())
    }

    // Fält 6a
    String rekommendationKontaktArbetsformedlingen() {
        getStringResult(getCurrentMethodName())
    }

    String rekommendationKontaktForetagshalsovarden() {
        getStringResult(getCurrentMethodName())
    }

    String rekommendationOvrigt() {
        getStringResult(getCurrentMethodName())
    }

    // Fält 6b
    String atgardSjukvard() {
        getStringResult(getCurrentMethodName())
    }

    String atgardAnnan() {
        getStringResult(getCurrentMethodName())
    }

    // Fält 7
    String rehabiliteringJa() {
        getStringResult(getCurrentMethodName())
    }

    String rehabiliteringNej() {
        getStringResult(getCurrentMethodName())
    }

    String rehabiliteringGarInteAttBedoma() {
        getStringResult(getCurrentMethodName())
    }

    // Fält 8a
    String nuvarandeArbetsuppgifter() {
        getStringResult(getCurrentMethodName())
    }

    String arbetslos() {
        getStringResult(getCurrentMethodName())
    }

    String foraldraledig() {
        getStringResult(getCurrentMethodName())
    }

    // Fält 8b
    String nedsattMed25() {
        getStringResult(getCurrentMethodName())
    }

    String nedsattMed25Beskrivning() {
        getStringResult(getCurrentMethodName())
    }

    String nedsattMed50() {
        getStringResult(getCurrentMethodName())
    }

    String nedsattMed50Beskrivning() {
        getStringResult(getCurrentMethodName())
    }

    String nedsattMed75() {
        getStringResult(getCurrentMethodName())
    }

    String nedsattMed75Beskrivning() {
        getStringResult(getCurrentMethodName())
    }

    String nedsattMed100() {
        getStringResult(getCurrentMethodName())
    }

    String nedsattMed100Beskrivning() {
        getStringResult(getCurrentMethodName())
    }

    // Fält 9
    String arbetsformagaPrognos() {
        getStringResult(getCurrentMethodName())
    }

    // Fält 10
    String arbetsformagaPrognosJa() {
        getStringResult(getCurrentMethodName())
    }

    String arbetsformagaPrognosDelvisJa() {
        getStringResult(getCurrentMethodName())
    }

    String arbetsformagaPrognosNej() {
        getStringResult(getCurrentMethodName())
    }

    String arbetsformagaPrognosGarInteAttBedoma() {
        getStringResult(getCurrentMethodName())
    }

    // Fält 11
    String resaTillArbetetJa() {
        getStringResult(getCurrentMethodName())
    }

    String resaTillArbetetNej() {
        getStringResult(getCurrentMethodName())
    }

    // Fält 12
    String kontaktFkJa() {
        getStringResult(getCurrentMethodName())
    }

    String kontaktFkNej() {
        getStringResult(getCurrentMethodName())
    }

    // Fält 13
    String kommentar() {
        getStringResult(getCurrentMethodName())
    }

    // Fält 14
    String signeringsdatum() {
        getStringResult(getCurrentMethodName())
    }

    // Fält 15
    String vardpersonNamn() {
        getStringResult(getCurrentMethodName())
    }

    String vardpersonEnhet() {
        getStringResult(getCurrentMethodName())
    }

    String vardpersonPostadress() {
        getStringResult(getCurrentMethodName())
    }

    String vardpersonPostnummerOrt() {
        getStringResult(getCurrentMethodName())
    }

    String vardpersonTelefonnummer() {
        getStringResult(getCurrentMethodName())
    }

    // Fält 17
    String forskrivarkodOchArbetsplatskod() {
        getStringResult(getCurrentMethodName())
    }

}
