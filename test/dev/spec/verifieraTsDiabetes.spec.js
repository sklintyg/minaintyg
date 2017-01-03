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
            expect(browser.getCurrentUrl()).toContain('ts-diabetes');
            expect(viewPage.certificateId()).toEqual(intygsId);
        });

        it('Verifiera patient och utfärdare', function() {
            expect(viewPage.getTextContent('patient-name')).toEqual('Herr Dundersjuk');
            expect(viewPage.getTextContent('patient-crn')).toEqual(personId);
            expect(viewPage.getTextContent('careunit')).toEqual('WebCert Enhet 1');
            expect(viewPage.getTextContent('caregiver')).toEqual('WebCert Vårdgivare 1');
        });

        it('Verifiera intyg avser och identitet styrkt genom', function() {
            expect(viewPage.getTextContent('intygAvser')).toEqual('AM, A1, A2, A, B, BE, TRAKTOR, C1, C1E, C, CE, D1, D1E, D, DE, TAXI');
            expect(viewPage.getTextContent('identitet')).toEqual('Pass');
        });

        it('Verifiera "1. Allmänt"', function() {
            expect(viewPage.getTextContent('observationsperiod')).toEqual('2012');
            expect(viewPage.getTextContent('diabetestyp')).toEqual('Typ 2');
            expect(viewPage.getTextContent('endastKost')).toEqual('Ja');
            expect(viewPage.getTextContent('tabletter')).toEqual('Ja');
            expect(viewPage.getTextContent('insulin')).toEqual('Ja');
            expect(viewPage.getTextContent('insulinBehandlingsperiod')).toEqual('2012');
            expect(viewPage.getTextContent('annanBehandlingBeskrivning')).toEqual('Hypnos');
        });

        it('Verifiera "2. Hypoglykemier (lågt blodsocker)"', function() {
            expect(viewPage.getTextContent('kunskapOmAtgarder')).toEqual('Ja');
            expect(viewPage.getTextContent('teckenNedsattHjarnfunktion')).toEqual('Ja');
            expect(viewPage.getTextContent('saknarFormagaKannaVarningstecken')).toEqual('Ja');
            expect(viewPage.getTextContent('allvarligForekomst')).toEqual('Ja');
            expect(viewPage.getTextContent('allvarligForekomstBeskrivning')).toEqual('Beskrivning');
            expect(viewPage.getTextContent('allvarligForekomstTrafiken')).toEqual('Ja');
            expect(viewPage.getTextContent('allvarligForekomstTrafikBeskrivning')).toEqual('Beskrivning');
            expect(viewPage.getTextContent('egenkontrollBlodsocker')).toEqual('Ja');
            expect(viewPage.getTextContent('allvarligForekomstVakenTid')).toEqual('Ja');
            expect(viewPage.getTextContent('allvarligForekomstVakenTidObservationstid')).toEqual('2012-12-12');
        });

        it('Verifiera "3. Synintyg"', function() {
            expect(viewPage.getTextContent('separatOgonlakarintyg')).toEqual('Nej');
            expect(viewPage.getTextContent('synfaltsprovningUtanAnmarkning')).toEqual('Ja');
            expect(viewPage.getTextContent('hogerutanKorrektion')).toEqual('0,0');
            expect(viewPage.getTextContent('hogermedKorrektion')).toEqual('0,0');
            expect(viewPage.getTextContent('vansterutanKorrektion')).toEqual('0,0');
            expect(viewPage.getTextContent('vanstermedKorrektion')).toEqual('0,0');
            expect(viewPage.getTextContent('binokulartutanKorrektion')).toEqual('0,0');
            expect(viewPage.getTextContent('binokulartmedKorrektion')).toEqual('0,0');
            expect(viewPage.getTextContent('diplopi')).toEqual('Ja');
        });

        it('Verifiera "4. Bedömning"', function() {
            expect(viewPage.getTextContent('lamplighetInnehaBehorighet')).toEqual('Ej ifyllt');
            expect(viewPage.getTextContent('bedomning')).toEqual('AM, A1, A2, A, B, BE, TRAKTOR, C1, C1E, C, CE, D1, D1E, D, DE, TAXI');
            expect(viewPage.fieldNotShown('bedomningKanInteTaStallning')).toBeTruthy();
            expect(viewPage.getTextContent('lakareSpecialKompetens')).toEqual('Kronologisk bastuberedning');
        });

        it('Verifiera "Övriga kommentarer och upplysningar"', function() {
            expect(viewPage.getTextContent('kommentar')).toEqual('Kommentarer av det viktiga slaget');
        });

        it('Verifiera signeringsdatum', function() {
            expect(viewPage.getTextContent('signeringsdatum')).toEqual('26 maj 2016');
        });

        it('Verifiera skapad av', function() {
            expect(viewPage.getTextContent('physician-name-id')).toEqual('Jan Nilsson');
            expect(viewPage.getTextContent('careunit-name-id')).toEqual('WebCert Enhet 1');
            expect(viewPage.getTextContent('careunit-postal_address')).toEqual('Enhetsvägen 12');
            expect(viewPage.getTextContent('careunit-postal_code')).toEqual('54321');
            expect(viewPage.getTextContent('careunit-postal_city')).toEqual('Tumba');
            expect(viewPage.getTextContent('careunit-postal_phone')).toEqual('08-1337');
        });
    });

});
