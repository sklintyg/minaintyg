'use strict';

/* Directives */
listCertApp.directive("message", ['$rootScope', function ($rootScope) {
    return {
        restrict: "E",
        scope: {},
        replace: true,
        //template: '<span ng-bind-html-unsafe="resultValue"></span>',
        template: "<span>{{resultValue}}</span>",
        compile: function (element, attr, transclusionFunc) {
            return function ($scope, element, attr) {
                var lang = $rootScope.lang;
                var value = getProperty(messages[lang], attr.key);
                if (value == null) {
                    var ordlista = "";
                    if (typeof attr.noFallback === "undefined") {
                        value = getProperty(messages[$rootScope.DEFAULT_LANG], attr.key);
                    } else {
                        ordlista = " '" + lang + "'."
                    }
                    if (value == null) {
                        value = getProperty(messages[$rootScope.DEFAULT_LANG], "error.missingvalue") + " '" + attr.key + "' " + getProperty(messages[$rootScope.DEFAULT_LANG], "error.indictionary") + ordlista;
                        element.addClass("text-error");
                    }
                }
                $scope.resultValue = value;
            };

            function getProperty(resources, propertyKey) {
                var splitKey = propertyKey.split(".");
                while (splitKey.length && (resources = resources[splitKey.shift()]));
                return resources;
            };
        }
    }
}]);