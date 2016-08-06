router.controller('userAuctionsPendingController', function($state, $scope, $http, $cookies, AuthenticationService, AuctionItemService){
	$scope.user = {};
	$scope.itemsPending = {};
	$scope.hasAuctions = false;
	$scope.user.userId = $cookies.get('userId');
	var token = $cookies.get('authToken');
	

	AuctionItemService.getAuctionItemsOfUserByStatus(token, $scope.user.userId, "PENDING")
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
});