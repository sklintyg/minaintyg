angular.module('minaintyg').controller('minaintyg.ConsentGivenCtrl',
    function($filter, $location, $scope, $window) {
        'use strict';
        $scope.continueToMI = function() {
            $window.location.href = '/web/start';
        };

        $scope.pagefocus = true;
    });
