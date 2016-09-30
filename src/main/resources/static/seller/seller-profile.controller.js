router.controller('sellerProfileController', function($scope,$state, $stateParams, $cookies, AuthenticationService, AuctionItemService){
	
	var sellerId = $stateParams.id;
	$scope.seller = {};
	
	
	AuthenticationService.getSeller(sellerId,$scope.token)
							.then(function(response){
								$scope.seller = response.data;
								$scope.registrationDateConverted = $.datepicker.formatDate("M d, yy", new Date(response.data.registrationDate));
								$scope.seller.registrationDate = new Date(response.data.registrationDate);
								
							}, function errorCallback(response){
								if(response.status == 404)
									$state.go('main.notfound');
								
							});
	AuctionItemService.getAuctionItemsOfUserByStatus($scope.token, sellerId, 'ACTIVE', 1, 4)
					.then(function(response){
							$scope.userItems = response.data.splice(0,4);
							
							
					}, function(response){
						console.log(response);
					});
	$scope.contactUser = function(){
		
		$scope.$parent.contact = $scope.seller.username;
		
		$state.go('main.profile.userMessages');
	}
});

