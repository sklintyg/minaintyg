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
var startPage = miTestTools.pages.startPage;

describe('Logga in som medborgare', function() {

    it ('Ta bort tidigare samtycken', function() {
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
            welcomePage.login();
            specHelper.waitForAngularTestability();
        });

        it('Ge samtycke', function() {
            specHelper.waitForAngularTestability();
            expect(consentPage.isAt()).toBeTruthy();
            consentPage.clickGiveConsent();
        });

        it('Header ska var Inkorgen', function() {
            specHelper.waitForAngularTestability();
            expect(startPage.isAt()).toBeTruthy();
            expect(element(by.id('inboxHeader')).getText()).toBe('Inkorgen');
        });

        it('Acceptera cookie', function() {
            specHelper.waitForAngularTestability();
            element(by.id('cookie-usage-consent-btn')).click();
        });

    });

    it('Logga ut', function() {
        specHelper.logout();
    });

});
