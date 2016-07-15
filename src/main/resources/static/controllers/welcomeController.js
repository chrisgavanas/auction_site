var welcomeController = router.controller('welcomeController', function($scope, $rootScope, $state, $http,$cookies, $route,  AuthenticationService){
	var data = new FormData();
	console.log(data);
	$scope.user = {};
	$scope.signedIn = {};
	var token = $cookies.get('authToken');
	
	if($cookies.get('signedIn') === 'yes'){
		$scope.user.userId = $cookies.get('userId');
		$scope.signedIn = true;
		$http.get('/api/user/'+ $scope.user.userId, {headers: {'authToken': token}}).then(function successCallback(response){
			$scope.user.username = response.data.username;
		}, function errorCallback(response){
			alert("error");
			
			
		});
		
		
		
	}else
		$scope.signedIn = false;
	 
	
	$scope.redirectRegister = function(){
		$state.go("register");
	}
	
	$scope.logout = function(){
		$cookies.remove('userId');
		$cookies.remove('authToken');
		$cookies.put('signedIn', 'no');
		$state.go($state.current, {}, {reload: true}); 
	}
	
	$scope.myProfile = function(){
		$state.go("profile");
	}
	
	$scope.openModal = function(){
		$rootScope.$emit("CallParentMethod", {});
	}
});