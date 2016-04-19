 var userDataService = router.service("userDataService", function () {
	var userId = null;
	var authToken = null;
	
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
	
	 return {
		    setUserId : setUserId,
		    getUserId: getUserId,
		    setAuthToken: setAuthToken,
		    getAuthToken: getAuthToken
	 };
});
