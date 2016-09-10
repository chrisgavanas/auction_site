var profileController = router.controller('profileController', function($scope, $state, $http,$cookies, $route, AuthenticationService, MessageService){
	$scope.user = {};
	$scope.user.userId = $cookies.get('userId');
	var token = $cookies.get('authToken');
	
	console.log($scope.navbar);
	
	AuthenticationService.getUser($scope.user.userId, token)
							.then(function(response){
								$scope.user = response.data;
								
							}, function errorCallback(response){
								$cookies.remove('userId');
								$cookies.remove('authToken');
								$cookies.put('signedIn', 'no');
								$state.go('main.welcome');
							});
	
	
	
});