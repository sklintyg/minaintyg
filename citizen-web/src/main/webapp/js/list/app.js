'use strict';

/* App Module */

var listCertApp = angular.module('ListCertApp', [ 'ui.bootstrap', 'mi.filters', 'services.listCertService', 'services.consent', 'modules.messages', 'directives.mi' ]).config(
        [ '$routeProvider', function($routeProvider) {
            $routeProvider.when('/lista', {
                templateUrl : '/views/list.html',
                controller : 'ListCtrl'
            }).when('/arkiverade', {
                templateUrl : '/views/list-archived.html',
                controller : 'ListArchivedCtrl'
            }).when('/intyg', {
                templateUrl : '/views/certificate.html',
                controller : 'CertCtrl'
            }).when('/skicka-intyg', {
                templateUrl : '/views/send-cert-confirm.html',
                controller : 'SendCertCtrl'
            }).when('/omminaintyg', {
                templateUrl : '/views/om-mina-intyg.html',
                controller : 'AboutCtrl'
            }).when('/hjalp', {
                templateUrl : '/views/hjalp.html',
            // controller: 'SendCertCtrl'

            }).otherwise({
                redirectTo : '/lista'
            });
        } ]);

listCertApp.run([ '$rootScope', 'messageService', function($rootScope, messageService) {
    $rootScope.lang = 'sv';
    $rootScope.DEFAULT_LANG = 'sv';
    messageService.addResources(minaIntygResources);
} ]);
