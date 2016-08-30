router.factory('AuctionItemService', function($http, $cookies, $state) {
	var auctionitemService = {};
	
	auctionitemService.getCategories = function(token){
		return $http.get('/api/category', {headers: {'authToken': token}})
					.then(function successCallback(response){
						return response;
						
					});
	};
	
	auctionitemService.addAuctionItem = function(token, item){
		var x = 1;
		console.log(item);
		return $http.post('/api/auctionitem', item)
					.then(function(response){
							$state.go('main.profile.userAuctions');
							return response;
					});
	};
	
	auctionitemService.editAuctionItem = function(token, itemId, item){
		return $http.post('/api/auctionitem/'+ itemId, item)
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
		return $http.post('/api/auctionitem/'+itemId+'/start', data)
					.then(function(response){
						$state.go('main.profile.userAuctions');
						return response;
					});
		
	};
	
	auctionitemService.getAuctionItemsOfUserByStatus = function(token, userId, status){
		return $http.get('api/auctionitem/user/' + userId + "?status=" + status)
					.then(function successCallback(response){
						return response;
					});
	};
	
	auctionitemService.bid = function(token, bid, auctionItemId){
		return $http.post('api/auctionitem/'+auctionItemId+'/bid' , bid)
					.then(function successCallback(response){
						return response;
					});
	}
	
	return auctionitemService;
	
});