router.controller('sellerProfileController', function($scope,$state, $stateParams, $cookies, AuthenticationService, AuctionItemService){
	
	var sellerId = $stateParams.id;
	
	AuthenticationService.getSeller(sellerId,$scope.token)
							.then(function(response){
								$scope.user = response.data;
								$scope.registrationDateConverted = $.datepicker.formatDate("M d, yy", new Date(response.data.registrationDate));
								$scope.user.registrationDate = new Date(response.data.registrationDate);
								
							}, function errorCallback(response){
								
								
							});
	AuctionItemService.getAuctionItemsOfUserByStatus($scope.token, sellerId, 'ACTIVE', 1, 4)
					.then(function(response){
							$scope.userItems = response.data.splice(0,4);
							$scope.images = [];
							$scope.imagesCounter = [];
							var i;
							for (i = 0; i < $scope.userItems.length; i++){
								console.log($scope.userItems[i].length);
									if($scope.userItems[i].images.length > 0){
										var res = $scope.userItems[i].images[0].replace(/\\/g, '/');
										var res2 =res.split('/static/');
										console.log(res2);
										$scope.userItems[i].displayImage = res2[1];
										if(i!=0)
											$scope.imagesCounter.push[i];
									}else{
										$scope.userItems[i].displayImage = null;
									}
							
							}
							console.log($scope.images);
					}, function(response){
						console.log(response);
					});
});

