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
var textHelper = miTestTools.helpers.text;

var welcomePage = miTestTools.pages.welcomePage;
var inboxPage = miTestTools.pages.inboxPage;
var viewPage = miTestTools.pages.viewPage;

var genericTestdataBuilder = miTestTools.testdata.generic;

describe('Verifiera Lisjp', function() {

    var texts = null;

    var intygsId1 = null;
    var intygsId2 = null;

    beforeAll(function() {
        browser.ignoreSynchronization = false;

        //Load and cache expected dynamictext-values for this intygstype.
        textHelper.readTextsFromFkTextFile('texterMU_LISJP_v1.0.xml').then(function(textResources) {
            texts = textResources;
        }, function(err) {
            fail('Error during text lookup ' + err);
        });

        // Skapa intygen
        var intyg1 = genericTestdataBuilder.getLisjpSmittskydd();
        var intyg2 = genericTestdataBuilder.getLisjpFull();
        intygsId1 = intyg1.id;
        intygsId2 = intyg2.id;
        restHelper.createIntyg(intyg1);
        restHelper.createIntyg(intyg2);
    });

    afterAll(function() {
        restHelper.deleteIntyg(intygsId1);
        restHelper.deleteIntyg(intygsId2);
    });

    describe('Logga in', function() {
        // Logga in
        it('Logga in och ge samtycke', function() {
            welcomePage.get();
            specHelper.waitForAngularTestability();
            welcomePage.login();
            specHelper.waitForAngularTestability();
        });

    });

    describe('Kontrollera att bägge intygen finns i intygslistan', function() {
        // Logga in
        it('Header ska var Inkorgen', function() {
            expect(inboxPage.isAt()).toBeTruthy();
            expect(element(by.id('inboxHeader')).getText()).toBe('Översikt över dina intyg');
        });

        it('Intyg 1 ska finnas i listan', function() {
            expect(element(by.id('certificate-' + intygsId1)).isPresent());
            expect(element(by.id('viewCertificateBtn-' + intygsId1)).isPresent());
        });

        it('Intyg 2 ska finnas i listan', function() {
            expect(element(by.id('certificate-' + intygsId2)).isPresent());
            expect(element(by.id('viewCertificateBtn-' + intygsId2)).isPresent());
        });

    });

    describe('intyg med smittbärarpenning', function() {

        it('intyget visas', function() {
            inboxPage.viewCertificate(intygsId1);
            expect(viewPage.isAt()).toBeTruthy();
        });

        it('Verifiera att anpassa utskrift är disabled om smittskydd Ja', function() {
            expect(viewPage.customizeCertificateIsDisplayed()).toBe(false);
            expect(element(by.id('customize-disabled-message')).isPresent()).toBe(true);
        });

        it('Verifiera smittbärarpenning är Ja', function() {
            expect(viewPage.getDynamicLabelText('KAT_10.RBK')).toBe(texts['KAT_10.RBK']);
            expect(viewPage.getDynamicLabelText('FRG_27.RBK')).toBe(texts['FRG_27.RBK']);
            expect(viewPage.getTextContent('avstangningSmittskydd')).toEqual('Ja');
        });

        it('Verifiera att frågor under grund för medicinskt underlag är ej angivet', function() {
            expect(viewPage.showsNoValue('undersokningAvPatienten')).toBeTruthy();
            expect(viewPage.showsNoValue('telefonkontaktMedPatienten')).toBeTruthy();
            expect(viewPage.showsNoValue('journaluppgifter')).toBeTruthy();
            expect(viewPage.showsNoValue('annatGrundForMU')).toBeTruthy();
            expect(viewPage.showsNoValue('annatGrundForMUBeskrivning')).toBeTruthy();
        });

        it('Verifiera att frågor sysselsättning är ej angivet', function() {
            expect(viewPage.showsNoValue('sysselsattning')).toBeTruthy();
            expect(viewPage.showsNoValue('nuvarandeArbete')).toBeTruthy();
        });

        it('Verifiera diagnos', function() {
            expect(viewPage.getDynamicLabelText('KAT_3.RBK')).toBe(texts['KAT_3.RBK']);
            expect(viewPage.getDynamicLabelText('FRG_6.RBK')).toBe(texts['FRG_6.RBK']);

            expect(element(by.id('diagnoser-row0-col0')).getText()).toBe('O63');
            expect(element(by.id('diagnoser-row0-col1')).getText()).toBe('Utdragen förlossning');
        });

        it('Verifiera att frågor konsekvenser är ej angivet', function() {
            expect(viewPage.showsNoValue('funktionsnedsattning')).toBeTruthy();
            expect(viewPage.showsNoValue('aktivitetsbegransning')).toBeTruthy();
        });

        it('Verifiera att frågor behandling är ej angivet', function() {
            expect(viewPage.showsNoValue('pagaendeBehandling')).toBeTruthy();
            expect(viewPage.showsNoValue('planeradBehandling')).toBeTruthy();
        });

        it('Verifiera bedömning', function() {
            expect(viewPage.getDynamicLabelText('KAT_6.RBK')).toBe(texts['KAT_6.RBK']);
            expect(viewPage.getDynamicLabelText('FRG_32.RBK')).toBe(texts['FRG_32.RBK']);
            expect(viewPage.getTextContent('sjukskrivningar-row0-col0')).toEqual(texts['KV_FKMU_0003.HELT_NEDSATT.RBK']);
            expect(viewPage.getTextContent('sjukskrivningar-row1-col0')).toEqual(texts['KV_FKMU_0003.HALFTEN.RBK']);


            expect(viewPage.showsNoValue('forsakringsmedicinsktBeslutsstod')).toBeTruthy();

            expect(viewPage.getTextContent('arbetstidsforlaggning')).toEqual('Ej angivet');

            expect(viewPage.showsNoValue('arbetstidsforlaggningMotivering')).toBeTruthy();

            expect(viewPage.getTextContent('arbetsresor')).toEqual('Ej angivet');

            expect(viewPage.showsNoValue('prognos')).toBeTruthy();

        });

        it('Verifiera att åtgärder är ej angivet', function() {
            expect(viewPage.showsNoValue('arbetslivsinriktadeAtgarder')).toBeTruthy();
            expect(viewPage.showsNoValue('arbetslivsinriktadeAtgarderBeskrivning')).toBeTruthy();
        });

        it('Verifiera att Övriga upplysningar är ej angivet', function() {
            expect(viewPage.showsNoValue('ovrigt')).toBeTruthy();
        });

        it('Verifiera att tilläggsfrågorna är ej angivet', function() {
            expect(viewPage.showsNoValue('tillaggsfragor-0--svar')).toBeTruthy();
            expect(viewPage.showsNoValue('tillaggsfragor-1--svar')).toBeTruthy();
        });

    });

    describe('Visa fullt lisjp intyg (ej smittskydd)', function() {

        it('Visa intyg', function() {
            // Navigera tillbaka till listan och välj den andra intyget
            viewPage.backToList();
            expect(inboxPage.isAt()).toBeTruthy();
            inboxPage.viewCertificate(intygsId2);
            expect(viewPage.isAt()).toBeTruthy();
        });

        it('Verifiera att anpassa utskrift är enablat om smittskydd Nej', function() {
            expect(viewPage.customizeCertificateIsDisplayed()).toBe(true);
            expect(element(by.id('customize-disabled-message')).isPresent()).toBe(false);
        });

        it('Verifiera smittbärarpenning är Nej', function() {
            expect(viewPage.getDynamicLabelText('KAT_10.RBK')).toBe(texts['KAT_10.RBK']);
            expect(viewPage.getDynamicLabelText('FRG_27.RBK')).toBe(texts['FRG_27.RBK']);
            expect(viewPage.getTextContent('avstangningSmittskydd')).toEqual('Ej angivet');
        });

        it('Verifiera att frågor under grund för medicinskt underlag är angivet', function() {
            expect(viewPage.getTextContent('undersokningAvPatienten')).toEqual('2017-05-26');
            expect(viewPage.showsNoValue('undersokningAvPatienten')).toBeFalsy();

            expect(viewPage.getTextContent('telefonkontaktMedPatienten')).toEqual('2017-05-26');
            expect(viewPage.showsNoValue('telefonkontaktMedPatienten')).toBeFalsy();

            expect(viewPage.getTextContent('journaluppgifter')).toEqual('2017-05-26');
            expect(viewPage.showsNoValue('journaluppgifter')).toBeFalsy();

            expect(viewPage.getTextContent('annatGrundForMU')).toEqual('2017-05-26');
            expect(viewPage.showsNoValue('annatGrundForMU')).toBeFalsy();

            expect(viewPage.getTextContent('annatGrundForMUBeskrivning')).toEqual('baserat på annat');
        });

        it('Verifiera att frågor sysselsättning är angivna', function() {
            expect(viewPage.showsNoValue('sysselsattning')).toBeFalsy();

            expect(viewPage.getTextContent('sysselsattning-0')).toBe(texts['KV_FKMU_0002.NUVARANDE_ARBETE.RBK']);
            expect(viewPage.getTextContent('sysselsattning-1')).toBe(texts['KV_FKMU_0002.ARBETSSOKANDE.RBK']);
            expect(viewPage.getTextContent('sysselsattning-2')).toBe(texts['KV_FKMU_0002.FORALDRALEDIG.RBK']);
            expect(viewPage.getTextContent('sysselsattning-3')).toBe(texts['KV_FKMU_0002.STUDIER.RBK']);

            expect(viewPage.showsNoValue('nuvarandeArbete')).toBeFalsy();
            expect(viewPage.getTextContent('nuvarandeArbete')).toBe('Ett yrke med arbetsuppgifter');
        });

        it('Verifiera korrekta diagnoser', function() {
            expect(viewPage.getDynamicLabelText('KAT_3.RBK')).toBe(texts['KAT_3.RBK']);
            expect(viewPage.getDynamicLabelText('FRG_6.RBK')).toBe(texts['FRG_6.RBK']);

            expect(viewPage.getTextContent('diagnoser-row0-col0')).toBe('J22');
            expect(viewPage.getTextContent('diagnoser-row0-col1')).toBe('Icke specificerad akut infektion i nedre luftvägarna');
            expect(viewPage.getTextContent('diagnoser-row1-col0')).toBe('M46');
            expect(viewPage.getTextContent('diagnoser-row1-col1')).toBe('Andra inflammatoriska sjukdomar i ryggraden');
            expect(viewPage.getTextContent('diagnoser-row2-col0')).toBe('S22');
            expect(viewPage.getTextContent('diagnoser-row2-col1')).toBe('Fraktur på revben, bröstbenet och bröstkotpelaren');
        });

        it('Verifiera att frågor konsekvenser är angivna', function() {
            expect(viewPage.getDynamicLabelText('KAT_4.RBK')).toBe(texts['KAT_4.RBK']);

            expect(viewPage.getDynamicLabelText('FRG_35.RBK')).toBe(texts['FRG_35.RBK']);
            expect(viewPage.getDynamicLabelText('DFR_35.1.RBK')).toBe(texts['DFR_35.1.RBK']);
            expect(viewPage.showsNoValue('funktionsnedsattning')).toBeFalsy();
            expect(viewPage.getTextContent('funktionsnedsattning')).toBe('Funktionell nedsättning');

            expect(viewPage.getDynamicLabelText('FRG_17.RBK')).toBe(texts['FRG_17.RBK']);
            expect(viewPage.getDynamicLabelText('DFR_17.1.RBK')).toBe(texts['DFR_17.1.RBK']);
            expect(viewPage.showsNoValue('aktivitetsbegransning')).toBeFalsy();
            expect(viewPage.getTextContent('aktivitetsbegransning')).toBe('Begränsning i aktivitet');
        });

        it('Verifiera att frågor behandling är ej angivet', function() {
            expect(viewPage.getDynamicLabelText('KAT_5.RBK')).toBe(texts['KAT_5.RBK']);

            expect(viewPage.getDynamicLabelText('FRG_19.RBK')).toBe(texts['FRG_19.RBK']);
            expect(viewPage.getDynamicLabelText('DFR_19.1.RBK')).toBe(texts['DFR_19.1.RBK']);
            expect(viewPage.showsNoValue('pagaendeBehandling')).toBeFalsy();
            expect(viewPage.getTextContent('pagaendeBehandling')).toBe('En pågående behandling');

            expect(viewPage.getDynamicLabelText('FRG_19.RBK')).toBe(texts['FRG_19.RBK']);
            expect(viewPage.getDynamicLabelText('DFR_19.1.RBK')).toBe(texts['DFR_19.1.RBK']);
            expect(viewPage.showsNoValue('planeradBehandling')).toBeFalsy();
            expect(viewPage.getTextContent('planeradBehandling')).toBe('En planerad behandling');

        });

        it('Verifiera bedömning', function() {
            expect(viewPage.getDynamicLabelText('KAT_6.RBK')).toBe(texts['KAT_6.RBK']);
            expect(viewPage.getDynamicLabelText('FRG_32.RBK')).toBe(texts['FRG_32.RBK']);

            expect(viewPage.getTextContent('sjukskrivningar-row0-col0')).toEqual(texts['KV_FKMU_0003.HELT_NEDSATT.RBK']);
            expect(viewPage.getTextContent('sjukskrivningar-row0-col1')).toEqual('2017-06-19');
            expect(viewPage.getTextContent('sjukskrivningar-row0-col2')).toEqual('2017-07-10');

            expect(viewPage.getTextContent('sjukskrivningar-row1-col0')).toEqual(texts['KV_FKMU_0003.TRE_FJARDEDEL.RBK']);
            expect(viewPage.getTextContent('sjukskrivningar-row1-col1')).toEqual('2017-05-28');
            expect(viewPage.getTextContent('sjukskrivningar-row1-col2')).toEqual('2017-06-18');

            expect(viewPage.getTextContent('sjukskrivningar-row2-col0')).toEqual(texts['KV_FKMU_0003.HALFTEN.RBK']);
            expect(viewPage.getTextContent('sjukskrivningar-row2-col1')).toEqual('2017-05-27');
            expect(viewPage.getTextContent('sjukskrivningar-row2-col2')).toEqual('2017-05-27');

            expect(viewPage.getTextContent('sjukskrivningar-row3-col0')).toEqual(texts['KV_FKMU_0003.EN_FJARDEDEL.RBK']);
            expect(viewPage.getTextContent('sjukskrivningar-row3-col1')).toEqual('2017-05-26');
            expect(viewPage.getTextContent('sjukskrivningar-row3-col2')).toEqual('2017-05-26');


            expect(viewPage.showsNoValue('forsakringsmedicinsktBeslutsstod')).toBeFalsy();
            expect(viewPage.getTextContent('forsakringsmedicinsktBeslutsstod')).toEqual('Arbetsförmågan bedöms nedsatt en längre tid.');

            expect(viewPage.getTextContent('arbetstidsforlaggning')).toEqual('Ja');

            expect(viewPage.showsNoValue('arbetstidsforlaggningMotivering')).toBeFalsy();
            expect(viewPage.getTextContent('arbetstidsforlaggningMotivering')).toEqual('Ett medicinskt skäl till annan förläggning');


            expect(viewPage.getTextContent('arbetsresor')).toEqual('Ja');

            expect(viewPage.showsNoValue('prognos')).toBeFalsy();
            expect(viewPage.getTextContent('prognos-typ')).toBe(texts['KV_FKMU_0006.ATER_X_ANTAL_DGR.RBK']);
            expect(viewPage.getTextContent('prognos-dagarTillArbete')).toBe(texts['KV_FKMU_0007.NITTIO_DGR.RBK']);

        });

        it('Verifiera att åtgärder är angivet', function() {
            expect(viewPage.showsNoValue('arbetslivsinriktadeAtgarder')).toBeFalsy();
            expect(viewPage.getTextContent('arbetslivsinriktadeAtgarder-0')).toBe(texts['KV_FKMU_0004.ARBETSTRANING.RBK']);
            expect(viewPage.getTextContent('arbetslivsinriktadeAtgarder-1')).toBe(texts['KV_FKMU_0004.ARBETSANPASSNING.RBK']);
            expect(viewPage.getTextContent('arbetslivsinriktadeAtgarder-2')).toBe(texts['KV_FKMU_0004.SOKA_NYTT_ARBETE.RBK']);
            expect(viewPage.getTextContent('arbetslivsinriktadeAtgarder-3')).toBe(texts['KV_FKMU_0004.BESOK_ARBETSPLATS.RBK']);
            expect(viewPage.getTextContent('arbetslivsinriktadeAtgarder-4')).toBe(texts['KV_FKMU_0004.ERGONOMISK.RBK']);
            expect(viewPage.getTextContent('arbetslivsinriktadeAtgarder-5')).toBe(texts['KV_FKMU_0004.HJALPMEDEL.RBK']);
            expect(viewPage.getTextContent('arbetslivsinriktadeAtgarder-6')).toBe(texts['KV_FKMU_0004.KONFLIKTHANTERING.RBK']);
            expect(viewPage.getTextContent('arbetslivsinriktadeAtgarder-7')).toBe(texts['KV_FKMU_0004.KONTAKT_FHV.RBK']);
            expect(viewPage.getTextContent('arbetslivsinriktadeAtgarder-8')).toBe(texts['KV_FKMU_0004.OMFORDELNING.RBK']);
            expect(viewPage.getTextContent('arbetslivsinriktadeAtgarder-9')).toBe(texts['KV_FKMU_0004.OVRIGA_ATGARDER.RBK']);

            expect(viewPage.showsNoValue('arbetslivsinriktadeAtgarderBeskrivning')).toBeFalsy();
            expect(viewPage.getTextContent('arbetslivsinriktadeAtgarderBeskrivning')).toEqual('Övriga åtgärder finns');


        });

        it('Verifiera att Övriga upplysningar är  angivet', function() {
            expect(viewPage.showsNoValue('ovrigt')).toBeFalsy();
            expect(viewPage.getTextContent('ovrigt')).toEqual('En del övrigt om patienten');

        });

        it('Verifiera att tilläggsfrågorna är besvarade', function() {
            expect(viewPage.showsNoValue('tillaggsfragor-0--svar')).toBeFalsy();
            expect(viewPage.getTextContent('tillaggsfragor-0--svar')).toEqual('Nej, men rolig');

            expect(viewPage.showsNoValue('tillaggsfragor-1--svar')).toBeFalsy();
            expect(viewPage.getTextContent('tillaggsfragor-1--svar')).toEqual('Absolut');

        });

        it('Verifiera att skapad av är angivet', function() {
            expect(viewPage.getTextContent('fullstandigtNamn')).toEqual('Jan Nilsson');
            expect(viewPage.getTextContent('vardenhet-telefon')).toEqual('Tel: 0101112131416');
            expect(viewPage.getTextContent('vardenhet-namn')).toEqual('WebCert Enhet 1, NMT vg3');
            expect(viewPage.getTextContent('vardenhet-adress')).toEqual('NMT gata 3, 12345 Testhult');
        });



    });

});
