var profileController = router.controller('profileController', function($scope, $state, $http,$cookies, $route, AuthenticationService){
	$scope.user = {};
	$scope.user.userId = $cookies.get('userId');
	var token = $cookies.get('authToken');
		
	AuthenticationService.getUser($scope.user.userId, token)
							.then(function(response){
								$scope.user = response.data;
								$state.go('main.profile.userInfo');
							}, function errorCallback(response){
								$cookies.remove('userId');
								$cookies.remove('authToken');
								$cookies.put('signedIn', 'no');
								$state.go('main.welcome');
							});
});