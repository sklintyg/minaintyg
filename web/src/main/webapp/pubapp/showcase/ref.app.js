/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
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

(function() {
    'use strict';


    // --- end test hooks

    // Globally configure jquery not to cache ajax requests.
    // Our other angular $http service driven requests have their own solution (using an interceptor)

    $.ajaxSetup({cache: false});



    angular.module('minaintyg', [ 'ui.bootstrap', 'ngCookies', 'ui.router', 'ngSanitize', 'ngAnimate', 'common' ]);

    var app = angular.module('showcase',
        ['ui.bootstrap', 'ui.router', 'ngCookies', 'ngSanitize', 'minaintyg', 'common', 'ngAnimate', 'ngMockE2E']);

    app.value('networkConfig', {
        defaultTimeout: 30000 // test: 1000
    });



    app.config(['$httpProvider', '$logProvider',
        function($httpProvider, $logProvider) {
            // Add cache buster interceptor
            $httpProvider.interceptors.push('common.httpRequestInterceptorCacheBuster');

            // Enable debug logging
            $logProvider.debugEnabled(true);
        }]);

    app.config([ '$httpProvider', 'common.http403ResponseInterceptorProvider',
        function($httpProvider, http403ResponseInterceptorProvider) {

            // Add cache buster interceptor
            $httpProvider.interceptors.push('common.httpRequestInterceptorCacheBuster');

            // Configure 403 interceptor provider
            http403ResponseInterceptorProvider.setRedirectUrl('/pubapp/showcase/login.html');
            $httpProvider.interceptors.push('common.http403ResponseInterceptor');
        }]);


    // Inject language resources
    app.run(['$log', '$rootScope', '$window', '$location', '$state', '$q', 'common.messageService', '$httpBackend', 'minaintyg.messages',
        function($log, $rootScope, $window, $location, $state, $q, messageService, $httpBackend, miMessages) {

            $rootScope.lang = 'sv';
            $rootScope.DEFAULT_LANG = 'sv';

            //Kanske vi kan (i resp controller) sätta upp 'when' mockning så att direktiven kan köra som i en sandbox (Se exempel i arendehantering.controller.js)?
            // Detta kanske gör det möjligt att kunna laborera med ett direktivs alla funktioner som även kräver backendkommunikation.
            $httpBackend.whenGET(/^\/api\/*/).respond(200);
            $httpBackend.whenPOST(/^\/api\/*/).respond(200);
            $httpBackend.whenPUT(/^\/api\/*/).respond(200);

            $httpBackend.whenGET(/^\/moduleapi\/*/).respond(200);
            $httpBackend.whenPOST(/^\/moduleapi\/*/).respond(200);
           // $httpBackend.whenPUT(/^\/moduleapi\/*/).respond(200);

            //Ev. templates skall få hämtas på riktigt
            $httpBackend.whenGET(/^.+\.html/).passThrough();

            messageService.addResources(miMessages);
            messageService.addResources({'sv': {
                'test.fraga.label' : 'En testfråga'
            }});
        }]);


    // Ladda alla dependencies
    $.get('/api/certificates/map').then(function(modules) {


        var modulesIds = [];
        var modulePromises = [];
        //Add wc/common resources
        modulePromises.push(loadScriptFromUrl('/web/webjars/common/minaintyg/module.min.js'));
        modulePromises.push(loadScriptFromUrl('/app/views/miCookieBanner.directive.js'));
        modulePromises.push(loadScriptFromUrl('/app/views/miHeader.directive.js'));
        modulePromises.push(loadScriptFromUrl('/app/views/miMainNavigation.directive.js'));
        modulePromises.push(loadScriptFromUrl('/app/views/mvkTopBar.directive.js'));

        angular.forEach(modules, function(module) {
            modulesIds.push(module.id);
            loadCssFromUrl(module.cssPath);
            modulePromises.push(loadScriptFromUrl(module.scriptPath + '.min.js'));

        });

        // Wait for all modules and module dependency definitions to load.
        $.when.apply(this, modulePromises).then(function() {

            angular.element(document).ready(function() {

                var allModules = [app.name, 'showcase', 'common'].concat(Array.prototype.slice.call(modulesIds, 0));



                // Everything is loaded, bootstrap the application with all dependencies.
                document.documentElement.setAttribute('ng-app', 'showcase');
                angular.bootstrap(document, allModules);

            });
        }).fail(function(error) {
            if (window.console) {
                console.log(error);
            }
        });
    }).fail(function(error) {
        console.log(error);
        if (error.status === 403) {
            window.location = 'login.html';
        }
    });

    function loadCssFromUrl(url) {


        var link = document.createElement('link');
        link.type = 'text/css';
        link.rel = 'stylesheet';
        link.href = url;
        document.getElementsByTagName('head')[0].appendChild(link);
    }

    function loadScriptFromUrl(url) {


        var result = $.Deferred();

        var script = document.createElement('script');
        script.async = 'async';
        script.type = 'text/javascript';
        script.src = url;
        script.onload = script.onreadystatechange = function(_, isAbort) {
            if (!script.readyState || /loaded|complete/.test(script.readyState)) {
                if (isAbort) {
                    result.reject();
                } else {
                    result.resolve();
                }
            }
        };
        script.onerror = function() {
            result.reject();
        };
        document.getElementsByTagName('head')[0].appendChild(script);
        return result.promise();
    }



}());
