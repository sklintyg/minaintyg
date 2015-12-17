/*
 * Copyright (C) 2015 Inera AB (http://www.inera.se)
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

angular.module('minaintyg').controller('minaintyg.HelpCtrl',['$scope','common.messageService',
    function($scope, messageService) {
        'use strict';

        $scope.pagefocus = true;

        // Hold left side navigation state
        $scope.visibility = {
            helpdescription: true,
            helpfaq: false
        };

        // Hold focus state for sub pages
        $scope.subpagefocus = {
            helpdescription: false,
            helpfaq: false
        };

        $scope.navigateTo = function(section) {
            angular.forEach($scope.visibility, function(value, key) {
                $scope.visibility[key] = (key === section);
                $scope.subpagefocus[key] = (key === section);
            });
        };

        $scope.faqs = messageService.getProperty('help.faq');
    }]);
