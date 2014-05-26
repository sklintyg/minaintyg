define([ 'angular', 'app' ], function(angular, app) {
	'use strict';

	app.config([ '$routeProvider', function($routeProvider) {
		$routeProvider.when('/consent', {
			templateUrl : '/views/consent/consent-start.html',
			controller : 'ConsentCtrl',
			title : 'Ditt samtycke',
			keepInboxTabActive : false
		}).when('/lista', {
			templateUrl : '/views/list.html',
			controller : 'ListCtrl',
			title : 'Inkorgen',
		    keepInboxTabActive : false
		}).when('/arkiverade', {
			templateUrl : '/views/list-archived.html',
			controller : 'ListArchivedCtrl',
			title : 'Arkiverade intyg',
			keepInboxTabActive : false
		}).when('/omminaintyg', {
			templateUrl : '/views/om-mina-intyg.html',
			controller : 'AboutCtrl',
			title : 'Om mina intyg',
			keepInboxTabActive : false
		}).when('/hjalp', {
			templateUrl : '/views/hjalp.html',
			controller : 'HelpCtrl',
			title : 'Hjälp',
			keepInboxTabActive : false
		}).when('/fel/:errorCode', {
			templateUrl : '/views/error.html',
			controller : 'ErrorViewCtrl',
			title : 'Fel',
			keepInboxTabActive : false
		}).otherwise({
			redirectTo : '/lista'
		});
	} ]);

	return null;
});
