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
var aboutPage = miTestTools.pages.aboutPage;

describe('Logga in som medborgare', function() {

    var personId = '19121212-1212';

    beforeAll(function() {
        // Ta bort tidigare samtycken
        restHelper.deleteConsent(personId);
    });

    afterAll(function() {
        restHelper.deleteConsent(personId);
    });

    describe('Logga in och hamna på startsidan', function() {

        beforeEach(function() {
            browser.ignoreSynchronization = false;
        });

        it('Logga in', function() {
            welcomePage.get();
            specHelper.waitForAngularTestability();
            welcomePage.login(personId, true);
            specHelper.waitForAngularTestability();
        });

        it('Acceptera cookie', function() {
            consentPage.get();
            expect(element(by.id('cookie-usage-consent-btn')).isDisplayed()).toBeTruthy();
            element(by.id('cookie-usage-consent-btn')).sendKeys(protractor.Key.SPACE);
            expect(element(by.id('cookie-usage-consent-btn')).isPresent()).toBeFalsy();
        });

        it('Ge samtycke', function() {
            expect(consentPage.isAt()).toBeTruthy();
            consentPage.clickGiveConsent();
        });

        it('Header ska var Inkorgen', function() {
            expect(inboxPage.isAt()).toBeTruthy();
            expect(element(by.id('inboxHeader')).getText()).toBe('Inkorgen');
        });

        it('Logga in igen och verifiera att man då hamnar direkt på inkorgssidan', function() {
            specHelper.logout();
            welcomePage.get();
            specHelper.waitForAngularTestability();
            welcomePage.login(personId, false);
            specHelper.waitForAngularTestability();
            expect(inboxPage.isAt()).toBeTruthy();
        });

    });

    describe('Redan samtyckt', function() {

        beforeEach(function() {
            browser.ignoreSynchronization = false;
            restHelper.setConsent(personId);
        });

        it('Logga in', function() {
            welcomePage.get();
            specHelper.waitForAngularTestability();
            welcomePage.login(personId, false);
            specHelper.waitForAngularTestability();
        });

        it('Inkorgen visas', function() {
            expect(inboxPage.isAt()).toBeTruthy();
        });
    });

    describe('Återta samtycke', function() {

        beforeEach(function() {
            browser.ignoreSynchronization = false;
            restHelper.setConsent(personId);
        });

        it('Logga in', function() {
            welcomePage.get();
            specHelper.waitForAngularTestability();
            welcomePage.login(personId, false);
            specHelper.waitForAngularTestability();
        });

        it('Gå till om samtycke', function() {
            expect(inboxPage.isAt()).toBeTruthy();
            inboxPage.viewAbout();
            expect(aboutPage.isAt()).toBeTruthy();
            expect(aboutPage.aboutMinaIntygIsDisplayed()).toBeTruthy();
            aboutPage.viewConsent();
            expect(aboutPage.aboutConsentIsDisplayed()).toBeTruthy();
        });

        it('Återta samtycke', function() {
            aboutPage.revokeConsent();
            expect(aboutPage.revokeConsentDialogIsDisplayed()).toBeTruthy();
            aboutPage.confirmRevokeConsent();
        });

        it('Verifiera att ge samtyckesidan visas', function() {
            expect(consentPage.isAt()).toBeTruthy();
        });
    });

    it('Logga ut', function() {
        specHelper.logout();
    });

});
