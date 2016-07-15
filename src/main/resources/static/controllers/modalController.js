var modalController = router.controller('modalController', function($scope, $rootScope, $http, $state, AuthenticationService) {
	// Get the modal
	var modal = document.getElementById('myModal');

	// Get the <span> element that closes the modal
	var span = document.getElementsByClassName("close")[0];

	// When the user clicks on the button, open the modal
	$rootScope.$on("CallParentMethod", function(){
		$scope.openModal();
	});
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
    	console.log("mhak)");
    	AuthenticationService.login(user).then(function (user){
    	
    		if($state.current.name == "welcome"){
    			$state.go($state.current, {}, {reload: true}); 
    		}else{
    			$state.go("welcome");
    		}
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