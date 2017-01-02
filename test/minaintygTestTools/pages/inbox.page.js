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
        this.at = element(by.id('inboxHeader'));
    },
    get: function () {
        this.getPage('start');
    },
    isAt: function isAt() {
        specHelper.waitForAngularTestability();
        return isAt._super.call(this);
    },
    viewArchivedList: function() {
        element(by.id('archivedTab')).click();
    },
    viewAbout: function() {
        element(by.id('aboutTab')).click();
    },
    certificateExists: function(intygId) {
        return element(by.id('certificate-' + intygId)).isPresent();
    },
    viewCertificate: function(intygId) {
        element(by.id('viewCertificateBtn-' + intygId)).click();
    },
    archiveCertificate: function(intygId) {
        element(by.id('archiveCertificateBtn-' + intygId)).click();
    },
    confirmArchiveCertificate: function() {
        element(by.id('archive-button')).click();
    },
    archiveDialogIsDisplayed: function() {
        return element(by.id('archive-confirmation-dialog')).isDisplayed();
    }
});

module.exports = new MinaintygStartPageBase();
