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

describe('Visa intyg ts-diabetes v4', function() {

  var personId = '190101010101';
  var intygsId = null;

  beforeAll(function() {
    browser.get('/web/logga-ut');
    var tsDiabetesIntyg = genericTestDataBuilder.getTsDiabetes(personId, '4.0');
    intygsId = tsDiabetesIntyg.id;
    restHelper.createIntyg(tsDiabetesIntyg);
  });

  afterAll(function() {
    restHelper.deleteIntyg(intygsId);
  });

  describe('Verifiera ts-diabetes', function() {

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

    it('Verifiera intyg avser', function() {
      expect(viewPage.getTextContent('intygAvser-kategorier-0')).toEqual('AM');
    });

    it('Verifiera identitet styrkt genom', function() {
      expect(viewPage.getTextContent('identitetStyrktGenom-typ')).toEqual('ID-kort');
    });

    it('Verifiera "Allmänt"', function() {
      expect(viewPage.getTextContent('allmant-patientenFoljsAv')).toEqual('Primärvård');
      expect(viewPage.getTextContent('allmant-diabetesDiagnosAr')).toEqual('2000');
      expect(viewPage.getTextContent('allmant-typAvDiabetes')).toEqual('Typ 1');
      expect(viewPage.getTextContent('allmant-beskrivningAnnanTypAvDiabetes')).toEqual('Ej angivet');
      expect(viewPage.getTextContent('allmant-medicineringForDiabetes')).toEqual('Ja');
      expect(viewPage.getTextContent('allmant-medicineringMedforRiskForHypoglykemi')).toEqual('Ja');
      expect(viewPage.getTextContent(
          'allmant-behandling-insulin-allmant-behandling-tabletter-allmant-behandling-annan-0')).toEqual(
          'Insulin');
      expect(viewPage.getTextContent(
          'allmant-behandling-insulin-allmant-behandling-tabletter-allmant-behandling-annan-1')).toEqual(
          'Tabletter');
      expect(viewPage.getTextContent('allmant-behandling-annanAngeVilken')).toEqual('Ej angivet');

      expect(viewPage.getTextContent('allmant-medicineringMedforRiskForHypoglykemiTidpunkt')).toEqual('2021-10-30');
    });

    it('Verifiera "Hypoglykemi"', function() {
      expect(viewPage.getTextContent('hypoglykemi-kontrollSjukdomstillstand')).toEqual('Nej');
      expect(viewPage.getTextContent('hypoglykemi-kontrollSjukdomstillstandVarfor')).toEqual('Varför inte kontroll sjukdomstillstand');
      expect(viewPage.getTextContent('hypoglykemi-forstarRiskerMedHypoglykemi')).toEqual('Ja');
      expect(viewPage.getTextContent('hypoglykemi-formagaKannaVarningstecken')).toEqual('Ja');
      expect(viewPage.getTextContent('hypoglykemi-vidtaAdekvataAtgarder')).toEqual('Ja');
      expect(viewPage.getTextContent('hypoglykemi-aterkommandeSenasteAret')).toEqual('Ja');
      expect(viewPage.getTextContent('hypoglykemi-aterkommandeSenasteAretTidpunkt')).toEqual('2021-10-30');
      expect(viewPage.getTextContent('hypoglykemi-aterkommandeSenasteAretKontrolleras')).toEqual('Ja');
      expect(viewPage.getTextContent('hypoglykemi-aterkommandeSenasteAretTrafik')).toEqual('Ja');

      expect(viewPage.getTextContent('hypoglykemi-aterkommandeVaketSenasteTolv')).toEqual('Ja');
      expect(viewPage.getTextContent('hypoglykemi-aterkommandeVaketSenasteTre')).toEqual('Ja');
      expect(viewPage.getTextContent('hypoglykemi-aterkommandeVaketSenasteTreTidpunkt')).toEqual('2021-10-30');

      expect(viewPage.getTextContent('hypoglykemi-allvarligSenasteTolvManaderna')).toEqual('Ja');
      expect(viewPage.getTextContent('hypoglykemi-allvarligSenasteTolvManadernaTidpunkt')).toEqual('2021-10-30');
      expect(viewPage.getTextContent('hypoglykemi-regelbundnaBlodsockerkontroller')).toEqual('Ja');
    });

    it('Verifiera "Övrigt"', function() {
      expect(viewPage.getTextContent('ovrigt-komplikationerAvSjukdomen')).toEqual('Ja');
      expect(viewPage.getTextContent('ovrigt-komplikationerAvSjukdomenAnges')).toEqual('Komplikationer anges');
      expect(viewPage.getTextContent('ovrigt-borUndersokasAvSpecialist')).toEqual('Bör undersökas av specialistläkare');
    });

    it('Verifiera "Bedömning"', function() {
      desktopSize();

      expect(viewPage.getTextContent('bedomning-uppfyllerBehorighetskrav-0')).toEqual('AM');
      expect(viewPage.getTextContent('bedomning-ovrigaKommentarer')).toEqual('Övriga kommentarer');
    });

    it('Verifiera skapad av', function() {
      expect(viewPage.getTextContent('fullstandigtNamn')).toEqual('Jan Nilsson');
      expect(viewPage.getTextContent('vardenhet-telefon')).toEqual('Tel: 0812341234');
      expect(viewPage.getTextContent('vardenhet-namn')).toEqual('WebCert Enhet 1, WebCert Vårdgivare 1');
      expect(viewPage.getTextContent('vardenhet-adress')).toEqual('Enhetsg. 1, 100 10 Stadby');
    });
  });

});
