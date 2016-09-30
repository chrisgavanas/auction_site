router.controller('itemController', function($scope, $state, $http,$cookies, $route, $stateParams, AuthenticationService, AuctionItemService){
	$scope.item = {};
	$scope.shown = false;
	var auctionItemId = $stateParams.id;
	$scope.bytes = [];
	
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
								for (i = 0; i < $scope.item.images.length; i++){
									AuctionItemService.getImage($scope.item.images[i])
		                    							.then(function(response){
		                    				
		                    								if($scope.selectedImage == null)
		                    									$scope.selectedImage = response.data;
		                    								$scope.bytes.push(response.data);
		                    		
		                    							}, function(response){
		                    								console.log(response);
		                    							});
								}
							}
						
							
						
							AuthenticationService.getSeller($scope.item.userId, $scope.token)
													.then(function(response){
															$scope.seller = response.data;
													}, function errorCallback(response){
														console.log(response);
												
													});
						}, function(response){
							if(response.status == 404)
								$state.go('main.notfound');
						});	
	
	$scope.go  = function(){
		$('#myModal2').modal('hide');
		$('.modal-backdrop').remove();
		$state.go('main.biddingexamples');
	};
	
	$scope.placeBid = function(){
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

	
	
	
	
	
	AuctionItemService.getBidsOfAuctionItem($scope.token, auctionItemId)
					.then(function(response){
						$scope.bidds = response.data;
					}, function(response){
						console.log(response);
					});
	
});