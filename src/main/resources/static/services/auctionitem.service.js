router.factory('AuctionItemService', function($http, $cookies, $state) {
	var auctionitemService = {};
	
	auctionitemService.getCategories = function(token){
		return $http.get('/api/category', {headers: {'authToken': token}})
					.then(function successCallback(response){
						return response;
						
					});
	};
	
	auctionitemService.getCategory = function(categoryId){
		return $http.get('/api/category/' + categoryId)
					.then (function (response){
						return response;
					});
		
	};
	auctionitemService.addAuctionItem = function(token, item){
		return $http.post('/api/auctionitem', item,  {headers: {'authToken': token}})
					.then(function(response){
							$state.go('main.profile.userAuctions');
							return response;
					});
	};
	
	auctionitemService.editAuctionItem = function(token, itemId, item){
		return $http.post('/api/auctionitem/'+ itemId, item,  {headers: {'authToken': token}})
					.then(function(response){
							$state.go('main.profile.userAuctions');
							return response;
					});
	};
	
	auctionitemService.getAuctionItemById = function(token, itemId){
			return $http.get('/api/auctionitem/'+ itemId)
						.then(function(response){
							return response;
						});
	};
	
	auctionitemService.startAuction = function(token, itemId, data){
		return $http.post('/api/auctionitem/'+itemId+'/start', data,  {headers: {'authToken': token}})
					.then(function(response){
						$state.go('main.profile.userAuctions');
						return response;
					});
		
	};
	
	auctionitemService.getAuctionItemsOfUserByStatus = function(token, userId, status, from, to){
		return $http.get('api/auctionitem/user/' + userId + "/"+from+"-"+to+"?status=" + status)
					.then(function successCallback(response){
						return response;
					});
	};
	
	auctionitemService.bid = function(token, bid, auctionItemId){
		return $http.post('api/auctionitem/'+auctionItemId+'/bid' , bid,  {headers: {'authToken': token}})
					.then(function successCallback(response){
						return response;
					});
	}
	
	auctionitemService.getBidsOfAuctionItem = function(token, auctionId){
		return $http.get('api/auctionitem/'+auctionId+'/bids', {headers: {'authToken': token}})
					.then(function successCallback(response){
						return response;
					});
	}
	
	auctionitemService.buyout = function(token, auctionItemId , buyerId){
		return $http.post('api/auctionitem/'+auctionItemId+'/buyout', buyerId, {headers:{'authToken':token}})
					.then(function (response){
						return response;
					});
	}
	
	auctionitemService.search = function(from, to, searchInfo){
		return $http.post('api/auctionitem/search/'+from+'-'+to, searchInfo)
					.then(function(response){
						return response;
					});
	}
	
	auctionitemService.getCountry = function(lat, lon, i){
		return $http.get('https://maps.googleapis.com/maps/api/geocode/json?latlng='+lat+','+lon+'&sensor=false&language=en')
					.then(function (response){
						return [response, i];
					});
	}
	
	auctionitemService.recommend = function(token, userId){
		console.log(userId);
		return $http.post('api/auctionitem/recommend', userId, {headers:{'Content-Type': 'text/plain','authToken':token}})
					.then (function (response){
						return response;
					});
	};
	
	auctionitemService.randomRecommend = function(){
		return $http.get('api/auctionitem/random-recommended')
					.then(function(response){
						return response;
					});
	}
	
	auctionitemService.getImage = function(path){
		return $http.get('/api/auctionitem/image?imagePath=' + path)
					.then(function(response){
						return response;
					});
	};
	
	auctionitemService.getCountry = function(lat, lon){
		return $http.get('https://maps.googleapis.com/maps/api/geocode/json?latlng='+lat+','+lon+'&sensor=false&language=en')
					.then(function(response){
						return response;
					});
	};
	
	auctionitemService.getAllCountries = function(){
		return 	$http.get('https://restcountries.eu/rest/v1/all')
						.then(function(response){
							return response;
						});

	};
	return auctionitemService;
	
});