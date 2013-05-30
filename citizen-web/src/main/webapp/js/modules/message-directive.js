'use strict';

/**
 * message directive for externalizing text resources.
 *  
 * All resourcekeys are expected to be defined in lowercase and available in a 
 * global js object named "messages"
 * Also supports dynamic key values such as key="status.{{scopedvalue}}"
 * 
 * Usage: <message key="some.resource.key" [fallback="defaulttextifnokeyfound"]/>
 */
angular.module('directives.message', []).directive("message", ['$rootScope', function($rootScope) {
    return {
        restrict : "E",
        scope : true,
        replace : true,
        template : "<span>{{resultValue}}</span>",
        link : function($scope, element, attr) {
            var lang = $rootScope.lang;
            // drilldown through all my.message.key structure
            function getProperty(resources, propertyKey) {
                var splitKey = propertyKey.split(".");
                while (splitKey.length && (resources = resources[splitKey.shift()]))
                    ;
                return resources;
            }

            // observe changes to interpolated attribute
            attr.$observe('key', function(interpolatedKey) {
                var normalizedKey = angular.lowercase(interpolatedKey);
                var value = getProperty(messages[$rootScope.lang], normalizedKey);
                if (typeof value === "undefined") {
                    // use fallback attr value if defined
                    value = (typeof attr.fallback === "undefined") ? "[Missing '" + normalizedKey + "']" : attr.fallback;
                }
                // now we have some value to display..
                $scope.resultValue = value;

            });
        }

    }
} ]);