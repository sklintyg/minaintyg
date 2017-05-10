angular.module('minaintyg').directive('miScrollToTop', function() {
    'use strict';

    return {
        restrict: 'E',
        templateUrl: '/app/components/miScrollToTop/miScrollToTop.directive.html',
        controller: function($scope, $window, $document) {

            $scope.lowerHalf = false;

            $scope.$on('$locationChangeStart', function(event) {
                $scope.lowerHalf = false;
            });

            $document.on('scroll', function() {
                checkScroller();
            });

            function checkScroller() {
                var windowHeight = window.innerHeight;
                var currentScroll = $(document).scrollTop();
                if (currentScroll > (windowHeight / 2) && !$scope.lowerHalf) {
                    $scope.$apply(function() {
                        $scope.lowerHalf = true;
                    });
                } else if (currentScroll < (windowHeight / 2) && $scope.lowerHalf) {
                    $scope.$apply(function() {
                        $scope.lowerHalf = false;
                    });
                }
            }

            $scope.scrollToTop = function() {
                $window.scrollTo(0, 0);
            };

        }

    };
});
