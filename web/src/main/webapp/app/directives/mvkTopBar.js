angular.module('minaintyg').directive('mvkTopBar',
    function() {
        'use strict';

        return {
            restrict: 'E',
            replace: true,
            scope: {
                hideLogout: '@'
            },
            templateUrl: '/app/directives/mvkTopBar.html'
        };
    });
