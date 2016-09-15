router.controller('sellerProfileController', function($scope,$state, $stateParams, $cookies, AuthenticationService){
	
	var sellerId = $stateParams.id;
	
	AuthenticationService.getSeller(sellerId,$scope.token)
							.then(function(response){
								$scope.user = response.data;
								$scope.registrationDateConverted = $.datepicker.formatDate("M d, yy", new Date(response.data.registrationDate));
								$scope.user.registrationDate = new Date(response.data.registrationDate);
								
							}, function errorCallback(response){
								
								
							});
});

