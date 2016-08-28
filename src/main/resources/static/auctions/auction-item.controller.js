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
	$scope.user.userId = $cookies.get('userId');
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
	

});