router.controller('itemController', function($scope, $state, $http,$cookies, $route, $stateParams, AuthenticationService, AuctionItemService){
	$scope.item = {};
	$scope.shown = false;
	var auctionItemId = $stateParams.id;
	$scope.user = {};
	$scope.hasLatLon = null;
	$scope.hasBuyout = null;
	$scope.user.userId = $cookies.get('userId');
	var token = $cookies.get('authToken');
	
	
	AuthenticationService.getUser($scope.user.userId, token)
							.then(function(response){
							$scope.user = response.data;
						}, function errorCallback(response){
							console.log(response);
							$cookies.remove('userId');
							$cookies.remove('authToken');
							$cookies.put('signedIn', 'no');
							$state.go('main.welcome', {}, {reload: true});
						});
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