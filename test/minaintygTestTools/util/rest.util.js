/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
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
 * Created by BESA on 2015-11-17.
 */
/*globals JSON*/
'use strict';
var restClient = require('./restclient.util.js');
var env = require('./../environment.js').envConfig;

module.exports = {
  login: function(personId) {
    var user = personId || '191212121212';

    var options = {
      url: 'web/sso?guid=' + user,
      method: 'GET',
    };
    return restClient.run(options, 'urlenc');
  },
  get: function(url, loggedIn) {
    if (loggedIn) {
      this.login();
    }
    var options = {
      url: url,
      method: 'GET'
    };
    return restClient.run(options, 'json');
  },
  // get any kind of resource from the app environment
  getResource: function(location) {
    var options = {
      url: 'testability/resources/resource?location=' + location,
      method: 'GET',
      accept: 'application/octet-stream'
    };
    return restClient.run(options);
  },

  // Intygstjänst - intyg

  createIntyg: function(createJson) {
    var options = {
      url: 'certificate/',
      method: 'POST',
      body: createJson
    };
    return restClient.run(options, 'json', env.INTYGTJANST_URL + '/resources/');
  },
  deleteIntyg: function(intygId) {
    var options = {
      url: 'certificate/' + intygId,
      method: 'DELETE'
    };
    return restClient.run(options, 'json', env.INTYGTJANST_URL + '/resources/');
  },
  deleteAllIntyg: function(userId) {
    var options = {
      url: 'certificate/citizen/' + userId,
      method: 'DELETE'
    };
    return restClient.run(options, 'json', env.INTYGTJANST_URL + '/resources/');
  },
  createApprovedReceivers: function(intygsId, createJson) {
    var options = {
      url: 'certificate/' + intygsId + '/approvedreceivers/',
      method: 'POST',
      body: createJson
    };
    return restClient.run(options, 'json', env.INTYGTJANST_URL + '/resources/');
  },
  deleteApprovedReceivers: function(intygsId) {
    var options = {
      url: 'certificate/' + intygsId + '/approvedreceivers/',
      method: 'DELETE'
    };
    return restClient.run(options, 'json', env.INTYGTJANST_URL + '/resources/');
  },
  createBanners: function(banner) {
    var options = {
      url: 'services/api/ia-api/banner',
      method: 'PUT',
      body: banner
    };
    return restClient.run(options, 'json');
  },
  clearBanners: function() {
    var options = {
      url: 'services/api/ia-api/cache',
      method: 'DELETE'
    };
    return restClient.run(options, 'json');
  }
};
