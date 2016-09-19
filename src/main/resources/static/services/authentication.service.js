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
	
	authService.updateUser = function(user, token){
		console.log(token);
		return $http.post('/api/user/'+user.userId, user, {headers:{'authToken': token}})
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
	
	authService.getSeller = function(sellerId, token){
		return $http.get('/api/seller/'+ sellerId, {headers: {'authToken': token}})
					.then(function successCallback(response){
						return response;
					});
	};
	
	authService.getUnverified = function (token, from, to){
		return $http.get('/api/user/unverified/' +from+'-'+to, {headers: {'authToken': token}} )
					.then(function successCallback(response){
						return response;
					});
	};
	
	authService.getVerified = function (token, from, to){
		return $http.get('/api/user/verified/' +from+'-'+to, {headers: {'authToken': token}} )
					.then(function successCallback(response){
						return response;
					});
	};
	
	authService.exportToXML = function(token){
		return $http.get('/api/auctionitem-as-xml', {headers:{'authToken': token}})
					.then(function (response){
						return response;
					});
	};
	
	authService.voteSeller = function (token, userId, sellerId, rating){
		return $http.post('/api/user/'+userId+'/vote-seller/'+rating, sellerId, {headers:{'authToken':token}})
				.then(function (response){
					return response;
				});
	};
	return authService;
})