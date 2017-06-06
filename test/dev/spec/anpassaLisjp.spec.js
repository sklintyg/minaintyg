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

var welcomePage = miTestTools.pages.welcomePage;
var consentPage = miTestTools.pages.consentPage;
var inboxPage = miTestTools.pages.inboxPage;
var viewPage = miTestTools.pages.viewPage;
var anpassaPage = miTestTools.pages.anpassaLisjpPage;

var genericTestdataBuilder = miTestTools.testdata.generic;

describe('Anpassa lisjp intyg för utskrift till arbetsgivare', function() {

    var intygsId = null;

    beforeAll(function() {
        // Rensa alla intyg för tolvan
        restHelper.deleteAllIntygForCitizen();

        // Ta bort tidigare samtycken
        restHelper.deleteConsent();

        var intyg = genericTestdataBuilder.getLisjp();
        intygsId = intyg.id;
        restHelper.createIntyg(intyg);
    });

    afterAll(function() {
        restHelper.deleteConsent();
        restHelper.deleteIntyg(intygsId);
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
            expect(element(by.id('inboxHeader')).getText()).toBe('Översikt över dina intyg');
        });

        it('Intyget ska finnas i listan', function() {
            expect(element(by.id('certificate-' + intygsId)).isPresent());
            expect(element(by.id('viewCertificateBtn-' + intygsId)).isPresent());
        });

        it('Visa intyg', function() {
            inboxPage.viewCertificate(intygsId);
            expect(viewPage.isAt()).toBeTruthy();
        });

        it('Gå till anpassa intyg', function() {
            viewPage.clickCustomizeCertificate();
            expect(anpassaPage.isAt()).toBeTruthy();
        });

        it('Gå till summary sidan med alla valda', function() {
            anpassaPage.clickShowSummary();
            expect(element(by.id('customizeCertificateSummaryHeader')).isDisplayed());
            expect(element.all(by.css('#lisjp-included-fields div.selectable-field-wrapper')).count()).toEqual(18);
            expect(element.all(by.css('#lisjp-excluded-fields div.selectable-field-wrapper')).count()).toEqual(0);
        });

        it('Gå tillbaka till första sidan', function() {
            anpassaPage.clickShowSelection();
            expect(anpassaPage.isAt()).toBeTruthy();
            expect(element(by.id('toggle-select-option-FRG_1.RBK')).isDisplayed());
        });


        it('Bocka ur "grund för MU" och "Diagnos" och gå till summary igen', function() {
            element(by.id('toggle-select-option-FRG_1.RBK')).click();
            element(by.id('toggle-select-option-FRG_6.RBK')).click();

            anpassaPage.clickShowSummary();
            expect(element(by.id('customizeCertificateSummaryHeader')).isDisplayed());

        });

        it('Nu skall 2 vara bortvalda', function() {
            expect(element.all(by.css('#lisjp-included-fields div.selectable-field-wrapper')).count()).toEqual(16);
            expect(element.all(by.css('#lisjp-excluded-fields div.selectable-field-wrapper')).count()).toEqual(2);
        });

        it('gå till nedladdningssteget', function() {
            anpassaPage.showDownloadBtn.click();
            expect(element(by.id('downloadprint')).isDisplayed());
        });

    });


    afterAll(function() {
        restHelper.deleteIntyg(intygsId);
    });

});
