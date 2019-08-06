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
var anpassaPage = miTestTools.pages.anpassaAg114Page;

var genericTestdataBuilder = miTestTools.testdata.generic;

describe('Anpassa AG1-14', function() {

    var intygsId = null;
    var texts = null;

    beforeAll(function() {
        browser.ignoreSynchronization = false;

        // Load and cache expected dynamictext-values for this intygstype.
        restHelper.getTextResource('texterMU_AG114_v1.0.xml').then(function(textResource) {
            texts = textResource;
        }, function(err) {
            fail('Error during text lookup ' + err);
        });

        // Skapa intyget
        var intyg = genericTestdataBuilder.getAg114();
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


    describe('Verifiera anpassad utskrift', function() {

        it('Visa intyg', function() {
            inboxPage.viewCertificate(intygsId);
            expect(viewPage.isAt()).toBeTruthy();
        });


        it('Gå till anpassa intyg', function() {
            viewPage.clickCustomizeCertificate();
            expect(anpassaPage.isAt()).toBeTruthy();
        });

        it('Gå till summary sidan utan att toggla bort diagnos', function() {
            anpassaPage.clickShowSummary();
            expect(element(by.id('customizeCertificateSummaryHeader')).isDisplayed());
            expect(element.all(by.css('#ag114-included-fields div.selectable-field-wrapper')).count()).toEqual(8);
            expect(element.all(by.css('#ag114-excluded-fields div.selectable-field-wrapper')).count()).toEqual(0);
        });

        it('Gå tillbaka till första sidan', function() {
            anpassaPage.clickShowSelection();
            expect(anpassaPage.isAt()).toBeTruthy();
            expect(element(by.id('toggle-select-option-FRG_3.RBK')).isDisplayed());
        });


        it('Bocka ur "Diagnos" och gå till summary igen', function() {
            element(by.id('toggle-select-option-FRG_3.RBK')).click();

            anpassaPage.clickShowSummary();
            expect(element(by.id('customizeCertificateSummaryHeader')).isDisplayed());
        });

        it('Nu skall 1 vara bortvalda', function() {
            expect(element.all(by.css('#ag114-included-fields div.selectable-field-wrapper')).count()).toEqual(7);
            expect(element.all(by.css('#ag114-excluded-fields div.selectable-field-wrapper')).count()).toEqual(1);
        });

        it('gå till nedladdningssteget', function() {
            anpassaPage.showDownloadBtn.click();
            expect(element(by.id('downloadprint')).isDisplayed());
        });
    });

});
