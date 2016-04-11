var welcomeController = router.controller('welcomeController', function($scope, $location){
	$scope.redirectLogin = function(){
		$location.path("/login");
	
	}
	$scope.redirectRegister = function(){
		$location.path("/register");
	}
	
	
	
});