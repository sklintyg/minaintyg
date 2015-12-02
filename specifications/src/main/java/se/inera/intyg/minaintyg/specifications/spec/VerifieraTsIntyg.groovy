package se.inera.intyg.minaintyg.specifications.spec

class VerifieraTsIntyg extends VerifieraIntyg {

    String patientnamn() {
        getStringResult("patientnamn")
    }

    String patientpersonnummer() {
        getStringResult("patientpersonnummer")
    }

    String intygAvser() {
        getStringResult("intygAvser")
    }

    String identitet() {
        getStringResult("identitet")
    }

    String bedomning() {
        getStringResult("bedomning")
    }

    String bedomningKanInteTaStallning() {
        getStringResult("bedomningKanInteTaStallning")
    }

    String lakareSpecialKompetens() {
        getStringResult("lakareSpecialKompetens")
    }

    String lakareSpecialKompetensEjAngivet() {
        getStringResult("lakareSpecialKompetensEjAngivet")
    }

    String signeringsdatum() {
        getStringResult("signeringsdatum")
    }

    String vardperson_namn() {
        getStringResult("vardperson_namn")
    }

    String vardperson_enhetsnamn() {
        getStringResult("vardperson_enhetsnamn")
    }

    String vardenhet_postadress() {
        getStringResult("vardenhet_postadress")
    }

    String vardenhet_postnummer() {
        getStringResult("vardenhet_postnummer")
    }

    String vardenhet_postort() {
        getStringResult("vardenhet_postort")
    }

    String vardenhet_telefonnummer() {
        getStringResult("vardenhet_telefonnummer")
    }
}
