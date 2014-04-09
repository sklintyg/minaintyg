define([ 'angular', 'app' ], function(angular, app) {
	'use strict';

	app.config([ '$routeProvider', function($routeProvider) {
		$routeProvider.when('/start', {
			templateUrl : '/views/consent/consent-start.html',
			controller : 'ConsentCtrl',
			title : 'Ditt samtycke'
		}).when('/lista', {
			templateUrl : '/views/list.html',
			controller : 'ListCtrl',
			title : 'Inkorgen'
		}).when('/arkiverade', {
			templateUrl : '/views/list-archived.html',
			controller : 'ListArchivedCtrl',
			title : 'Arkiverade intyg'
		}).when('/omminaintyg', {
			templateUrl : '/views/om-mina-intyg.html',
			controller : 'AboutCtrl',
			title : 'Om mina intyg'
		}).when('/hjalp', {
			templateUrl : '/views/hjalp.html',
			controller : 'HelpCtrl',
			title : 'Hjälp'
		}).when('/fel/:errorCode', {
			templateUrl : '/views/error.html',
			controller : 'ErrorViewCtrl',
			title : 'Fel'
		}).otherwise({
			redirectTo : '/lista'
		});
	} ]);

	return null;
});
