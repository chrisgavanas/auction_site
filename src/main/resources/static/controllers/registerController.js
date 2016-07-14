var registerController = router.controller('registerController', function($scope, $http, $state, AuthenticationService) {
    $scope.user = {};
    $scope.user.address = {};
    $scope.user.registrationDate = new Date();
    $scope.register = function(user) {
    	AuthenticationService.register(user).then(function (user){
    		$state.go("welcome");
    	}, function (response) {
    		alert(response.statusText+response.status);
    		if (response.data.message === "Username is already in use.") 
        		document.getElementById("inUsePanel1").style.display = "block";
    		if(response.data.message === "Email is already in use.")	
    			document.getElementById("inUsePanel2").style.display = "block";
        	
           
    	});
    };
    
    
    $scope.redirectLogin = function(){
		$state.go("login");
	
	};	
	$scope.redirectRegister = function(){
		$state.go("register");
	};	
	
	
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
