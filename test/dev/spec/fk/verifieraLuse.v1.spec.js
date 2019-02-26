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

/*globals browser */
/*globals describe,it,beforeAll,afterAll */
'use strict';

var specHelper = miTestTools.helpers.spec;
var restHelper = miTestTools.helpers.rest;

var welcomePage = miTestTools.pages.welcomePage;
var inboxPage = miTestTools.pages.inboxPage;
var viewPage = miTestTools.pages.viewPage;

var genericTestdataBuilder = miTestTools.testdata.generic;

describe('Verifiera LUSE', function() {

    var intygsId = null;
    var texts = null;

    beforeAll(function() {
        browser.ignoreSynchronization = false;

        // Load and cache expected dynamictext-values for this intygstype.
        restHelper.getTextResource('texterMU_LUSE_v1.0.xml').then(function(textResources) {
            texts = textResources;
        }, function(err) {
            fail('Error during text lookup ' + err);
        });

        // Skapa intygen
        var intyg = genericTestdataBuilder.getLuse();
        intygsId = intyg.id;
        restHelper.createIntyg(intyg);
    });

    afterAll(function() {
        restHelper.deleteIntyg(intygsId);
    });

    describe('Logga in', function() {
        // Logga in
        it('Logga in', function() {
            welcomePage.get();
            specHelper.waitForAngularTestability();
            welcomePage.login();
            specHelper.waitForAngularTestability();
        });

        it('Header ska var Inkorgen', function() {
            expect(inboxPage.isAt()).toBeTruthy();
            expect(element(by.id('inboxHeader')).getText()).toBe('Översikt över dina intyg');
        });

        it('Intyg ska finnas i listan', function() {
            expect(element(by.id('certificate-' + intygsId)).isPresent());
            expect(element(by.id('viewCertificateBtn-' + intygsId)).isPresent());
        });
    });

    describe('Verifiera intyg', function() {

        it('Visa intyg', function() {
            inboxPage.viewCertificate(intygsId);
            expect(viewPage.isAt()).toBeTruthy();
        });

        it('Verifiera att frågor under grund för medicinskt underlag är angivet', function() {
            expect(viewPage.getDynamicLabelText('KAT_1.RBK')).toBe(texts['KAT_1.RBK']);

            expect(viewPage.getTextContent('undersokningAvPatienten')).toEqual('2016-04-21');
            expect(viewPage.getTextContent('journaluppgifter')).toEqual('2016-04-21');
            expect(viewPage.getTextContent('anhorigsBeskrivningAvPatienten')).toEqual('2016-04-21');
            expect(viewPage.getTextContent('annatGrundForMU')).toEqual('2016-04-21');
            expect(viewPage.getTextContent('annatGrundForMUBeskrivning')).toEqual('Annat underlag.');

            expect(viewPage.getTextContent('kannedomOmPatient')).toEqual('2016-04-21');

            // Andra medicinska underlag
            expect(viewPage.getTextContent('underlagFinns')).toEqual('Ja');

            expect(viewPage.getTextContent('underlag-row0-col0')).toEqual('Underlag från habiliteringen');
            expect(viewPage.getTextContent('underlag-row0-col1')).toEqual('2016-04-07');
            expect(viewPage.getTextContent('underlag-row0-col2')).toEqual('Information om utredning.');

            expect(viewPage.getTextContent('mobile-underlag-row0-col0')).toEqual('notshown');
            expect(viewPage.getTextContent('mobile-underlag-row0-col1')).toEqual('notshown');
            expect(viewPage.getTextContent('mobile-underlag-row0-col2')).toEqual('notshown');
        });

        it('Verifiera medicinskt underlag mobil ', function() {
            mobileSize();

            expect(viewPage.getTextContent('mobile-underlag-row0-col0')).toEqual('Underlag från habiliteringen');
            expect(viewPage.getTextContent('mobile-underlag-row0-col1')).toEqual('2016-04-07');
            expect(viewPage.getTextContent('mobile-underlag-row0-col2')).toEqual('Information om utredning.');

            expect(viewPage.getTextContent('underlag-row0-col0')).toEqual('notshown');
            expect(viewPage.getTextContent('underlag-row0-col1')).toEqual('notshown');
            expect(viewPage.getTextContent('underlag-row0-col2')).toEqual('notshown');
        });

        it('Verifiera att korrekta diagnoser är angivet', function() {
            desktopSize();

            expect(viewPage.getDynamicLabelText('KAT_4.RBK')).toBe(texts['KAT_4.RBK']);

            expect(viewPage.getDynamicLabelText('FRG_6.RBK')).toBe(texts['FRG_6.RBK']);
            expect(viewPage.getTextContent('diagnoser-row0-col0')).toBe('Z60');
            expect(viewPage.getTextContent('diagnoser-row0-col1')).toBe('Problem som har samband med social miljö');

            expect(viewPage.getDynamicLabelText('FRG_7.RBK')).toBe(texts['FRG_7.RBK']);
            expect(viewPage.getTextContent('diagnosgrund')).toBe('När ställdes diagnos.');

            expect(viewPage.getDynamicLabelText('FRG_45.RBK')).toBe(texts['FRG_45.RBK']);
            expect(viewPage.getTextContent('nyBedomningDiagnosgrund')).toBe('Ja');

            expect(viewPage.getDynamicLabelText('DFR_45.2.RBK')).toBe(texts['DFR_45.2.RBK']);
            expect(viewPage.getTextContent('diagnosForNyBedomning')).toBe('Hela diagnosen kan vara trasig');
        });

        it('Verifiera att sjukdomens bakgrund är angivet', function() {
            expect(viewPage.getDynamicLabelText('KAT_4.RBK')).toBe(texts['KAT_4.RBK']);

            expect(viewPage.getDynamicLabelText('FRG_5.RBK')).toBe(texts['FRG_5.RBK']);
            expect(viewPage.getTextContent('sjukdomsforlopp')).toBe('Sjukdomsförlopp.');
        });

        it('Verifiera att funktionsnedsättningar är angivet', function() {
            expect(viewPage.getDynamicLabelText('KAT_5.RBK')).toBe(texts['KAT_5.RBK']);

            expect(viewPage.getDynamicLabelText('FRG_8.RBK')).toBe(texts['FRG_8.RBK']);
            expect(viewPage.getDynamicLabelText('DFR_8.1.RBK')).toBe(texts['DFR_8.1.RBK']);
            expect(viewPage.getTextContent('funktionsnedsattningIntellektuell')).toBe('Intellektuell.');

            expect(viewPage.getDynamicLabelText('FRG_9.RBK')).toBe(texts['FRG_9.RBK']);
            expect(viewPage.getDynamicLabelText('DFR_9.1.RBK')).toBe(texts['DFR_9.1.RBK']);
            expect(viewPage.getTextContent('funktionsnedsattningKommunikation')).toBe('Social.');

            expect(viewPage.getDynamicLabelText('FRG_10.RBK')).toBe(texts['FRG_10.RBK']);
            expect(viewPage.getDynamicLabelText('DFR_10.1.RBK')).toBe(texts['DFR_10.1.RBK']);
            expect(viewPage.getTextContent('funktionsnedsattningKoncentration')).toBe('Koncentration.');

            expect(viewPage.getDynamicLabelText('FRG_11.RBK')).toBe(texts['FRG_11.RBK']);
            expect(viewPage.getDynamicLabelText('DFR_11.1.RBK')).toBe(texts['DFR_11.1.RBK']);
            expect(viewPage.getTextContent('funktionsnedsattningPsykisk')).toBe('Psykisk.');

            expect(viewPage.getDynamicLabelText('FRG_12.RBK')).toBe(texts['FRG_12.RBK']);
            expect(viewPage.getDynamicLabelText('DFR_12.1.RBK')).toBe(texts['DFR_12.1.RBK']);
            expect(viewPage.getTextContent('funktionsnedsattningSynHorselTal')).toBe('Tal.');

            expect(viewPage.getDynamicLabelText('FRG_13.RBK')).toBe(texts['FRG_13.RBK']);
            expect(viewPage.getDynamicLabelText('DFR_13.1.RBK')).toBe(texts['DFR_13.1.RBK']);
            expect(viewPage.getTextContent('funktionsnedsattningBalansKoordination')).toBe('Balans.');

            expect(viewPage.getDynamicLabelText('FRG_14.RBK')).toBe(texts['FRG_14.RBK']);
            expect(viewPage.getDynamicLabelText('DFR_14.1.RBK')).toBe(texts['DFR_14.1.RBK']);
            expect(viewPage.getTextContent('funktionsnedsattningAnnan')).toBe('Annan funktion.');
        });

        it('Verifiera att aktivitetsbegränsningar är angivet', function() {
            expect(viewPage.getDynamicLabelText('KAT_6.RBK')).toBe(texts['KAT_6.RBK']);

            expect(viewPage.getDynamicLabelText('FRG_17.RBK')).toBe(texts['FRG_17.RBK']);
            expect(viewPage.getDynamicLabelText('DFR_17.1.RBK')).toBe(texts['DFR_17.1.RBK']);
            expect(viewPage.getTextContent('aktivitetsbegransning')).toBe('Aktivitetsbegränsning allt.');
        });

        it('Verifiera medicinska behandlingar/åtgärder är angivet', function() {
            expect(viewPage.getDynamicLabelText('KAT_7.RBK')).toBe(texts['KAT_7.RBK']);

            expect(viewPage.getDynamicLabelText('FRG_18.RBK')).toBe(texts['FRG_18.RBK']);
            expect(viewPage.getDynamicLabelText('DFR_18.1.RBK')).toBe(texts['DFR_18.1.RBK']);
            expect(viewPage.getTextContent('avslutadBehandling')).toBe('Åtgärder.');

            expect(viewPage.getDynamicLabelText('FRG_19.RBK')).toBe(texts['FRG_19.RBK']);
            expect(viewPage.getDynamicLabelText('DFR_19.1.RBK')).toBe(texts['DFR_19.1.RBK']);
            expect(viewPage.getTextContent('pagaendeBehandling')).toBe('Pågående åtgärder.');

            expect(viewPage.getDynamicLabelText('FRG_20.RBK')).toBe(texts['FRG_20.RBK']);
            expect(viewPage.getDynamicLabelText('DFR_20.1.RBK')).toBe(texts['DFR_20.1.RBK']);
            expect(viewPage.getTextContent('planeradBehandling')).toBe('Planerade åtgärder.');

            expect(viewPage.getDynamicLabelText('FRG_21.RBK')).toBe(texts['FRG_21.RBK']);
            expect(viewPage.getDynamicLabelText('DFR_21.1.RBK')).toBe(texts['DFR_21.1.RBK']);
            expect(viewPage.getTextContent('substansintag')).toBe('Substans.');
        });

        it('Verifiera de medicinska föutsättningarna för arbete är angivet', function() {
            expect(viewPage.getDynamicLabelText('KAT_8.RBK')).toBe(texts['KAT_8.RBK']);

            expect(viewPage.getDynamicLabelText('FRG_22.RBK')).toBe(texts['FRG_22.RBK']);
            expect(viewPage.getTextContent('medicinskaForutsattningarForArbete')).toBe('Förutsättningar över tid.');

            expect(viewPage.getDynamicLabelText('FRG_23.RBK')).toBe(texts['FRG_23.RBK']);
            expect(viewPage.getTextContent('formagaTrotsBegransning')).toBe('Vad kan patienten göra.');
        });

        it('Verifiera att Övriga upplysningar är angivet', function() {
            expect(viewPage.getDynamicLabelText('KAT_9.RBK')).toBe(texts['KAT_9.RBK']);

            expect(viewPage.getDynamicLabelText('FRG_25.RBK')).toBe(texts['FRG_25.RBK']);
            expect(viewPage.getTextContent('ovrigt')).toEqual('Övrigt lång text.');
        });

        it('Verifiera att Kontakt är angivet', function() {
            expect(viewPage.getDynamicLabelText('KAT_10.RBK')).toBe(texts['KAT_10.RBK']);

            expect(viewPage.getDynamicLabelText('FRG_26.RBK')).toBe(texts['FRG_26.RBK']);
            expect(viewPage.getDynamicLabelText('DFR_26.1.RBK')).toBe(texts['DFR_26.1.RBK']);
            expect(viewPage.getTextContent('kontaktMedFk')).toEqual('Ja');
            expect(viewPage.getDynamicLabelText('DFR_26.2.RBK')).toBe(texts['DFR_26.2.RBK']);
            expect(viewPage.getTextContent('anledningTillKontakt')).toEqual('Kontaktinfo.');
        });

        it('Verifiera att skapad av är angivet', function() {
            expect(viewPage.getTextContent('fullstandigtNamn')).toEqual('Jan Nilsson');
            expect(viewPage.getTextContent('vardenhet-telefon')).toEqual('Tel: 0101112131415');
            expect(viewPage.getTextContent('vardenhet-namn')).toEqual('WebCert Enhet 1, NMT vg1');
            expect(viewPage.getTextContent('vardenhet-adress')).toEqual('NMT gata 2, 12345 Testhult');

        });



    });

});
