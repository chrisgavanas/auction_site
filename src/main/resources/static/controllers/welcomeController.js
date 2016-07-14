var welcomeController = router.controller('welcomeController', function($scope, $state, $http,$cookies, $route,  AuthenticationService){
	var data = new FormData();
	console.log(data);
	$scope.user = {};
	$scope.signedIn = {};
	var token = $cookies.get('authToken');
	
	if($cookies.get('signedIn') === 'yes'){
		$scope.user.userId = $cookies.get('userId');
		$scope.signedIn = true;
		$http.get('/api/user/'+ $scope.user.userId, {headers: {'authToken': token}}).then(function successCallback(response){
			$scope.user.username = response.data.username;
		}, function errorCallback(response){
			alert("error");
			
			
		});
		
		
		
	}else
		$scope.signedIn = false;
	 
	
	$scope.redirectRegister = function(){
		$state.go("register");
	}
	
	$scope.logout = function(){
		$cookies.remove('userId');
		$cookies.remove('authToken');
		$cookies.put('signedIn', 'no');
		$state.go($state.current, {}, {reload: true}); 
	}
	
	$scope.myProfile = function(){
		$state.go("profile");
	}
	// Get the modal
	var modal = document.getElementById('myModal');

	// Get the <span> element that closes the modal
	var span = document.getElementsByClassName("close")[0];

	// When the user clicks on the button, open the modal 
	$scope.openModal = function() {
		
	    modal.style.display = "block";
	}

	// When the user clicks on <span> (x), close the modal
	span.onclick = function() {
	    modal.style.display = "none";
	}

	// When the user clicks anywhere outside of the modal, close it
	window.onclick = function(event) {
	    if (event.target == modal) {
	        modal.style.display = "none";
	    }
	}
	
	$scope.user = {};
    $scope.login = function(user) {
    	
    	AuthenticationService.login(user).then(function (user){
    		$state.go($state.current, {}, {reload: true});
    	}, function (response) {
    		if (response.status == 400){
        		if($scope.user.username === "" | $scope.user.username === undefined)
        			document.getElementById("usernamePanel").style.display = "block";
        		if($scope.user.password === "" | $scope.user.password === undefined)
        			document.getElementById("passwordPanel").style.display = "block";
        		
        	}
            if (response.status == 401 || response.status == 404) {
               document.getElementById("donotmatchPanel").style.display = "block";
               
            }
            
    	});
    };
});