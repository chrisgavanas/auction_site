router.controller('userAuctionsSoldController', function($state, $scope, $http, $cookies, AuthenticationService, AuctionItemService){
	
	$scope.itemsSold = {};
	$scope.hasAuctions = false;
	$scope.pageCounter = 1;
	$scope.totalAuctions = 0;
	
	$scope.pageNumbers = 0;
	$scope.maxSize = 5;

	$scope.bigCurrentPage = 1;
	$scope.currentPage = 4;
	
	if($scope.signedIn == false)
		$state.go('main.signedout');
	
	AuctionItemService.getAuctionItemsOfUserByStatus($scope.token, $scope.user.userId, "INACTIVE", "1", "10" )
						.then( function(response){
							if(response.data.length != 0)
								
									$scope.hasAuctions = true;
									$scope.itemsSold = {};
									$scope.itemsSold = response.data;
									$scope.totalAuctions = response.headers().totalauctions;
									$scope.pageNumbers = Math.ceil($scope.totalAuctions / 10);

									var i;
									for(i = 0; i < $scope.itemsSold.length; i++){
									
										if($scope.itemsSold[i].buyout == null)
											$scope.itemsSold[i].hasBuyout = false;
										else
											$scope.itemsSold[i].hasBuyout = true;
										
										
										
									}
									
									
						}, function(response){
							
						});
	
	
	
	$scope.change = function (current){
		$scope.to = current * 10;
		$scope.from = $scope.to - 9;
		AuctionItemService.getAuctionItemsOfUserByStatus($scope.token, $scope.user.userId, "INACTIVE",$scope.from, $scope.to )
		.then( function(response){
			if(response.data.length != 0)
				$scope.hasAuctions = true;
			$scope.itemsSold = {};
			$scope.itemsSold = response.data;
			var i;
			for(i = 0; i < $scope.itemsSold.length; i++){
				if($scope.itemsSold[i].buyout == null)
					$scope.itemsSold[i].hasBuyout = false;
				else
					$scope.itemsSold[i].hasBuyout = true;

				if($scope.itemsSold[i].buyerId != null){

					AuthenticationService.getSeller($scope.itemsSold[i].buyerId, $scope.token)
						.then(function(response){
						
							$scope.buyerUsername = response.data.username;
						
						}, function (response){
							console.log(response);
						});
					$scope.itemsSold[i].buyerUsername = $scope.buyerUsername;

				}
			}
		}, function(response){
			console.log(response);
		});	
	}
});