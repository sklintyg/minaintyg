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
/*globals describe,it,beforeAll,afterAll */
'use strict';

var specHelper = miTestTools.helpers.spec;
var restHelper = miTestTools.helpers.rest;

var welcomePage = miTestTools.pages.welcomePage;
var inboxPage = miTestTools.pages.inboxPage;
var viewPage = miTestTools.pages.viewPage;

var genericTestdataBuilder = miTestTools.testdata.generic;

describe('Verifiera AG1-14', function() {

  var intygsId = null;
  var texts = null;

  beforeAll(function() {
    browser.ignoreSynchronization = false;

    // Load and cache expected dynamictext-values for this intygstype.
    restHelper.getTextResource('texterMU_AG114_v1.0.xml').then(function(textResources) {
      texts = textResources;
    }, function(err) {
      fail('Error during text lookup ' + err);
    });

    // Skapa intyget
    var intyg = genericTestdataBuilder.getAg114();
    intygsId = intyg.id;
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

    it('Verifiera grund för medicinskt underlag', function() {
      expect(viewPage.getTextContent('undersokningAvPatienten')).toEqual('2015-12-09');
      expect(viewPage.showsNoValue('undersokningAvPatienten')).toBeFalsy();

      expect(viewPage.getTextContent('telefonkontaktMedPatienten')).toEqual('2015-12-08');
      expect(viewPage.showsNoValue('telefonkontaktMedPatienten')).toBeFalsy();

      expect(viewPage.getTextContent('journaluppgifter')).toEqual('2015-12-10');
      expect(viewPage.showsNoValue('journaluppgifter')).toBeFalsy();

      expect(viewPage.getTextContent('annatGrundForMU')).toEqual('2015-12-13');
      expect(viewPage.showsNoValue('annatGrundForMU')).toBeFalsy();

      expect(viewPage.getTextContent('annatGrundForMUBeskrivning')).toEqual('Annat grund för MU i MI');
    });

    it('Verifiera sysselsättning', function() {
      expect(viewPage.getDynamicLabelText('KAT_1.RBK')).toBe(texts['KAT_1.RBK']);

      expect(viewPage.getTextContent('sysselsattning')).toBe(texts['KV_FKMU_0002.NUVARANDE_ARBETE.RBK']);
      expect(viewPage.getTextContent('nuvarandeArbete')).toEqual('nuvarande yrkesbeskrivning');
    });

    it('Verifiera diagnos', function() {
      desktopSize();
      expect(viewPage.getDynamicLabelText('KAT_2.RBK')).toBe(texts['KAT_2.RBK']);

      expect(viewPage.getTextContent('onskarFormedlaDiagnos')).toBe('Ja');

      expect(viewPage.getDynamicLabelText('FRG_4.RBK')).toBe(texts['FRG_4.RBK']);
      expect(viewPage.getTextContent('diagnoser-row0-col0')).toBe('S47');
      expect(viewPage.getTextContent('diagnoser-row0-col1')).toBe('Diagnos S47');

      expect(viewPage.getTextContent('diagnoser-row1-col0')).toBe('S48');
      expect(viewPage.getTextContent('diagnoser-row1-col1')).toBe('Diagnos S48');

      expect(viewPage.getTextContent('diagnoser-row2-col0')).toBe('S49');
      expect(viewPage.getTextContent('diagnoser-row2-col1')).toBe('Diagnos S49');

    });

    it('Verifiera arbetsförmåga', function() {
      expect(viewPage.getDynamicLabelText('KAT_3.RBK')).toBe(texts['KAT_3.RBK']);

      expect(viewPage.getTextContent('nedsattArbetsformaga')).toEqual('Kan jobba halvtid numera.');
      expect(viewPage.getTextContent('arbetsformagaTrotsSjukdom')).toEqual('Ja');
      expect(viewPage.getTextContent('arbetsformagaTrotsSjukdomBeskrivning')).toEqual('Beskrivning av förmåga');
    });

    it('Verifiera sjukskrivning', function() {
      expect(viewPage.getDynamicLabelText('KAT_4.RBK')).toBe(texts['KAT_4.RBK']);

      expect(viewPage.getTextContent('sjukskrivningsgrad')).toEqual('70%');
      expect(viewPage.getTextContent('sjukskrivningsperiod')).toContain('2018-11-01');
      expect(viewPage.getTextContent('sjukskrivningsperiod')).toContain('2018-11-20');
      //Alert box
      expect(viewPage.getDynamicLabelText('SKL-001.ALERT')).toBe(texts['SKL-001.ALERT']);
    });

    it('Verifiera Övriga upplysningar', function() {
      expect(viewPage.getDynamicLabelText('KAT_5.RBK')).toBe(texts['KAT_5.RBK']);

      expect(viewPage.getTextContent('ovrigaUpplysningar')).toEqual('Övriga upplysningar text');
    });

    it('Verifiera att Kontakt är angivet', function() {
      expect(viewPage.getDynamicLabelText('KAT_6.RBK')).toBe(texts['KAT_6.RBK']);

      expect(viewPage.getDynamicLabelText('FRG_9.RBK')).toBe(texts['FRG_9.RBK']);
      expect(viewPage.getTextContent('kontaktMedArbetsgivaren')).toEqual('Ja');
      expect(viewPage.getDynamicLabelText('DFR_9.2.RBK')).toBe(texts['DFR_9.2.RBK']);
      expect(viewPage.getTextContent('anledningTillKontakt')).toEqual('Gillar kontakt');
    });

    it('Verifiera att skapad av är angivet', function() {
      expect(viewPage.getTextContent('fullstandigtNamn')).toEqual('Jan Nilsson');
      expect(viewPage.getTextContent('vardenhet-telefon')).toEqual('Tel: 0101112131416');
      expect(viewPage.getTextContent('vardenhet-namn')).toEqual('WebCert Enhet 1, NMT vg3');
      expect(viewPage.getTextContent('vardenhet-adress')).toEqual('NMT gata 3, 12345 Testhult');

    });

  });

});
