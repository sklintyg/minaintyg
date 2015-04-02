/**
 * Created by stephenwhite on 05/03/15.
 */
angular.module('minaintyg').config(function($stateProvider, $urlRouterProvider) {
    'use strict';

    $stateProvider.
        state('consent', {
            url :'/consent',
            templateUrl: '/views/consent/consent-start.html',
            controller: 'minaintyg.ConsentCtrl',
            data:{title: 'Ditt samtycke'},
            keepInboxTabActive: false
        }).
        state('lista', {
            url : '/lista',
            templateUrl: '/views/list.html',
            controller: 'minaintyg.ListCtrl',
            data:{title: 'Inkorgen'},
            keepInboxTabActive: false
        }).
        state('arkiverade', {
            url : '/arkiverade',
            templateUrl: '/views/list-archived.html',
            controller: 'minaintyg.ListArchivedCtrl',
            data:{title: 'Arkiverade intyg'},
            keepInboxTabActive: false
        }).
        state('om-minaintyg', {
            url :'/omminaintyg',
            templateUrl: '/views/om-mina-intyg.html',
            controller: 'minaintyg.AboutCtrl',
            data:{title: 'Om mina intyg'},
            keepInboxTabActive: false
        }).
        state('hjalp', {
            url : '/hjalp',
            templateUrl: '/views/hjalp.html',
            controller: 'minaintyg.HelpCtrl',
            data:{title: 'Hjälp'},
            keepInboxTabActive: false
        }).
        state('fel', {
            url :'/fel/:errorCode',
            templateUrl: '/views/error.html',
            controller: 'minaintyg.ErrorViewCtrl',
            data:{title: 'Fel'},
            keepInboxTabActive: false
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