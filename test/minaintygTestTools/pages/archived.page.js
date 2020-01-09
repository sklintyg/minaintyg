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

/* globals browser */

'use strict';
var specHelper = require('./../helpers/specHelper.js')
var MinaintygBasePage = require('./minaintyg.base.page.js');

var MinaintygStartPageBase = MinaintygBasePage._extend({

  init: function init() {
    init._super.call(this);
    this.at = element(by.id('archivedHeader'));
  },
  get: function() {
    browser.get('web/start/#/arkiverade');
  },
  isAt: function isAt() {
    specHelper.waitForAngularTestability();
    return isAt._super.call(this);
  },
  certificateRestoreLink: function(id) {
    return element(by.id('restoreCertificate-' + id));
  },
  certificateRestoreMobileLink: function(id) {
    return element(by.id('restoreCertificate-' + id + '-list'));
  },
  certificateExists: function(id) {
    return this.certificateRestoreLink(id).isPresent();
  },
  restoreCertificate: function(intygId) {
    this.certificateRestoreLink(intygId).click();
  },
  certificateExistsMobile: function(id) {
    return this.certificateRestoreMobileLink(id).isDisplayed();
  }
});

module.exports = new MinaintygStartPageBase();
