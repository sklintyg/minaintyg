angular.module('showcase').controller('showcase.navigationCtrl',
    ['$scope', '$window',
        function($scope, $window) {
            'use strict';

            $scope.showCookieBanner = false;
            $scope.doShowCookieBanner = function() {
                $window.localStorage.setItem('mi-cookie-consent-given', '0');
                $scope.showCookieBanner = !$scope.showCookieBanner;
            };


        }]);
