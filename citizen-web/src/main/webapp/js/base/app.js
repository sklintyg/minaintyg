'use strict';

/* App Module */

var baseApp = angular.module('BaseApp', [ 'ui.bootstrap', 'modules.messages', 'directives.mi' ]);

baseApp.run([ '$rootScope','messageService', function($rootScope, messageService) {
    $rootScope.lang = 'sv';
    $rootScope.DEFAULT_LANG = 'sv';
    messageService.addResources(commonMessageResources);
} ]);
