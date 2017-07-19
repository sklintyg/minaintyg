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

describe('Visa intyg ts-bas', function() {

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
        });

        it('Verifiera intyg avser och identitet styrkt genom', function() {
            expect(viewPage.getTextContent('intygAvser-korkortstyp-0')).toEqual('C1');
            expect(viewPage.getTextContent('intygAvser-korkortstyp-1')).toEqual('C1E');
            expect(viewPage.getTextContent('intygAvser-korkortstyp-2')).toEqual('C');
            expect(viewPage.getTextContent('intygAvser-korkortstyp-3')).toEqual('CE');
            expect(viewPage.getTextContent('intygAvser-korkortstyp-4')).toEqual('D1');
            expect(viewPage.getTextContent('intygAvser-korkortstyp-5')).toEqual('D1E');
            expect(viewPage.getTextContent('intygAvser-korkortstyp-6')).toEqual('D');
            expect(viewPage.getTextContent('intygAvser-korkortstyp-7')).toEqual('DE');
            expect(viewPage.getTextContent('intygAvser-korkortstyp-8')).toEqual('Taxi');
            expect(viewPage.getTextContent('intygAvser-korkortstyp-9')).toEqual('Annat');
            expect(viewPage.getTextContent('vardkontakt-idkontroll')).toEqual('Pass');
        });

        it('Verifiera "1. Synfunktioner"', function() {
            expect(viewPage.getTextContent('syn-synfaltsdefekter')).toEqual('Ja');
            expect(viewPage.getTextContent('syn-nattblindhet')).toEqual('Ja');
            expect(viewPage.getTextContent('syn-diplopi')).toEqual('Ja');
            expect(viewPage.getTextContent('syn-progressivOgonsjukdom')).toEqual('Ja');
            expect(viewPage.getTextContent('syn-nystagmus')).toEqual('Ja');
            expect(viewPage.getTextContent('syn-row0-col1')).toEqual('0,0');
            expect(viewPage.getTextContent('syn-row0-col2')).toEqual('0,0');
            expect(viewPage.getTextContent('syn-row0-col3')).toEqual('Ja');
            expect(viewPage.getTextContent('syn-row1-col1')).toEqual('0,0');
            expect(viewPage.getTextContent('syn-row1-col2')).toEqual('0,0');
            expect(viewPage.getTextContent('syn-row1-col3')).toEqual('Ja');
            expect(viewPage.getTextContent('syn-row2-col1')).toEqual('0,0');
            expect(viewPage.getTextContent('syn-row2-col2')).toEqual('0,0');
            expect(viewPage.getTextContent('syn-korrektionsglasensStyrka')).toEqual('Ja');

            expect(viewPage.getTextContent('mobile-syn-row0-col1')).toEqual('notshown');
            expect(viewPage.getTextContent('mobile-syn-row0-col2')).toEqual('notshown');
            expect(viewPage.getTextContent('mobile-syn-row0-col3')).toEqual('notshown');
            expect(viewPage.getTextContent('mobile-syn-row1-col1')).toEqual('notshown');
            expect(viewPage.getTextContent('mobile-syn-row1-col2')).toEqual('notshown');
            expect(viewPage.getTextContent('mobile-syn-row1-col3')).toEqual('notshown');
            expect(viewPage.getTextContent('mobile-syn-row2-col1')).toEqual('notshown');
            expect(viewPage.getTextContent('mobile-syn-row2-col2')).toEqual('notshown');
        });

        it('Verifiera "1. Synfunktioner" mobil-tabell', function() {
            mobileSize();

            expect(viewPage.getTextContent('mobile-syn-row0-col1')).toEqual('0,0');
            expect(viewPage.getTextContent('mobile-syn-row0-col2')).toEqual('0,0');
            expect(viewPage.getTextContent('mobile-syn-row0-col3')).toEqual('Ja');
            expect(viewPage.getTextContent('mobile-syn-row1-col1')).toEqual('0,0');
            expect(viewPage.getTextContent('mobile-syn-row1-col2')).toEqual('0,0');
            expect(viewPage.getTextContent('mobile-syn-row1-col3')).toEqual('Ja');
            expect(viewPage.getTextContent('mobile-syn-row2-col1')).toEqual('0,0');
            expect(viewPage.getTextContent('mobile-syn-row2-col2')).toEqual('0,0');

            expect(viewPage.getTextContent('syn-row0-col1')).toEqual('notshown');
            expect(viewPage.getTextContent('syn-row0-col2')).toEqual('notshown');
            expect(viewPage.getTextContent('syn-row0-col3')).toEqual('notshown');
            expect(viewPage.getTextContent('syn-row1-col1')).toEqual('notshown');
            expect(viewPage.getTextContent('syn-row1-col2')).toEqual('notshown');
            expect(viewPage.getTextContent('syn-row1-col3')).toEqual('notshown');
            expect(viewPage.getTextContent('syn-row2-col1')).toEqual('notshown');
            expect(viewPage.getTextContent('syn-row2-col2')).toEqual('notshown');
        });

        it('Verifiera "2. Hörsel och balanssinne"', function() {
            desktopSize();

            expect(viewPage.getTextContent('horselBalans-balansrubbningar')).toEqual('Ja');
            expect(viewPage.getTextContent('horselBalans-svartUppfattaSamtal4Meter')).toEqual('Ja');
        });

        it('Verifiera "3. Rörelseorganens funktioner"', function() {
            expect(viewPage.getTextContent('funktionsnedsattning-funktionsnedsattning')).toEqual('Ja');
            expect(viewPage.getTextContent('funktionsnedsattning-beskrivning')).toEqual('Spik i foten');
            expect(viewPage.getTextContent('funktionsnedsattning-otillrackligRorelseformaga')).toEqual('Ja');
        });

        it('Verifiera "4. Hjärt- och kärlsjukdomar"', function() {
            expect(viewPage.getTextContent('hjartKarl-hjartKarlSjukdom')).toEqual('Ja');
            expect(viewPage.getTextContent('hjartKarl-hjarnskadaEfterTrauma')).toEqual('Ja');
            expect(viewPage.getTextContent('hjartKarl-riskfaktorerStroke')).toEqual('Ja');
            expect(viewPage.getTextContent('hjartKarl-beskrivningRiskfaktorer')).toEqual('Förkärlek för Elvismackor');
        });

        it('Verifiera "5. Diabetes"', function() {
            expect(viewPage.getTextContent('diabetes-harDiabetes')).toEqual('Ja');
            expect(viewPage.getTextContent('diabetes-diabetesTyp')).toEqual('Typ 2');
            expect(viewPage.getTextContent('diabetes-kost-diabetes-tabletter-diabetes-insulin-0')).toEqual('Kost');
        });

        it('Verifiera "6. Neurologiska sjukdomar"', function() {
            expect(viewPage.getTextContent('neurologi-neurologiskSjukdom')).toEqual('Ja');
        });

        it('Verifiera "7. Epilepsi, epileptiskt anfall och annan medvetandestörning"', function() {
            expect(viewPage.getTextContent('medvetandestorning-medvetandestorning')).toEqual('Ja');
            expect(viewPage.getTextContent('medvetandestorning-beskrivning')).toEqual('Beskrivning');
        });

        it('Verifiera "8. Njursjukdomar"', function() {
            expect(viewPage.getTextContent('njurar-nedsattNjurfunktion')).toEqual('Ja');
        });

        it('Verifiera "9. Demens och andra kognitiva störningar"', function() {
            expect(viewPage.getTextContent('kognitivt-sviktandeKognitivFunktion')).toEqual('Ja');
        });

        it('Verifiera "10. Sömn- och vakenhetsstörningar"', function() {
            expect(viewPage.getTextContent('somnVakenhet-teckenSomnstorningar')).toEqual('Ja');
        });

        it('Verifiera "11. Alkohol, narkotika och läkemedel"', function() {
            expect(viewPage.getTextContent('narkotikaLakemedel-teckenMissbruk')).toEqual('Ja');
            expect(viewPage.getTextContent('narkotikaLakemedel-foremalForVardinsats')).toEqual('Ja');
            expect(viewPage.getTextContent('narkotikaLakemedel-provtagningBehovs')).toEqual('Ja');
            expect(viewPage.getTextContent('narkotikaLakemedel-lakarordineratLakemedelsbruk')).toEqual('Ja');
            expect(viewPage.getTextContent('narkotikaLakemedel-lakemedelOchDos')).toEqual('Läkemedel och dos');
        });

        it('Verifiera "12. Psykiska sjukdomar och störningar"', function() {
            expect(viewPage.getTextContent('psykiskt-psykiskSjukdom')).toEqual('Ja');
        });

        it('Verifiera "13. ADHD, autismspektrumtillstånd och likartade tillstånd samt psykisk utvecklingsstörning"', function() {
            expect(viewPage.getTextContent('utvecklingsstorning-psykiskUtvecklingsstorning')).toEqual('Ja');
            expect(viewPage.getTextContent('utvecklingsstorning-harSyndrom')).toEqual('Ja');
        });

        it('Verifiera "14. Övrig medicinering"', function() {
            expect(viewPage.getTextContent('sjukhusvard-sjukhusEllerLakarkontakt')).toEqual('Ja');
            expect(viewPage.getTextContent('sjukhusvard-tidpunkt')).toEqual('20 Januari');
            expect(viewPage.getTextContent('sjukhusvard-vardinrattning')).toEqual('Vårdcentralen');
            expect(viewPage.getTextContent('sjukhusvard-anledning')).toEqual('Akut lungsot');
        });

        it('Verifiera "15. Övrig medicinering"', function() {
            expect(viewPage.getTextContent('medicinering-stadigvarandeMedicinering')).toEqual('Ja');
            expect(viewPage.getTextContent('medicinering-beskrivning')).toEqual('Alvedon');
        });

        it('Verifiera "Övrig kommentar"', function() {
            expect(viewPage.getTextContent('kommentar')).toEqual('Här kommer en övrig kommentar');
        });

        it('Verifiera "Bedömning"', function() {
            expect(viewPage.getTextContent('bedomning-korkortstyp-0')).toEqual('C1');
            expect(viewPage.getTextContent('bedomning-korkortstyp-1')).toEqual('C1E');
            expect(viewPage.getTextContent('bedomning-korkortstyp-2')).toEqual('C');
            expect(viewPage.getTextContent('bedomning-korkortstyp-3')).toEqual('CE');
            expect(viewPage.getTextContent('bedomning-korkortstyp-4')).toEqual('D1');
            expect(viewPage.getTextContent('bedomning-korkortstyp-5')).toEqual('D1E');
            expect(viewPage.getTextContent('bedomning-korkortstyp-6')).toEqual('D');
            expect(viewPage.getTextContent('bedomning-korkortstyp-7')).toEqual('DE');
            expect(viewPage.getTextContent('bedomning-korkortstyp-8')).toEqual('Taxi');
            expect(viewPage.getTextContent('bedomning-korkortstyp-9')).toEqual('Annat');
            expect(viewPage.getTextContent('bedomning-lakareSpecialKompetens')).toEqual('Spektralanalys');
        });

        it('Verifiera skapad av', function() {
            expect(viewPage.getTextContent('fullstandigtNamn')).toEqual('Jan Nilsson');
            expect(viewPage.getTextContent('vardenhet-telefon')).toEqual('Tel: 08-1337');
            expect(viewPage.getTextContent('vardenhet-namn')).toEqual('WebCert Enhet 1, WebCert Vårdgivare 1');
            expect(viewPage.getTextContent('vardenhet-adress')).toEqual('Enhetsvägen 12, 54321 Tumba');
        });
    });

});
