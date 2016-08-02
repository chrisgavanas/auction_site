router.factory('AuthenticationService', function($http, $cookies) {
	var authService = {};
	
	authService.login = function(user){
		return $http.post('/api/login', user)
					.then(function (response) {
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
		//var data = {};
		//data = user;
		/*data.userId = user.userId;
		
		data.email = user.email;
		data.firstName = user.firstName;
		data.lastName = user.lastName;
		data.gender = user.gender;
		data.mobileNumber = user.mobileNumber;
		data.phoneNumber = user.phoneNumber;
		data.dateOfBirth = user.dateOfBirth;
		data.vat = user.vat;
		data.address = user.address;
		data.country = user.country;*/
		
		
		var postData = {Integer: user.userId, UserUpdateRequestDto: user};
		return $http.post('/api/user/'+user.userId, user)
					.then(function(response){
						return response.data;
					});
	};
	
	authService.changePassword = function(passwords, userId, token){
		var data = {};
		data.oldPassword = passwords.oldPassword;
		data.newPassword = passwords.newPassword;
		return $http.post('/api/user/'+userId+'/change-password', data, {headers: {'authToken': token}})
					.then(function(response){
						return response.data;
					});
	};
	
	authService.getUser = function(userId, token){
		return $http.get('/api/user/'+ userId, {headers: {'authToken': token}})
					.then(function successCallback(response){
						return response;
					});
	};
	
	
	return authService;
})