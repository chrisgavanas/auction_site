router.controller('userAuctionsStartController', function ($scope, $http, $state,$stateParams, $cookies, AuthenticationService, AuctionItemService) {
	$scope.data = {};
	
		
	$scope.start = function(){
		var auctionItemId = $stateParams.id;
		AuctionItemService.startAuction($scope.token, auctionItemId, $scope.data)
							.then(function(response){
								$state.go('main.profile.userAuctions');
							}, function(response){
								alert("error");
							});
	};
});

