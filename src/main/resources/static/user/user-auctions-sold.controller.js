router.controller('userAuctionsSoldController', function($state, $scope, $http, $cookies, AuthenticationService, AuctionItemService){
	
	$scope.itemsSold = {};
	$scope.hasAuctions = false;
	$scope.pageCounter = 1;
	
	
	

	AuctionItemService.getAuctionItemsOfUserByStatus($scope.token, $scope.user.userId, "INACTIVE", "1", "10" )
						.then( function(response){
							if(response.data.length != 0)
								console.log(response);
									$scope.hasAuctions = true;
									$scope.itemsSold = {};
									$scope.itemsSold = response.data;
									var i;
									for(i = 0; i < $scope.itemsSold.length; i++){
										if($scope.itemsSold[i].buyout == null)
											$scope.itemsSold[i].hasBuyout = false;
										else
											$scope.itemsSold[i].hasBuyout = true;
									}
						}, function(response){
							console.log(response);
						});
	
	$scope.nextPage = function (){
		$scope.pageCounter++;
		var to = $scope.pageCounter * 10;
		var from = to - 9;
		
	}
	
	$scope.previousPage = function(){
		$scope.pageCounter--;
		if($scope.pageCounter >= 1){
			var to = $scope.pageCounter * 10;
			var from = to - 9;
		}
	}
});