router.factory('AuthenticationService', function($http, $cookies, userDataService) {
	var authService = {};
	
	authService.login = function(user){
		
		return $http.post('/api/login', user)
					.then(function (response) {
						userDataService.setUserId(response.data.useId);
			        	userDataService.setAuthToken(response.data.authToken);
			        	$cookies.put('userId', response.data.useId);
			        	$cookies.put('authToken', response.data.authToken);
			        	$cookies.put('signedIn', 'yes');
			        	return response.data;
					});
				
	};
	
	authService.register = function(user){
		console.log(user);
		return $http.post('/api/user', user)
					.then(function(response){
						return response.data;
					});
		
	};
	
	authService.updateUser = function(user){
		var data = {};
		data.userId = user.userId;
		
		data.email = user.email;
		data.firstName = user.firstName;
		data.lastName = user.lastName;
		data.gender = user.gender;
		data.mobileNumber = user.mobileNumber;
		data.phoneNumber = user.phoneNumber;
		data.dateOfBirth = user.dateOfBirth;
		data.vat = user.vat;
		data.street = user.street;
		data.city = user.city;
		data.country = user.country;
		data.postalCode = user.postalCode;
		console.log(data);
		var postData = {Integer: data.userId, UserUpdateRequestDto: data};
		return $http.post('/api/user/'+user.userId, data)
					.then(function(response){
						return response.data;
					});
	}
	authService.isAuntenticated = function () {
		return !!userDataService.getUserId();
		
	};
	
	return authService;
})