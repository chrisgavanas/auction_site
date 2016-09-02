router.controller('itemController', function($scope, $state, $http,$cookies, $route, $stateParams, AuthenticationService, AuctionItemService){
	$scope.item = {};
	$scope.shown = false;
	var auctionItemId = $stateParams.id;
	$scope.user = {};
	$scope.images = [];
	$scope.imagesCounter = [];
	$scope.hasLatLon = null;
	$scope.hasBuyout = null;
	$scope.seller = {};
	$scope.bid = {};
	$scope.conflict = false;
	$scope.completed = false;
	$scope.user.userId = $cookies.get('userId');
	$scope.bid.userId = $scope.user.userId;
	$scope.selectedImage = null;
	var token = $cookies.get('authToken');
	
	
	
	AuctionItemService.getAuctionItemById(token, auctionItemId)
						.then(function(response){
							$scope.item = response.data;
							console.log($scope.item);
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
							
							var i;
							for (i = 0; i < $scope.item.images.length; i++){
								var res = $scope.item.images[i].replace(/\\/g, '/');
								var res2 =res.split('/static/');
								console.log(res2);
								$scope.images.push("./"+res2[1]);
								if(i!=0)
									$scope.imagesCounter.push[i];
							}
							console.log($scope.images);
							$scope.selectedImage = $scope.images[0];
							AuthenticationService.getSeller($scope.item.userId, token)
													.then(function(response){
															$scope.seller = response.data;
													}, function errorCallback(response){
														console.log(response);
														alert("Error");
													});
						}, function(response){
							console.log(response);
						});	
	
	$scope.go  = function(){
		if(!$scope.shown){
			$scope.shown = true;
			$state.go('main.item.offers');
			
		}else{
			$scope.shown = false;
			$state.go('main.item');
		}
	}
	
	$scope.placeBid = function(){
		console.log($scope.bid);
		if($scope.item.buyout != null){
			if($scope.bid.amount < $scope.item.buyout){
				
				AuctionItemService.bid(token, $scope.bid, $scope.item.auctionItemId)
							.then(function(response){
								$scope.completed = true;
								console.log(response);
							}, function errorCallBack(response){
								console.log(response);
								if(response.status == 409)
									$scope.conflict = true;
							});
				
			}else{
				$scope.completed = false;
			}
		}else{
			
			$scope.completed = true;
			AuctionItemService.bid(token, $scope.bid, $scope.item.auctionItemId)
						.then(function(response){
							$scope.completed = true;
							console.log(response);
						}, function errorCallBack(response){
							if(response.status == 409)
								$scope.conflict = true;
						});
			
		}
				
		
	}

	
	$scope.changeImage = function (image){
		$scope.selectedImage = image;
		console.log("mphka");
	}

});