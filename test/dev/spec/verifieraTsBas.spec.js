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

xdescribe('Visa intyg ts-bas', function() {

    var personId = '19010101-0101';
    var intygsId = null;

    beforeAll(function() {
        restHelper.setConsent(personId);

        var tsBasIntyg = genericTestDataBuilder.getTsBas(personId);
        intygsId = tsBasIntyg.id;
        restHelper.createIntyg(tsBasIntyg);
    });

    afterAll(function() {
        restHelper.deleteConsent(personId);
        restHelper.deleteIntyg(intygsId);
    });

    describe('Verifiera ts-bas', function() {

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
            expect(browser.getCurrentUrl()).toContain('ts-bas');
            expect(viewPage.certificateId()).toEqual(intygsId);
        });

        it('Verifiera patient och utfärdare', function() {
            expect(viewPage.getTextContent('patient-name')).toEqual('Herr Dundersjuk');
            expect(viewPage.getTextContent('patient-crn')).toEqual(personId);
            expect(viewPage.getTextContent('careunit')).toEqual('WebCert Enhet 1');
            expect(viewPage.getTextContent('caregiver')).toEqual('WebCert Vårdgivare 1');
        });

        it('Verifiera intyg avser och identitet styrkt genom', function() {
            expect(viewPage.getTextContent('intygAvser')).toEqual('C1, C1E, C, CE, D1, D1E, D, DE, TAXI, ANNAT');
            expect(viewPage.getTextContent('identitet')).toEqual('Pass');
        });

        it('Verifiera "1. Synfunktioner"', function() {
            expect(viewPage.getTextContent('synfaltsdefekter')).toEqual('Ja');
            expect(viewPage.getTextContent('nattblindhet')).toEqual('Ja');
            expect(viewPage.getTextContent('diplopi')).toEqual('Ja');
            expect(viewPage.getTextContent('nystagmus')).toEqual('Ja');
            expect(viewPage.getTextContent('hogerOgautanKorrektion')).toEqual('0,0');
            expect(viewPage.getTextContent('hogerOgamedKorrektion')).toEqual('0,0');
            expect(viewPage.getTextContent('hogerOgakontaktlins')).toEqual('Ja');
            expect(viewPage.getTextContent('vansterOgautanKorrektion')).toEqual('0,0');
            expect(viewPage.getTextContent('vansterOgamedKorrektion')).toEqual('0,0');
            expect(viewPage.getTextContent('vansterOgakontaktlins')).toEqual('Ja');
            expect(viewPage.getTextContent('binokulartutanKorrektion')).toEqual('0,0');
            expect(viewPage.getTextContent('binokulartmedKorrektion')).toEqual('0,0');
            expect(viewPage.getTextContent('korrektionsglasensStyrka')).toEqual('Ja');
        });

        it('Verifiera "2. Hörsel och balanssinne"', function() {
            expect(viewPage.getTextContent('horselBalansbalansrubbningar')).toEqual('Ja');
            expect(viewPage.getTextContent('horselBalanssvartUppfattaSamtal4Meter')).toEqual('Ja');
        });

        it('Verifiera "3. Rörelseorganens funktioner"', function() {
            expect(viewPage.getTextContent('funktionsnedsattning')).toEqual('Ja');
            expect(viewPage.getTextContent('funktionsnedsattningbeskrivning')).toEqual('Spik i foten');
            expect(viewPage.getTextContent('funktionsnedsattningotillrackligRorelseformaga')).toEqual('Ja');
        });

        it('Verifiera "4. Hjärt- och kärlsjukdomar"', function() {
            expect(viewPage.getTextContent('hjartKarlSjukdom')).toEqual('Ja');
            expect(viewPage.getTextContent('hjarnskadaEfterTrauma')).toEqual('Ja');
            expect(viewPage.getTextContent('riskfaktorerStroke')).toEqual('Ja');
            expect(viewPage.getTextContent('beskrivningRiskfaktorer')).toEqual('Förkärlek för Elvismackor');
        });

        it('Verifiera "5. Diabetes"', function() {
            expect(viewPage.getTextContent('harDiabetes')).toEqual('Ja');
            expect(viewPage.getTextContent('diabetesTyp')).toEqual('Typ 2');
            expect(viewPage.getTextContent('kost')).toEqual('Kost');
            expect(viewPage.fieldNotShown('tabletter')).toBeTruthy();
            expect(viewPage.fieldNotShown('insulin')).toBeTruthy();
        });

        it('Verifiera "6. Neurologiska sjukdomar"', function() {
            expect(viewPage.getTextContent('neurologiskSjukdom')).toEqual('Ja');
        });

        it('Verifiera "7. Epilepsi, epileptiskt anfall och annan medvetandestörning"', function() {
            expect(viewPage.getTextContent('medvetandestorning')).toEqual('Ja');
            expect(viewPage.getTextContent('medvetandestorningbeskrivning')).toEqual('Beskrivning');
        });

        it('Verifiera "8. Njursjukdomar"', function() {
            expect(viewPage.getTextContent('nedsattNjurfunktion')).toEqual('Ja');
        });

        it('Verifiera "9. Demens och andra kognitiva störningar"', function() {
            expect(viewPage.getTextContent('sviktandeKognitivFunktion')).toEqual('Ja');
        });

        it('Verifiera "10. Sömn- och vakenhetsstörningar"', function() {
            expect(viewPage.getTextContent('teckenSomnstorningar')).toEqual('Ja');
        });

        it('Verifiera "11. Alkohol, narkotika och läkemedel"', function() {
            expect(viewPage.getTextContent('teckenMissbruk')).toEqual('Ja');
            expect(viewPage.getTextContent('foremalForVardinsats')).toEqual('Ja');
            expect(viewPage.getTextContent('provtagningBehovs')).toEqual('Ja');
            expect(viewPage.getTextContent('lakarordineratLakemedelsbruk')).toEqual('Ja');
            expect(viewPage.getTextContent('lakemedelOchDos')).toEqual('Läkemedel och dos');
        });

        it('Verifiera "12. Psykiska sjukdomar och störningar"', function() {
            expect(viewPage.getTextContent('psykiskSjukdom')).toEqual('Ja');
        });

        it('Verifiera "13. ADHD, autismspektrumtillstånd och likartade tillstånd samt psykisk utvecklingsstörning"', function() {
            expect(viewPage.getTextContent('psykiskUtvecklingsstorning')).toEqual('Ja');
            expect(viewPage.getTextContent('harSyndrom')).toEqual('Ja');
        });

        it('Verifiera "15. Övrig medicinering"', function() {
            expect(viewPage.getTextContent('stadigvarandeMedicinering')).toEqual('Ja');
            expect(viewPage.getTextContent('medicineringbeskrivning')).toEqual('Alvedon');
        });

        it('Verifiera "Övrig kommentar"', function() {
            expect(viewPage.getTextContent('kommentar')).toEqual('Här kommer en övrig kommentar');
            expect(viewPage.fieldNotShown('kommentarEjAngivet')).toBeTruthy();
        });

        it('Verifiera "Bedömning"', function() {
            expect(viewPage.getTextContent('bedomning')).toEqual('C1, C1E, C, CE, D1, D1E, D, DE, TAXI, ANNAT');
            expect(viewPage.fieldNotShown('bedomningKanInteTaStallning')).toBeTruthy();
            expect(viewPage.getTextContent('lakareSpecialKompetens')).toEqual('Spektralanalys');
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
