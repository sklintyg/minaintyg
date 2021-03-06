/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

angular.module('minaintyg').directive('miScrollToTop', function() {
  'use strict';

  return {
    restrict: 'E',
    templateUrl: '/app/components/miScrollToTop/miScrollToTop.directive.html',
    controller: function($log, $timeout, $scope, $window, $document, scrollToTopConfig, $state) {

      $scope.lowerHalf = false;

      $scope.showComponent = true;

      $scope.$on('$stateChangeSuccess', function(event, toState, toParams, fromState, fromParams) {
        $scope.lowerHalf = false;

        // Check if directive should be used on this state (config in app.js)
        $scope.showComponent = true;
        for (var i = 0; i < scrollToTopConfig.excludedStates.length; i++) {
          var excludedState = scrollToTopConfig.excludedStates[i];
          var currentStateName = $state.current.name;
          if (excludedState === currentStateName) {
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
