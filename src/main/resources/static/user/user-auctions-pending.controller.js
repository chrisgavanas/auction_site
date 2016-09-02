router.controller('userAuctionsPendingController', function($state, $scope, $http, $cookies, AuthenticationService, AuctionItemService){
	$scope.user = {};
	$scope.itemsPending = {};
	$scope.hasAuctions = false;
	$scope.pageCounter = 1;
	$scope.user.userId = $cookies.get('userId');
	var token = $cookies.get('authToken');
	

	AuctionItemService.getAuctionItemsOfUserByStatus(token, $scope.user.userId, "PENDING", 1, 10)
						.then( function(response){
							if(response.data.length != 0)
									$scope.hasAuctions = true;
									$scope.itemsPending = {};
									$scope.itemsPending = response.data;
									var i;
									for(i = 0; i < $scope.itemsPending.length; i++){
										if($scope.itemsPending[i].buyout == null)
											$scope.itemsPending[i].hasBuyout = false;
										else
											$scope.itemsPending[i].hasBuyout = true;
									}
						}, function(response){
							alert("error");
						});
	
	$scope.nextPage = function (){
		$scope.pageCounter++;
		
		var to = $scope.pageCounter * 10;
		var from = to - 9;
		console.log(from);
		console.log(to);
		AuctionItemService.getAuctionItemsOfUserByStatus(token, $scope.user.userId, "PENDING", from, to)
		.then( function(response){
			if(response.data.length != 0)
					$scope.hasAuctions = true;
					$scope.itemsPending = {};
					$scope.itemsPending = response.data;
					console.log(response);
					var i;
					for(i = 0; i < $scope.itemsPending.length; i++){
						if($scope.itemsPending[i].buyout == null)
							$scope.itemsPending[i].hasBuyout = false;
						else
							$scope.itemsPending[i].hasBuyout = true;
					}
		}, function(response){
			alert("error");
		});
	}
	
	$scope.previousPage = function(){
		$scope.pageCounter--;
		if($scope.pageCounter >= 1){
			var to = $scope.pageCounter * 10;
			var from = to - 9;
		}
		AuctionItemService.getAuctionItemsOfUserByStatus(token, $scope.user.userId, "PENDING", from, to)
		.then( function(response){
			if(response.data.length != 0)
					$scope.hasAuctions = true;
					$scope.itemsPending = {};
					$scope.itemsPending = response.data;
					var i;
					for(i = 0; i < $scope.itemsPending.length; i++){
						if($scope.itemsPending[i].buyout == null)
							$scope.itemsPending[i].hasBuyout = false;
						else
							$scope.itemsPending[i].hasBuyout = true;
					}
		}, function(response){
			alert("error");
		});
	}
});