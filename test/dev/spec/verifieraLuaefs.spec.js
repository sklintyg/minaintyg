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
/*globals pages */
/*globals describe,it,helpers */
'use strict';

var specHelper = miTestTools.helpers.spec;
var restHelper = miTestTools.helpers.rest;
var textHelper = miTestTools.helpers.text;

var welcomePage = miTestTools.pages.welcomePage;
var consentPage = miTestTools.pages.consentPage;
var inboxPage = miTestTools.pages.inboxPage;
var viewPage = miTestTools.pages.viewPage;

var luaefs = miTestTools.testdata.luaefs;

describe('Verifiera LUAE_FS intyg', function() {

    var texts = null;
    var intygsId = null;

    beforeAll(function() {
        //Load and cache expected dynamictext-values for this intygstype.
        textHelper.readTextsFromFkTextFile('texterMU_LUAE_FS_v1.0.xml').then(function(textResources) {
            texts = textResources;
        }, function(err) {
            fail('Error during text lookup ' + err);
        });

        // Ta bort tidigare samtycken
        restHelper.deleteConsent();

        var intyg = luaefs.getIntyg();
        intygsId = intyg.id;
        restHelper.createIntyg(intyg);
    });

    describe('Logga in', function() {

        beforeEach(function() {
            browser.ignoreSynchronization = false;
        });

        // Logga in
        it('Logga in', function() {
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

    describe('Verifiera intyg', function() {

        it('Header ska var Inkorgen', function() {
            expect(inboxPage.isAt()).toBeTruthy();
            expect(element(by.id('inboxHeader')).getText()).toBe('Inkorgen');
        });

        it('Intyget ska finnas i listan', function() {
            expect(element(by.id('certificate-' + intygsId)).isPresent());
            expect(element(by.id('viewCertificateBtn-' + intygsId)).isPresent());
        });

        it('Visa intyg', function() {
            inboxPage.viewCertificate('viewCertificateBtn-' + intygsId);
            expect(viewPage.isAt()).toBeTruthy();
        });

        it('Verifiera intygshuvud', function() {
            //startPage.viewCertificate('viewCertificateBtn-' + intygsId);
        });

        it('Verifiera grund för medicinskt underlag', function() {
            expect(viewPage.getDynamicLabelText('KAT_1.RBK')).toBe(texts['KAT_1.RBK']);
            expect(viewPage.getDynamicLabelText('FRG_1.RBK')).toBe(texts['FRG_1.RBK']);
            expect(viewPage.getDynamicLabelText('KV_FKMU_0001.UNDERSOKNING.RBK')).toBe(texts['KV_FKMU_0001.UNDERSOKNING.RBK']);
            expect(viewPage.getDynamicLabelText('KV_FKMU_0001.JOURNALUPPGIFTER.RBK')).toBe(texts['KV_FKMU_0001.JOURNALUPPGIFTER.RBK']);
            expect(viewPage.getDynamicLabelText('KV_FKMU_0001.ANHORIG.RBK')).toBe(texts['KV_FKMU_0001.ANHORIG.RBK']);
            expect(viewPage.getDynamicLabelText('KV_FKMU_0001.ANNAT.RBK')).toBe(texts['KV_FKMU_0001.ANNAT.RBK']);
            expect(viewPage.getDynamicLabelText('FRG_2.RBK')).toBe(texts['FRG_2.RBK']);
            expect(viewPage.getDynamicLabelText('FRG_3.RBK')).toBe(texts['FRG_3.RBK']);
        });

        it('Verifiera diagnos', function() {
            expect(viewPage.getDynamicLabelText('KAT_3.RBK')).toBe(texts['KAT_3.RBK']);
            expect(viewPage.getDynamicLabelText('FRG_3.RBK')).toBe(texts['FRG_3.RBK']);
            expect(viewPage.getDynamicLabelText('DFR_6.2.RBK')).toBe(texts['DFR_6.2.RBK']);
        });

        it('Verifiera funktionsnedsättning', function() {
            expect(viewPage.getDynamicLabelText('KAT_4.RBK')).toBe(texts['KAT_4.RBK']);
            expect(viewPage.getDynamicLabelText('FRG_15.RBK')).toBe(texts['FRG_15.RBK']);
            expect(viewPage.getDynamicLabelText('FRG_16.RBK')).toBe(texts['FRG_16.RBK']);
        });

        it('Verifiera övrigt', function() {
            expect(viewPage.getDynamicLabelText('KAT_5.RBK')).toBe(texts['KAT_5.RBK']);
            expect(viewPage.getDynamicLabelText('FRG_25.RBK')).toBe(texts['FRG_25.RBK']);
        });

        it('Verifiera kontakt', function() {
            expect(viewPage.getDynamicLabelText('KAT_6.RBK')).toBe(texts['KAT_6.RBK']);
            expect(viewPage.getDynamicLabelText('DFR_26.1.RBK')).toBe(texts['DFR_26.1.RBK']);
            expect(viewPage.getDynamicLabelText('DFR_26.2.RBK')).toBe(texts['DFR_26.2.RBK']);
        });

        it('Verifiera tilläggsfrågor', function() {
            expect(viewPage.getDynamicLabelText('KAT_9999.RBK')).toBe(texts['KAT_9999.RBK']);
            // These test should work when the internal transport format changes
            //expect(viewPage.getDynamicLabelText('DFR_9001.1.RBK')).toBe(texts['DFR_9001.1.RBK']);
            //expect(viewPage.getDynamicLabelText('DFR_9002.1.RBK')).toBe(texts['DFR_9002.1.RBK']);
        });

    });

    afterAll(function() {
        restHelper.deleteIntyg(intygsId);
    });

});
