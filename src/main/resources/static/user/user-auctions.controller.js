router.controller('userAuctionsController', function($state, $scope, $http, $cookies, AuthenticationService){
	$state.go('main.profile.userAuctions.active');
	
	$scope.newAuction = function(){
		$state.go("main.newAuction");
	};
	
});