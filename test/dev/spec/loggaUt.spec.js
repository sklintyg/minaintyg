/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
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
var accessdeniedPage = miTestTools.pages.accessdeniedPage;

describe('Logga ut', function() {

  var personId = '19010101-0101';

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

    it('Mina Intyg visas när man loggar ut', function() {
      browser.ignoreSynchronization = true;
      specHelper.logout();
      browser.driver.sleep(3000);
      expect(browser.getTitle()).toEqual('Mina intyg');
      expect(browser.getCurrentUrl()).toContain('/error.jsp?reason=logout');
    });

    it('Access denied visas om invånaren försöker navigera till startsidan', function() {
      browser.driver.get(browser.baseUrl + 'web/start');
      specHelper.waitForAngularTestability();
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
      browser.driver.sleep(3000);
      expect(browser.getCurrentUrl()).toContain('https://e-tjanster.1177.se/');
    });

    it('Access denied visas om invånaren försöker navigera till startsidan', function() {
      browser.driver.get(browser.baseUrl + 'web/start');
      specHelper.waitForAngularTestability();
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
      specHelper.waitForAngularTestability();
      expect(inboxPage.isAt()).toBeTruthy();
    });

    it('Besök extern sida och vänta 5 sekunder', function() {
      browser.ignoreSynchronization = true;
      browser.driver.get('http://www.google.com');
      expect(browser.getTitle()).toEqual('Google');
      browser.driver.sleep(7000);
    });

    it('Access denied visas om invånaren försöker navigera till startsidan', function() {
      browser.driver.get(browser.baseUrl + 'web/start');
      specHelper.waitForAngularTestability();
      expect(accessdeniedPage.isAt()).toBeTruthy();
    });

  });

});
