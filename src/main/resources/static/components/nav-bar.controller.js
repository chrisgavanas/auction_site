router.controller('navBarController', function($state, $scope, $rootScope, $cookies, $http, AuthenticationService){
	var data = new FormData();
	$scope.user = {};
	$scope.signedIn = {};
	var token = $cookies.get('authToken');
	$scope.user.userId = $cookies.get('userId');
	
	
	if($cookies.get('signedIn') == 'yes')
		$scope.signedIn = true;
	else
		$scope.signedIn = false;
	
	
	if($scope.signedIn == true)
		AuthenticationService.getUser($scope.user.userId, token)
								.then(function(response){
								$scope.user = response.data;
							}, function errorCallback(response){
								console.log(ressonse);
								$cookies.remove('userId');
								$cookies.remove('authToken');
								$cookies.put('signedIn', 'no');
								$state.go('main.welcome', {}, {reload: true});
							});
		
		
	$scope.redirectRegister = function(){
		$state.go("main.register");
	}
	
	$scope.logout = function(){
		$cookies.remove('userId');
		$cookies.remove('authToken');
		$cookies.put('signedIn', 'no');
		if($state.current.name != 'main.welcome'){
			$state.go('main.welcome', {}, {reload: true});
		}else
			$state.go($state.current, {}, {reload: true});	
	}
	
	$scope.myProfile = function(){
		$state.go("main.profile", {}, {reload: true});
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
    	AuthenticationService.login(user, token)
    							.then(function (response){
    								$scope.user = response;
    								if($scope.user.isAdmin == true)
    									$state.go('main.admin', {} , {reload: true});
    								else
    									$state.go('main.welcome', {}, {reload: true});
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