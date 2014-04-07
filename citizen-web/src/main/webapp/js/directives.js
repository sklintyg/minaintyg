define([
    'angular',
    'directives/miHeader',
    'directives/miMainNavigation',
    'directives/miField',
    'directives/miSpinner',
    'directives/mvkTopBar',
    'directives/ngFocus',
], function (angular, miHeader, miMainNavigation, miField, miSpinner, mvkTopBar, ngFocus) {
    'use strict';

    var moduleName = 'intyg.app.directives';

    angular.module(moduleName, [])
        .directive('miHeader', miHeader)
        .directive('miMainNavigation', miMainNavigation)
        .directive('miField', miField)
        .directive('miSpinner', miSpinner)
        .directive('mvkTopBar', mvkTopBar)
        .directive('ngFocus', ngFocus);

    return moduleName;
});
