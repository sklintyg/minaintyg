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

/*globals browser */
/*globals pages */
/*globals describe,it,helpers */
'use strict';

var specHelper = miTestTools.helpers.spec;
var restHelper = miTestTools.helpers.rest;

var welcomePage = miTestTools.pages.welcomePage;
var inboxPage = miTestTools.pages.inboxPage;
var viewPage = miTestTools.pages.viewPage;

var genericTestdataBuilder = miTestTools.testdata.generic;

describe('Verifiera LUAE_FS', function() {

  var intygsId = null;
  var texts = null;

  beforeAll(function() {
    browser.ignoreSynchronization = false;

    // Load and cache expected dynamictext-values for this intygstype.
    restHelper.getTextResource('texterMU_LUAE_FS_v1.0.xml').then(function(textResources) {
      texts = textResources;
    }, function(err) {
      fail('Error during text lookup ' + err);
    });

    // Skapa intygen
    var intyg = genericTestdataBuilder.getLuaefs();
    intygsId = intyg.id;
    debug(intygsId);
    restHelper.createIntyg(intyg);
  });

  afterAll(function() {
    restHelper.deleteIntyg(intygsId);
  });

  describe('Logga in', function() {
    // Logga in
    it('Logga in', function() {
      welcomePage.get();
      specHelper.waitForAngularTestability();
      welcomePage.login();
      specHelper.waitForAngularTestability();
    });

    it('Header ska var Inkorgen', function() {
      expect(inboxPage.isAt()).toBeTruthy();
      expect(element(by.id('inboxHeader')).getText()).toBe('Översikt över dina intyg');
    });

    it('Intyg ska finnas i listan', function() {
      expect(element(by.id('certificate-' + intygsId)).isPresent());
      expect(element(by.id('viewCertificateBtn-' + intygsId)).isPresent());
    });
  });

  describe('Verifiera intyg', function() {

    it('Visa intyg', function() {
      inboxPage.viewCertificate(intygsId);
      expect(viewPage.isAt()).toBeTruthy();
    });

    it('Verifiera att frågor under grund för medicinskt underlag är angivet', function() {
      expect(viewPage.getDynamicLabelText('KAT_1.RBK')).toBe(texts['KAT_1.RBK']);

      expect(viewPage.getTextContent('undersokningAvPatienten')).toEqual('2016-05-26');
      expect(viewPage.getTextContent('journaluppgifter')).toEqual('2016-05-26');
      expect(viewPage.getTextContent('anhorigsBeskrivningAvPatienten')).toEqual('2016-05-26');
      expect(viewPage.getTextContent('annatGrundForMU')).toEqual('2016-05-26');
      expect(viewPage.getTextContent('annatGrundForMUBeskrivning')).toEqual('Uppgifter från habiliteringscentrum.');

      expect(viewPage.getTextContent('kannedomOmPatient')).toEqual('2016-05-20');

      // Andra medicinska underlag
      expect(viewPage.getTextContent('underlagFinns')).toEqual('Ja');

      expect(viewPage.getTextContent('underlag-row0-col0')).toEqual('Underlag från psykolog');
      expect(viewPage.getTextContent('underlag-row0-col1')).toEqual('2015-09-03');
      expect(viewPage.getTextContent('underlag-row0-col2')).toEqual('Skickas med posten');

      expect(viewPage.getTextContent('mobile-underlag-row0-col0')).toEqual('notshown');
      expect(viewPage.getTextContent('mobile-underlag-row0-col1')).toEqual('notshown');
      expect(viewPage.getTextContent('mobile-underlag-row0-col2')).toEqual('notshown');
    });

    it('Verifiera medicinskt underlag mobil ', function() {
      mobileSize();

      expect(viewPage.getTextContent('mobile-underlag-row0-col0')).toEqual('Underlag från psykolog');
      expect(viewPage.getTextContent('mobile-underlag-row0-col1')).toEqual('2015-09-03');
      expect(viewPage.getTextContent('mobile-underlag-row0-col2')).toEqual('Skickas med posten');

      expect(viewPage.getTextContent('underlag-row0-col0')).toEqual('notshown');
      expect(viewPage.getTextContent('underlag-row0-col1')).toEqual('notshown');
      expect(viewPage.getTextContent('underlag-row0-col2')).toEqual('notshown');
    });

    it('Verifiera att korrekta diagnoser är angivet', function() {
      desktopSize();

      expect(viewPage.getDynamicLabelText('KAT_3.RBK')).toBe(texts['KAT_3.RBK']);

      expect(viewPage.getDynamicLabelText('FRG_6.RBK')).toBe(texts['FRG_6.RBK']);
      expect(viewPage.getTextContent('diagnoser-row0-col0')).toBe('S47');
      expect(viewPage.getTextContent('diagnoser-row0-col1'))
      .toBe('Klämskada skuldra');

    });

    it('Verifiera att funktionsnedsättningar är angivet', function() {
      expect(viewPage.getDynamicLabelText('KAT_4.RBK')).toBe(texts['KAT_4.RBK']);

      expect(viewPage.getDynamicLabelText('FRG_15.RBK')).toBe(texts['FRG_15.RBK']);
      expect(viewPage.getTextContent('funktionsnedsattningDebut')).toBe('Skoldansen');

      expect(viewPage.getDynamicLabelText('FRG_16.RBK')).toBe(texts['FRG_16.RBK']);
      expect(viewPage.getTextContent('funktionsnedsattningPaverkan')).toBe('Haltar när han dansar');
    });

    it('Verifiera att Övriga upplysningar är angivet', function() {
      expect(viewPage.getDynamicLabelText('KAT_5.RBK')).toBe(texts['KAT_5.RBK']);

      expect(viewPage.getDynamicLabelText('FRG_25.RBK')).toBe(texts['FRG_25.RBK']);
      expect(viewPage.getTextContent('ovrigt')).toEqual('Detta skulle kunna innebära sämre möjlighet att få ställa upp i danstävlingar');
    });

    it('Verifiera att Kontakt är angivet', function() {
      expect(viewPage.getDynamicLabelText('KAT_6.RBK')).toBe(texts['KAT_6.RBK']);
      expect(viewPage.getDynamicLabelText('FRG_26.RBK')).toBe(texts['FRG_26.RBK']);
      expect(viewPage.getDynamicLabelText('DFR_26.1.RBK')).toBe(texts['DFR_26.1.RBK']);
      expect(viewPage.getTextContent('kontaktMedFk')).toEqual('Ja');
      expect(viewPage.getDynamicLabelText('DFR_26.2.RBK')).toBe(texts['DFR_26.2.RBK']);
      expect(viewPage.getTextContent('anledningTillKontakt')).toEqual('Vill stämma av ersättningen');
      expect(viewPage.showsNoValue('anledningTillKontakt')).toBeFalsy();
    });

    it('Verifiera att skapad av är angivet', function() {
      expect(viewPage.getTextContent('fullstandigtNamn')).toEqual('Jan Nilsson');
      expect(viewPage.getTextContent('vardenhet-telefon')).toEqual('Tel: 0812341234');
      expect(viewPage.getTextContent('vardenhet-namn')).toEqual('WebCert Enhet 1, WebCert Vårdgivare 1');
      expect(viewPage.getTextContent('vardenhet-adress')).toEqual('Enhetsg. 1, 100 10 Stadby');

    });

    it('Verifiera att tilläggsfrågorna är besvarade och har rätt rubrik', function() {
      expect(viewPage.showsNoValue('tillaggsfragor-0--svar')).toBeFalsy();
      expect(viewPage.getDynamicLabelText('DFR_9001.1.RBK')).toBe(texts['DFR_9001.1.RBK']);
      expect(viewPage.getTextContent('tillaggsfragor-0--svar')).toEqual('Tämligen påverkad');

      expect(viewPage.showsNoValue('tillaggsfragor-1--svar')).toBeFalsy();
      expect(viewPage.getDynamicLabelText('DFR_9002.1.RBK')).toBe(texts['DFR_9002.1.RBK']);
      expect(viewPage.getTextContent('tillaggsfragor-1--svar')).toEqual('Minst 3 fot');

    });

  });

});
