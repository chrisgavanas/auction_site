var validateEmail = router.directive('pass', function() {
	console.log("mphka");
  var PASS_REGEXP = /^(((?=.*[a-z])(?=.*[A-Z]))|((?=.*[a-z])(?=.*[0-9]))|((?=.*[A-Z])(?=.*[0-9])))(?=.{6,})/;
  return {
    require: 'ngModel',
    restrict: '',
    link: function(scope, elm, attrs, ngModel) {
    	
      if (ngModel.$validators) {
    	  	ngModel.$validators.pass = function(modelValue) {
          return ngModel.$isEmpty(modelValue) || PASS_REGEXP.test(modelValue);
        };
      }
    }
  };
});