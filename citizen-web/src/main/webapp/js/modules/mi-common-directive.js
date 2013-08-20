/** Directives that can be used anywhere */

angular.module('directives.mi').directive("miSpinner", ['$rootScope', function($rootScope) {
    return {
        restrict : "A",
	    transclude : true,
        replace : true,
        scope : {
          label: "@",
	      showSpinner: "=",
	      showContent: "="
        },
        template :
			'<div>'
		   +'  <div ng-show="showSpinner" style="text-align: center; padding: 20px;">'
	       +'    <img aria-labelledby="loading-message" src="/img/ajax-loader.gif" style="text-align: center;" />'
	       +'    <p id="loading-message" style="text-align: center; color: #64ABC0; margin-top: 20px;">'
	       +'      <strong><span message key="{{ label }}"></span></strong>'
	       +'    </p>'
	       +'  </div>'
	       +'  <div ng-show="showContent">'
		   +'    <div ng-transclude></div>'
		   +'  </div>'
		   +'</div>'
    }
} ]);

angular.module('directives.mi').directive('ngFocus',function($parse,$timeout){
  return function(scope,element,attrs){
    var ngFocusGet = $parse(attrs.ngFocus);
    var ngFocusSet = ngFocusGet.assign;
    if (!ngFocusSet) {
      throw Error("Non assignable expression");
    }

    var digesting = false;

    var abortFocusing = false;
    var unwatch = scope.$watch(attrs.ngFocus,function(newVal){
      if(newVal){
        $timeout(function(){
          element[0].focus();
        },0)
      }
      else {
        $timeout(function(){
          element[0].blur();
        },0);
      }
    });


    element.bind("blur",function(){

      if(abortFocusing) return;

      $timeout(function(){
        ngFocusSet(scope,false);
      },0);

    });


    var timerStarted = false;
    var focusCount = 0;

    function startTimer(){
      $timeout(function(){
        timerStarted = false;
        if(focusCount > 3){
          unwatch();
          abortFocusing = true;
          throw new Error("Aborting : ngFocus cannot be assigned to the same variable with multiple elements");
        }
      },200);
    }

    element.bind("focus",function(){

      if(abortFocusing) return;

      if(!timerStarted){
        timerStarted = true;
        focusCount = 0;
        startTimer();
      }
      focusCount++;

      $timeout(function(){
        ngFocusSet(scope,true);
      },0);

    });
  };
});