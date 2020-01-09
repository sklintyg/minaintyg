/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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
/*globals describe, beforeAll, afterAll, it */
'use strict';

var specHelper = miTestTools.helpers.spec;
var restHelper = miTestTools.helpers.rest;

var welcomePage = miTestTools.pages.welcomePage;
var viewPage = miTestTools.pages.viewPage;
var inboxPage = miTestTools.pages.inboxPage;

var genericTestDataBuilder = miTestTools.testdata.generic;

describe('Visa intyg Fk7263', function() {

  var personId = '19010101-0101';
  var intygsId1 = null;
  var intygsId2 = null;

  beforeAll(function() {
    var fk7263Intyg = genericTestDataBuilder.getFk7263(personId);
    var fk7263Intyg2 = genericTestDataBuilder.getFk7263Smittskydd(personId);
    intygsId1 = fk7263Intyg.id;
    intygsId2 = fk7263Intyg2.id;
    restHelper.createIntyg(fk7263Intyg);
    restHelper.createIntyg(fk7263Intyg2);
  });

  afterAll(function() {
    restHelper.deleteIntyg(intygsId1);
    restHelper.deleteIntyg(intygsId2);
  });

  describe('Verifiera Fk7263 smittskydd', function() {

    beforeEach(function() {
      browser.ignoreSynchronization = false;
    });

    it('Logga in', function() {
      welcomePage.get();
      specHelper.waitForAngularTestability();
      welcomePage.login(personId, false);
      specHelper.waitForAngularTestability();
    });

    it('Visa ett intyg och verifiera att det är rätt intyg och av rätt typ', function() {
      expect(inboxPage.isAt()).toBeTruthy();
      expect(inboxPage.certificateExists(intygsId2)).toBeTruthy();
      inboxPage.viewCertificate(intygsId2);
      expect(viewPage.isAt()).toBeTruthy();
      expect(browser.getCurrentUrl()).toContain('fk7263');
      expect(viewPage.isAtCert(intygsId2)).toBeTruthy();
    });

    it('Verifiera att anpassa utskrift är disabled', function() {
      expect(viewPage.customizeCertificateIsDisplayed()).toBe(false);
    });

    it('Verifiera fält 1: Smittskydd', function() {
      expect(viewPage.getTextContent('avstangningSmittskydd')).toEqual('Ja');
    });

    it('Verifiera fält 2: Diagnoskod och -beskrivning', function() {
      expect(viewPage.getTextContent('diagnosKod')).toEqual('Ej angivet');
      expect(viewPage.getTextContent('diagnosBeskrivning')).toEqual('Ej angivet');
    });

    it('Verifiera fält 3: Aktuell sjukdomsförlopp', function() {
      expect(viewPage.getTextContent('sjukdomsforlopp')).toEqual('Ej angivet');
    });

    it('Verifiera fält 4: Funktionsnedsättning', function() {
      expect(viewPage.getTextContent('funktionsnedsattning')).toEqual('Ej angivet');
    });

    it('Verifiera fält 4b: Intyg baseras på', function() {
      expect(viewPage.getTextContent('undersokningAvPatienten')).toEqual('Ej angivet');
      expect(viewPage.getTextContent('telefonkontaktMedPatienten')).toEqual('Ej angivet');
      expect(viewPage.getTextContent('journaluppgifter')).toEqual('Ej angivet');
      expect(viewPage.getTextContent('annanReferens')).toEqual('Ej angivet');
    });

    it('Verifiera fält 5: Aktivitetsbegränsning', function() {
      expect(viewPage.getTextContent('aktivitetsbegransning')).toEqual('Ej angivet');
    });

    it('Verifiera fält 6a: Rekommendationer', function() {
      expect(viewPage.getTextContent('rekommendationKontaktArbetsformedlingen')).toEqual('Ej angivet');
      expect(viewPage.getTextContent('rekommendationKontaktForetagshalsovarden')).toEqual('Ej angivet');
      expect(viewPage.getTextContent('rekommendationOvrigt')).toEqual('Ej angivet');
    });

    it('Verifiera fält 6b: Planerad eller pågående behandling/åtgärd', function() {
      expect(viewPage.getTextContent('atgardInomSjukvarden')).toEqual('Ej angivet');
      expect(viewPage.getTextContent('annanAtgard')).toEqual('Ej angivet');
    });

    it('Verifiera fält 7: Arbetsinriktad rehabilitering', function() {
      expect(viewPage.getTextContent('rehabilitering')).toEqual('Ej angivet');
    });

    it('Verifiera fält 8a: Arbetsförmåga bedömning', function() {
      expect(viewPage.getTextContent('patientworkcapacity-no-value')).toEqual('Ej angivet');
    });

    it('Verifiera fält 8b: Arbetsförmåga nedsatthet', function() {
      expect(viewPage.getTextContent('nedsattMed50-row-col1')).toEqual('2017-06-14');
      expect(viewPage.getTextContent('nedsattMed50-row-col2')).toEqual('2017-06-20');
    });

    it('Verifiera fält 9: Arbetsförmåga nedsatthet längre tid', function() {
      expect(viewPage.getTextContent('arbetsformagaPrognos')).toEqual('Ej angivet');
    });

    it('Verifiera fält 10: Prognos', function() {
      expect(viewPage.getTextContent('prognosBedomning')).toEqual('Ja');
    });

    it('Verifiera fält 11: Resor till och från arbetet', function() {
      expect(viewPage.getTextContent('resaTillArbetet')).toEqual('Nej');
    });

    it('Verifiera fält 12: Kontakt önskas med FK', function() {
      expect(viewPage.getTextContent('kontaktMedFk')).toEqual('Ej angivet');
    });

    it('Verifiera fält 13: Övriga upplysningar', function() {
      expect(viewPage.getTextContent('kommentar')).toEqual('Ej angivet');
    });

    it('Verifiera fält 17: Förskrivarkod och arbetsplatskod', function() {
      expect(viewPage.getTextContent('forskrivarkodOchArbetsplatskod')).toEqual('1234567 - 123456789011');
    });

  });

  describe('Verifiera Fk7263 (ej smittskydd)', function() {

    beforeEach(function() {
      browser.ignoreSynchronization = false;
    });

    it('Logga in', function() {
      welcomePage.get();
      specHelper.waitForAngularTestability();
      welcomePage.login(personId, false);
      specHelper.waitForAngularTestability();
    });

    it('Visa ett intyg och verifiera att det är rätt intyg och av rätt typ', function() {
      expect(inboxPage.isAt()).toBeTruthy();
      expect(inboxPage.certificateExists(intygsId1)).toBeTruthy();
      inboxPage.viewCertificate(intygsId1);
      expect(viewPage.isAt()).toBeTruthy();
      expect(browser.getCurrentUrl()).toContain('fk7263');
      expect(viewPage.isAtCert(intygsId1)).toBeTruthy();
    });

    it('Verifiera att anpassa utskrift är disabled', function() {
      expect(viewPage.customizeCertificateIsDisplayed()).toBe(false);
    });

    it('Verifiera fält 1: Smittskydd', function() {
      expect(viewPage.getTextContent('avstangningSmittskydd')).toEqual('Ej angivet');
    });

    it('Verifiera fält 2: Diagnoskod och -beskrivning', function() {
      expect(viewPage.getTextContent('diagnosKod')).toEqual('S47');
      expect(viewPage.getTextContent('diagnosBeskrivning')).toEqual(
          'Klämskada på skuldra och överarm. Samsjuklighet föreligger. Z233 Vaccination avseende pest. Z600 Problem med anpassning till övergångar i livscykeln. Här förtydligar vi våra diagnoser ytterligare och skriver litegrann för att göra saker tydligare för andra som ska läsa på tex Försäkringskassan.');
    });

    it('Verifiera fält 3: Aktuell sjukdomsförlopp', function() {
      expect(viewPage.getTextContent('sjukdomsforlopp')).toEqual(
          'Patienten klämde höger överarm vid olycka i hemmet. Problemen har pågått en längre tid.');
    });

    it('Verifiera fält 4: Funktionsnedsättning', function() {
      expect(viewPage.getTextContent('funktionsnedsattning')).toEqual(
          'Kraftigt nedsatt rörlighet i överarmen pga skadan. Böj- och sträckförmågan är mycket dålig. Smärtar vid rörelse vilket ger att patienten inte kan använda armen särkilt mycket.');
    });

    it('Verifiera fält 4b: Intyg baseras på', function() {
      expect(viewPage.getTextContent('undersokningAvPatienten')).toEqual('2013-04-01');
      expect(viewPage.getTextContent('telefonkontaktMedPatienten')).toEqual('2013-04-01');
      expect(viewPage.getTextContent('journaluppgifter')).toEqual('2013-04-01');
      expect(viewPage.getTextContent('annanReferens')).toEqual('Ej angivet');
    });

    it('Verifiera fält 5: Aktivitetsbegränsning', function() {
      expect(viewPage.getTextContent('aktivitetsbegransning')).toEqual(
          'Patienten bör/kan inte använda armen förrän skadan läkt. Skadan förvärras vid för tidigt påtvingad belastning. Patienten kan inte lyfta armen utan den ska hållas riktad nedåt och i fast läge så mycket som möjligt under tiden för läkning.');
    });

    it('Verifiera fält 6a: Rekommendationer', function() {
      expect(viewPage.getTextContent('rekommendationKontaktArbetsformedlingen')).toEqual('Ja');
      expect(viewPage.getTextContent('rekommendationKontaktForetagshalsovarden')).toEqual('Ja');
      expect(viewPage.getTextContent('rekommendationOvrigt')).toEqual(
          'När skadan förbättrats rekommenderas muskeluppbyggande sjukgymnastik');
    });

    it('Verifiera fält 6b: Planerad eller pågående behandling/åtgärd', function() {
      expect(viewPage.getTextContent('atgardInomSjukvarden')).toEqual('Utreds om operation är nödvändig');
      expect(viewPage.getTextContent('annanAtgard')).toEqual('Patienten ansvarar för att armen hålls i stillhet');
    });

    it('Verifiera fält 7: Arbetsinriktad rehabilitering', function() {
      expect(viewPage.getTextContent('rehabilitering')).toEqual('Går inte att bedöma');
    });

    it('Verifiera fält 8a: Arbetsförmåga bedömning', function() {
      expect(viewPage.getTextContent('nuvarandeArbetsuppgifter-text')).toEqual('Dirigent. Dirigerar en större orkester på deltid');
      expect(viewPage.getTextContent('arbetsloshet')).toEqual(
          'Arbetslöshet - att utföra sådant arbete som är normalt förekommande på arbetsmarknaden');
      expect(viewPage.getTextContent('foraldrarledighet')).toEqual('Föräldraledighet med föräldrapenning - att vårda sitt barn');
    });

    it('Verifiera fält 8b: Arbetsförmåga nedsatthet', function() {
      expect(viewPage.getTextContent('nedsattMed25-row-col1')).toEqual('2011-04-01');
      expect(viewPage.getTextContent('nedsattMed25-row-col2')).toEqual('2011-05-31');

      expect(viewPage.getTextContent('nedsattMed50-row-col1')).toEqual('2011-03-07');
      expect(viewPage.getTextContent('nedsattMed50-row-col2')).toEqual('2011-03-31');

      expect(viewPage.getTextContent('nedsattMed75-row-col1')).toEqual('2011-02-14');
      expect(viewPage.getTextContent('nedsattMed75-row-col2')).toEqual('2011-03-06');

      expect(viewPage.getTextContent('nedsattMed100-row-col1')).toEqual('2011-01-26');
      expect(viewPage.getTextContent('nedsattMed100-row-col2')).toEqual('2011-02-13');
    });

    it('Verifiera fält 9: Arbetsförmåga nedsatthet längre tid', function() {
      expect(viewPage.getTextContent('arbetsformagaPrognos')).toEqual(
          'Skadan har förvärrats vid varje tillfälle patienten använt armen. Måste hållas i total stillhet tills läkningsprocessen kommit en bit på väg. Eventuellt kan utredning visa att operation är nödvändig för att läka skadan.');
    });

    it('Verifiera fält 10: Prognos', function() {
      expect(viewPage.getTextContent('prognosBedomning')).toEqual('Går inte att bedöma');
    });

    it('Verifiera fält 11: Resor till och från arbetet', function() {
      expect(viewPage.getTextContent('resaTillArbetet')).toEqual('Nej');
    });

    it('Verifiera fält 12: Kontakt önskas med FK', function() {
      expect(viewPage.getTextContent('kontaktMedFk')).toEqual('Ja');
    });

    it('Verifiera fält 13: Övriga upplysningar', function() {
      expect(viewPage.getTextContent('kommentar')).toEqual(
          'Prognosen att återgå till arbete är svår att bedömma förrän utredningen är genomförd.');
    });

    it('Verifiera fält 17: Förskrivarkod och arbetsplatskod', function() {
      expect(viewPage.getTextContent('forskrivarkodOchArbetsplatskod')).toEqual('1234567 - 123456789011');
    });

    it('Verifiera Skapad av', function() {
      expect(viewPage.getTextContent('fullstandigtNamn')).toEqual('Jan Nilsson');
      expect(viewPage.getTextContent('vardenhet-namn')).toEqual('WebCert Enhet 1, WebCert Vårdgivare 1');
      expect(viewPage.getTextContent('vardenhet-adress')).toEqual('Lasarettsvägen 13, 85150 Sundsvall');
      expect(viewPage.getTextContent('vardenhet-telefon')).toEqual('Tel: 060-1818000');
    });

  });

});
