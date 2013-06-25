'use strict';

/* App Module */

var consentApp = angular.module('ConsentApp', [ 'ui.bootstrap', 'services.consent', 'modules.messages', 'directives.mi', 'services.util' ]).config([ '$routeProvider', '$httpProvider', function($routeProvider, $httpProvider) {
    $routeProvider.when('/start', {
        templateUrl : '/views/consent/consent-start.html',
        controller : 'ConsentCtrl'
    }).when('/samtycke-givet', {
        templateUrl : '/views/consent/consent-given.html',
        controller : 'ConsentGivenCtrl'
    }).when('/fel', {
        templateUrl : '/views/error.html',
    }).otherwise({
        redirectTo : '/start'
    });
    
    $httpProvider.interceptors.push('httpRequestInterceptorCacheBuster');
} ]);

consentApp.run([ '$rootScope','messageService', function($rootScope, messageService) {
    $rootScope.lang = 'sv';
    $rootScope.DEFAULT_LANG = 'sv';
    messageService.addResources(commonMessageResources);
    messageService.addResources(consentAppResources);
} ]);
