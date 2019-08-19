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

/**
 * Created by bennysce on 17-12-15.
 */
/*globals browser*/
'use strict';

var Class = require('jclass');

/**
 * Elements always shown in Mina intyg are connected here. Header etc.
 */
var MinaIntygBasePage = Class._extend({

  init: function() {
    this.at = null;
    this.citizen = element(by.css('.logged-in'));
    this.header = element(by.css('.headerbox-user-profile'));
    this.navBar = element(by.css('.navbar-header'));
  },
  isAt: function() {
    return this.at.isDisplayed();
  },
  getPage: function(page) {
    browser.setLocation(page);
  },
  clickAbout: function() {
    this.navbarAbout().click();
  },
  clickInbox: function() {
    this.navbarInbox().click();
  },
  clickArchived: function() {
    this.navbarArchived().click();
  },
  navbarLocation: function() {
    return element(by.id('location'));
  },
  navbarAbout: function() {
    return element.all(by.id('aboutTab')).first();
  },
  navbarInbox: function() {
    return element.all(by.id('inboxTab')).first();
  },
  navbarArchived: function() {
    return element.all(by.id('archivedTab')).first();
  },
  activeTab: function() {
    return element.all(by.css('.navbar-nav li.active > a')).first().getAttribute('id');
  },

  //Locates the dynamic text based on text-key
  getDynamicLabelText: function(textKey) {
    return element(by.xpath('//span[@key="' + textKey + '"]')).getText();
  }
});

module.exports = MinaIntygBasePage;
