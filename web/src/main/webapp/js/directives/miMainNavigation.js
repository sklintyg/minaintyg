angular.module('minaintyg').directive('miMainNavigation',
    function($rootScope, $location) {
        'use strict';

        return {
            restrict: 'E',
            replace: true,
            scope: {
                linkPrefix: '@',
                defaultActive: '@'
            },
            controller: function($scope) {
                $scope.navClass = function(page) {
                    if (angular.isString($scope.defaultActive)) {
                        if (page === $scope.defaultActive) {
                            return 'active';
                        }
                    }
                    var currentRoute;
                    if ($rootScope.keepInboxTab) {
                        currentRoute = 'lista';
                    } else {
                        currentRoute = $location.path().substring(1) || 'lista';
                    }
                    return page === currentRoute ? 'active' : '';
                };
            },
            templateUrl: '/js/directives/miMainNavigation.html'
        };
    });
