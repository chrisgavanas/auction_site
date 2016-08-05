router.controller('userAuctionsPendingController', function($state, $scope, $http, $cookies, AuthenticationService){
	$scope.user = {};
	$scope.signedIn = {};
	$scope.itemsPending = {};
	$scope.hasAuctions = false;
	
	
	if($cookies.get('signedIn') === 'yes'){
		$scope.user.userId = $cookies.get('userId');
		$scope.signedIn = true;
	
		var token = $cookies.get('authToken');
		
		AuthenticationService.getUser($scope.user.userId, token).then(function(response){
			getAuctionItems();

			
		}, function errorCallback(response){
			$cookies.remove('userId');
			$cookies.remove('authToken');
			$cookies.put('signedIn', 'no');
			$state.go('main.welcome');
		});
	}
	var getAuctionItems = function(){
		$http.get('api/auctionitem/user/'+ $scope.user.userId+"?status=PENDING").then(function successCallback(response){
			if(response.data.length != 0)
				$scope.hasAuctions = true;
			$scope.itemsPending = {};
			$scope.itemsPending = response.data;
			var i;
			
			console.log($scope.itemsPending.length);
			console.log($scope.itemsPending);
		}, function errorCallback(response){
			console.log(response);
		});
		
		
	}
})