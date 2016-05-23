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
/*globals pages */
/*globals describe,it,helpers */

/**
 * Created by stephenwhite on 09/06/15.
 */
'use strict';

module.exports = {

    get: function() {
        browser.get('welcome.jsp');
    },

    isAt: function isAt() {
        return element(by.id('customguidform')).isDisplayed();
    },

    login: function(userId) {
        element(by.id('guid')).sendKeys(userId || '19121212-1212');
        element(by.id('loginBtn')).click();
    }

};