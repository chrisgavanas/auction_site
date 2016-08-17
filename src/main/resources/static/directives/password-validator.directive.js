router.directive("validatePass", function(){
	return {
		require: "ngModel",
		scope: {
			otherModelValue: "=validatePass"
		},
		link: function(scope, elm, attrs, ctrl){
		
			ctrl.$validators.validatePass = function(value){
				console.log(value + " "+ scope.otherModelValue);
				return value == scope.otherModelValue;
			};
			
			scope.$watch("otherModelValue", function(){
				ctrl.$validate();
			});
		}
	};
});


/*router.directive('confirmPwd', function($interpolate, $parse) {
	  return {
	    require: 'ngModel',
	    link: function(scope, elem, attr, ngModelCtrl) {

	      var pwdToMatch = $parse(attr.confirmPwd);
	      var pwdFn = $interpolate(attr.confirmPwd)(scope);

	      scope.$watch(pwdFn, function(newVal) {
	    	  console.log(ngModelCtrl.$viewValue);
	          ngModelCtrl.$setValidity('password', ngModelCtrl.$viewValue == newVal);
	      })

	      ngModelCtrl.$validators.password = function(modelValue, viewValue) {
	        var value = modelValue || viewValue;
	        console.log(value + " "+modelValue+ " "+viewValue);
	        console.log(pwdToMatch(scope));
	        return value == pwdToMatch(scope);
	      };

	    }
	  }
	});

*/