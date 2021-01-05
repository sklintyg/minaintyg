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

angular.module('minaintyg').config(function($stateProvider) {
  'use strict';
  $stateProvider
  .state('omminaintyg', {
    url: '/omminaintyg',
    templateUrl: '/app/views/about/about-mina-intyg-page.html',
    controller: 'minaintyg.AboutCtrl',
    data: {title: 'Om Mina intyg', keepInboxTabActive: false, breadcrumb: ['om']}
  }).state('omminaintyg.info', {
    url: '/info',
    views: {
      'content@omminaintyg': {
        templateUrl: '/app/views/about/section-info.html'
      }
    }
  }).state('omminaintyg.help-info', {
    url: '/help-info',
    views: {
      'content@omminaintyg': {
        templateUrl: '/app/views/about/section-help-and-support.html'
      }
    }
  }).state('omminaintyg.faq', {
    url: '/faq',
    views: {
      'content@omminaintyg': {
        templateUrl: '/app/views/about/faq/faq.html',
        controller: 'AboutFaqPageCtrl'
      }
    }
  }).state('omminaintyg.juridik', {
    url: '/juridik',
    views: {
      'content@omminaintyg': {
        templateUrl: '/app/views/about/section-juridik.html'
      }
    }
  }).state('omminaintyg.personuppgifter-info', {
    url: '/personuppgifter',
    views: {
      'content@omminaintyg': {
        templateUrl: '/app/views/about/section-personuppgifter.html'
      }
    }
  });
});
