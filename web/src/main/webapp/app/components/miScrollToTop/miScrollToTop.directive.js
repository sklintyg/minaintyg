angular.module('minaintyg').directive('miScrollToTop',
    function ($rootScope, $location) {
        'use strict';

        return {
            restrict: 'E',
            controller: function ($scope, $window, $document) {

                $scope.lowerHalf = false;

                $scope.$on('$locationChangeStart', function (event) {
                    $scope.lowerHalf = false;
                });

                $document.on('scroll', function () {
                    checkScroller();
                });

                function checkScroller() {
                    var scrollbarPercentage = getScrollbarPercentage();
                    if (
                        (scrollbarPercentage > 0.5 && !$scope.lowerHalf) ||
                        (scrollbarPercentage <= 0.5 && $scope.lowerHalf)
                    ) {
                        $scope.$apply(function () {
                            $scope.lowerHalf = !$scope.lowerHalf;
                        });
                    }
                }

                function getScrollbarPercentage() {
                    var wintop = $(window).scrollTop(), docheight = $(document).height(), winheight = $(window).height();
                    var percentage = (wintop / (docheight - winheight));
                    return percentage;
                }

                $scope.scrollToTop = function () {
                    $window.scrollTo(0, 0);
                }

            },
            templateUrl: '/app/components/miScrollToTop/miScrollToTop.directive.html'
        };
    });
