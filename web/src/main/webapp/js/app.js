/* global MI_CONFIG, miMessages */
window.name = 'NG_DEFER_BOOTSTRAP!'; // jshint ignore:line

var app = angular.module('minaintyg', [ 'ui.bootstrap', 'ngCookies', 'ngRoute', 'ngSanitize', 'ngAnimate', 'common' ]);

app.config(function($routeProvider) {
    'use strict';

    $routeProvider.
        when('/consent', {
            templateUrl: '/views/consent/consent-start.html',
            controller: 'minaintyg.ConsentCtrl',
            title: 'Ditt samtycke',
            keepInboxTabActive: false
        }).
        when('/lista', {
            templateUrl: '/views/list.html',
            controller: 'minaintyg.ListCtrl',
            title: 'Inkorgen',
            keepInboxTabActive: false
        }).
        when('/arkiverade', {
            templateUrl: '/views/list-archived.html',
            controller: 'minaintyg.ListArchivedCtrl',
            title: 'Arkiverade intyg',
            keepInboxTabActive: false
        }).
        when('/omminaintyg', {
            templateUrl: '/views/om-mina-intyg.html',
            controller: 'minaintyg.AboutCtrl',
            title: 'Om mina intyg',
            keepInboxTabActive: false
        }).
        when('/hjalp', {
            templateUrl: '/views/hjalp.html',
            controller: 'minaintyg.HelpCtrl',
            title: 'Hjälp',
            keepInboxTabActive: false
        }).
        when('/fel/:errorCode', {
            templateUrl: '/views/error.html',
            controller: 'minaintyg.ErrorViewCtrl',
            title: 'Fel',
            keepInboxTabActive: false
        }).
        otherwise({
            redirectTo: function() {
                // When running IE in QA the VerifyConsentInterceptor doesnt give us the #/consent after the redirect.
                // This is a workaround to add it back.
                if (window.location.href.indexOf('/web/visa-ge-samtycke') > -1) {
                    return '/consent';
                }
                else {
                    return '/lista';
                }
            }
        });
});

app.config([ '$httpProvider', 'common.http403ResponseInterceptorProvider',
    function($httpProvider, http403ResponseInterceptorProvider) {
        'use strict';

        // Add cache buster interceptor
        $httpProvider.interceptors.push('common.httpRequestInterceptorCacheBuster');

        // Configure 403 interceptor provider
        http403ResponseInterceptorProvider.setRedirectUrl('/web/start');
        $httpProvider.responseInterceptors.push('common.http403ResponseInterceptor');
    }]);


app.run([ '$rootScope', '$state', '$window', 'common.messageService',
    function($rootScope, $state, $window, messageService) {
        'use strict';

        $rootScope.lang = 'sv';
        $rootScope.DEFAULT_LANG = 'sv';
        $rootScope.MI_CONFIG = MI_CONFIG;
        messageService.addResources(miMessages);

        // Update page title
        $rootScope.page_title = 'Titel';
        $rootScope.$on('$routeChangeSuccess', function() {
            if ($state.current.$$route) {

                if ($state.current.$$route.keepInboxTabActive === false) {
                    $rootScope.keepInboxTab = false;
                }
                $rootScope.page_title = $state.current.$$route.title + ' | Mina intyg';
            }
        });

        $window.doneLoading = false;
        $window.dialogDoneLoading = true;

        $rootScope.$on('$routeChangeStart', function() {
            $window.doneLoading = false;
        });
        $rootScope.$on('$routeChangeSuccess', function() {
            $window.doneLoading = true;
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
        modulePromises.push(loadScriptFromUrl('/web/webjars/common/minaintyg/js/module.min.js?' +
            MI_CONFIG.BUILD_NUMBER));
        // All dependencies in module-deps.json are included in module.min.js
        // All dependencies in app-deps.json are included in app.min.js

    } else {
        modulePromises.push(loadScriptFromUrl('/web/webjars/common/minaintyg/js/module.js'));
        modulePromises.push($.get('/web/webjars/common/minaintyg/js/module-deps.json'));
        modulePromises.push($.get('/js/app-deps.json'));

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
