router.factory('AdminService', function($http, $cookies, $state) {
	
		var adminService = {};
		
		adminService.getVerified = function(token, from, to){
			return $http.get('/api/user/verified/' + from + '-' + to, {headers: {'authToken': token}})
						.then(function (response){
							return response;
						});
		};
		
	
		adminService.getUnverified = function(token, from, to){
			return $http.get('/api/user/unverified/' + from + '-' + to, {headers: {'authToken': token}})
						.then(function (response){
							return response;
						});
		};
		
		adminService.exportXml = function(token){
			return $http.get('/api/')
		}
		
		adminService.exportToXML = function(token){
			return $http.get('/api/auctionitem-as-xml', {headers:{'authToken': token}})
						.then(function (response){
							return response;
						});
		};
			
		adminService.verify = function(token, userId){
			return $http.post('/api/user/'+userId+'/verify-user', {}, {headers: {'authToken': token}})
						.then(function (response){
							return response;
						});
		}
		return adminService;
		
});
