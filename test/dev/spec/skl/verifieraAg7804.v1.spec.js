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
/*globals describe,it,beforeAll,afterAll */
'use strict';

var specHelper = miTestTools.helpers.spec;
var restHelper = miTestTools.helpers.rest;

var welcomePage = miTestTools.pages.welcomePage;
var inboxPage = miTestTools.pages.inboxPage;
var viewPage = miTestTools.pages.viewPage;

var genericTestdataBuilder = miTestTools.testdata.generic;

describe('Verifiera AG7804', function() {

  var intygsId1 = null;
  var intygsId2 = null;
  var texts = null;

  beforeAll(function() {
    browser.ignoreSynchronization = false;

    // Load and cache expected dynamictext-values for this intygstype.
    restHelper.getTextResource('texterMU_AG7804_v1.0.xml').then(function(textResources) {
      texts = textResources;
    }, function(err) {
      fail('Error during text lookup ' + err);
    });

    // Skapa intygen
    var intyg1 = genericTestdataBuilder.getAg7804Smittskydd();
    intygsId1 = intyg1.id;
    restHelper.createIntyg(intyg1);

    var intyg2 = genericTestdataBuilder.getAg7804();
    intygsId2 = intyg2.id;
    restHelper.createIntyg(intyg2);
  });

  afterAll(function() {
    restHelper.deleteIntyg(intygsId1);
    restHelper.deleteIntyg(intygsId2);
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

    it('Intyg 1 ska finnas i listan', function() {
      expect(element(by.id('certificate-' + intygsId1)).isPresent());
      expect(element(by.id('viewCertificateBtn-' + intygsId1)).isPresent());
    });

    it('Intyg 2 ska finnas i listan', function() {
      expect(element(by.id('certificate-' + intygsId2)).isPresent());
      expect(element(by.id('viewCertificateBtn-' + intygsId2)).isPresent());
    });
  });

  describe('Visa intyg med smittbärarpenning', function() {

    it('Intyget visas', function() {
      inboxPage.viewCertificate(intygsId1);
      expect(viewPage.isAt()).toBeTruthy();
    });

    it('Verifiera smittbärarpenning är Ja', function() {
      expect(viewPage.getDynamicLabelText('KAT_10.RBK')).toBe(texts['KAT_10.RBK']);
      expect(viewPage.getTextContent('avstangningSmittskydd')).toEqual('Ja');
    });

    it('Verifiera grund för medicinskt underlag är ej angivet', function() {
      expect(viewPage.showsNoValue('undersokningAvPatienten')).toBeTruthy();
      expect(viewPage.showsNoValue('telefonkontaktMedPatienten')).toBeTruthy();
      expect(viewPage.showsNoValue('journaluppgifter')).toBeTruthy();
      expect(viewPage.showsNoValue('annatGrundForMU')).toBeTruthy();
      expect(viewPage.showsNoValue('annatGrundForMUBeskrivning')).toBeTruthy();
    });

    it('Verifiera sysselsättning är ej angivet', function() {
      expect(viewPage.showsNoValue('sysselsattning')).toBeTruthy();
      expect(viewPage.showsNoValue('nuvarandeArbete')).toBeTruthy();
    });

    it('Verifiera diagnos', function() {
      desktopSize();
      expect(viewPage.getDynamicLabelText('KAT_3.RBK')).toBe(texts['KAT_3.RBK']);

      expect(viewPage.getTextContent('onskarFormedlaDiagnos')).toBe('Ja');
      expect(viewPage.getTextContent('diagnoser-row0-col0')).toBe('M234');
      expect(viewPage.getTextContent('diagnoser-row0-col1')).toBe('Fri kropp i knäled');
      expect(viewPage.getTextContent('diagnoser-row1-col0')).toBe('K11');
      expect(viewPage.getTextContent('diagnoser-row1-col1')).toBe('Sjukdomar i spottkörtlarna');
    });

    it('Verifiera att konsekvenser är ej angivet', function() {
      expect(viewPage.showsNoValue('funktionsnedsattning')).toBeTruthy();
      expect(viewPage.showsNoValue('aktivitetsbegransning')).toBeTruthy();
    });

    it('Verifiera att medicinsk behandling är ej angivet', function() {
      expect(viewPage.showsNoValue('pagaendeBehandling')).toBeTruthy();
      expect(viewPage.showsNoValue('planeradBehandling')).toBeTruthy();
    });

    it('Verifiera bedömning', function() {
      expect(viewPage.getDynamicLabelText('KAT_6.RBK')).toBe(texts['KAT_6.RBK']);

      expect(viewPage.getTextContent('sjukskrivningar')).toContain('100 procent');
      expect(viewPage.getTextContent('sjukskrivningar')).toContain('2018-12-12');
      expect(viewPage.getTextContent('sjukskrivningar')).toContain('2018-12-21');

      expect(viewPage.showsNoValue('forsakringsmedicinsktBeslutsstod')).toBeTruthy();
      expect(viewPage.getTextContent('arbetstidsforlaggning')).toEqual('Ej angivet');
      expect(viewPage.showsNoValue('arbetstidsforlaggningMotivering')).toBeTruthy();
      expect(viewPage.getTextContent('arbetsresor')).toEqual('Ej angivet');
      expect(viewPage.showsNoValue('prognos')).toBeTruthy();
    });

    it('Verifiera åtgärder är ej angivet', function() {
      expect(viewPage.showsNoValue('arbetslivsinriktadeAtgarder')).toBeTruthy();
      expect(viewPage.showsNoValue('arbetslivsinriktadeAtgarderBeskrivning')).toBeTruthy();
    });

    it('Verifiera att övriga upplysningar är ej angivet', function() {
      expect(viewPage.showsNoValue('ovrigt')).toBeTruthy();
    });

  });

  describe('Visa fullständigt ifyllt intyg', function() {

    it('Visa intyg', function() {
      // Navigera tillbaka till listan och välj den andra intyget
      viewPage.backToList();
      expect(inboxPage.isAt()).toBeTruthy();
      inboxPage.viewCertificate(intygsId2);
      expect(viewPage.isAt()).toBeTruthy();
    });

    it('Verifiera smittbärarpenning', function() {
      expect(viewPage.getDynamicLabelText('KAT_10.RBK')).toBe(texts['KAT_10.RBK']);

      expect(viewPage.getTextContent('avstangningSmittskydd')).toEqual('Ej angivet');
    });

    it('Verifiera grund för medicinskt underlag', function() {
      expect(viewPage.getTextContent('undersokningAvPatienten')).toEqual('2018-12-11');
      expect(viewPage.showsNoValue('undersokningAvPatienten')).toBeFalsy();

      expect(viewPage.getTextContent('telefonkontaktMedPatienten')).toEqual('2018-12-07');
      expect(viewPage.showsNoValue('telefonkontaktMedPatienten')).toBeFalsy();

      expect(viewPage.getTextContent('journaluppgifter')).toEqual('2018-12-10');
      expect(viewPage.showsNoValue('journaluppgifter')).toBeFalsy();

      expect(viewPage.getTextContent('annatGrundForMU')).toEqual('2018-12-11');
      expect(viewPage.showsNoValue('annatGrundForMU')).toBeFalsy();

      expect(viewPage.getTextContent('annatGrundForMUBeskrivning')).toEqual('baserat på annat');
    });

    it('Verifiera sysselsättning', function() {
      expect(viewPage.getDynamicLabelText('KAT_2.RBK')).toBe(texts['KAT_2.RBK']);

      expect(viewPage.getTextContent('sysselsattning')).toBe(texts['KV_FKMU_0002.NUVARANDE_ARBETE.RBK']);
      expect(viewPage.getTextContent('nuvarandeArbete')).toEqual('nuvarande yrkesbeskrivning');
    });

    it('Verifiera diagnos', function() {
      desktopSize();
      expect(viewPage.getDynamicLabelText('KAT_3.RBK')).toBe(texts['KAT_3.RBK']);

      expect(viewPage.getTextContent('onskarFormedlaDiagnos')).toBe('Ja');
      expect(viewPage.getTextContent('diagnoser-row0-col0')).toBe('M234');
      expect(viewPage.getTextContent('diagnoser-row0-col1')).toBe('Fri kropp i knäled');
    });

    it('Verifiera sjukdomens konsekvenser', function() {
      expect(viewPage.getDynamicLabelText('KAT_4.RBK')).toBe(texts['KAT_4.RBK']);

      expect(viewPage.getTextContent('funktionsnedsattning')).toEqual('Stela knän');
      expect(viewPage.getTextContent('aktivitetsbegransning')).toEqual('Hen har svårt att sitta eftersom hen inte kan böja på knäna.');
    });

    it('Verifiera medicinsk behandling', function() {
      expect(viewPage.getDynamicLabelText('KAT_5.RBK')).toBe(texts['KAT_5.RBK']);

      expect(viewPage.getTextContent('pagaendeBehandling')).toEqual('Syfte och tidplan');
      expect(viewPage.getTextContent('planeradBehandling')).toEqual('Införskaffa ståbord');
    });

    it('Verifiera bedöming', function() {
      expect(viewPage.getDynamicLabelText('KAT_6.RBK')).toBe(texts['KAT_6.RBK']);

      expect(viewPage.getTextContent('sjukskrivningar')).toContain('75 procent');
      expect(viewPage.getTextContent('sjukskrivningar')).toContain('2018-12-11');
      expect(viewPage.getTextContent('sjukskrivningar')).toContain('2018-12-31');

      expect(viewPage.getTextContent('forsakringsmedicinsktBeslutsstod')).toEqual('För att FMB har fel');
      expect(viewPage.getTextContent('arbetstidsforlaggning')).toEqual('Nej');
      expect(viewPage.getTextContent('arbetstidsforlaggningMotivering')).toEqual('Ej angivet');
      expect(viewPage.getTextContent('arbetsresor')).toEqual('Ja');
      expect(viewPage.getTextContent('prognos-typ')).toBe(texts['KV_FKMU_0006.STOR_SANNOLIKHET.RBK']);
    });

    it('Verifiera åtgärder', function() {
      expect(viewPage.getDynamicLabelText('KAT_7.RBK')).toBe(texts['KAT_7.RBK']);

      expect(viewPage.getTextContent('arbetslivsinriktadeAtgarder-0')).toBe(texts['KV_FKMU_0004.ARBETSANPASSNING.RBK']);
      expect(viewPage.getTextContent('arbetslivsinriktadeAtgarder-1')).toBe(texts['KV_FKMU_0004.ERGONOMISK.RBK']);

      expect(viewPage.getTextContent('arbetslivsinriktadeAtgarderBeskrivning')).toEqual('Ej angivet');
    });

    it('Verifiera Övriga upplysningar', function() {
      expect(viewPage.getDynamicLabelText('KAT_8.RBK')).toBe(texts['KAT_8.RBK']);
      expect(viewPage.getTextContent('ovrigt')).toEqual('Övriga upplysningar kan var nyttiga');
    });

    it('Verifiera att Kontakt är angivet', function() {
      expect(viewPage.getDynamicLabelText('KAT_9.RBK')).toBe(texts['KAT_9.RBK']);

      expect(viewPage.getDynamicLabelText('FRG_103.RBK')).toBe(texts['FRG_103.RBK']);
      expect(viewPage.getTextContent('kontaktMedAg')).toEqual('Ja');
      expect(viewPage.getTextContent('anledningTillKontakt')).toEqual('Kontakt med arbetsgivaren');
    });

    it('Verifiera att skapad av är angivet', function() {
      expect(viewPage.getTextContent('fullstandigtNamn')).toEqual('Jan Nilsson');
      expect(viewPage.getTextContent('vardenhet-telefon')).toEqual('Tel: 0101112131416');
      expect(viewPage.getTextContent('vardenhet-namn')).toEqual('WebCert Enhet 1, NMT vg3');
      expect(viewPage.getTextContent('vardenhet-adress')).toEqual('NMT gata 3, 12345 Testhult');

    });

  });

});
