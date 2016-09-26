router.controller('itemController', function($scope, $state, $http,$cookies, $route, $stateParams, AuthenticationService, AuctionItemService){
	$scope.item = {};
	$scope.shown = false;
	var auctionItemId = $stateParams.id;
	
	
	$scope.images = [];
	$scope.imagesCounter = [];
	$scope.hasLatLon = null;
	$scope.hasBuyout = null;
	$scope.seller = {};
	$scope.bid = {};
	$scope.conflict = false;
	$scope.conflict1 = false;
	$scope.conflict2 = false;
	$scope.completed = false;
	$scope.rate = false;
	$scope.bid.userId = $scope.user.userId;
	$scope.selectedImage = null;
	$scope.inactive = false;
	
	$scope.bidds = [];
	
	
	
	AuctionItemService.getAuctionItemById($scope.token, auctionItemId)
						.then(function(response){
							$scope.item = response.data;
							console.log($scope.item);
							if($scope.item.buyerId != null){
								AuthenticationService.getSeller($scope.item.buyerId, $scope.token)
									.then(function(response){
									
									
										$scope.buyer = response.data.username;
										
									}, function (response){
										console.log(response);
									});
							}
							var lat = $scope.item.geoLocationDto.latitude;
							var lon = $scope.item.geoLocationDto.longitude;
							if(lat == null || lon == null)
								$scope.hasLatLon = false;
							else
								$scope.hasLatLon = true;
							
							if($scope.item.buyout == undefined){
								$scope.hasBuyout = false;
							}else
								$scope.hasBuyout = true;
							if($scope.item.endDate == null){
								$scope.inactive = true;
							}
							var i;
							for (i = 0; i < $scope.item.images.length; i++){
								var res = $scope.item.images[i].replace(/\\/g, '/');
								var res2 =res.split('/static/');
								
								$scope.images.push("./"+res2[1]);
								if(i!=0)
									$scope.imagesCounter.push[i];
							}
						
							$scope.selectedImage = $scope.images[0];
							AuthenticationService.getSeller($scope.item.userId, $scope.token)
													.then(function(response){
															$scope.seller = response.data;
													}, function errorCallback(response){
														console.log(response);
												
													});
						}, function(response){
							console.log(response);
						});	
	
	$scope.go  = function(){
		
		$state.go('main.biddingexamples');
	};
	
	$scope.placeBid = function(){
		console.log($scope.user.userId);
		if($scope.user.userId == $scope.item.userId)
			$scope.conflict2 = true;
		else{
			if($scope.item.buyout != null){
				if($scope.bid.amount < $scope.item.buyout){
					console.log($scope.token, $scope.bid, $scope.item.auctionItemId);
					AuctionItemService.bid($scope.token, $scope.bid, $scope.item.auctionItemId)
									.then(function(response){
										$scope.completed = true;
										
									}, function errorCallBack(response){
								
										console.log(response);
										if(response.status == 409)
											$scope.conflict = true;
										if(response.status == 500)
											$('#myModal').modal('show');
									});
				
				}else{
					$scope.completed = false;
				}
			}else{
			
			
				AuctionItemService.bid($scope.token, $scope.bid, $scope.item.auctionItemId)
									.then(function(response){
										$scope.completed = true;
										
									}, function errorCallBack(response){
										if(response.status == 409)
											$scope.conflict = true;
									});
			
			}
				
		}
	};

	
	$scope.changeImage = function (image){
		$scope.selectedImage = image;
		
	}
	$scope.buyoutComplete = false;
	$scope.buyout = function(){
		$scope.buyout = {};
		$scope.buyout.buyerId = $scope.user.userId;
		AuctionItemService.buyout($scope.token, $scope.item.auctionItemId, $scope.buyout)
						.then(function(response){
							$scope.buyoutComplete = true;
						}, function(response){
							if(response.status == 409){
								$scope.conflict1 = true;
								$('#completeModal').modal('hide');
							}
							console.log(response);
						});
	};

	$scope.ratePositive = function(){
		console.log($scope.token);
		AuthenticationService.voteSeller($scope.token, $scope.user.userId, $scope.seller.sellerId, 'UP')
							.then(function(response){
								$scope.rate = true;
								
							}, function(response){
								console.log(response);
							});
	};
	
	$scope.rateNegative = function(){
		console.log($scope.seller);
		AuthenticationService.voteSeller($scope.token, $scope.user.userId, $scope.seller.sellerId, 'DOWN')
							.then(function(response){
								$scope.rate = true;
								
							}, function(response){
								console.log(response);
							});
	};
	
	
	
	
	AuctionItemService.getBidsOfAuctionItem($scope.token, auctionItemId)
					.then(function(response){
						console.log(response);
						$scope.bidds = response.data;
						console.log($scope.bidds);
					}, function(response){
						console.log(response);
					});
	
});