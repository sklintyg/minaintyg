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
angular.module('minaintyg').controller('minaintyg.ErrorViewCtrl',

    function($scope, $stateParams) {
        'use strict';

        // set a default if no errorCode is given in stateParams
        $scope.errorCode = $stateParams.errorCode || 'generic';
        $scope.pagefocus = true;
    });
