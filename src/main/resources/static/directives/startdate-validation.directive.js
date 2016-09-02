router.directive("validateDate", function(){
	return {
		require: "ngModel",
		restrict: '',
		link: function(scope, elm, attrs, ctrl){
		
			ctrl.$validators.validateDate = function(endDate){
				var date = new Date();
				console.log(date);
				if(endDate <= date)
					return false;
				else
					return true;
			};
			
			
		}
	};
});