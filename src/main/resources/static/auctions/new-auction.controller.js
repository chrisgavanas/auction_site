router.controller('newAuctionController', function($scope, $state, $http,$cookies, $route, AuthenticationService, AuctionItemService){
	$scope.user = {};
	$scope.signedIn = {};
	$scope.item = {};
	$scope.item ={
		categories: []
	};
	$scope.item.geoLocationDto = {};
	$scope.categories = {};
	

	
	AuctionItemService.getCategories(token)
						.then(function(response){
							$scope.categories = angular.copy(response.data);
						}, function(response){
							console.log(response);
						});

	$scope.cont = function(){
		console.log($scope.item);
		$scope.item.userId = $scope.user.userId;
		$scope.item.images = [];
		$scope.item.categories.push($scope.selectedCat.categoryId);
		
		AuctionItemService.addAuctionItem(token, $scope.item)
							.then(function(response){
								$state.go('main.profile.userAuctions');
							}, function(response){
								alert("error");
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