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

package se.inera.intyg.minaintyg.specifications.pages

class IntygTsDiabetesPage extends IntygPage {

    static content = {
        patientnamn { $("#patient-name") }
        patientpersonnummer { $("#patient-crn") }
        intygAvser { $("#intygAvser") }
        identitet { $("#identitet ") }
        observationsperiod { $("#observationsperiod") }
        diabetestyp { $("#diabetestyp") }
        endastKost { $("#endastKost") }
        tabletter { $("#tabletter") }
        insulin { $("#insulin") }
        insulinBehandlingsperiod { $("#insulinBehandlingsperiod") }
        annanBehandlingBeskrivning { $("#annanBehandlingBeskrivning") }
        kunskapOmAtgarder { $("#kunskapOmAtgarder") }
        teckenNedsattHjarnfunktion { $("#teckenNedsattHjarnfunktion") }
        saknarFormagaKannaVarningstecken { $("#saknarFormagaKannaVarningstecken") }
        allvarligForekomst { $("#allvarligForekomst") }
        allvarligForekomstBeskrivning { $("#allvarligForekomstBeskrivning") }
        allvarligForekomstTrafiken { $("#allvarligForekomstTrafiken") }
        allvarligForekomstTrafikBeskrivning { $("#allvarligForekomstTrafikBeskrivning") }
        egenkontrollBlodsocker { $("#egenkontrollBlodsocker") }
        allvarligForekomstVakenTid { $("#allvarligForekomstVakenTid") }
        allvarligForekomstVakenTidObservationstid { $("#allvarligForekomstVakenTidObservationstid") }
        separatOgonlakarintyg { $("#separatOgonlakarintyg") }
        synfaltsprovningUtanAnmarkning { $("#synfaltsprovningUtanAnmarkning") }
        hogerutanKorrektion { $("#hogerutanKorrektion") }
        hogermedKorrektion { $("#hogermedKorrektion") }
        vansterutanKorrektion { $("#vansterutanKorrektion") }
        vanstermedKorrektion { $("#vanstermedKorrektion") }
        binokulartutanKorrektion { $("#binokulartutanKorrektion") }
        binokulartmedKorrektion { $("#binokulartmedKorrektion") }
        diplopi { $("#diplopi") }
        lamplighetInnehaBehorighet { $("#lamplighetInnehaBehorighet") }
        bedomningKanInteTaStallning { $("#bedomningKanInteTaStallning") }
        kommentar { $("#kommentar") }
        kommentarEjAngivet { $("#kommentarEjAngivet") }
        bedomning { $("#bedomning") }
        lakareSpecialKompetens { $("#lakareSpecialKompetens") }
        signeringsdatum { $("#signeringsdatum") }
        vardperson_namn { $("#physician-name-id") }
        vardperson_enhetsnamn { $("#careunit-name-id") }
        vardenhet_postadress { $("#careunit-postal_address") }
        vardenhet_postnummer { $("#careunit-postal_code") }
        vardenhet_postort { $("#careunit-postal_city") }
        vardenhet_telefonnummer { $("#careunit-postal_phone") }
    }
}
