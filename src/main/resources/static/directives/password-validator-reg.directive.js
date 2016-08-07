var validateEmail = router.directive('validatePasswordreg', function() {
  var PASS_REGEXP = /^(((?=.*[a-z])(?=.*[A-Z]))|((?=.*[a-z])(?=.*[0-9]))|((?=.*[A-Z])(?=.*[0-9])))(?=.{6,})/;
  return {
    require: 'ngModel',
    restrict: '',
    link: function(scope, elm, attrs, ctrl) {
      if (ctrl && ctrl.$validators.password) {
        ctrl.$validators.password = function(modelValue) {
          return ctrl.$isEmpty(modelValue) || PASS_REGEXP.test(modelValue);
        };
      }
    }
  };
});