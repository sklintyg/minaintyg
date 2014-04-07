define([
    'angular',
    'controllers/ListCtrl',
    'controllers/ListArchivedCtrl',
    'controllers/AboutCtrl',
    'controllers/HelpCtrl',
    'controllers/ErrorViewCtrl'
], function (angular, ListCtrl, ListArchivedCtrl, AboutCtrl, HelpCtrl, ErrorViewCtrl) {
    'use strict';

    var moduleName = 'intyg.app.controllers';

    angular.module(moduleName, [])
        .controller('ListCtrl', ListCtrl)
        .controller('ListArchivedCtrl', ListArchivedCtrl)
        .controller('AboutCtrl', AboutCtrl)
        .controller('HelpCtrl', HelpCtrl)
        .controller('ErrorViewCtrl', ErrorViewCtrl);

    return moduleName;
});
