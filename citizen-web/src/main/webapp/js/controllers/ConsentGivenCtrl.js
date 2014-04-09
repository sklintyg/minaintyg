define([], function() {
	'use strict';

	return [ '$scope', '$location', '$filter', 'consentService', '$window',
			function ($scope, $location, $filter, consentService, $window) {
				$scope.continueToMI = function() {
					$window.location.href = "/web/start";
				}

				$scope.pagefocus = true;
			} ];
});
