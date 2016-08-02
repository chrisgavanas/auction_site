router.controller('userAuctionsController', function($state, $scope, $http, $cookies){
	$scope.user = {};
	$scope.signedIn = {};
	$scope.items = {};
	$scope.hasAuctions = false;
	
	if($cookies.get('signedIn') === 'yes'){
		$scope.user.userId = $cookies.get('userId');
		$scope.signedIn = true;
		var token = $cookies.get('authToken');
		$http.get('/api/user/'+ $scope.user.userId, {headers: {'authToken': token}}).then(function successCallback(response){
			getAuctionItems();
		}, function errorCallback(response){
			
			$cookies.remove('userId');
			$cookies.remove('authToken');
			$cookies.put('signedIn', 'no');
			$state.go('main.welcome');
		});
	}
	
	
	var getAuctionItems = function(){
		$http.get('api/auctionitem/user/'+ $scope.user.userId+"?status=ACTIVE").then(function successCallback(response){
			if(response.data.length != 0)
				$scope.hasAuctions = true;
			$scope.items = {};
			$scope.items = response.data;
			var i;
			
			console.log($scope.items.length);
			console.log($scope.items);
		}, function errorCallback(response){
			console.log(response);
		});
		$http.get('api/auctionitem/user/'+ $scope.user.userId+"?status=PENDING").then(function successCallback(response){
			if(response.data.length != 0)
				$scope.hasAuctions = true;
			$scope.itemsPending = {};
			$scope.itemsPending = response.data;
			var i;
			
			
		}, function errorCallback(response){
			console.log(response);
		});
		
	}
	$scope.newAuction = function(){
		$state.go("main.newAuction");
	};
});