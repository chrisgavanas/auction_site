router.directive('categorySelection', function() {
  
	return {
		require: 'ngModel',
		scope: {
			otherModelValue: "=categorySelection"
		},
		link: function(scope, elm, attrs, ctrl){
		
			ctrl.$validators.categorySelection = function(value){
				console.log(value);
				return value.length == 0;
			};
			
			scope.$watch("otherModelValue", function(){
				ctrl.$validate();
			});
		}
	};
});