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

angular.module('minaintyg').controller(
    'minaintyg.AboutCtrl',
    ['$scope', 'common.messageService', 'MIConfig', 'monitoringLogService', 'MIUser',
      function($scope, messageService, MIConfig, monitoringLogService, MIUser) {
        'use strict';

        $scope.version = MIConfig.version;
        monitoringLogService.openedAbout(MIUser.personId);

        //faq's are actually arrays of faq items
        $scope.faqs = messageService.getProperty('help.faq');

        $scope.menuItems = [];

        $scope.menuItems.push({
          id: 'link-about-omminaintyg',
          link: 'omminaintyg.info',
          label: 'Om Mina intyg'
        });

        $scope.menuItems.push({
          id: 'link-help-faq',
          link: 'omminaintyg.faq',
          label: 'Vanliga frågor och svar'
        });

        $scope.menuItems.push({
          id: 'link-about-juridik',
          link: 'omminaintyg.juridik',
          label: 'Lagring och hantering av intyg'
        });

        $scope.menuItems.push({
          id: 'link-help-info',
          link: 'omminaintyg.help-info',
          label: 'Hjälp och support'
        });

        $scope.menuItems.push({
          id: 'link-about-personuppgifter',
          link: 'omminaintyg.personuppgifter-info',
          label: 'Om behandling av personuppgifter'
        });

      }]);
