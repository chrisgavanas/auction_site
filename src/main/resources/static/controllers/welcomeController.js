var welcomeController = router.controller('welcomeController', function($scope, $state){

	
	$scope.redirectLogin = function(){
		$state.go("login");
	
	}
	$scope.redirectRegister = function(){
		$state.go("register");
	}
	
	
	
});