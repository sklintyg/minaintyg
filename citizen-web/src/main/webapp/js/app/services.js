'use strict';

/* Services */

/*
 * This service gets a list of all certificates. It also caches the list to
 * avoid having to fetch the list every time a controller requests it.
 */
angular.module('services.listCertService', []);
angular.module('services.listCertService').factory('listCertService', [ '$http', '$log', '$modal', function($http, $log, $modal) {


    // cached certificates response
    var cachedList = null;

    var _selectedCertificate = null;

    function _getCertificates(callback) {
        if (cachedList != null) {
            $log.debug("returning cached response");
            callback(cachedList);
            return;
        }
        $http.get('/api/certificates').success(function(data) {
            $log.debug("populating cache");
            cachedList = data;
            callback(cachedList);
        }).error(function(data, status, headers, config) {
            $log.error("error " + status);
            //give calling code a chance to handle error 
            callback(null);
        });
    }

    function _archiveCertificate(item, callback) {
        $log.debug("Archiving " + item.id);
        $http.put('/api/certificates/' + item.id + "/archive").success(function(data) {
            callback(data, item);
        }).error(function(data, status, headers, config) {
            $log.error("error " + status);
            //give calling code a chance to handle error 
            callback(null);
        });

    }

    function _restoreCertificate(item, callback) {
        $log.debug("restoring " + item.id);
        $http.put('/api/certificates/' + item.id + "/restore").success(function(data) {
            callback(data, item);
        }).error(function(data, status, headers, config) {
            $log.error("error " + status);
            //give calling code a chance to handle error 
            callback(null);
        });

    }
    
    function _showDialog(scope, options) {
    	
    	options.dialogId = (options.dialogId == undefined) ? "id"+Math.floor(Math.random()*11) : options.dialogId;
    	options.button1text = (options.button1text == undefined) ? "common.ok" : options.button1text;
    	options.button2text = (options.button2text == undefined) ? "common.cancel" : options.button2text;
    	options.button3text = (options.button3text == undefined) ? "common.nodontask" : options.button3text;
    	options.button1id = (options.button1id == undefined) ? "button1"+Math.floor(Math.random()*11) : options.button1id;
    	options.button2id = (options.button2id == undefined) ? "button2"+Math.floor(Math.random()*11) : options.button2id;
    	options.button3id = (options.button3id == undefined) ? "button3"+Math.floor(Math.random()*11) : options.button3id;
    	options.autoClose = (options.autoClose == undefined) ? true : options.autoClose;
    	
      var msgbox = $modal.open({
      			scope: scope,
            templateUrl: '/views/partials/general-dialog.html',
            controller: DialogInstanceCtrl,
            resolve: {
            		dialogId: function() { return angular.copy(options.dialogId); },
            		titleId: function() { return angular.copy(options.titleId); },
	            	bodyTextId: function() { return angular.copy(options.bodyTextId); },
	            	button1id: function() { return angular.copy(options.button1id); },
	            	button2id: function() { return angular.copy(options.button2id); },
	            	button3id: function() { return angular.copy(options.button3id); },
	            	button1click: function() { return options.button1click; },
	            	button2click: function() { return options.button2click; },
	            	button3click: function() { return options.button3click; },
	            	button1text: function() { return angular.copy(options.button1text); },
	            	button2text: function() { return angular.copy(options.button2text); },
	            	button3text: function() { return angular.copy(options.button3text); },
	            	autoClose: function() { return angular.copy(options.autoClose); }
            }
      });

      msgbox.result.then(function(result) {
      	if (options.callback) {
      		options.callback(result)
        }
      }, function() {});
      
      return msgbox;
    }
    
    // Return public API for our service
    return {
        getCertificates : _getCertificates,
        archiveCertificate : _archiveCertificate,
        restoreCertificate : _restoreCertificate,
        selectedCertificate : _selectedCertificate,
        showDialog : _showDialog
    }
} ]);

var DialogInstanceCtrl = function ($scope, $modalInstance, dialogId, titleId, bodyTextId, button1id,button2id,button3id, button1click, button2click, button3click, button1text, button2text, button3text, autoClose) {
	
	$scope.dialogId = dialogId;
	$scope.titleId = titleId;
	$scope.bodyTextId = bodyTextId;
	$scope.button1click = function(result) { 
		button1click(); 
		if(autoClose) {
			$modalInstance.close(result)
		}
	};
	$scope.button2click = function(result) { 
		if(button2click) button2click(); 
		$modalInstance.dismiss('button2 dismiss')
	};
	$scope.button3visible = button3click != undefined;
	if($scope.button3visible){
		$scope.button3click = function(result) { button3click(); $modalInstance.dismiss('button3 dismiss') };
	}
	$scope.button1text = button1text;
	$scope.button2text = button2text;
	$scope.button3text = button3text;
	$scope.button1id = button1id;
	$scope.button2id = button2id;
	$scope.button3id = button3id;
};
