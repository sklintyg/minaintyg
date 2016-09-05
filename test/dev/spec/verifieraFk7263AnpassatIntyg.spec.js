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
var customPage = miTestTools.pages.customPage;

var fk7263 = miTestTools.testdata.fk7263;

var EC = protractor.ExpectedConditions;

fdescribe('Verifiera FK7263 anpassat arbetsivarintyg', function() {

    var intygsId = null;

    beforeAll(function() {
        // Ta bort tidigare samtycken
        restHelper.deleteConsent();

        var intyg = fk7263.getIntyg();
        intygsId = intyg.id;
        restHelper.createIntyg(intyg);

    });

    fdescribe('Logga in och gå till anpassat intyg', function() {

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

        it('Acceptera cookie', function() {
            browser.wait(EC.elementToBeClickable(element(by.id('cookie-usage-consent-btn'))), 5000);
            element(by.id('cookie-usage-consent-btn')).sendKeys(protractor.Key.SPACE);
            browser.wait(EC.invisibilityOf(element(by.id('cookie-usage-consent-btn'))), 5000);
        });

        it('Ge samtycke', function() {
            expect(consentPage.isAt()).toBeTruthy();
            consentPage.clickGiveConsent();
        });


        it('Header ska var Inkorgen', function() {
            expect(inboxPage.isAt()).toBeTruthy();
            expect(element(by.id('inboxHeader')).getText()).toBe('Inkorgen');
        });

        it('Intyget ska finnas i listan', function() {
            expect(element(by.id('certificate-' + intygsId)).isPresent());
            expect(element(by.id('viewCertificateBtn-' + intygsId)).isPresent());
        });

        it('Visa intyg', function() {
            expect(inboxPage.isAt()).toBeTruthy();
            inboxPage.viewCertificate('viewCertificateBtn-' + intygsId);
        });

        it('Gå till anpassat intyg', function() {
            expect(viewPage.isAt()).toBeTruthy();
            viewPage.clickCustomizeCertificate();
        });

    });

    fdescribe('Verifiera anpassat intyg', function() {
        expect(element(by.id('options.2')).isSelected()).toBe(true);
        expect(element(by.id('options.3')).isSelected()).toBe(true);
        expect(element(by.id('options.4')).isSelected()).toBe(true);
        expect(element(by.id('options.4b')).isSelected()).toBe(true);
        expect(element(by.id('options.5')).isSelected()).toBe(true);
        expect(element(by.id('options.6a')).isSelected()).toBe(true);
        expect(element(by.id('options.7')).isSelected()).toBe(true);
        expect(element(by.id('options.8a')).isSelected()).toBe(true);
        expect(element(by.id('options.9')).isSelected()).toBe(true);
        expect(element(by.id('options.10')).isSelected()).toBe(true);
        expect(element(by.id('options.12')).isSelected()).toBe(true);
        expect(element(by.id('options.13')).isSelected()).toBe(true);
    });

    afterAll(function() {
        restHelper.deleteIntyg(intygsId);
    });

});
