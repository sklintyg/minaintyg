/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
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

/**
 * Generic controller that exposes an errorCode extracted from stateParams to
 * the scope. used by the modules as a common component. Make a dependency to
 * 'controllers.util' in the app: and ha this as a controller for your error
 * page routing e.g: ... }).when('/fel/:errorCode', { templateUrl :
 * '/views/error.html', controller : 'ErrorViewCtrl' })
 *
 * and then in code route to this controller like this:
 *
 * $location.path("/fel/certnotfound");
 *
 */
angular.module('minaintyg').controller('minaintyg.ErrorViewCtrl',

    function($scope, $stateParams) {
      'use strict';

      // set a default if no errorCode is given in stateParams
      $scope.errorCode = $stateParams.errorCode || 'generic';
      $scope.pagefocus = true;
    });
