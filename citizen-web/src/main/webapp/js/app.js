define([
    'angular',
    'angularRoute',
    'angularSanitize',
    'controllers',
    'directives',
    'filters',
    'messages',
    'services',
    'mi-common-message-resources',
    'webjars/common/js/wc-message-module',
    'webjars/common/js/wc-utils'
], function (angular, angularRoute, angularSanitize, controllers, directives, filters, messages, services, commonMessageResources, wcMessageModule, wcUtils) {
    'use strict';

    var app = angular.module('intygApp', ['ui.bootstrap', 'ngCookies', 'ngRoute', 'ngSanitize',
        controllers, directives, filters, services, wcMessageModule, wcUtils]);

    app.config(['$routeProvider', '$controllerProvider', '$compileProvider', '$filterProvider', '$provide', '$httpProvider', 'http403ResponseInterceptorProvider',
        function ($routeProvider, $controllerProvider, $compileProvider, $filterProvider, $provide, $httpProvider, http403ResponseInterceptorProvider) {

            app.register = {
                controller : $controllerProvider.register,
                directive : $compileProvider.directive,
                filter : $httpProvider.register,
                factory : $provide.factory,
                service : $provide.service,
                $routeProvider : $routeProvider
            };
            
            //Configure interceptor provider
            http403ResponseInterceptorProvider.setRedirectUrl("/web/start");
            
            //Add interceptor
            $httpProvider.interceptors.push('httpRequestInterceptorCacheBuster');
            $httpProvider.responseInterceptors.push('http403ResponseInterceptor');
        }]);

    app.run([ '$rootScope', '$route', 'messageService', '$window', '$log', '$http', function($rootScope, $route, messageService, $window, $log, $http) {
        $rootScope.lang = 'sv';
        $rootScope.DEFAULT_LANG = 'sv';
        $rootScope.MI_CONFIG = MI_CONFIG;
        messageService.addResources(commonMessageResources);
        messageService.addResources(messages);
        
        $window.onbeforeunload = function() {
        	var request = new XMLHttpRequest();
        	// `false` makes the request synchronous
        	request.open('GET', '/api/certificates/onbeforeunload', false);
        	request.send(null);
            $log.debug('onbeforeunload');

        };

    	// Update page title
    	$rootScope.page_title = 'Titel';
        $rootScope.$on('$routeChangeSuccess', function() {
    	  if ($route.current.$$route){
    		  $rootScope.page_title = $route.current.$$route.title + ' | Mina intyg';
    	  }
        });
    }]);

    require(['text!/api/certificates/map'], function (modules) {

        var modulesMap = JSON.parse(modules);

        var modulesUrls = [];
        for (var artifactId in modulesMap) {
            modulesUrls.push(modulesMap[artifactId].id + modulesMap[artifactId].scriptPath);
            loadCssFromUrl('/web/webjars/' + modulesMap[artifactId].id + modulesMap[artifactId].cssPath);
        }

        require({ baseUrl: '/web/webjars/' }, modulesUrls, function () {
            var modules = arguments;

            angular.element().ready(function () {
                angular.resumeBootstrap([app.name].concat(Array.prototype.slice.call(modules, 0)));
            });
        });
    });

    function loadCssFromUrl(url) {
        var link = createLinkElement(url);
        document.getElementsByTagName('head')[0].appendChild(link);
    }

    function createLinkElement(url) {
        var link = document.createElement('link');
        link.type = 'text/css';
        link.rel = 'stylesheet';
        link.href = url;
        return link;
    }

    return app;
});
