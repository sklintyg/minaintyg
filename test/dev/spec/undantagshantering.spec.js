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
var notfoundPage = miTestTools.pages.notfoundPage;

describe('Undantagshantering', function() {

  var personId = '19010101-0101';

  describe('Sidan hittades ej', function() {

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

    it('Gå till sida som inte finns', function() {
      browser.driver.get(browser.baseUrl + '../okändsida');
    });

    it('Verifiera att 404-sidan visas korrekt', function() {
      expect(notfoundPage.isAt()).toBeTruthy();
    });

  });

});
