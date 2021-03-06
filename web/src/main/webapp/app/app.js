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

(function() {
  'use strict';

  var app = angular.module('minaintyg',
      ['ui.bootstrap', 'ngCookies', 'ui.router', 'ngSanitize', 'ngAnimate', 'common', 'smoothScroll']);

  // before we do anything...we need all modules
  var moduleArray = [];

  app.value('scrollToTopConfig', {
    excludedStates: ['index']
  });

  app.value('networkConfig', {
    defaultTimeout: 30000
    // test: 1000
  });

  //http://stackoverflow.com/a/29153678/411284
  // This method method works BUT runs on back/forward buttons too
  app.run(function($rootScope, $state, $stateParams, $anchorScroll, $uibModalStack) {
    $rootScope.$on('$stateChangeStart', function(event, toState, toParams, fromState, fromParams) {
      $anchorScroll();
      // INTYG-4465: prevent state change when user press 'backwards' if modal is open, but close modal.
      if ($uibModalStack.getTop()) {
        event.preventDefault();
        $uibModalStack.dismissAll();
        // Restore original state in order to make it work for DJUPINTEGRATION and avoid messing up the history.
        $state.go(fromState, fromParams);
      }
    });
  });

  function getModules() {
    return $.get('/appconfig/api/map').then(function(data) {
      return data;
    });
  }

  function getMIConfig() {
    return $.get('/appconfig/api/app').then(function(configResponse) {
      app.constant('MIConfig', configResponse);
      return configResponse;
    });
  }

  function getMIUser() {
    return $.get('/api/certificates/user').then(function(userResponse) {
      app.constant('MIUser', userResponse);
      return userResponse;
    }, function(error) {
      //When boot-strapping on landing page, a 403 is expected.
      app.constant('MIUser', {});
      return null;
    });
  }

  app.config(['$logProvider', '$uiViewScrollProvider', '$httpProvider', 'common.http403ResponseInterceptorProvider', '$uibTooltipProvider',
    '$windowProvider', '$locationProvider', '$compileProvider',
    function($logProvider, $uiViewScrollProvider, $httpProvider, http403ResponseInterceptorProvider, $uibTooltipProvider,
        $windowProvider, $locationProvider, $compileProvider) {

      // Set in boot-app.jsp
      var debugMode = angular.isDefined(MINA_INTYG_DEBUG_MODE) ? MINA_INTYG_DEBUG_MODE : true; //jshint ignore:line

      // START TEMP 1.6 migration compatibility flags
      $compileProvider.preAssignBindingsEnabled(true);
      $locationProvider.hashPrefix('');
      // END

      // Disable angular debug info.
      $compileProvider.debugInfoEnabled(debugMode);

      // Disable comment and css directives
      $compileProvider.commentDirectivesEnabled(false);
      $compileProvider.cssClassDirectivesEnabled(false);

      $logProvider.debugEnabled(debugMode);
      $uiViewScrollProvider.useAnchorScroll();

      // Add cache buster interceptor
      $httpProvider.interceptors.push('common.httpRequestInterceptorCacheBuster');

      // Configure 403 interceptor provider
      http403ResponseInterceptorProvider.setRedirectUrl('/error.jsp?reason=denied');
      $httpProvider.interceptors.push('common.http403ResponseInterceptor');

      // Configure default triggers for tooltipProvider to disable triggers for
      // devices supporting touch events (caused by INTYG-4301). These defaults can be overridden by explict
      // directive attribute 'popover-trigger', but then it will be enabled for all devices.
      if ('ontouchstart' in $windowProvider.$get()) {
        $uibTooltipProvider.options({
          trigger: 'dontTrigger'
        });
      } else {
        $uibTooltipProvider.options({
          trigger: 'mouseenter'
        });
      }

    }]);

  app.config(function($provide) {
    $provide.decorator('$$rAF', function($delegate, $window, $timeout, $browser) {
      var requestAnimationFrame = $window.requestAnimationFrame || $window.webkitRequestAnimationFrame;

      var cancelAnimationFrame = $window.cancelAnimationFrame || $window.webkitCancelAnimationFrame ||
          $window.webkitCancelRequestAnimationFrame;

      var rafSupported = !!requestAnimationFrame;

      var raf = rafSupported ? function(fn) {
        var fn2 = function() {
          $browser.$$completeOutstandingRequest(fn);
        };
        $browser.$$incOutstandingRequestCount();
        var id = requestAnimationFrame(fn2);
        return function() {
          $browser.$$completeOutstandingRequest(angular.noop);
          cancelAnimationFrame(id);
        };
      } : function(fn) {
        var fn2 = function() {
          $browser.$$completeOutstandingRequest(fn);
        };
        $browser.$$incOutstandingRequestCount();
        var timer = $timeout(fn2, 16.66, false); // 1000 / 60 = 16.666
        return function() {
          $browser.$$completeOutstandingRequest(angular.noop);
          $timeout.cancel(timer);
        };
      };

      raf.supported = rafSupported;

      return raf;
    });
  });

  app.run(['$log', '$rootScope', '$state', '$window', 'common.moduleService', 'common.messageService',
    'common.dynamicLinkService', 'common.recipientsFactory',
    'minaintyg.messages', 'MIConfig', 'monitoringLogService',
    function($log, $rootScope, $state, $window, moduleService, messageService, dynamicLinkService,
        recipientsFactory, miMessages, MIConfig, monitoringLogService) {
      $rootScope.lang = 'sv';
      $rootScope.DEFAULT_LANG = 'sv';
      $rootScope.page_title = 'Titel'; // jshint ignore:line
      $rootScope.banners = MIConfig.banners;

      messageService.addResources(miMessages);
      messageService.addLinks(MIConfig.links);
      dynamicLinkService.addLinks(MIConfig.links);
      moduleService.setModules(moduleArray);

      //Initialize commmon recipientsFactory with known recipients
      recipientsFactory.setRecipients(MIConfig.knownRecipients);
      //Log startup screen resolution (server also uses userAgent header to detect and log other browser info)
      monitoringLogService.screenResolution($window.innerWidth, $window.innerHeight);

      $rootScope.$on('$stateChangeSuccess', function(event, toState/*, toParams, fromState, fromParams*/) {
        if (toState.data.keepInboxTabActive === false) {
          $rootScope.keepInboxTab = false;
        }
        if (toState.data.keepInboxTabActive === true) {
          $rootScope.keepInboxTab = true;
        }
        $rootScope.page_title = toState.data.title + ' | Mina intyg'; // jshint ignore:line
      });

      $rootScope.$on('$stateChangeError', function(event, toState/*, toParams, fromState, fromParams, error*/) {
        $log.log('$stateChangeError');
        $log.log(toState);
      });

      $window.onbeforeunload = function() {
        // Must be done async
        // https://www.chromestatus.com/feature/4664843055398912
        $.get('/api/certificates/onbeforeunload');
      };

    }]);

  // First load config, then user and modules
  getMIConfig().then(
      function(miConfig) {
        getMIUser().always(
            function(miUser) {
              getModules().then(
                  function(modules) {
                    //Load mi common css
                    loadCssFromUrl('/web/webjars/common/minaintyg/mi-common.css?' + miConfig.buildNumber);

                    var modulePromises = [];

                    if (miConfig.useMinifiedJavascript) {
                      modulePromises.push(loadScriptFromUrl('/web/webjars/common/minaintyg/module.min.js?' +
                          miConfig.buildNumber));
                      // All dependencies in module-deps.json are included in module.min.js
                      // All dependencies in app-deps.json are included in app.min.js

                    } else {
                      modulePromises.push(loadScriptFromUrl('/web/webjars/common/minaintyg/module.js'));
                      modulePromises.push($.get('/web/webjars/common/minaintyg/module-deps.json'));
                      modulePromises.push($.get('/app/app-deps.json'));

                      // Prevent jQuery from appending cache buster to the url to make it easier to debug.
                      $.ajaxSetup({
                        cache: true
                      });
                    }

                    angular.forEach(modules, function(module) {
                      // Add module to array as is
                      moduleArray.push(module);

                      if (module.cssPath && module.cssPath !== '') {
                        loadCssFromUrl(module.cssPath + '?' + miConfig.buildNumber);
                      }

                      if (miConfig.useMinifiedJavascript) {
                        modulePromises.push(
                            loadScriptFromUrl(module.scriptPath + '.min.js?' + miConfig.buildNumber));
                        // All dependencies for the modules are included in module.min.js
                      } else {
                        modulePromises.push(loadScriptFromUrl(module.scriptPath + '.js'));
                        modulePromises.push($.get(module.dependencyDefinitionPath));
                      }
                    });

                    // Wait for all modules and module dependency definitions to load.
                    $.when.apply(this, modulePromises).then(
                        function() {
                          var dependencyPromises = [];

                          // Only needed for development since all dependencies are included in other files.
                          if (!miConfig.useMinifiedJavascript) {
                            angular.forEach(arguments, function(data) {
                              if (data !== undefined && data[0] instanceof Array) {
                                angular.forEach(data[0], function(depdendency) {
                                  dependencyPromises.push(loadScriptFromUrl(depdendency));
                                });
                              }
                            });
                          }

                          // Wait for all dependencies to load (for production dependencies are empty which is resolved immediately)
                          $.when.apply(this, dependencyPromises).then(
                              function() {
                                angular.element().ready(
                                    function() {

                                      var allModules = ['minaintyg', 'common'].concat(
                                          Array.prototype.slice
                                          .call(
                                              Array.prototype.map.call(moduleArray, function(module) {
                                                return module.id;
                                              }), 0));

                                      // Everything is loaded, bootstrap the application with all dependencies.
                                      document.documentElement.setAttribute('ng-app', 'minaintyg');
                                      angular.bootstrap(document, allModules);
                                    });
                              }).fail(function(error) {
                            if (window.console) {
                              console.log(error);
                            }
                          });
                        }).fail(function(error) {
                      if (window.console) {
                        console.log(error);
                      }
                    });

                  });
            });
      });

  function loadCssFromUrl(url) {
    var link = document.createElement('link');
    link.type = 'text/css';
    link.rel = 'stylesheet';
    link.href = url;
    document.getElementsByTagName('head')[0].appendChild(link);
  }

  function loadScriptFromUrl(url) {
    var result = $.Deferred();
    var script = document.createElement('script');
    script.async = 'async';
    script.type = 'text/javascript';
    script.src = url;
    script.onload = script.onreadystatechange = function(_, isAbort) {
      if (!script.readyState || /loaded|complete/.test(script.readyState)) {
        if (isAbort) {
          result.reject();
        } else {
          result.resolve();
        }
      }
    };
    script.onerror = function() {
      result.reject();
    };
    document.getElementsByTagName('head')[0].appendChild(script);
    return result.promise();
  }

}());
