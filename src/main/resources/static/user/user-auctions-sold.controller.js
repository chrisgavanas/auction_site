router.controller('userAuctionsSoldController', function($state, $scope, $http, $cookies, AuthenticationService, AuctionItemService){
	
	$scope.itemsSold = {};
	$scope.hasAuctions = false;
	$scope.pageCounter = 1;
	
	if($scope.signedIn == false)
		$state.go('main.signedout');
	
	AuctionItemService.getAuctionItemsOfUserByStatus($scope.token, $scope.user.userId, "INACTIVE", "1", "10" )
						.then( function(response){
							if(response.data.length != 0)
								
									$scope.hasAuctions = true;
									$scope.itemsSold = {};
									$scope.itemsSold = response.data;
									

									var i;
									for(i = 0; i < $scope.itemsSold.length; i++){
										addBuyer(i, null);
										if($scope.itemsSold[i].buyout == null)
											$scope.itemsSold[i].hasBuyout = false;
										else
											$scope.itemsSold[i].hasBuyout = true;
										
										
										
									}
									
									
						}, function(response){
							
						});
	
	$scope.nextPage = function (){
		$scope.pageCounter++;
		$scope.to  = $scope.pageCounter * 10;
		$scope.from = $scope.to  - 9;
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
						console.log($scope.itemsSold[i]);
					}
				}
		}, function(response){
			console.log(response);
		});	
		
	}
	
	$scope.previousPage = function(){
		$scope.pageCounter--;
		if($scope.pageCounter >= 1){
			$scope.to  = $scope.pageCounter * 10;
			$scope.from = $scope.to  - 9;
		}
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