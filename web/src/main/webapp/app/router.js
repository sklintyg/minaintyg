/*
 * Copyright (C) 2015 Inera AB (http://www.inera.se)
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
 * Created by stephenwhite on 05/03/15.
 */
angular.module('minaintyg').config(function($stateProvider, $urlRouterProvider) {
    'use strict';

    $stateProvider.
        state('consent', {
            url :'/consent',
            templateUrl: '/app/views/consent/consent-start.html',
            controller: 'minaintyg.ConsentCtrl',
            data:{title: 'Ditt samtycke',keepInboxTabActive: false}
        }).
        state('lista', {
            url : '/lista',
            templateUrl: '/app/views/list/list.html',
            controller: 'minaintyg.ListCtrl',
            data:{title: 'Inkorgen',keepInboxTabActive: false}
        }).
        state('arkiverade', {
            url : '/arkiverade',
            templateUrl: '/app/views/list/archive/list-archived.html',
            controller: 'minaintyg.ListArchivedCtrl',
            data:{title: 'Arkiverade intyg',keepInboxTabActive: false}
        }).
        state('omminaintyg', {
            url :'/omminaintyg',
            templateUrl: '/app/views/about/about-mina-intyg.html',
            controller: 'minaintyg.AboutCtrl',
            data:{title: 'Om mina intyg',keepInboxTabActive: false}
        }).
        state('hjalp', {
            url : '/hjalp',
            templateUrl: '/app/views/help/help.html',
            controller: 'minaintyg.HelpCtrl',
            data:{title: 'Hjälp',keepInboxTabActive: false}
        }).
        state('fel', {
            url :'/fel/:errorCode',
            templateUrl: '/app/views/error/error.html',
            controller: 'minaintyg.ErrorViewCtrl',
            data:{title: 'Fel',keepInboxTabActive: false}
        });

        $urlRouterProvider.otherwise(function() {
            // When running IE in QA the VerifyConsentInterceptor doesnt give us the #/consent after the redirect.
            // This is a workaround to add it back.
            if (window.location.href.indexOf('/web/visa-ge-samtycke') > -1) {
                return '/consent';
            }
            else {
                return '/lista';
            }
        });
});
