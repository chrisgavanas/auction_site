var itemPreviewController = router.controller('itemPreviewController', function($scope, $state, $http,$cookies, $route, AuthenticationService, itemDataService){
	$scope.item = {};
	$scope.user = {};
	
	
	$scope.item = $cookies.getObject('item');
	$scope.signedIn = false;
	if($cookies.get('signedIn') === 'yes'){
		
		$scope.user.userId = $cookies.get('userId');
		$scope.signedIn = true;
		$http.get('/api/user/'+ $scope.user.userId).then(function successCallback(response){
			$scope.user.username = response.data.username;
		}, function errorCallback(response){
			alert("error");
			
			
		});
		
		
			
			
	}





/*$http.post('/api/auctionitem', $scope.item).then (function successCallback(response){
			console.log(response);
		}, function errorCallback(response){
			console.log(response);
		});*/
	
});