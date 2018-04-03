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

var archivedPage = miTestTools.pages.archivedPage;
var inboxPage = miTestTools.pages.inboxPage;
var sendPage = miTestTools.pages.sendPage;
var viewPage = miTestTools.pages.viewPage;
var welcomePage = miTestTools.pages.welcomePage;

var genericTestDataBuilder = miTestTools.testdata.generic;

describe('Skicka intyg', function() {

	var personId = '191212121212';
	var intygsId1 = null;
	var intygsId2 = null;

    beforeAll(function() {
        var intyg1 = genericTestDataBuilder.getLisjpSmittskydd();
        var intyg2 = genericTestDataBuilder.getLisjpSmittskydd();
        intygsId1 = intyg1.id;
        intygsId2 = intyg2.id;
        restHelper.createIntyg(intyg1);
        restHelper.createIntyg(intyg2);
    });

    afterAll(function() {
        restHelper.deleteIntyg(intygsId1);
        restHelper.deleteIntyg(intygsId2);
    });

    describe('Skicka lisjpintyg', function() {

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

        it('Visa ett intyg och verifiera att det är rätt intyg och av rätt typ', function() {
            expect(inboxPage.isAt()).toBeTruthy();
            expect(inboxPage.certificateExists(intygsId1)).toBeTruthy();
            inboxPage.viewCertificate(intygsId1);
            expect(viewPage.isAt()).toBeTruthy();
            expect(browser.getCurrentUrl()).toContain('lisjp');
            expect(browser.getCurrentUrl()).toContain(intygsId1);
            expect(viewPage.hasNoEvent(intygsId1, 'Inga händelser')).toBeTruthy();
        });

        it('Välj att skicka intyget', function() {
            expect(viewPage.isAt()).toBeTruthy();
            viewPage.sendCertificate();
            expect(sendPage.isAt()).toBeTruthy();
        });

        it('Välj mottagare, bekräfta och skicka intyget', function() {
            expect(sendPage.isAt()).toBeTruthy();

            //Default recipient (FK) skall vara förvald..
            expect(sendPage.recipientIsDeselectable('FKASSA')).toBeTruthy();

            //Den fejkade mottagaren FBA skall däremot vara valbar..
            //Toggla lite fram och tillbaka..
            expect(sendPage.recipientIsSelectable('FBA')).toBeTruthy();
            sendPage.selectRecipient('FBA');
            expect(sendPage.recipientIsDeselectable('FBA')).toBeTruthy();
            sendPage.deselectRecipient('FBA');
            expect(sendPage.recipientIsSelectable('FBA')).toBeTruthy();
            sendPage.selectRecipient('FBA');
            expect(sendPage.recipientIsDeselectable('FBA')).toBeTruthy();

            //Nu skall båda vara valda, skicka dom!
            sendPage.confirmRecipientSelection();

            expect(sendPage.sendDialogIsShown()).toBeTruthy();

            //FK skall gå bra, FBA skall misslyckas
            expect(sendPage.verifySendResultForRecipient('FKASSA', true)).toBeTruthy();
            expect(sendPage.verifySendResultForRecipient('FBA', false)).toBeTruthy();

            //Klart, gå tillbaka till intyget..
            sendPage.backToViewCertificate();
        });

        it('Verifiera att intyget nu har status "skickat"', function() {
            expect(viewPage.isAt()).toBeTruthy();
            expect(browser.getCurrentUrl()).toContain('lisjp');
            expect(browser.getCurrentUrl()).toContain(intygsId1);
            expect(viewPage.hasEvent(intygsId1, 'Skickat till Försäkringskassan')).toBeTruthy();
        });

        it('Välj att skicka redan skickat intyg', function() {
            viewPage.sendCertificate();
            expect(sendPage.isAt()).toBeTruthy();
        });

        it('mottagare som redan tagit emot intyget skall inte vara valbar', function() {
            //Already sent it to FK, should not be selectable..
            expect(sendPage.recipientIsUnselectable('FKASSA')).toBeTruthy();

            //but FBA that failed should still be selectable
            expect(sendPage.recipientIsSelectable('FBA')).toBeTruthy();

        });

    });

    describe('Skicka och avbryt', function() {

        // INTYG-1431 regressionstest
        // Verifierar testfall där "Inkorgen" i menyn ska vara markerad i skicka flödet men inte om man avbryter genom att gå till ett annat menyalternativ.
        // Verifierar att sidtitel är rätt satt.

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

        it('Visa ett intyg och verifiera att det är rätt intyg och av rätt typ', function() {
            expect(inboxPage.isAt()).toBeTruthy();
            expect(inboxPage.certificateExists(intygsId2)).toBeTruthy();
            inboxPage.viewCertificate(intygsId2);
            expect(viewPage.isAt()).toBeTruthy();
            expect(browser.getCurrentUrl()).toContain('lisjp');
            expect(browser.getCurrentUrl()).toContain(intygsId2);
            expect(browser.getTitle()).toEqual('Läkarintyg för sjukpenning | Mina intyg');
        });

        it('Gå till skicka intyget sidan', function() {
            expect(viewPage.isAt()).toBeTruthy();
            viewPage.sendCertificate();
            expect(sendPage.isAt()).toBeTruthy();
        });

        it('navigera till inboxen och arkiverade intyg', function() {
            expect(sendPage.isAt()).toBeTruthy();
            expect(browser.getTitle()).toEqual('Skicka intyg till mottagare | Mina intyg');
            expect(sendPage.activeTab()).toEqual('inboxTab');
            sendPage.clickArchived();
            expect(archivedPage.activeTab()).toEqual('archivedTab');
        });

    });
});
