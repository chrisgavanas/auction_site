router.controller('sellerProfileController', function($scope,$state, $stateParams, $cookies, AuthenticationService){
	$scope.user = {};
	var token = $cookies.get('authToken');
	var sellerId = $stateParams.id;
	
	AuthenticationService.getSeller(sellerId, token)
							.then(function(response){
								$scope.user = response.data;
								$scope.registrationDateConverted = $.datepicker.formatDate("M d, yy", new Date(response.data.registrationDate));
								$scope.user.registrationDate = new Date(response.data.registrationDate);
								console.log($scope.user);
							}, function errorCallback(response){
								console.log(response);
								
							});
});

