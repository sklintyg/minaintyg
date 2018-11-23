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

/* globals browser */

'use strict';
var specHelper = require('./../helpers/specHelper.js')
var MinaintygBasePage = require('./minaintyg.base.page.js');

var MinaintygStartPageBase = MinaintygBasePage._extend({

    init: function init() {
        init._super.call(this);
        this.at = element(by.id('send-certificate-page'));
        this.confirmRecipientSelectionBtn = element(by.id('send-to-recipients-btn'));
        this.sendingDialog = element(by.id('mi-sending-dialog'));
        this.backToIntygBtn = element(by.id('back-to-intyg-btn'));
    },
    
    isAt: function isAt() {
        return isAt._super.call(this);
    },

    selectRecipient: function(recipientId) {
        element(by.id('selectable-recipient-' + recipientId)).click();
    },

    recipientIsSelectable: function(recipientId) {
        return element(by.id('selectable-recipient-' + recipientId)).isDisplayed().then(function (isVisible) { return isVisible; });
    },

    deselectRecipient: function(recipientId) {
        element(by.id('removable-recipient-' + recipientId)).click();
    },

    recipientIsDeselectable: function(recipientId) {
        return element(by.id('removable-recipient-' + recipientId)).isDisplayed().then(function (isVisible) { return isVisible; });
    },

    recipientIsUnselectable: function(recipientId) {
        return element(by.id('unselectable-recipient-' + recipientId)).isDisplayed().then(function (isVisible) { return isVisible; });
    },

    verifySendResultForRecipient: function(recipientId, success) {
        return element(by.id((success ? 'success':'fail') + '-result-' + recipientId)).isDisplayed().then(function (isVisible) { return isVisible; });
    },

    confirmRecipientSelection: function() {
        this.confirmRecipientSelectionBtn.click();
    },

    sendDialogIsShown: function() {
        return this.sendingDialog.isDisplayed();
    },

    sendSekretessDialogIsPresent: function() {
        return element(by.id('mi-send-sekretess-dialog')).isPresent();
    },

    sendSekretessDialogAbortClick: function() {
        element(by.id('sekretessDialog-button--cancel')).click();
    },

    sendSekretessDialogConfirmClick: function() {
        element(by.id('sekretessDialog-button--confirm')).click();
    },

    backToViewCertificate: function() {
        this.backToIntygBtn.click();
    }
});

module.exports = new MinaintygStartPageBase();
