router.controller('userAuctionsStartController', function ($scope, $http, $state,$stateParams, $cookies, AuthenticationService, AuctionItemService) {
	$scope.data = {};
	$scope.user= {};
	$scope.user.userId = $cookies.get('userId');
	var token = $cookies.get('authToken');
		
	$scope.start = function(){
		var auctionItemId = $stateParams.id;
		AuctionItemService.startAuction(token, auctionItemId, $scope.data)
							.then(function(response){
								$state.go('main.profile.userAuctions');
							}, function(response){
								alert("error");
							});
	};
});

