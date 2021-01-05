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
angular.module('minaintyg').directive('miServiceBanner', function() {
  'use strict';

  return {
    restrict: 'E',
    scope: {},
    templateUrl: '/app/components/miServiceBanner/miServiceBanner.directive.html',
    controller: function($scope, MIConfig) {

      function getSeverity(priority) {
        switch(priority) {
        case 'HOG':
          return 'danger';
        case 'MEDEL':
          return 'warning';
        case 'LAG':
          return 'info';
        }
      }

      var bannersShown = [];
      var i = 0;

      angular.forEach(MIConfig.banners, function(banner) {
        bannersShown.push({
          id: 'serviceBanner' + i++,
          severity: getSeverity(banner.priority),
          text: addExternalIcon(banner.message)
        });
      });

      $scope.bannersShown = bannersShown;
    }
  };

  function addExternalIcon(text) {
    return text.replace(new RegExp('</a>', 'g'), '<i aria-hidden="true" class="icon icon-external_link"></i></a>');
  }
});
