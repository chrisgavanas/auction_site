var validatePass = router.directive("validatePass", function(){
	return {
		require: "ngModel",
		scope: {
			otherModelValue: "=validatePass"
		},
		link: function(scope, elm, attrs, ctrl){
			
			ctrl.$validators.validatePass = function(value){
				return value == scope.otherModelValue;
			};
			
			scope.$watch("otherModelValue", function(){
				ctrl.$validate();
			});
		}
	};
});



