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
 * Created by BESA on 2015-11-25.
 * Holds helper functions for actions that are needed often in specs.
 */
/*globals browser */
'use strict';

var pages = require('./../pages/pages.js');

module.exports = {

  logout: function() {
    element(by.id('mvklogoutLink')).click();
  },
  backToMvk: function() {
    element(by.id('backToMvkLink')).click();
  },

  waitForAngularTestability: function() {
    var clientScript =
        'var asyncCallback = arguments[2];\n' +
        'var rootSelector = arguments[0];\n' +
        'var attempts = arguments[1];\n' +
        'var el = document.querySelector(rootSelector);\n' +
        '  var callback = function(args) {\n' +
        '    setTimeout(function() {\n' +
        '      asyncCallback(args);\n' +
        '    }, 0);\n' +
        '  };\n' +
        '  var check = function(n) {\n' +
        '    var exception = null;\n' +
        '    try {\n' +
        '      if (window.angular && window.angular.getTestability && window.angular.getTestability(el)) {\n' +
        '        callback([true, null]);\n' +
        '        return;\n' +
        '      }\n' +
        '    } catch (e) {\n' +
        '      exception = e;\n' +
        '    }\n' +
        '    if (n < 1) {\n' +
        '      if (exception) {\n' +
        '        callback([false, exception]);\n' +
        '      } else if (window.angular) {\n' +
        '        callback([false, "angular never provided getTestability"]);\n' +
        '      } else {\n' +
        '        callback([false, "retries looking for angular exceeded"]);\n' +
        '      }\n' +
        '    } else {\n' +
        '      window.setTimeout(function() {check(n - 1);}, 500);\n' +
        '    }\n' +
        '  };\n' +
        '  check(attempts);';

    browser.driver.executeAsyncScript(clientScript, 'body', 30).then(function(result) {
      if (!result[0]) {
        throw result[1];
      }
    });
  },

  loadHttpBackendMock: function() {
    browser.addMockModule('httpBackendMock', function() {
      function loadScript(urls) {
        for (var i = 0; i < urls.length; i++) {
          var newScript = document.createElement('script');
          newScript.type = 'text/javascript';
          newScript.src = urls[i];
          newScript.async = false;
          newScript.defer = false;

          document.getElementsByTagName('head')[0].appendChild(newScript);
        }
      }

      loadScript([
        '/web/webjars/angularjs/1.4.10/angular-mocks.js'
      ]);

      angular.module('httpBackendMock', ['ngMockE2E']);
    });
  },

  waitForUrlPattern: function(pattern) {
    browser.driver.wait(function() {
      return browser.driver.getCurrentUrl().then(function(url) {
        return new RegExp(pattern).test(url);
      });
    }, 10000);
  }
};
