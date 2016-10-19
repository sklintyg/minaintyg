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
var consentPage = miTestTools.pages.consentPage;
var inboxPage = miTestTools.pages.inboxPage;

var EC = protractor.ExpectedConditions;

describe('Logga in som medborgare', function() {

    beforeAll(function() {
        // Ta bort tidigare samtycken
        restHelper.deleteConsent();
    });

    describe('Logga in och hamna på startsidan', function() {

        beforeEach(function() {
            browser.ignoreSynchronization = false;
        });

        // Logga in
        it('Logga in', function() {
            welcomePage.get();
            specHelper.waitForAngularTestability();
            welcomePage.login('19121212-1212', true);
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

    });

    it('Logga ut', function() {
        specHelper.logout();
    });

});
