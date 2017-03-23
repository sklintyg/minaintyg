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
var consentPage = miTestTools.pages.consentPage;
var inboxPage = miTestTools.pages.inboxPage;
var viewPage = miTestTools.pages.viewPage;

var genericTestdataBuilder = miTestTools.testdata.generic;

fdescribe('Verifiera Lisjp med smittbärarpenning', function() {

    var personId = '191212121212';

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

        // Ta bort tidigare samtycken
        restHelper.deleteConsent();

        // Skapa intygen
        var intyg1 = genericTestdataBuilder.getLisjpSmittskydd();
        var intyg2 = genericTestdataBuilder.getLisjpSmittskyddOvrigt();
        intygsId1 = intyg1.id;
        intygsId2 = intyg2.id;
        restHelper.createIntyg(intyg1);
        restHelper.createIntyg(intyg2);
    });

    afterAll(function() {
        restHelper.deleteIntyg(intygsId1);
        restHelper.deleteIntyg(intygsId2);
        restHelper.deleteConsent(personId);
    });

    describe('Logga in', function() {
        // Logga in
        it('Logga in och ge samtycke', function() {
            welcomePage.get();
            specHelper.waitForAngularTestability();
            welcomePage.login();
            specHelper.waitForAngularTestability();
        });

        it('Ge samtycke', function() {
            expect(consentPage.isAt()).toBeTruthy();
            consentPage.clickGiveConsent();
        });

    });

    describe('Kontrollera att bägge intygen finns i intygslistan', function() {
        // Logga in
        it('Header ska var Inkorgen', function() {
            expect(inboxPage.isAt()).toBeTruthy();
            expect(element(by.id('inboxHeader')).getText()).toBe('Inkorgen');
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

    describe('Visa intyg med smittbärarpenning (övrigt ej ifyllt)', function() {

        it('Visa intyg', function() {
            inboxPage.viewCertificate(intygsId1);
            expect(viewPage.isAt()).toBeTruthy();
        });

        it('Verifiera smittbärarpenning', function() {
            expect(viewPage.getDynamicLabelText('KAT_10.RBK')).toBe(texts['KAT_10.RBK']);
            expect(viewPage.getDynamicLabelText('FRG_27.RBK')).toBe(texts['FRG_27.RBK']);
            expect(viewPage.getTextContent('avstangningsmittskydd-yes')).toEqual('Ja');
            expect(viewPage.fieldNotShown('avstangningsmittskydd-no')).toBeTruthy();
        });

        it('Verifiera diagnos', function() {
            expect(viewPage.getDynamicLabelText('KAT_3.RBK')).toBe(texts['KAT_3.RBK']);
            expect(viewPage.getDynamicLabelText('FRG_6.RBK')).toBe(texts['FRG_6.RBK']);

            expect(element(by.id('diagnosKod0')).getText()).toBe('O63');
            expect(element(by.id('diagnosBeskrivning0')).getText()).toBe('Utdragen förlossning');
        });

        it('Verifiera bedömning', function() {
            expect(viewPage.getDynamicLabelText('KAT_6.RBK')).toBe(texts['KAT_6.RBK']);
            expect(viewPage.getDynamicLabelText('FRG_32.RBK')).toBe(texts['FRG_32.RBK']);
            expect(viewPage.getDynamicLabelText('KV_FKMU_0003.HELT_NEDSATT.RBK')).toBe(texts['KV_FKMU_0003.HELT_NEDSATT.RBK']);
            expect(viewPage.getDynamicLabelText('KV_FKMU_0003.HALFTEN.RBK')).toBe(texts['KV_FKMU_0003.HALFTEN.RBK']);

            expect(viewPage.getTextContent('grad_HELT_NEDSATT')).toEqual('100 procent');
            expect(viewPage.getTextContent('grad_HALFTEN')).toEqual('50 procent');
        });

        it('Verifiera att Övriga upplysningar syns med texten Ej ifyllt', function() {
            expect(viewPage.getDynamicLabelText('KAT_8.RBK')).toBe(texts['KAT_8.RBK']);
            expect(viewPage.getDynamicLabelText('FRG_25.RBK')).toBe(texts['FRG_25.RBK']);

            expect(element(by.id('ovrigt')).isPresent()).toBe(true);
            expect(viewPage.getTextContent('ovrigt')).toEqual('Ej ifyllt');
        });

        it('Kontrollera att övriga fält inte visas', function() {
            expect(element(by.css('div[category-label=KAT_1.RBK]')).isDisplayed()).toBe(false);
            expect(element(by.css('div[category-label=KAT_2.RBK]')).isDisplayed()).toBe(false);
            expect(element(by.css('div[category-label=KAT_4.RBK]')).isDisplayed()).toBe(false);
            expect(element(by.css('div[category-label=KAT_5.RBK]')).isDisplayed()).toBe(false);
            expect(element(by.css('field-wrapper[field-name=forsakringsmedicinsktBeslutsstod]')).isDisplayed()).toBe(false);
            expect(element(by.css('field-wrapper[field-name=arbetstidsforlaggning]')).isDisplayed()).toBe(false);
            expect(element(by.css('field-wrapper[field-name=arbetstidsforlaggningMotivering]')).isDisplayed()).toBe(false);
            expect(element(by.css('field-wrapper[field-name=arbetsresor]')).isDisplayed()).toBe(false);
            expect(element(by.css('field-wrapper[field-name=prognos]')).isDisplayed()).toBe(false);
            expect(element(by.css('div[category-label=KAT_7.RBK]')).isDisplayed()).toBe(false);
            expect(element(by.css('div[category-label=KAT_9.RBK]')).isDisplayed()).toBe(false);
        });

    });

    describe('Visa intyg med smittbärarpenning (övrigt ifyllt)', function() {

        it('Visa intyg', function() {
            // Navigera tillbaka till listan
            viewPage.backToList();
            expect(inboxPage.isAt()).toBeTruthy();
            inboxPage.viewCertificate(intygsId2);
            expect(viewPage.isAt()).toBeTruthy();
        });

        it('Verifiera smittbärarpenning', function() {
            expect(viewPage.getDynamicLabelText('KAT_10.RBK')).toBe(texts['KAT_10.RBK']);
            expect(viewPage.getDynamicLabelText('FRG_27.RBK')).toBe(texts['FRG_27.RBK']);
            expect(viewPage.getTextContent('avstangningsmittskydd-yes')).toEqual('Ja');
            expect(viewPage.fieldNotShown('avstangningsmittskydd-no')).toBeTruthy();
        });

        it('Verifiera diagnos', function() {
            expect(viewPage.getDynamicLabelText('KAT_3.RBK')).toBe(texts['KAT_3.RBK']);
            expect(viewPage.getDynamicLabelText('FRG_6.RBK')).toBe(texts['FRG_6.RBK']);

            expect(element(by.id('diagnosKod0')).getText()).toBe('J22');
            expect(element(by.id('diagnosBeskrivning0')).getText())
                .toBe('Icke specificerad akut infektion i nedre luftvägarna');
            expect(element(by.id('diagnosKod1')).getText()).toBe('J37');
            expect(element(by.id('diagnosBeskrivning1')).getText())
                .toBe('Kronisk laryngit och laryngotrakeit (inflammation i struphuvudet och struphuvudet-luftstrupen)');
        });
        
        it('Verifiera bedömning', function() {
            expect(viewPage.getDynamicLabelText('KAT_6.RBK')).toBe(texts['KAT_6.RBK']);
            expect(viewPage.getDynamicLabelText('FRG_32.RBK')).toBe(texts['FRG_32.RBK']);
            expect(viewPage.getDynamicLabelText('KV_FKMU_0003.HELT_NEDSATT.RBK')).toBe(texts['KV_FKMU_0003.HELT_NEDSATT.RBK']);
            expect(viewPage.getDynamicLabelText('KV_FKMU_0003.TRE_FJARDEDEL.RBK')).toBe(texts['KV_FKMU_0003.TRE_FJARDEDEL.RBK']);

            expect(viewPage.getTextContent('grad_HELT_NEDSATT')).toEqual('100 procent');
            expect(viewPage.getTextContent('grad_TRE_FJARDEDEL')).toEqual('75 procent');
        });

        it('Verifiera att Övriga upplysningar syns', function() {
            expect(viewPage.getDynamicLabelText('KAT_8.RBK')).toBe(texts['KAT_8.RBK']);
            expect(viewPage.getDynamicLabelText('FRG_25.RBK')).toBe(texts['FRG_25.RBK']);

            expect(element(by.id('ovrigt')).isPresent()).toBe(true);
            expect(viewPage.getTextContent('ovrigt')).toEqual('Upplysningar ska skrivas här');
        });

        it('Kontrollera att övriga fält inte visas', function() {
            expect(element(by.css('div[category-label=KAT_1.RBK]')).isDisplayed()).toBe(false);
            expect(element(by.css('div[category-label=KAT_2.RBK]')).isDisplayed()).toBe(false);
            expect(element(by.css('div[category-label=KAT_4.RBK]')).isDisplayed()).toBe(false);
            expect(element(by.css('div[category-label=KAT_5.RBK]')).isDisplayed()).toBe(false);
            expect(element(by.css('field-wrapper[field-name=forsakringsmedicinsktBeslutsstod]')).isDisplayed()).toBe(false);
            expect(element(by.css('field-wrapper[field-name=arbetstidsforlaggning]')).isDisplayed()).toBe(false);
            expect(element(by.css('field-wrapper[field-name=arbetstidsforlaggningMotivering]')).isDisplayed()).toBe(false);
            expect(element(by.css('field-wrapper[field-name=arbetsresor]')).isDisplayed()).toBe(false);
            expect(element(by.css('field-wrapper[field-name=prognos]')).isDisplayed()).toBe(false);
            expect(element(by.css('div[category-label=KAT_7.RBK]')).isDisplayed()).toBe(false);
            expect(element(by.css('div[category-label=KAT_9.RBK]')).isDisplayed()).toBe(false);
        });

    });

});
