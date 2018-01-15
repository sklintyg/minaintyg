/*
 * Copyright (C) 2018 Inera AB (http://www.inera.se)
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
    state('index', {
        url :'/',
        templateUrl: '/app/views/index/index.html',
        controller: 'minaintyg.IndexCtrl',
        data:{title: 'Mina Intyg', keepInboxTabActive: false}
    }).
    state('fk-logged-out', {
        url :'/fk-logged-out',
        templateUrl: '/app/views/fk-logged-out/fk-logged-out.html',
        controller: 'minaintyg.FkLoggedOutCtrl',
        data:{title: 'Mina Intyg', keepInboxTabActive: false}
    }).
        state('consent', {
            url :'/consent',
            templateUrl: '/app/views/consent/consent-start.html',
            controller: 'minaintyg.ConsentCtrl',
            data:{title: 'Ditt samtycke', keepInboxTabActive: false}
        }).
        state('inkorg', {
            url : '/inkorg',
            templateUrl: '/app/views/list/list.html',
            controller: 'minaintyg.ListCtrl',
            data:{title: 'Inkorgen', keepInboxTabActive: false,
                breadcrumb: ['inkorg']}
        }).
        state('send', {
            url : '/send/:type/:certificateId/:defaultRecipient',
            templateUrl: '/app/views/send/send.page.html',
            controller: 'minaintyg.SendCtrl',
            data:{title: 'Skicka intyg till mottagare', keepInboxTabActive: true,
                breadcrumb: ['inkorg', 'intyg', 'skicka'], backState: 'history-back'}
        }).
        state('arkiverade', {
            url : '/arkiverade',
            templateUrl: '/app/views/list/archive/list-archived.html',
            controller: 'minaintyg.ListArchivedCtrl',
            data:{title: 'Arkiverade intyg', keepInboxTabActive: false,
                breadcrumb: ['arkiv']}
        }).
        state('fel', {
            url :'/fel/:errorCode',
            templateUrl: '/app/views/error/error.html',
            controller: 'minaintyg.ErrorViewCtrl',
            data:{title: 'Fel', keepInboxTabActive: false}
        });

        $urlRouterProvider.otherwise(function() {
            // This block handles the initial selection of state of the app, depending on the url
            // (The PageController / consentInterceptor sends redirects etc.)
            if (window.location.href.indexOf('/web/visa-ge-samtycke') > -1) {
                //Handle and detect server-redirect to samtycke.
                return '/consent';
            }
            if (window.location.href.indexOf('/web/logga-ut-fk') > -1) {
                //Handle and detect server-redirect to special fk-logged-out-view.
                return '/fk-logged-out';
            } else if (window.location.pathname ==='/') {
                //Handle landningpage startup
                return '/';
            }
            else {
                //Otherwise default to inkorg
                return '/inkorg';
            }
        });
});
