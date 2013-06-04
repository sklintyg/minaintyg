'use strict';

/* App Module */

var consentApp = angular.module('ConsentApp', [ 'ui.bootstrap', 'services.consent', 'modules.messages', 'directives.mi' ]).config([ '$routeProvider', function($routeProvider) {
    $routeProvider.when('/start', {
        templateUrl : '/views/consent/consent-start.html',
        controller : 'ConsentCtrl'
    }).when('/samtycke-givet', {
        templateUrl : '/views/consent/consent-given.html',
        controller : 'ConsentCtrl'
    }).when('/fel', {
        templateUrl : '/views/error.html',
    }).otherwise({
        redirectTo : '/start'
    });
} ]);

consentApp.run([ '$rootScope', function($rootScope) {
    $rootScope.lang = 'sv';
    $rootScope.DEFAULT_LANG = 'sv';
} ]);
