router.factory('AuthenticationService', function($http, $cookies, userDataService) {
	var authService = {};
	
	authService.login = function(user){
		return $http.post('/api/login', user)
					.then(function (response) {
						userDataService.setUserId(response.data.useId);
			        	userDataService.setAuthToken(response.data.authToken);
			        	$cookies.put('userId', response.data.useId);
			        	$cookies.put('authToken', response.data.authToken);
			        	return response.data;
					});
				
	};
	
	authService.isAuntenticated = function () {
		return !!userDataService.getUserId();
		
	};
	
	return authService;
})