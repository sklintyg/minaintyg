'use strict';
/**
 * Common services/interceptors for cross cutting concerns in the app.
 * @author marced
 * 
 */


/**
 * Interceptor that decorates all GET requests made by the $http service. Can be
 * used by the modules as a common component. To hook up the interceptor, simply
 * config the http provider for the app like this (in 1.1.5):
 * 
 * app.config(function ($httpProvider) {
 * $httpProvider.interceptors.push('httpRequestInterceptorCacheBuster'); })
 * 
 */
angular.module('services.util', []);
angular.module('services.util').factory('httpRequestInterceptorCacheBuster', function($q) {
    return {
        request : function(config) {
            // Don't mess with view loading, ok if cached..
            if (config.url.indexOf(".html") == -1) {
                var sep = config.url.indexOf('?') === -1 ? '?' : '&';
                config.url = config.url + sep + 'cacheSlayer=' + new Date().getTime();
            }
            return config || $q.when(config);
        }
    };
});

/**
 * Response interceptor intercepts ALL responses coming back through the $http service.On 403 status responses, 
 * the browser is redirected to the web apps main starting point. To hook up the interceptor, simply
 * config the http provider for the app like this (in 1.1.5):
 * 
 * $httpProvider.responseInterceptors.push('http403ResponseInterceptor');
 * 
 * TODO: refactor into a "provider", so that the redirecturl can be configured in app.config. 
 * That way, this service becomes more reusable.
 * 
 */
angular.module('services.util').factory('http403ResponseInterceptor', function($q, $window) {
    return function(promise) {
        // Add our custom success/failure handlers to the promise chain..
        return promise.then(function(response) {
            //when success - simply return response as-is
            return response;
        }, function(response) {
            //for 403 responses - redirect browser to start url (that will actually do the 403 handling)
            if (response.status == "403") {
                $window.location.href = "/web/start";
            }
            //signal rejection  (not really meaningful here since we already issued a redirect of the browser)
            return $q.reject(response);
        });
    }
});

/**
 * Generic controller that exposes an errorCode extracted from routeparams to
 * the scope. used by the modules as a common component. Make a dependency to
 * 'controllers.util' in the app: and ha this as a controller for your error
 * page routing e.g: ... }).when('/fel/:errorCode', { templateUrl :
 * '/views/error.html', controller : 'ErrorViewCtrl' })
 * 
 * and then in code route to this controller like this:
 * 
 * $location.path("/fel/certnotfound");
 * 
 */
angular.module('controllers.util', []);
angular.module('controllers.util').controller('ErrorViewCtrl', [ '$scope', '$routeParams', function ErrorViewCtrl($scope, $routeParams) {

    // set a default if no errorCode is given in routeparams
    $scope.errorCode = $routeParams.errorCode || "generic";
    $scope.pagefocus = true;
} ]);
