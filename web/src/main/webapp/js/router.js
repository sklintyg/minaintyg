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
            url : '/arkiverade'
            templateUrl: '/views/list-archived.html',
            controller: 'minaintyg.ListArchivedCtrl',
            data:{title: 'Arkiverade intyg'},
            keepInboxTabActive: false
        }).
        state('omminaintyg', {
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

        $urlRouterProvider.otherwise('/lista');
});