'use strict';

/* App Module */

var consentApp = angular.module('ConsentApp', [ 'ui.bootstrap', 'services.consent', 'modules.messages', 'directives.mi', 'services.util', 'controllers.util' ]).config(
        [ '$routeProvider', '$httpProvider', function($routeProvider, $httpProvider) {
            $routeProvider.when('/start', {
                templateUrl : '/views/consent/consent-start.html',
                controller : 'ConsentCtrl',
                title : 'Ditt samtycke'
            }).when('/samtycke-givet', {
                templateUrl : '/views/consent/consent-given.html',
                controller : 'ConsentGivenCtrl',
                title : 'Samtycke godkänt'
            }).when('/fel/:errorCode', {
                templateUrl : '/views/error.html',
                controller : 'ErrorViewCtrl',
                title : 'Fel'
            }).otherwise({
                redirectTo : '/start'
            });

            $httpProvider.interceptors.push('httpRequestInterceptorCacheBuster');
        } ]);

consentApp.run([ '$rootScope', '$route', 'messageService', function($rootScope, $route, messageService) {
    $rootScope.lang = 'sv';
    $rootScope.DEFAULT_LANG = 'sv';
    messageService.addResources(commonMessageResources);
    messageService.addResources(consentAppResources);

    // Update page title
    $rootScope.page_title = 'Titel';
    $rootScope.$on('$routeChangeSuccess', function() {
        if ($route.current.$$route) {
            $rootScope.page_title = $route.current.$$route.title + ' | Mina intyg';
        }
    });
} ]);
