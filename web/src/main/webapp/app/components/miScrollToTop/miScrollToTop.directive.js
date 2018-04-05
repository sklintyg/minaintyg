angular.module('minaintyg').directive('miScrollToTop', function() {
    'use strict';

    return {
        restrict: 'E',
        templateUrl: '/app/components/miScrollToTop/miScrollToTop.directive.html',
        controller: function($log, $timeout, $scope, $window, $document, scrollToTopConfig, $state) {

            $scope.lowerHalf = false;

            $scope.showComponent = true;

            $scope.$on('$stateChangeSuccess', function (event, toState, toParams, fromState, fromParams) {
                $scope.lowerHalf = false;

                // Check if directive should be used on this state (config in app.js)
                $scope.showComponent = true;
                for (var i = 0; i < scrollToTopConfig.excludedStates.length; i++){
                    var excludedState = scrollToTopConfig.excludedStates[i];
                    var currentStateName = $state.current.name;
                    if(excludedState === currentStateName){
                        $scope.showComponent = false;
                    }
                }
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
                $('body, html').animate({
                    scrollTop: 0
                }, 500);
            };

        }

    };
});
