router.controller('userAuctionsController', function($state, $scope, $http, $cookies, AuthenticationService){
	if($scope.signedIn == false)
		$state.go('main.signedout');
	else
		$state.go('main.profile.userAuctions.active');
	
	$scope.newAuction = function(){
		$state.go("main.newAuction");
	};
	
});