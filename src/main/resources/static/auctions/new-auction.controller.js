router.controller('newAuctionController', function($scope, $state, $http,$cookies, $route, AuthenticationService, AuctionItemService){
	$scope.user = {};
	$scope.signedIn = {};
	$scope.item = {};
	$scope.item ={
		categories: []
	};
	$scope.item.geoLocationDto = {};
	$scope.categories = {};
	$scope.user.userId = $cookies.get('userId');
	var token = $cookies.get('authToken');

	
	AuctionItemService.getCategories(token)
						.then(function(response){
							$scope.categories = angular.copy(response.data);
						}, function(response){
							console.log(response);
						});

	$scope.cont = function(){
	
		$scope.item.userId = $scope.user.userId;
		$scope.item.images = [];
		$scope.item.categories.push($scope.selectedCat.categoryId);
		
		AuctionItemService.addAuctionItem(token, $scope.item)
							.then(function(response){
								$state.go('main.profile.userAuctions');
							}, function(response){
								console.log(response);
							});
	};
	
	var formdata = new FormData();
	
	$scope.getTheFiles = function($files){
		console.log($files);
		angular.forEach($files, function(value, key){
			console.log(value);
			formdata.append(key, value);
			formdata.append(2, "koula");
		});
		alert(formdata);
	};
});