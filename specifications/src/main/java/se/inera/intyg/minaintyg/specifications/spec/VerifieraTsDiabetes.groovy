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

class VerifieraTsDiabetes extends VerifieraTsIntyg {

    String observationsperiod() {
        getStringResult("observationsperiod")
    }

    String diabetestyp() {
        getStringResult("diabetestyp")
    }

    String endastKost() {
        getStringResult("endastKost")
    }

    String tabletter() {
        getStringResult("tabletter")
    }

    String insulin() {
        getStringResult("insulin")
    }

    String insulinBehandlingsperiod() {
        getStringResult("insulinBehandlingsperiod")
    }

    String annanBehandlingBeskrivning() {
        getStringResult("annanBehandlingBeskrivning")
    }

    String kunskapOmAtgarder() {
        getStringResult("kunskapOmAtgarder")
    }

    String teckenNedsattHjarnfunktion() {
        getStringResult("teckenNedsattHjarnfunktion")
    }

    String saknarFormagaKannaVarningstecken() {
        getStringResult("saknarFormagaKannaVarningstecken")
    }

    String allvarligForekomst() {
        getStringResult("allvarligForekomst")
    }

    String allvarligForekomstBeskrivning() {
        getStringResult("allvarligForekomstBeskrivning")
    }

    String allvarligForekomstTrafiken() {
        getStringResult("allvarligForekomstTrafiken")
    }

    String allvarligForekomstTrafikBeskrivning() {
        getStringResult("allvarligForekomstTrafikBeskrivning")
    }

    String egenkontrollBlodsocker() {
        getStringResult("egenkontrollBlodsocker")
    }

    String allvarligForekomstVakenTid() {
        getStringResult("allvarligForekomstVakenTid")
    }

    String allvarligForekomstVakenTidObservationstid() {
        getStringResult("allvarligForekomstVakenTidObservationstid")
    }

    String separatOgonlakarintyg() {
        getStringResult("separatOgonlakarintyg")
    }

    String synfaltsprovningUtanAnmarkning() {
        getStringResult("synfaltsprovningUtanAnmarkning")
    }

    String hogerutanKorrektion() {
        getStringResult("hogerutanKorrektion")
    }

    String hogermedKorrektion() {
        getStringResult("hogermedKorrektion")
    }

    String vansterutanKorrektion() {
        getStringResult("vansterutanKorrektion")
    }

    String vanstermedKorrektion() {
        getStringResult("vanstermedKorrektion")
    }

    String binokulartutanKorrektion() {
        getStringResult("binokulartutanKorrektion")
    }

    String binokulartmedKorrektion() {
        getStringResult("binokulartmedKorrektion")
    }

    String diplopi() {
        getStringResult("diplopi")
    }

    String lamplighetInnehaBehorighet() {
        getStringResult("lamplighetInnehaBehorighet")
    }

    String kommentar() {
        getStringResult("kommentar")
    }

}
