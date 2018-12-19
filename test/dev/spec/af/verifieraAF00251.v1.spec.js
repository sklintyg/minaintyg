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
var textHelper = miTestTools.helpers.text;

var welcomePage = miTestTools.pages.welcomePage;
var inboxPage = miTestTools.pages.inboxPage;
var viewPage = miTestTools.pages.viewPage;

var genericTestdataBuilder = miTestTools.testdata.generic;

function windowScrollTo(id) {
    //Scroll to center the element in the middle of the screen

    var centerScript = 'if (!Element.prototype.documentOffsetTop) {'
    centerScript += 'Element.prototype.documentOffsetTop = function () {';
    centerScript += ' return this.offsetTop + ( this.offsetParent ? this.offsetParent.documentOffsetTop() : 0 );';
    centerScript += ' };';
    centerScript += '}';
    centerScript += 'var top = document.getElementById("' + id + '").documentOffsetTop() - (window.innerHeight / 2 );'
    centerScript += ' window.scrollTo( 0, top );';

    return browser.executeScript(centerScript);
}

describe('Verifiera AF00251', function() {

    var intygsId = null;
    var texts = null;

    beforeAll(function() {
        browser.ignoreSynchronization = false;

        // Load and cache expected dynamictext-values for this intygstype.
        textHelper.readTextsFromFkTextFile('texterMU_AF00251_v1.0.xml').then(function(textResources) {
            texts = textResources;
        }, function(err) {
            fail('Error during text lookup ' + err);
        });

        // Skapa intyget
        var intyg = genericTestdataBuilder.getAF00251();
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


        it('Verifiera Medicinskt underlag', function() {
            windowScrollTo('undersokningsDatum');
            expect(viewPage.getDynamicLabelText('KAT_1.RBK')).toBe(texts['KAT_1.RBK']);

            expect(viewPage.getTextContent('undersokningsDatum')).toBe('2018-10-30');
            expect(viewPage.getTextContent('annatDatum')).toBe('2018-10-31');
            expect(viewPage.getTextContent('annatBeskrivning')).toEqual('Vi fikade tillsammans, han bjöd på kaffet!');
        });


        it('Verifiera Arbetsmarknadspolitist program', function() {
            desktopSize();
            windowScrollTo('arbetsmarknadspolitisktProgram-medicinskBedomning');
            expect(viewPage.getDynamicLabelText('KAT_2.RBK')).toBe(texts['KAT_2.RBK']);

            expect(viewPage.getTextContent('arbetsmarknadspolitisktProgram-medicinskBedomning')).toBe('Kan nästan jobba.');
            expect(viewPage.getTextContent('arbetsmarknadspolitisktProgram-omfattning')).toBe('Deltid');
            expect(viewPage.getTextContent('arbetsmarknadspolitisktProgram-omfattningDeltid')).toBe('22');
        });


        it('Verifiera funktionsnedsättning', function() {
            windowScrollTo('funktionsnedsattning');
            expect(viewPage.getDynamicLabelText('KAT_3.RBK')).toBe(texts['KAT_3.RBK']);

            expect(viewPage.getTextContent('funktionsnedsattning')).toBe('Ont i armen.');
            expect(viewPage.getTextContent('aktivitetsbegransning')).toBe('Använd bara höger arm.');
        });

        it('Verifiera Har förhinder', function() {
            windowScrollTo('harForhinder');

            expect(viewPage.getTextContent('harForhinder')).toBe('Ja');
        });

        it('Verifiera sjukfrånvaro', function() {
            windowScrollTo('sjukfranvaro-row0-col0');

            expect(viewPage.getTextContent('sjukfranvaro-row0-col0')).toBe('44');
            expect(viewPage.getTextContent('sjukfranvaro-row0-col1')).toBe('2018-09-01');
            expect(viewPage.getTextContent('sjukfranvaro-row0-col2')).toBe('2018-09-30');

            expect(viewPage.getTextContent('sjukfranvaro-row1-col0')).toBe('33');
            expect(viewPage.getTextContent('sjukfranvaro-row1-col1')).toBe('2018-10-01');
            expect(viewPage.getTextContent('sjukfranvaro-row1-col2')).toBe('2018-10-31');

            expect(viewPage.getTextContent('sjukfranvaro-row2-col0')).toBe('22');
            expect(viewPage.getTextContent('sjukfranvaro-row2-col1')).toBe('2018-11-01');
            expect(viewPage.getTextContent('sjukfranvaro-row2-col2')).toBe('2018-11-30');

            expect(viewPage.getTextContent('sjukfranvaro-row3-col0')).toBe('11');
            expect(viewPage.getTextContent('sjukfranvaro-row3-col1')).toBe('2018-12-01');
            expect(viewPage.getTextContent('sjukfranvaro-row3-col2')).toBe('2018-12-31');
        });

        it('Verifiera Begräsning av sjukfrånvaro', function() {
            windowScrollTo('begransningSjukfranvaro-kanBegransas');
            expect(viewPage.getDynamicLabelText('KAT_4.RBK')).toBe(texts['KAT_4.RBK']);

            expect(viewPage.getTextContent('begransningSjukfranvaro-kanBegransas')).toBe('Ja');
            expect(viewPage.getTextContent('begransningSjukfranvaro-beskrivning')).toBe('Många fikapauser.');
        });


        it('Verifiera Prognos återgång', function() {
            windowScrollTo('prognosAtergang-prognos');

            expect(viewPage.getTextContent('prognosAtergang-prognos')).toEqual('Patienten kan återgå med anpassning');
            expect(viewPage.getTextContent('prognosAtergang-anpassningar')).toEqual('Behöver assistent.');
        });

        it('Verifiera att skapad av är angivet', function() {
            windowScrollTo('fullstandigtNamn');

            expect(viewPage.getTextContent('fullstandigtNamn')).toEqual('Jan Nilsson');
            expect(viewPage.getTextContent('vardenhet-telefon')).toEqual('Tel: 0101112131416');
            expect(viewPage.getTextContent('vardenhet-namn')).toEqual('WebCert Enhet 1, NMT vg3');
            expect(viewPage.getTextContent('vardenhet-adress')).toEqual('NMT gata 3, 12345 Testhult');
        });

    });

});
