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
var viewPage = miTestTools.pages.viewPage;
var inboxPage = miTestTools.pages.inboxPage;

var genericTestDataBuilder = miTestTools.testdata.generic;

describe('Visa intyg tstrk1062', function() {

  var EJ_ANGIVET = "Ej angivet";
  var personId = '190101010101';
  var intygsId = null;

  beforeAll(function() {
    var tstrk1062Intyg = genericTestDataBuilder.getTstrk1062(personId, '1.0');
    intygsId = tstrk1062Intyg.id;
    restHelper.createIntyg(tstrk1062Intyg);
  });

  afterAll(function() {
    restHelper.deleteIntyg(intygsId);
  });

  describe('Verifiera tstrk1062', function() {

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
      expect(inboxPage.certificateExists(intygsId)).toBeTruthy();
      inboxPage.viewCertificate(intygsId);
      expect(viewPage.isAt()).toBeTruthy();
    });

    it('Verifiera intyget avser', function() {
      expect(viewPage.getTextContent('intygAvser-behorigheter-0')).toEqual('AM');
      expect(viewPage.getTextContent('intygAvser-behorigheter-1')).toEqual('A1');
      expect(viewPage.getTextContent('intygAvser-behorigheter-2')).toEqual('A2');
    });

    it('Verifiera identitet', function() {
      expect(viewPage.getTextContent('idKontroll-typ')).toEqual('ID-kort');
    });

    it('Verifiera allmänt', function() {
      expect(viewPage.getTextContent('diagnosKodad-row0-col0')).toEqual('A01');
      expect(viewPage.getTextContent('diagnosKodad-row0-col1')).toEqual('Tyfoidfeber och paratyfoidfeber');
      expect(viewPage.getTextContent('diagnosKodad-row0-col2')).toEqual('2018');

      expect(viewPage.getTextContent('diagnosKodad-row1-col0')).toEqual('B02');
      expect(viewPage.getTextContent('diagnosKodad-row1-col1')).toEqual('Bältros');
      expect(viewPage.getTextContent('diagnosKodad-row1-col2')).toEqual('2017');

      expect(viewPage.getTextContent('diagnosKodad-row2-col0')).toEqual('C03');
      expect(viewPage.getTextContent('diagnosKodad-row2-col1')).toEqual('Malign tumör i tandköttet');
      expect(viewPage.getTextContent('diagnosKodad-row2-col2')).toEqual('2018');

      expect(viewPage.getTextContent('diagnosKodad-row3-col0')).toEqual('D04');
      expect(viewPage.getTextContent('diagnosKodad-row3-col1')).toEqual('Cancer in situ i huden');
      expect(viewPage.getTextContent('diagnosKodad-row3-col2')).toEqual('2011');

      expect(viewPage.getTextContent('diagnosFritext-diagnosFritext')).toEqual(EJ_ANGIVET);
      expect(viewPage.getTextContent('diagnosFritext-diagnosArtal')).toEqual(EJ_ANGIVET);
    });

    it('Verifiera läkemedelsbehandling', function() {
      expect(viewPage.getTextContent('lakemedelsbehandling-harHaft')).toEqual('Ja');
      expect(viewPage.getTextContent('lakemedelsbehandling-pagar')).toEqual('Nej');
      expect(viewPage.getTextContent('lakemedelsbehandling-aktuell')).toEqual(EJ_ANGIVET);
      expect(viewPage.getTextContent('lakemedelsbehandling-pagatt')).toEqual(EJ_ANGIVET);
      expect(viewPage.getTextContent('lakemedelsbehandling-effekt')).toEqual(EJ_ANGIVET);
      expect(viewPage.getTextContent('lakemedelsbehandling-foljsamhet')).toEqual(EJ_ANGIVET);
      expect(viewPage.getTextContent('lakemedelsbehandling-avslutadTidpunkt')).toEqual('2019-01-10');
      expect(viewPage.getTextContent('lakemedelsbehandling-avslutadOrsak')).toEqual('Behandlingen var fruktlös');
    });

    it('Verifiera symptom', function() {
      expect(viewPage.getTextContent('bedomningAvSymptom')).toEqual('Bedömning av aktuella symptom');
      expect(viewPage.getTextContent('prognosTillstand-typ')).toEqual('Kan ej bedöma');
    });

    it('Verifiera övrigt', function() {
      expect(viewPage.getTextContent('ovrigaKommentarer')).toEqual('Inga övriga kommentarer');
    });

    it('Verifiera bedömning', function() {
      expect(viewPage.getTextContent('bedomning-uppfyllerBehorighetskrav-0')).toEqual('AM');
      expect(viewPage.getTextContent('bedomning-uppfyllerBehorighetskrav-1')).toEqual('A1');
      expect(viewPage.getTextContent('bedomning-uppfyllerBehorighetskrav-2')).toEqual('A2');
    });

    it('Verifiera skapad av', function() {
      expect(viewPage.getTextContent('fullstandigtNamn')).toEqual('Jan Nilsson');
      expect(viewPage.getTextContent('vardenhet-telefon')).toEqual('Tel: 0101112131416');
      expect(viewPage.getTextContent('vardenhet-namn')).toEqual('WebCert Enhet 1, WebCert Vårdgivare 1');
      expect(viewPage.getTextContent('vardenhet-adress')).toEqual('NMT gata 3, 12345 Testhult');
    });

  });

});
