var profileController = router.controller('profileController', function($scope, $state, $http,$cookies, $route, AuthenticationService, MessageService){
	
	if($scope.signedIn == false)
		$state.go('main.signedout');
	else
		$state.go('main.profile.userInfo');
	
});