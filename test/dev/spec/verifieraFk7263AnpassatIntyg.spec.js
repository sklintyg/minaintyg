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

describe('Verifiera FK7263 anpassat arbetsivarintyg', function() {

    var intygsId = null;

    beforeAll(function() {
        // Givet samtycke
        restHelper.setConsent();

        var intyg = fk7263.getIntyg();
        intygsId = intyg.id;
        restHelper.createIntyg(intyg);

    });

    describe('Logga in och gå till anpassat intyg', function() {

        // Logga in
        it('Logga in', function() {
            welcomePage.get();
            specHelper.waitForAngularTestability();
            welcomePage.login('19121212-1212', false);
            specHelper.waitForAngularTestability();
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
            inboxPage.viewCertificate(intygsId);
        });

        it('Gå till anpassat intyg', function() {
            expect(viewPage.isAt()).toBeTruthy();
            viewPage.clickCustomizeCertificate();
        });

    });

    describe('Verifiera anpassat intyg', function() {
        it('options', function() {
            expect(customPage.isAt()).toBeTruthy();

            expect(element(by.id('option.2')).isSelected()).toBe(true);
            expect(element(by.id('option.3')).isSelected()).toBe(true);
            expect(element(by.id('option.4')).isSelected()).toBe(true);
            expect(element(by.id('option.4b')).isSelected()).toBe(true);
            expect(element(by.id('option.5')).isSelected()).toBe(true);
            expect(element(by.id('option.6a')).isSelected()).toBe(true);
            expect(element(by.id('option.7')).isSelected()).toBe(true);
            expect(element(by.id('option.8a')).isSelected()).toBe(true);
            expect(element(by.id('option.9')).isSelected()).toBe(true);
            expect(element(by.id('option.10')).isSelected()).toBe(true);
            expect(element(by.id('option.12')).isSelected()).toBe(true);
            expect(element(by.id('option.13')).isSelected()).toBe(true);
        });
    });

    afterAll(function() {
        restHelper.deleteIntyg(intygsId);
    });

});
