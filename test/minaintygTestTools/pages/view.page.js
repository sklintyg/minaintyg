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

/**
 * Created by erik
 */

'use strict';
var specHelper = require('./../helpers/specHelper.js')
var MinaintygBasePage = require('./minaintyg.base.page.js');

var MinaintygStartPageBase = MinaintygBasePage._extend({

    init: function init() {
        init._super.call(this);
        this.at = element(by.id('viewCertificateHeader'));
        this.customize = element(by.id('customizeCertificateBtn'));
    },
    isAt: function isAt() {
        specHelper.waitForAngularTestability();
        return isAt._super.call(this);
    },
    backToList: function() {
        element(by.className('backlink')).click();
    },
    certificateId: function() {
        return element(by.id('certId')).getAttribute('title');
    },
    sendCertificate: function() {
        element(by.id('sendCertificateBtn')).click();
    },
    archiveCertificate: function() {
        element(by.id('archiveCertificateBtn')).click();
    },
    confirmArchiveCertificate: function() {
        element(by.id('archive-button')).click();
    },
    archiveDialogIsDisplayed: function() {
        return element(by.id('archive-confirmation-dialog')).isDisplayed();
    },
    clickCustomizeCertificate: function() {
        this.customize.click();
    },
    hasEvent: function(status) {
        var found = false;
        return element(by.id('certificate-latestevents')).all(by.tagName('div')).each(function(item) {
            item.getText().then(function (text) {
                found = found || text.includes(status);
            })
        }).then(function() { return found; });
    },
    hasNoEvent: function(status) {
        var found = false;
        return element(by.id('certificate-noevents')).all(by.tagName('div')).each(function(item) {
            item.getText().then(function (text) {
                found = found || text.includes(status);
            });
        }).then(function() { return found; });
    },
    getTextContent: function(fieldId) {
        return element(by.id(fieldId)).isDisplayed().then(function (isVisible) {
            if (isVisible) {
                return element(by.id(fieldId)).getText();
            } else {
                return 'notshown';
            }
        });
    },
    fieldNotShown: function(fieldId) {
        return this.getTextContent(fieldId).then(function (value) { return value === 'notshown'; });
    }
});

module.exports = new MinaintygStartPageBase();
