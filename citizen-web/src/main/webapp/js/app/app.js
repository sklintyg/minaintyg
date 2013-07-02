'use strict';

/* App Module */

var listCertApp = angular.module('ListCertApp', [ 'ui.bootstrap', 'mi.filters', 'services.listCertService', 'services.consent', 'modules.messages', 'directives.mi', 'services.util', 'controllers.util' ]).config(
        [ '$routeProvider', '$httpProvider','http403ResponseInterceptorProvider', function($routeProvider, $httpProvider, http403ResponseInterceptorProvider) {
            $routeProvider.when('/lista', {
                templateUrl : '/views/list.html',
                controller : 'ListCtrl',
	            title: 'Inkorgen'
            }).when('/arkiverade', {
                templateUrl : '/views/list-archived.html',
                controller : 'ListArchivedCtrl',
			    title: 'Arkiverade intyg'
            }).when('/omminaintyg', {
                templateUrl : '/views/om-mina-intyg.html',
                controller : 'AboutCtrl',
			    title: 'Om mina intyg'
            }).when('/hjalp', {
                templateUrl : '/views/hjalp.html',
			    controller : 'HelpCtrl',
	            title: 'Hjälp'
            }).when('/fel/:errorCode', {
                templateUrl : '/views/error.html',
                controller : 'ErrorViewCtrl',
                title : 'Fel'
            }).otherwise({
                redirectTo : '/lista'
            });

            //Configure interceptor provider
           http403ResponseInterceptorProvider.setRedirectUrl("/web/start");
           
           //Add interceptor
           $httpProvider.interceptors.push('httpRequestInterceptorCacheBuster');
           $httpProvider.responseInterceptors.push('http403ResponseInterceptor');
            
        } ]);

listCertApp.run([ '$rootScope', '$route', 'messageService', function($rootScope, $route, messageService) {
    $rootScope.lang = 'sv';
    $rootScope.DEFAULT_LANG = 'sv';
    messageService.addResources(commonMessageResources);
    messageService.addResources(minaIntygResources);

	// Update page title
	$rootScope.page_title = 'Titel';
    $rootScope.$on('$routeChangeSuccess', function() {
	  if ($route.current.$$route){
		  $rootScope.page_title = $route.current.$$route.title + ' | Mina intyg';
	  }
    });
} ]);
