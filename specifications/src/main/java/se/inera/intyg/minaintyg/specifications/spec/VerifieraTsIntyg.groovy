/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
