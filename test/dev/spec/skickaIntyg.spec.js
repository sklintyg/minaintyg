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

xdescribe('Skicka intyg', function() {

	var personId = '19010101-0101';
	var fk7263IntygsId = null;
	var fk7263IntygsId2 = null;

    beforeAll(function() {
        restHelper.setConsent(personId);

        var fk7263Intyg = genericTestDataBuilder.getFk7263(personId);
        fk7263IntygsId = fk7263Intyg.id;
        restHelper.createIntyg(fk7263Intyg);

        var fk7263Intyg2 = genericTestDataBuilder.getFk7263(personId);
        fk7263IntygsId2 = fk7263Intyg2.id;
        restHelper.createIntyg(fk7263Intyg2);
    });

    afterAll(function() {
        restHelper.deleteConsent(personId);
        restHelper.deleteIntyg(fk7263IntygsId);
        restHelper.deleteIntyg(fk7263IntygsId2);
    });

    describe('Skicka Fk7263', function() {

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
            expect(inboxPage.certificateExists(fk7263IntygsId)).toBeTruthy();
            inboxPage.viewCertificate(fk7263IntygsId);
            expect(viewPage.isAt()).toBeTruthy();
            expect(browser.getCurrentUrl()).toContain('fk7263');
            expect(viewPage.certificateId()).toEqual(fk7263IntygsId);
            expect(viewPage.hasNoEvent('Inga händelser')).toBeTruthy();
        });

        it('Välj att skicka intyget', function() {
            expect(viewPage.isAt()).toBeTruthy();
            viewPage.sendCertificate();
            expect(sendPage.isAt()).toBeTruthy();
        });

        it('Välj mottagare, bekräfta och skicka intyget', function() {
            expect(sendPage.isAt()).toBeTruthy();
            expect(sendPage.chooseRecipientViewIsShown()).toBeTruthy();
            sendPage.chooseRecipient('FK');
            expect(sendPage.alreadySentWarningMessageIsShown()).toBeFalsy();
            sendPage.confirmRecipientSelection();
            expect(sendPage.confirmAndSendViewIsShown()).toBeTruthy();
            expect(sendPage.alreadySentWarningMessageIsShown()).toBeFalsy();
            expect(sendPage.selectedRecipient()).toEqual('Försäkringskassan');
            sendPage.confirmAndSend();
            expect(sendPage.resultViewIsShown()).toBeTruthy();
            sendPage.backToViewCertificate();
        });

        it('Verifiera att intyget nu har status "skickat"', function() {
            expect(viewPage.isAt()).toBeTruthy();
            expect(browser.getCurrentUrl()).toContain('fk7263');
            expect(viewPage.certificateId()).toEqual(fk7263IntygsId);
            expect(viewPage.hasEvent('Skickat till')).toBeTruthy();
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
            expect(inboxPage.certificateExists(fk7263IntygsId2)).toBeTruthy();
            inboxPage.viewCertificate(fk7263IntygsId2);
            expect(viewPage.isAt()).toBeTruthy();
            expect(browser.getCurrentUrl()).toContain('fk7263');
            expect(viewPage.certificateId()).toEqual(fk7263IntygsId2);
            expect(browser.getTitle()).toEqual('Läkarintyg FK7263 | Mina intyg');
        });

        it('Välj att skicka intyget', function() {
            expect(viewPage.isAt()).toBeTruthy();
            viewPage.sendCertificate();
            expect(sendPage.isAt()).toBeTruthy();
        });

        it('Välj mottagare, bekräfta men avbryt att skicka intyget', function() {
            expect(sendPage.isAt()).toBeTruthy();
            expect(sendPage.chooseRecipientViewIsShown()).toBeTruthy();
            sendPage.chooseRecipient('FK');
            sendPage.confirmRecipientSelection();
            expect(sendPage.confirmAndSendViewIsShown()).toBeTruthy();
            expect(browser.getTitle()).toEqual('Kontrollera och skicka intyget | Mina intyg');
            expect(sendPage.activeTab()).toEqual('inboxTab');
            sendPage.clickArchived();
            expect(archivedPage.activeTab()).toEqual('archivedTab');
        });

    });
});
