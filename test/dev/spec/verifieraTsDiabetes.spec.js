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

/*globals browser,protractor */
/*globals pages */
/*globals describe,it,helpers */
'use strict';

var specHelper = miTestTools.helpers.spec;
var restHelper = miTestTools.helpers.rest;

var welcomePage = miTestTools.pages.welcomePage;
var viewPage = miTestTools.pages.viewPage;
var inboxPage = miTestTools.pages.inboxPage;

var genericTestDataBuilder = miTestTools.testdata.generic;

describe('Visa intyg ts-diabetes', function() {

    var personId = '19010101-0101';
    var intygsId = null;

    beforeAll(function() {
        restHelper.setConsent(personId);

        var tsDiabetesIntyg = genericTestDataBuilder.getTsDiabetes(personId);
        intygsId = tsDiabetesIntyg.id;
        restHelper.createIntyg(tsDiabetesIntyg);
    });

    afterAll(function() {
        restHelper.deleteConsent(personId);
        restHelper.deleteIntyg(intygsId);
    });

    describe('Verifiera ts-diabetes', function() {

        beforeEach(function() {
            browser.ignoreSynchronization = false;
        });

        it('Logga in', function() {
            welcomePage.get();
            specHelper.waitForAngularTestability();
            welcomePage.login(personId, false);
            specHelper.waitForAngularTestability();
        });

        it('Visa ett intyg och verifiera att det är rätt intyg och av rätt typ', function() {
            expect(inboxPage.isAt()).toBeTruthy();
            expect(inboxPage.certificateExists(intygsId)).toBeTruthy();
            inboxPage.viewCertificate(intygsId);
            expect(viewPage.isAt()).toBeTruthy();
        });

        it('Verifiera intyg avser och identitet styrkt genom', function() {
            expect(viewPage.getTextContent('intygAvser-korkortstyp-0')).toEqual('AM');
            expect(viewPage.getTextContent('intygAvser-korkortstyp-1')).toEqual('A1');
            expect(viewPage.getTextContent('intygAvser-korkortstyp-2')).toEqual('A2');
            expect(viewPage.getTextContent('intygAvser-korkortstyp-3')).toEqual('A');
            expect(viewPage.getTextContent('intygAvser-korkortstyp-4')).toEqual('B');
            expect(viewPage.getTextContent('intygAvser-korkortstyp-5')).toEqual('BE');
            expect(viewPage.getTextContent('intygAvser-korkortstyp-6')).toEqual('Traktor');
            expect(viewPage.getTextContent('intygAvser-korkortstyp-7')).toEqual('C1');
            expect(viewPage.getTextContent('intygAvser-korkortstyp-8')).toEqual('C1E');
            expect(viewPage.getTextContent('intygAvser-korkortstyp-9')).toEqual('C');
            expect(viewPage.getTextContent('intygAvser-korkortstyp-10')).toEqual('CE');
            expect(viewPage.getTextContent('intygAvser-korkortstyp-11')).toEqual('D1');
            expect(viewPage.getTextContent('intygAvser-korkortstyp-12')).toEqual('D1E');
            expect(viewPage.getTextContent('intygAvser-korkortstyp-13')).toEqual('D');
            expect(viewPage.getTextContent('intygAvser-korkortstyp-14')).toEqual('DE');
            expect(viewPage.getTextContent('intygAvser-korkortstyp-15')).toEqual('Taxi');
            expect(viewPage.getTextContent('vardkontakt-idkontroll')).toEqual('Pass');
        });

        it('Verifiera "1. Allmänt"', function() {
            expect(viewPage.getTextContent('diabetes-observationsperiod')).toEqual('2012');
            expect(viewPage.getTextContent('diabetes-diabetestyp')).toEqual('Typ 2');
            expect(viewPage.getTextContent('diabetes-endastKost-diabetes-tabletter-diabetes-insulin-0')).toEqual('Endast kost');
            expect(viewPage.getTextContent('diabetes-endastKost-diabetes-tabletter-diabetes-insulin-1')).toEqual('Tabletter');
            expect(viewPage.getTextContent('diabetes-endastKost-diabetes-tabletter-diabetes-insulin-2')).toEqual('Insulin');
            expect(viewPage.getTextContent('diabetes-insulinBehandlingsperiod')).toEqual('2012');
            expect(viewPage.getTextContent('diabetes-annanBehandlingBeskrivning')).toEqual('Hypnos');
        });

        it('Verifiera "2. Hypoglykemier (lågt blodsocker)"', function() {
            expect(viewPage.getTextContent('hypoglykemier-kunskapOmAtgarder')).toEqual('Ja');
            expect(viewPage.getTextContent('hypoglykemier-teckenNedsattHjarnfunktion')).toEqual('Ja');
            expect(viewPage.getTextContent('hypoglykemier-saknarFormagaKannaVarningstecken')).toEqual('Ja');
            expect(viewPage.getTextContent('hypoglykemier-allvarligForekomst')).toEqual('Ja');
            expect(viewPage.getTextContent('hypoglykemier-allvarligForekomstBeskrivning')).toEqual('Beskrivning');
            expect(viewPage.getTextContent('hypoglykemier-allvarligForekomstTrafiken')).toEqual('Ja');
            expect(viewPage.getTextContent('hypoglykemier-allvarligForekomstTrafikBeskrivning')).toEqual('Beskrivning');
            expect(viewPage.getTextContent('hypoglykemier-egenkontrollBlodsocker')).toEqual('Ja');
            expect(viewPage.getTextContent('hypoglykemier-allvarligForekomstVakenTid')).toEqual('Ja');
            expect(viewPage.getTextContent('hypoglykemier-allvarligForekomstVakenTidObservationstid')).toEqual('2012-12-12');
        });

        it('Verifiera "3. Synintyg"', function() {
            expect(viewPage.getTextContent('syn-separatOgonlakarintyg')).toEqual('Nej');
            expect(viewPage.getTextContent('syn-synfaltsprovningUtanAnmarkning')).toEqual('Ja');
            expect(viewPage.getTextContent('syn-row0-col1')).toEqual('0,0');
            expect(viewPage.getTextContent('syn-row0-col2')).toEqual('0,0');
            expect(viewPage.getTextContent('syn-row1-col1')).toEqual('0,0');
            expect(viewPage.getTextContent('syn-row1-col2')).toEqual('0,0');
            expect(viewPage.getTextContent('syn-row2-col1')).toEqual('0,0');
            expect(viewPage.getTextContent('syn-row2-col2')).toEqual('0,0');
            expect(viewPage.getTextContent('syn-diplopi')).toEqual('Ja');
        });

        it('Verifiera "4. Bedömning"', function() {
            expect(viewPage.getTextContent('bedomning-lamplighetInnehaBehorighet')).toEqual('Nej');
            expect(viewPage.getTextContent('bedomning-korkortstyp-0')).toEqual('AM');
            expect(viewPage.getTextContent('bedomning-korkortstyp-1')).toEqual('A1');
            expect(viewPage.getTextContent('bedomning-korkortstyp-2')).toEqual('A2');
            expect(viewPage.getTextContent('bedomning-korkortstyp-3')).toEqual('A');
            expect(viewPage.getTextContent('bedomning-korkortstyp-4')).toEqual('B');
            expect(viewPage.getTextContent('bedomning-korkortstyp-5')).toEqual('BE');
            expect(viewPage.getTextContent('bedomning-korkortstyp-6')).toEqual('Traktor');
            expect(viewPage.getTextContent('bedomning-korkortstyp-7')).toEqual('C1');
            expect(viewPage.getTextContent('bedomning-korkortstyp-8')).toEqual('C1E');
            expect(viewPage.getTextContent('bedomning-korkortstyp-9')).toEqual('C');
            expect(viewPage.getTextContent('bedomning-korkortstyp-10')).toEqual('CE');
            expect(viewPage.getTextContent('bedomning-korkortstyp-11')).toEqual('D1');
            expect(viewPage.getTextContent('bedomning-korkortstyp-12')).toEqual('D1E');
            expect(viewPage.getTextContent('bedomning-korkortstyp-13')).toEqual('D');
            expect(viewPage.getTextContent('bedomning-korkortstyp-14')).toEqual('DE');
            expect(viewPage.getTextContent('bedomning-korkortstyp-15')).toEqual('Taxi');
            expect(viewPage.getTextContent('bedomning-lakareSpecialKompetens')).toEqual('Kronologisk bastuberedning');
        });

        it('Verifiera "Övriga kommentarer och upplysningar"', function() {
            expect(viewPage.getTextContent('kommentar')).toEqual('Kommentarer av det viktiga slaget');
        });

        it('Verifiera skapad av', function() {
            expect(viewPage.getTextContent('fullstandigtNamn')).toEqual('Jan Nilsson');
            expect(viewPage.getTextContent('vardenhet-telefon')).toEqual('Tel: 08-1337');
            expect(viewPage.getTextContent('vardenhet-namn')).toEqual('WebCert Enhet 1, WebCert Vårdgivare 1');
            expect(viewPage.getTextContent('vardenhet-adress')).toEqual('Enhetsvägen 12, 54321 Tumba');
        });
    });

});
