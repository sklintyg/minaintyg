define([], function() {
	'use strict';

	return [ '$scope', function($scope) {
		$scope.pagefocus = true;
		
		// Hold left side navigation state
		$scope.visibility = {
			"helpdescription" : true,
			"helpfaq" : false
		}

		// Hold focus state for sub pages
		$scope.subpagefocus = {
			"helpdescription" : false,
			"helpfaq" : false
		}

		$scope.navigateTo = function(section) {
			angular.forEach($scope.visibility, function(value, key) {
				$scope.visibility[key] = (key == section) ? true : false;
				$scope.subpagefocus[key] = (key == section) ? true : false;
			});
		}
	} ];
});
