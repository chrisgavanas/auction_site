router.controller('editAuctionController', function($scope, $state, $stateParams, $http, $cookies, AuthenticationService, AuctionItemService){
	var auctionId = $stateParams.id;
	var token = $cookies.get('authToken');
	
	AuctionItemService.getCategories(token)
						.then(function(response){
							$scope.categories = angular.copy(response.data);
						}, function(response){
							alert("error");
						});
	
	AuctionItemService.getAuctionItemById(token, auctionId)
						.then(function(response){
							$scope.item = response.data;
							$scope.item.categories = [];
							$scope.selectedCat = $scope.item.categoryResponseDtoList[0];
						}, function(response){
							alert("error");
						});
	
	$scope.categories = {};
	$scope.user = {};
	
	$scope.cont = function(){
		console.log($scope.item);
		$scope.item.userId = $scope.user.userId;
		$scope.item.images = [];
		$scope.item.categories = [];
		$scope.item.categories.push($scope.selectedCat.categoryId);
		
		AuctionItemService.editAuctionItem(token, auctionId, $scope.item)
							.then(function(response){
								$state.go('main.profile.userAuctions');
							}, function(response){
								alert("error");
							});
	};
});


