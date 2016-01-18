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

/* global MI_CONFIG, miMessages */
// defers bootstrapping until all modules have been loaded, further down ...
window.name = 'NG_DEFER_BOOTSTRAP!'; // jshint ignore:line

var app = angular.module('minaintyg', [ 'ui.bootstrap', 'ngCookies', 'ui.router', 'ngSanitize', 'ngAnimate', 'common' ]);

app.config([ '$httpProvider', 'common.http403ResponseInterceptorProvider',
    function($httpProvider, http403ResponseInterceptorProvider) {
        'use strict';

        // Add cache buster interceptor
        $httpProvider.interceptors.push('common.httpRequestInterceptorCacheBuster');

        // Configure 403 interceptor provider
        http403ResponseInterceptorProvider.setRedirectUrl('/web/start');
        $httpProvider.interceptors.push('common.http403ResponseInterceptor');
    }]);

app.config(function($provide) {
    'use strict';
    $provide.decorator('$$rAF', function($delegate, $window, $timeout, $browser) {
        var requestAnimationFrame = $window.requestAnimationFrame ||
                                    $window.webkitRequestAnimationFrame;

        var cancelAnimationFrame = $window.cancelAnimationFrame ||
            $window.webkitCancelAnimationFrame ||
            $window.webkitCancelRequestAnimationFrame;

        var rafSupported = !!requestAnimationFrame;

        var raf = rafSupported ? function(fn) {
            var fn2 = function() {
                $browser.$$completeOutstandingRequest(fn);
            };
            $browser.$$incOutstandingRequestCount();
            var id = requestAnimationFrame(fn2);
            return function() {
                $browser.$$completeOutstandingRequest(angular.noop);
                cancelAnimationFrame(id);
            };
        }
            : function(fn) {
            var fn2 = function() {
                $browser.$$completeOutstandingRequest(fn);
            };
            $browser.$$incOutstandingRequestCount();
            var timer = $timeout(fn2, 16.66, false); // 1000 / 60 = 16.666
            return function() {
                $browser.$$completeOutstandingRequest(angular.noop);
                $timeout.cancel(timer);
            };
        };

        raf.supported = rafSupported;

        return raf;
    });
});

app.run([ '$log', '$rootScope', '$state', '$window', 'common.messageService',
    function($log, $rootScope, $state, $window, messageService) {
        'use strict';

        $rootScope.lang = 'sv';
        $rootScope.DEFAULT_LANG = 'sv';
        $rootScope.MI_CONFIG = MI_CONFIG;
        messageService.addResources(miMessages);

        $rootScope.page_title = 'Titel'; // jshint ignore:line

        $window.doneLoading = false;
        $window.rendered = true;

        $rootScope.$on('$stateChangeStart',
            function(/*event, toState, toParams, fromState, fromParams*/){
                $window.doneLoading = false;
            });

        $rootScope.$on('$stateNotFound',
            function(event, unfoundState, fromState, fromParams){
            });
        $rootScope.$on('$stateChangeSuccess',
            function(event, toState/*, toParams, fromState, fromParams*/){
                $window.doneLoading = true;
                if (toState.data.keepInboxTabActive === false) {
                    $rootScope.keepInboxTab = false;
                }
                if (toState.data.keepInboxTabActive === true) {
                    $rootScope.keepInboxTab = true;
                }
                $rootScope.page_title = toState.data.title + ' | Mina intyg'; // jshint ignore:line
            });

        $rootScope.$on('$stateChangeError',
            function(event, toState/*, toParams, fromState, fromParams, error*/){
                $log.log('$stateChangeError');
                $log.log(toState);
            });

        $window.onbeforeunload = function() {
            var request = new XMLHttpRequest();
            // `false` makes the request synchronous
            request.open('GET', '/api/certificates/onbeforeunload', false);
            request.send(null);
        };

    } ]);

// Get a list of all modules to find all files to load.
$.get('/api/certificates/map').then(function(modules) {
    'use strict';

    var modulesIds = [];
    var modulePromises = [];

    if (MI_CONFIG.USE_MINIFIED_JAVASCRIPT === 'true') {
        modulePromises.push(loadScriptFromUrl('/web/webjars/common/minaintyg/module.min.js?' +
            MI_CONFIG.BUILD_NUMBER));
        // All dependencies in module-deps.json are included in module.min.js
        // All dependencies in app-deps.json are included in app.min.js

    } else {
        modulePromises.push(loadScriptFromUrl('/web/webjars/common/minaintyg/module.js'));
        modulePromises.push($.get('/web/webjars/common/minaintyg/module-deps.json'));
        modulePromises.push($.get('/app/app-deps.json'));

        // Prevent jQuery from appending cache buster to the url to make it easier to debug.
        $.ajaxSetup({
            cache: true
        });
    }

    angular.forEach(modules, function(module) {
        modulesIds.push(module.id);
        loadCssFromUrl(module.cssPath + '?' + MI_CONFIG.BUILD_NUMBER);

        if (MI_CONFIG.USE_MINIFIED_JAVASCRIPT === 'true') {
            modulePromises.push(loadScriptFromUrl(module.scriptPath + '.min.js?' + MI_CONFIG.BUILD_NUMBER));
            // All dependencies for the modules are included in module.min.js
        } else {
            modulePromises.push(loadScriptFromUrl(module.scriptPath + '.js'));
            modulePromises.push($.get(module.dependencyDefinitionPath));
        }
    });

    // Wait for all modules and module dependency definitions to load.
    $.when.apply(this, modulePromises).then(function() {
        var dependencyPromises = [];

        // Only needed for development since all dependencies are included in other files.
        if (MI_CONFIG.USE_MINIFIED_JAVASCRIPT === 'false') {
            angular.forEach(arguments, function(data) {
                if (data !== undefined && data[0] instanceof Array) {
                    angular.forEach(data[0], function(depdendency) {
                        dependencyPromises.push(loadScriptFromUrl(depdendency));
                    });
                }
            });
        }

        // Wait for all dependencies to load (for production dependencies are empty which is resolved immediately)
        $.when.apply(this, dependencyPromises).then(function() {
            angular.element().ready(function() {

                // Everything is loaded, bootstrap the application with all dependencies.
                angular.resumeBootstrap([app.name, 'common'].concat(Array.prototype.slice.call(modulesIds, 0)));
            });
        }).fail(function(error) {
            if (window.console) {
                console.log(error);
            }
        });
    }).fail(function(error) {
        if (window.console) {
            console.log(error);
        }
    });
});

function loadCssFromUrl(url) {
    'use strict';

    var link = document.createElement('link');
    link.type = 'text/css';
    link.rel = 'stylesheet';
    link.href = url;
    document.getElementsByTagName('head')[0].appendChild(link);
}

function loadScriptFromUrl(url) {
    'use strict';

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
