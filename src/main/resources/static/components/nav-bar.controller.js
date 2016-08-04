router.controller('navBarController', function($state, $scope, $rootScope, $cookies, $http, AuthenticationService){
	var data = new FormData();
	$scope.user = {};
	$scope.signedIn = {};
	var token = $cookies.get('authToken');
	
	if($cookies.get('signedIn') === 'yes'){
		$scope.user.userId = $cookies.get('userId');
		$scope.signedIn = true;
		$http.get('/api/user/'+ $scope.user.userId, {headers: {'authToken': token}}).then(function successCallback(response){
			$scope.user.username = response.data.username;
		}, function errorCallback(response){
		
			$cookies.remove('userId');
			$cookies.remove('authToken');
			$cookies.put('signedIn', 'no');
			$scope.signedIn = false;
			$state.go('main.welcome',{},{reload: true});
			
			
			
			
		});
		
		
		
	}else
		$scope.signedIn = false;
	
	
	$scope.redirectRegister = function(){
		$state.go("main.register");
	}
	
	$scope.logout = function(){
		$cookies.remove('userId');
		$cookies.remove('authToken');
		$cookies.put('signedIn', 'no');
		if($state.current.name != 'main.welcome'){
			console.log("current");
		
			$state.go('main.welcome', {}, {reload: true});
		}else
			$state.go($state.current, {}, {reload: true});	
	}
	
	$scope.myProfile = function(){
		$state.go("main.profile");
	}
	
	
	
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
    	
    		if($state.current.name == "main.welcome"){
    			$state.go($state.current, {}, {reload: true}); 
    		}else{
    		
    			$state.go("main.welcome", {}, {reload: true});
    		}
    	}, function (response) {
    		
    		if (response.status == 400){
        		if($scope.user.username === "" | $scope.user.username === undefined)
        			document.getElementById("usernamePanel").style.display = "block";
        		if($scope.user.password === "" | $scope.user.password === undefined)
        			document.getElementById("passwordPanel").style.display = "block";
        		
        	}
            if (response.status == 401 || response.status == 403) {
               document.getElementById("donotmatchPanel").style.display = "block";
               
            }
            
    	});
    };
	
	
	
});