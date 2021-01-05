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

angular.module('minaintyg').constant('minaintyg.BreadcrumbConfig', {
  inkorg: {icon: 'inbox', label: 'Inkorg', link: true},
  intyg: {
    label: 'Läsa och hantera intyg',
    link: function(stateParams) {
      'use strict';
      return {
        stateName: stateParams.type + '-view',
        stateParams: {
          certificateId: stateParams.certificateId,
          intygTypeVersion: stateParams.intygTypeVersion
        }
      };
    }
  },
  skicka: {label: 'Skicka intyg'},
  anpassa: {label: 'Anpassa intyg'},
  arkiv: {icon: 'box', label: 'Arkiverade intyg'},
  om: {icon: 'mina_intyg', label: 'Om Mina intyg'},
  hjalp: {icon: 'help-circled-1', label: 'Hjälp och support'}
});
