package se.inera.intyg.minaintyg.specifications.spec


class VerifieraFk7263 extends VerifieraIntyg {

    // Patient
    String patientnamn() {
        getStringResult("patientnamn")
    }

    String personnummer() {
        getStringResult("personnummer")
    }

    // Utfärdare
    String vardenhet() {
        getStringResult("vardenhet")
    }

    String vardgivare() {
        getStringResult("vardgivare")
    }

    // Fält 1
    String smittskyddJa() {
        getStringResult("smittskyddJa")
    }

    String smittskyddNej() {
        getStringResult("smittskyddNej")
    }

    // Fält 2
    String diagnosKod() {
        getStringResult("diagnosKod")
    }

    String diagnosBeskrivning() {
        getStringResult("diagnosBeskrivning")
    }

    // Fält 3
    String sjukdomsforlopp() {
        getStringResult("sjukdomsforlopp")
    }

    // Fält 4
    String funktionsnedsattning() {
        getStringResult("funktionsnedsattning")
    }

    // Fält 4b
    String undersokningAvPatienten() {
        getStringResult("undersokningAvPatienten")
    }

    String telefonkontaktMedPatienten() {
        getStringResult("telefonkontaktMedPatienten")
    }

    String journaluppgifter() {
        getStringResult("journaluppgifter")
    }

    String annanReferens() {
        getStringResult("annanReferens")
    }

    // Fält 5
    String aktivitetsbegransning() {
        getStringResult("aktivitetsbegransning")
    }

    // Fält 6a
    String rekommendationKontaktArbetsformedlingen() {
        getStringResult("rekommendationKontaktArbetsformedlingen")
    }

    String rekommendationKontaktForetagshalsovarden() {
        getStringResult("rekommendationKontaktForetagshalsovarden")
    }

    String rekommendationOvrigt() {
        getStringResult("rekommendationOvrigt")
    }

    // Fält 6b
    String atgardSjukvard() {
        getStringResult("atgardSjukvard")
    }

    String atgardAnnan() {
        getStringResult("atgardAnnan")
    }

    // Fält 7
    String rehabiliteringJa() {
        getStringResult("rehabiliteringJa")
    }

    String rehabiliteringNej() {
        getStringResult("rehabiliteringNej")
    }

    String rehabiliteringGarInteAttBedoma() {
        getStringResult("rehabiliteringGarInteAttBedoma")
    }

    // Fält 8a
    String nuvarandeArbetsuppgifter() {
        getStringResult("nuvarandeArbetsuppgifter")
    }

    String arbetslos() {
        getStringResult("arbetslos")
    }

    String foraldraledig() {
        getStringResult("foraldraledig")
    }

    // Fält 8b
    String nedsattMed25() {
        getStringResult("nedsattMed25")
    }

    String nedsattMed25Beskrivning() {
        getStringResult("nedsattMed25Beskrivning")
    }

    String nedsattMed50() {
        getStringResult("nedsattMed50")
    }

    String nedsattMed50Beskrivning() {
        getStringResult("nedsattMed50Beskrivning")
    }

    String nedsattMed75() {
        getStringResult("nedsattMed75")
    }

    String nedsattMed75Beskrivning() {
        getStringResult("nedsattMed75Beskrivning")
    }

    String nedsattMed100() {
        getStringResult("nedsattMed100")
    }

    String nedsattMed100Beskrivning() {
        getStringResult("nedsattMed100Beskrivning")
    }

    // Fält 9
    String arbetsformagaPrognos() {
        getStringResult("arbetsformagaPrognos")
    }

    // Fält 10
    String arbetsformagaPrognosJa() {
        getStringResult("arbetsformagaPrognosJa")
    }

    String arbetsformagaPrognosDelvisJa() {
        getStringResult("arbetsformagaPrognosDelvisJa")
    }

    String arbetsformagaPrognosNej() {
        getStringResult("arbetsformagaPrognosNej")
    }

    String arbetsformagaPrognosGarInteAttBedoma() {
        getStringResult("arbetsformagaPrognosGarInteAttBedoma")
    }

    // Fält 11
    String resaTillArbetetJa() {
        getStringResult("resaTillArbetetJa")
    }

    String resaTillArbetetNej() {
        getStringResult("resaTillArbetetNej")
    }

    // Fält 12
    String kontaktFkJa() {
        getStringResult("kontaktFkJa")
    }

    String kontaktFkNej() {
        getStringResult("kontaktFkNej")
    }

    // Fält 13
    String kommentar() {
        getStringResult("kommentar")
    }

    // Fält 14
    String signeringsdatum() {
        getStringResult("signeringsdatum")
    }

    // Fält 15
    String vardpersonNamn() {
        getStringResult("vardpersonNamn")
    }

    String vardpersonEnhet() {
        getStringResult("vardpersonEnhet")
    }

    String vardpersonPostadress() {
        getStringResult("vardpersonPostadress")
    }

    String vardpersonPostnummerOrt() {
        getStringResult("vardpersonPostnummerOrt")
    }

    String vardpersonTelefonnummer() {
        getStringResult("vardpersonTelefonnummer")
    }

    // Fält 17
    String forskrivarkodOchArbetsplatskod() {
        getStringResult("forskrivarkodOchArbetsplatskod")
    }

}
