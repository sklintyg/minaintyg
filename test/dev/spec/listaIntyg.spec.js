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

var genericTestDataBuilder = miTestTools.testdata.generic;

fdescribe('Lista intyg', function() {

  var personId = '19010101-0101';
  var personIdNoCertificates = '19121212-9999';
  var fk7263IntygsId = null;
  var tsBasIntygsId = null;
  var tsDiabetesIntygsId = null;
  var toLisjpIntygsId = null;
  var fromLisjpIntygsId = null;

  beforeAll(function() {
    var fk7263Intyg = genericTestDataBuilder.getFk7263(personId, '2017-03-18T00:00:01.234');
    fk7263IntygsId = fk7263Intyg.id;
    restHelper.createIntyg(fk7263Intyg);

    var tsBasIntyg = genericTestDataBuilder.getTsBas(personId, '2016-03-18T00:00:01');
    tsBasIntygsId = tsBasIntyg.id;
    restHelper.createIntyg(tsBasIntyg);

    var tsDiabetesIntyg = genericTestDataBuilder.getTsDiabetes(personId);
    //this revoked certificate should not appear
    tsDiabetesIntygsId = tsDiabetesIntyg.id;
    tsDiabetesIntyg.certificateStates.push({
      target: 'HSVARD',
      state: 'CANCELLED',
      timestamp: '2013-03-18T00:00:01.234'
    });
    restHelper.createIntyg(tsDiabetesIntyg);

    var toLisjpIntyg = genericTestDataBuilder.getLisjpFull(personId);
    var fromLisjpIntyg = genericTestDataBuilder.getLisjpFull(personId);
    toLisjpIntygsId = toLisjpIntyg.id;
    fromLisjpIntygsId = fromLisjpIntyg.id
    toLisjpIntyg.certificateRelation = {
    fromIntygsId : toLisjpIntygsId,
    toIntygsId : fromLisjpIntygsId,
    relationKod : 'KOMPLT',
    skapad : '2013-03-18T00:00:01.234'
    }
    restHelper.createIntyg(toLisjpIntyg);
    restHelper.createIntyg(fromLisjpIntyg);
  });

  afterAll(function() {
    restHelper.deleteIntyg(fk7263IntygsId);
    restHelper.deleteIntyg(tsBasIntygsId);
    restHelper.deleteIntyg(tsDiabetesIntygsId);
    restHelper.deleteIntyg(toLisjpIntygsId);
    restHelper.deleteIntyg(fromLisjpIntygsId);
  });

  describe('Invånare med intyg', function() {

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

    it('Givet att användare har befintliga intyg så skall dessa visas i listan', function() {
      expect(inboxPage.certificateTableIsShown()).toBeTruthy();
      expect(inboxPage.certificateExists(fk7263IntygsId)).toBeTruthy();
      expect(inboxPage.certificateExists(tsBasIntygsId)).toBeTruthy();
      //revoked should NOT exist in list
      expect(inboxPage.certificateExists(tsDiabetesIntygsId)).toBeFalsy();
    });

    it('Listan skall innehålla en årsavskiljare för 2016', function() {
      expect(element(by.id('mi-year-divider-2016')).isDisplayed()).toBeTruthy();
    });

    it('Intyg avser innehåller information för ett TS-intyg', function() {
      expect(inboxPage.complementaryInfo(tsBasIntygsId).length).not.toEqual(0);
    });

    it('Verifiera text för intyg som inte har någon händelse', function() {
      expect(inboxPage.hasEvent(fk7263IntygsId, 'Inga händelser')).toBeTruthy();
    });

    it('Verifiera text för kompletterat lisjp-intyg', function() {
    expect(inboxPage.hasEvent(fromLisjpIntygsId, 'Kompletterades av vården med ett nytt intyg.')).toBeTruthy;
    })

  });

  describe('Invånare utan intyg', function() {

    beforeEach(function() {
      browser.ignoreSynchronization = false;
    });

    it('Logga in', function() {
      welcomePage.get();
      specHelper.waitForAngularTestability();
      welcomePage.login(personIdNoCertificates, false);
      specHelper.waitForAngularTestability();
      expect(inboxPage.isAt()).toBeTruthy();
    });

    it('Givet att användaren EJ har intyg så skall det ej visas något', function() {
      expect(inboxPage.noCertificatesIsShown()).toBeTruthy();
    });

  });
});
