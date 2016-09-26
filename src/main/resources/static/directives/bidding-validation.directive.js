router.directive("validateBid", function(){
	return {
		require: "ngModel",
		scope: {
			minBid: "=validateBid"
		},
		link: function(scope, elm, attrs, ctrl){
		
			ctrl.$validators.validateBid = function(newBid){
			
				if(scope.minBid >= 0.01 && scope.minBid <= 0.99)
					if(newBid - scope.minBid >= 0.05)
						return true;
					else
						return false;
				if(scope.minBid >= 1.00 && scope.minBid <= 4.99)
					if(newBid - scope.minBid >= 0.25)
						return true;
					else
						return false;
				if(scope.minBid >= 5.00 && scope.minBid <= 24.99)
					if(newBid - scope.minBid >= 0.5)
						return true;
					else
						return false;
				if(scope.minBid >= 25.00 && scope.minBid <= 99.99)
					if(newBid - scope.minBid >= 1.00)
						return true;
					else
						return false;
				if(scope.minBid >= 100.00 && scope.minBid <= 249.99)
					if(newBid - scope.minBid >= 2.50)
						return true;
					else
						return false;
				if(scope.minBid >= 250.00)
					if(newBid - scope.minBid >= 5.0)
						return true;
					else
						return false;
			};
			
			scope.$watch("minBid", function(){
				ctrl.$validate();
			});
		}
	};
});

