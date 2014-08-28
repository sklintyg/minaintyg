angular.module('minaintyg').directive('miSpinner',
    function() {
        'use strict';

        return {
            restrict: 'A',
            transclude: true,
            replace: true,
            scope: {
                label: '@',
                showSpinner: '=',
                showContent: '='
            },
            templateUrl: '/js/directives/miSpinner.html'
        };
    });
