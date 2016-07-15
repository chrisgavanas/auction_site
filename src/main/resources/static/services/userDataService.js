 var userDataService = router.service("userDataService", function () {
	var userId = null;
	var authToken = null;
	var user = {};
	
	
	var setUserId = function (id){
		userId = id;
	};
	
	var getUserId = function(){
		return userId;
	};
	
	var setAuthToken = function (token){
		authToken = token;
	};
	
	var getAuthToken = function (){
		return authToken;
	};
	
	var setUser = function(user){
		this.user = user;
	}
	
	var getUser = function(){
		return user;
	}
	
	return {
		    setUserId : setUserId,
		    getUserId: getUserId,
		    setAuthToken: setAuthToken,
		    getAuthToken: getAuthToken,
		    setUser: setUser,
		    getUser: getUser
	 };
});
