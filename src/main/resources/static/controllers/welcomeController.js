var welcomeController = router.controller('welcomeController', function($scope, $state, $http,$cookies, $route){
	$scope.user = {};
	$scope.signedIn = {};
	if($cookies.get('signedIn') === 'yes'){
		$scope.user.userId = $cookies.get('userId');
		$scope.signedIn = true;
		$http.get('/api/user/'+ $scope.user.userId).then(function successCallback(response){
			$scope.user.username = response.data.username;
		}, function errorCallback(response){
			alert("error");
			
			
		});
		
		
		
	}else
		$scope.signedIn = false;
	 
	$scope.redirectLogin = function(){
		$state.go("login");
	
	}
	$scope.redirectRegister = function(){
		$state.go("register");
	}
	
	$scope.logout = function(){
		$cookies.remove('userId');
		$cookies.remove('authToken');
		$cookies.put('signedIn', 'no');
		$state.go($state.current, {}, {reload: true}); 
	}
	
});