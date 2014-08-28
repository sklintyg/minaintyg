/* global MI_CONFIG, miMessages */
window.name = 'NG_DEFER_BOOTSTRAP!'; // jshint ignore:line

var app = angular.module('minaintyg', [ 'ui.bootstrap', 'ngCookies', 'ngRoute', 'ngSanitize', 'common' ]);

app.run([ '$rootScope', 'common.messageService', function($rootScope, messageService) {
    'use strict';

    $rootScope.lang = 'sv';
    $rootScope.DEFAULT_LANG = 'sv';
    messageService.addResources(miMessages);
} ]);

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


// Wait for all modules and module dependency definitions to load.
$.when.apply(this, modulePromises).then(function() {
    'use strict';

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
            angular.resumeBootstrap([app.name, 'common']);
        });
    }).fail(function(error) {
        console.log(error);
    });
});


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
