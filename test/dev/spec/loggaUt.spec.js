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
var inboxPage = miTestTools.pages.inboxPage;
var mvklogoutPage = miTestTools.pages.mvklogoutPage;
var mvkloginPage = miTestTools.pages.mvkloginPage;
var accessdeniedPage = miTestTools.pages.accessdeniedPage;

describe('Logga ut', function() {

    var personId = '19010101-0101';

    beforeAll(function() {
        restHelper.setConsent(personId);
    });

    afterAll(function() {
        restHelper.deleteConsent(personId);
    });

    describe('Logga ut', function() {

        beforeEach(function() {
            browser.ignoreSynchronization = false;
        });

        it('Logga in', function() {
            welcomePage.get();
            specHelper.waitForAngularTestability();
            welcomePage.login(personId, false);
            specHelper.waitForAngularTestability();
            expect(inboxPage.isAt()).toBeTruthy();
        });

        it('Mvks utloggningssida visas när man loggar ut', function() {
            browser.ignoreSynchronization = true;
            specHelper.logout();
            expect(mvklogoutPage.isAt()).toBeTruthy();
        });

        it('Access denied visas om invånaren försöker navigera till startsidan', function() {
            browser.driver.get(browser.baseUrl + 'web/start');
            expect(accessdeniedPage.isAt()).toBeTruthy();
        });

    });

    describe('Tillbaka till Mvk', function() {

        beforeEach(function() {
            browser.ignoreSynchronization = false;
        });

        it('Logga in', function() {
            welcomePage.get();
            specHelper.waitForAngularTestability();
            welcomePage.login(personId, false);
            specHelper.waitForAngularTestability();
            expect(inboxPage.isAt()).toBeTruthy();
        });

        it('Välj "Tillbaka till Mina Vårdkontakter" i headern', function() {
            browser.ignoreSynchronization = true;
            specHelper.backToMvk();
            // Här visas MVK's inloggningssida eftersom vi kör i testmiljön och ej är inloggade i MVK.
            expect(mvkloginPage.isAt()).toBeTruthy();
        });

        it('Access denied visas om invånaren försöker navigera till startsidan', function() {
            browser.driver.get(browser.baseUrl + 'web/start');
            expect(accessdeniedPage.isAt()).toBeTruthy();
        });

    });

    describe('Utloggning vid vidarenavigering', function() {

        beforeEach(function() {
            browser.ignoreSynchronization = false;
        });

        it('Logga in', function() {
            welcomePage.get();
            specHelper.waitForAngularTestability();
            welcomePage.login(personId, false);
            specHelper.waitForAngularTestability();
            expect(inboxPage.isAt()).toBeTruthy();
        });

        it('Besök extern sida', function() {
            browser.ignoreSynchronization = true;
            browser.driver.get('http://www.google.com');
            expect(browser.getTitle()).toEqual('Google');
        });

        it('Inkorgsidan visas när invånaren går tillbaka direkt', function() {
            browser.driver.get(browser.baseUrl + 'web/start');
            expect(inboxPage.isAt()).toBeTruthy();
        });

        it('Besök extern sida och vänta 5 sekunder', function() {
            browser.ignoreSynchronization = true;
            browser.driver.get('http://www.google.com');
            expect(browser.getTitle()).toEqual('Google');
            browser.driver.sleep(5000);
        });

        it('Access denied visas om invånaren försöker navigera till startsidan', function() {
            browser.driver.get(browser.baseUrl + 'web/start');
            expect(accessdeniedPage.isAt()).toBeTruthy();
        });

    });

});
