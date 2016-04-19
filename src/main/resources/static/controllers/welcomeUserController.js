var welcomeController = router.controller('welcomeUserController', function($scope, $http, $state, $cookies, userDataService){
	$scope.user = {};
	$scope.user.id = $cookies.get('userId');
	$http.get('/api/user/'+ $scope.user.id).then(function successCallback(response){
		$scope.user.username = response.data.username;
	}, function errorCallback(response){
		alert("error");
		
		
	});
	
	
});