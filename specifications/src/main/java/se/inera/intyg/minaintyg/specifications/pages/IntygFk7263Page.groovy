/*
 * Copyright (C) 2015 Inera AB (http://www.inera.se)
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

package se.inera.intyg.minaintyg.specifications.pages

class IntygFk7263Page extends IntygPage {

    static content = {
        // Patient
        patientnamn { $("#patient-name") }
        personnummer { $("#patient-crn") }

        // Utfärdare
        vardenhet { $("#careunit") }
        vardgivare { $("#caregiver") }

        // Fält 1
        smittskyddJa { $("#smittskydd-yes") }
        smittskyddNej { $("#smittskydd-no") }

        // Fält 2
//        diagnosKodverk { $("#diagnosKodverk") }
        diagnosKod { $("#diagnosKod") }
        diagnosBeskrivning { $("#diagnosBeskrivning") }

        // Fält 3
        sjukdomsforlopp { $("#sjukdomsforlopp") }

        // Fält 4
        funktionsnedsattning { $("#funktionsnedsattning") }

        // Fält 4b
        undersokningAvPatienten { $($("#undersokningAvPatienten"), $("span")) }
        telefonkontaktMedPatienten { $($("#telefonkontaktMedPatienten"), $("span")) }
        journaluppgifter { $($("#journaluppgifter"), $("span")) }
        annanReferens { $($("#annanReferens"), $("span")) }

        // Fält 5
        aktivitetsbegransning { $("#aktivitetsbegransning") }

        // Fält 6a
//         rekommendationRessatt { $("#rekommendationRessatt") }
        rekommendationKontaktArbetsformedlingen { $("#rekommendationKontaktArbetsformedlingen") }
        rekommendationKontaktForetagshalsovarden { $("#rekommendationKontaktForetagshalsovarden") }
        rekommendationOvrigt { $("#rekommendationOvrigt") }

        // Fält 6b
        atgardSjukvard { $("#atgardSjukvard") }
        atgardAnnan { $("#atgardAnnan") }

        // Fält 7
        rehabiliteringJa { $("#rehabilitering-yes") }
        rehabiliteringNej { $("#rehabilitering-no") }
        rehabiliteringGarInteAttBedoma { $("#rehabilitering-unjudgeable") }

        // Fält 8a
        nuvarandeArbetsuppgifter { $("#nuvarandeArbetsuppgifter") }
        arbetslos { $("#arbetslos") }
        foraldraledig { $("#foraldraledig") }

        // Fält 8b
        nedsattMed25 { $("#nedsattMed25").children("span")[0] }
        nedsattMed25Beskrivning { $("#nedsattMed25").children("span")[1] }
        nedsattMed50 { $("#nedsattMed50").children("span")[0] }
        nedsattMed50Beskrivning { $("#nedsattMed50").children("span")[1] }
        nedsattMed75 { $("#nedsattMed75").children("span")[0] }
        nedsattMed75Beskrivning { $("#nedsattMed75").children("span")[1] }
        nedsattMed100 { $("#nedsattMed100").children("span")[0] }
        nedsattMed100Beskrivning { $("#nedsattMed100").children("span")[1] }

        // Fält 9
        arbetsformagaPrognos { $("#arbetsformagaPrognos") }

        // Fält 10
        arbetsformagaPrognosJa { $("#arbetsformagaPrognos-yes") }
        arbetsformagaPrognosDelvisJa { $("#arbetsformagaPrognos-partialyes") }
        arbetsformagaPrognosNej { $("#arbetsformagaPrognos-no") }
        arbetsformagaPrognosGarInteAttBedoma { $("#arbetsformagaPrognos-unjudgeable") }

        // Fält 11
        resaTillArbetetJa { $("#resaTillArbetet-yes") }
        resaTillArbetetNej { $("#resaTillArbetet-no") }

        // Fält 12
        kontaktFkJa { $("#kontaktFk-yes") }
        kontaktFkNej { $("#kontaktFk-no") }

        // Fält 13
        kommentar { $("#kommentar") }

        // Fält 14
        signeringsdatum { $("#signeringsdatum") }

        // Fält 15
        vardpersonNamn { $("#vardperson-namn") }
        vardpersonEnhet { $("#vardperson-enhet") }
        vardpersonPostadress { $("#vardperson-enhet") }
        vardpersonPostnummerOrt { $("#vardperson-postnummer-ort") }
        vardpersonTelefonnummer { $("#vardperson-telefonnummer") }

        // Fält 17
        forskrivarkodOchArbetsplatskod { $("#forskrivarkodOchArbetsplatskod") }

    }

}
