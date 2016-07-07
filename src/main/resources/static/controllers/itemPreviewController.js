var itemPreviewController = router.controller('itemPreviewController', function($scope, $state, $http,$cookies, $route, AuthenticationService, itemDataService){
	$scope.item = {};
	$scope.user = {};
	
	
	$scope.item = $cookies.getObject('item');
	$scope.signedIn = false;
	if($cookies.get('signedIn') === 'yes'){
		
		$scope.user.userId = $cookies.get('userId');
		$scope.signedIn = true;
		var token = $cookies.get('authToken');
		$http.get('/api/user/'+ $scope.user.userId, {headers: {'authToken': token}}).then(function successCallback(response){
			$scope.user.username = response.data.username;
		}, function errorCallback(response){
			alert("error");
			
			
		});
		
		
			
			
	}
	$scope.complete = function(){
		console.log($scope.item);
		$http.post('/api/auctionitem', $scope.item).then (function successCallback(response){
			console.log(response);
		}, function errorCallback(response){
			console.log(response);
		});
	}

	$scope.goBack = function(){
		$state.go("newAuction");
	
	}




	
});