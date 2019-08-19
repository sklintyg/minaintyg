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
var viewPage = miTestTools.pages.viewPage;
var inboxPage = miTestTools.pages.inboxPage;

var genericTestDataBuilder = miTestTools.testdata.generic;

describe('Visa intyg ts-diabetes', function() {

  var personId = '190101010101';
  var intygsId = null;

  beforeAll(function() {
    var tsDiabetesIntyg = genericTestDataBuilder.getTsDiabetes(personId, '3.0');
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
      expect(viewPage.getTextContent('intygAvser-kategorier-0')).toEqual('A1');
    });

    it('Verifiera identitet styrkt genom', function() {
      expect(viewPage.getTextContent('identitetStyrktGenom-typ')).toEqual('ID-kort');
    });

    it('Verifiera "Allmänt"', function() {
      expect(viewPage.getTextContent('allmant-diabetesDiagnosAr')).toEqual('2018');
      expect(viewPage.getTextContent('allmant-typAvDiabetes')).toEqual('Typ 1');
      expect(viewPage.getTextContent(
          'allmant-behandling-endastKost-allmant-behandling-tabletter-allmant-behandling-insulin-allmant-behandling-annanBehandling-0')).toEqual(
          'Endast kost');
      expect(viewPage.getTextContent('allmant-behandling-tablettRiskHypoglykemi')).toEqual('Ja');
      expect(viewPage.getTextContent('allmant-behandling-insulinSedanAr')).toEqual('2017');
      expect(viewPage.getTextContent('allmant-behandling-annanBehandlingBeskrivning')).toEqual('Hypnos');
    });

    it('Verifiera "Hypoglykemier"', function() {
      expect(viewPage.getTextContent('hypoglykemier-sjukdomenUnderKontroll')).toEqual('Ja');
      expect(viewPage.getTextContent('hypoglykemier-nedsattHjarnfunktion')).toEqual('Ja');
      expect(viewPage.getTextContent('hypoglykemier-forstarRisker')).toEqual('Nej');
      expect(viewPage.getTextContent('hypoglykemier-fortrogenMedSymptom')).toEqual('Ja');
      expect(viewPage.getTextContent('hypoglykemier-saknarFormagaVarningstecken')).toEqual('Nej');
      expect(viewPage.getTextContent('hypoglykemier-kunskapLampligaAtgarder')).toEqual('Ja');
      expect(viewPage.getTextContent('hypoglykemier-egenkontrollBlodsocker')).toEqual('Nej');
      expect(viewPage.getTextContent('hypoglykemier-aterkommandeSenasteAret')).toEqual('Ja');
      expect(viewPage.getTextContent('hypoglykemier-aterkommandeSenasteTidpunkt')).toEqual('2018-10-17');
      expect(viewPage.getTextContent('hypoglykemier-aterkommandeSenasteKvartalet')).toEqual('Ja');
      expect(viewPage.getTextContent('hypoglykemier-senasteTidpunktVaken')).toEqual('2018-10-09');
      expect(viewPage.getTextContent('hypoglykemier-forekomstTrafik')).toEqual('Ja');
      expect(viewPage.getTextContent('hypoglykemier-forekomstTrafikTidpunkt')).toEqual('2018-10-11');
    });

    it('Verifiera "Synfunktion"', function() {
      expect(viewPage.getTextContent('synfunktion-misstankeOgonsjukdom')).toEqual('Nej');
      expect(viewPage.getTextContent('synfunktion-ogonbottenFotoSaknas')).toEqual('Ja');
      expect(viewPage.getTextContent('synfunktion-row0-col1')).toEqual('1,0');
      expect(viewPage.getTextContent('synfunktion-row0-col2')).toEqual('0,0');
      expect(viewPage.getTextContent('synfunktion-row1-col1')).toEqual('1,0');
      expect(viewPage.getTextContent('synfunktion-row1-col2')).toEqual('0,0');
      expect(viewPage.getTextContent('synfunktion-row2-col1')).toEqual('1,0');
      expect(viewPage.getTextContent('synfunktion-row2-col2')).toEqual('0,0');

      expect(viewPage.getTextContent('mobile-synfunktion-row0-col1')).toEqual('notshown');
      expect(viewPage.getTextContent('mobile-synfunktion-row0-col2')).toEqual('notshown');
      expect(viewPage.getTextContent('mobile-synfunktion-row1-col1')).toEqual('notshown');
      expect(viewPage.getTextContent('mobile-synfunktion-row1-col2')).toEqual('notshown');
      expect(viewPage.getTextContent('mobile-synfunktion-row2-col1')).toEqual('notshown');
      expect(viewPage.getTextContent('mobile-synfunktion-row2-col2')).toEqual('notshown');
    });

    it('Verifiera "Synfunktion" mobil-tabell', function() {
      mobileSize();

      expect(viewPage.getTextContent('mobile-synfunktion-row0-col1')).toEqual('1,0');
      expect(viewPage.getTextContent('mobile-synfunktion-row0-col2')).toEqual('0,0');
      expect(viewPage.getTextContent('mobile-synfunktion-row1-col1')).toEqual('1,0');
      expect(viewPage.getTextContent('mobile-synfunktion-row1-col2')).toEqual('0,0');
      expect(viewPage.getTextContent('mobile-synfunktion-row2-col1')).toEqual('1,0');
      expect(viewPage.getTextContent('mobile-synfunktion-row2-col2')).toEqual('0,0');

      expect(viewPage.getTextContent('synfunktion-row0-col1')).toEqual('notshown');
      expect(viewPage.getTextContent('synfunktion-row0-col2')).toEqual('notshown');
      expect(viewPage.getTextContent('synfunktion-row1-col1')).toEqual('notshown');
      expect(viewPage.getTextContent('synfunktion-row1-col2')).toEqual('notshown');
      expect(viewPage.getTextContent('synfunktion-row2-col1')).toEqual('notshown');
      expect(viewPage.getTextContent('synfunktion-row2-col2')).toEqual('notshown');
    });

    it('Verifiera "Övrigt"', function() {
      expect(viewPage.getTextContent('ovrigt')).toEqual('Kommentarer av det viktiga slaget');
    });

    it('Verifiera "Bedömning"', function() {
      desktopSize();

      expect(viewPage.getTextContent('bedomning-lampligtInnehav')).toEqual('Ja');
      expect(viewPage.getTextContent('bedomning-uppfyllerBehorighetskrav-0')).toEqual('C1E');
      expect(viewPage.getTextContent('bedomning-uppfyllerBehorighetskrav-1')).toEqual('DE');
      expect(viewPage.getTextContent('bedomning-borUndersokasBeskrivning')).toEqual('Kronologisk bastuberedning');
    });

    it('Verifiera skapad av', function() {
      expect(viewPage.getTextContent('fullstandigtNamn')).toEqual('Jan Nilsson');
      expect(viewPage.getTextContent('vardenhet-telefon')).toEqual('Tel: 08-1337');
      expect(viewPage.getTextContent('vardenhet-namn')).toEqual('WebCert Enhet 1, WebCert Vårdgivare 1');
      expect(viewPage.getTextContent('vardenhet-adress')).toEqual('Enhetsvägen 12, 54321 Tumba');
    });
  });

});
