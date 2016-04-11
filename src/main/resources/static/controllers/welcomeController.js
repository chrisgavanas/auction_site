var welcomeController = router.controller('welcomeController', function($scope, $location){
	$scope.redirect = function(myPath){
		$location.path(myPath);
	
	}
	
	
	
	
});