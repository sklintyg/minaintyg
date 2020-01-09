/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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

describe('miBreadcrumb', function() {
  'use strict';

  var $scope;
  var element;
  var $state;
  var $window;
  var breadcrumbConfig;

  // Load the webcert module and mock away everything that is not necessary.
  beforeEach(angular.mock.module('htmlTemplates'));

  beforeEach(angular.mock.module('minaintyg'));

  beforeEach(angular.mock.inject(['$window', '$state', 'minaintyg.BreadcrumbConfig',
    function(_$window_, _$state_, _breadcrumbConfig_) {
      $window = _$window_;
      $state = _$state_;
      breadcrumbConfig = _breadcrumbConfig_;
    }]));

  describe('enableLinks', function() {

    describe('not provided', function() {

      beforeEach(angular.mock.inject(['$compile', '$rootScope',
        function($compile, $rootScope) {
          $scope = $rootScope.$new();
          element = $compile('<mi-breadcrumb></mi-breadcrumb>')($scope);

          $rootScope.$digest();
          $scope = element.isolateScope();
        }]));

      it('should default to enabled', function() {
        $scope.$apply();
        expect($scope.enableLinks).toBe(true);
      });

    });

    describe('provided false', function() {

      beforeEach(angular.mock.inject(['$compile', '$rootScope',
        function($compile, $rootScope) {
          $scope = $rootScope.$new();
          element = $compile('<mi-breadcrumb></mi-breadcrumb>')($scope);
          $scope.enableLinks = false;
          $rootScope.$digest();
          $scope = element.isolateScope();
        }]));

      describe('link', function() {
        it('should call go with stateParams null even if a link is provided due to enableLinks being disabled',
            function() {

              spyOn($state, 'go').and.callFake(function() {
              });

              breadcrumbConfig.test = {icon: 'iconName', label: 'Test'};

              breadcrumbConfig.test2 = {
                label: 'Test2',
                link: function(stateParams) {
                  return {stateParams: null};
                }
              };
              breadcrumbConfig.test3 = {label: 'Test3'};

              $state.current.data = {
                breadcrumb: ['test', 'test2', 'test3']
              };

              $scope.$apply();

              var step = {stateName: 'test2', stateConfig: breadcrumbConfig.test2};
              $scope.link(step);

              expect($state.go).toHaveBeenCalledWith('test2', null);

            });
      });

    });

    describe('provided', function() {

      beforeEach(angular.mock.inject(['$compile', '$rootScope',
        function($compile, $rootScope) {
          $scope = $rootScope.$new();
          $scope.enableLinks = true;
          element = $compile('<mi-breadcrumb enable-links="enableLinks"></mi-breadcrumb>')($scope);

          $rootScope.$digest();
          $scope = element.isolateScope();
        }]));

      it('should keep its value', function() {
        $scope.$apply();
        expect($scope.enableLinks).toBe(true);

        $scope.enableLinks = true;
        $scope.$apply();
        expect($scope.enableLinks).toBe(true);
      });

      it('should have no steps data if no breadcrumb data is available', function() {

        $state.current.data = null;
        $scope.$apply();
        expect($scope.steps).toBeNull();

      });

      it('should have steps data if breadcrumb data is available', function() {

        $state.current.data = {
          breadcrumb: ['test', 'test2', 'test3']
        };
        $scope.$apply();
        expect($scope.steps.length).toBe(3);

      });

      it('should have icon if specified for first part of the path', function() {

        breadcrumbConfig.test = {icon: 'iconName', label: 'Test'};

        $state.current.data = {
          breadcrumb: ['test', 'test2', 'test3']
        };
        $scope.$apply();
        expect($scope.stateIconName).toBe('iconName');

      });

      describe('back', function() {

        it('should call window.history.back if backState is specified as history-back', function() {

          spyOn($window.history, 'back').and.stub();

          breadcrumbConfig.test = {icon: 'iconName', label: 'Test'};
          breadcrumbConfig.test2 = {label: 'Test2'};
          breadcrumbConfig.test3 = {label: 'Test3'};

          $state.current.data = {
            breadcrumb: ['test', 'test2', 'test3'],
            backState: 'history-back'
          };

          $scope.$apply();

          $scope.back();

          expect($window.history.back).toHaveBeenCalled();

        });

        it('should call $state.go with specified state if backState is provided', function() {

          spyOn($state, 'go').and.callFake(function() {
          });

          breadcrumbConfig.test = {icon: 'iconName', label: 'Test'};
          breadcrumbConfig.test2 = {label: 'Test2'};
          breadcrumbConfig.test3 = {label: 'Test3'};

          $state.current.data = {
            breadcrumb: ['test', 'test2', 'test3'],
            backState: 'test'
          };

          $scope.$apply();

          $scope.back();

          expect($state.go).toHaveBeenCalledWith('test');

        });

        it('should call $state.go with previous state if backState is not provided', function() {

          spyOn($state, 'go').and.callFake(function() {
          });

          breadcrumbConfig.test = {icon: 'iconName', label: 'Test'};
          breadcrumbConfig.test2 = {label: 'Test2'};
          breadcrumbConfig.test3 = {label: 'Test3'};

          $state.current.data = {
            breadcrumb: ['test', 'test2', 'test3']
          };

          $scope.$apply();

          $scope.back();

          expect($state.go).toHaveBeenCalledWith('test2');

        });

      });

      describe('link', function() {

        it('should call go with state from stateName', function() {

          spyOn($state, 'go').and.callFake(function() {
          });

          breadcrumbConfig.test = {icon: 'iconName', label: 'Test'};
          breadcrumbConfig.test2 = {label: 'Test2'};
          breadcrumbConfig.test3 = {label: 'Test3'};

          $state.current.data = {
            breadcrumb: ['test', 'test2', 'test3']
          };

          $scope.$apply();

          var step = {stateName: 'test2', stateConfig: breadcrumbConfig.test2};
          $scope.link(step);

          expect($state.go).toHaveBeenCalledWith('test2', undefined);

        });

        it('should call go with stateParams from link funktion', function() {

          spyOn($state, 'go').and.callFake(function() {
          });

          breadcrumbConfig.test = {icon: 'iconName', label: 'Test'};

          var customStateParams = {
            stateName: 'customState',
            stateParams: 'customParams'
          };
          breadcrumbConfig.test2 = {
            label: 'Test2',
            link: function(stateParams) {
              return customStateParams;
            }
          };
          breadcrumbConfig.test3 = {label: 'Test3'};

          $state.current.data = {
            breadcrumb: ['test', 'test2', 'test3']
          };

          $scope.$apply();

          var step = {stateName: 'test2', stateConfig: breadcrumbConfig.test2};
          $scope.link(step);

          expect($state.go).toHaveBeenCalledWith('customState', customStateParams.stateParams);

        });

      });

    });

  });

})
;
