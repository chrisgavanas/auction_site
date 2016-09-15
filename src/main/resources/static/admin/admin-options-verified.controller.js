router.controller('adminOptionsVerifiedController', function($state, $scope, $cookies, $http, AuthenticationService){
	
	$scope.verified = {};
	
	$scope.usernamesAndIds = [];
	
	$scope.pageCounter = 1;
	
		
	$http.get('/api/user/verified/1-10', {headers: {'authToken': $scope.token}} ).then(function successCallback(response){
		$scope.verified = angular.copy(response.data);
			
		var i;
		for(i = 0; i < $scope.unverified.length; i ++){
			$scope.usernamesAndIds.push( { id: $scope.unverified[i].userId, text: $scope.unverified[i].username } );
		}
			
	}, function errorCallback(response){
			alert('error sthn unverified');
			
	});
		
	

	$scope.getVerified = function(form, to){
		AuthenticationService.getVerified($scope.token, form, to)
							.then(function(response){
								console.log(respone);
							}, function(response){
								console.log(response);
							});
	}
	
	
	$scope.nextPage = function(){
		$scope.pageCounter++;
		var to = $scope.pageCounter * 10;
		var from = to - 9;
		
		$http.get('/api/user/verified/' +from+'-'+to, {headers: {'authToken': $scope.token}} ).then(function successCallback(response){
			$scope.verified = angular.copy(response.data);
		}, function errorCallback(response){
			
			
		});
		
	
		
	}
	
	$scope.previousPage = function(){
		$scope.pageCounter--;
		if($scope.pageCounter >= 1){
			var to = $scope.pageCounter * 10;
			var from = to - 9;
			
			$http.get('/api/user/verified/' +from+'-'+to, {headers: {'authToken': $scope.token}} ).then(function successCallback(response){
				$scope.verified = angular.copy(response.data);
			}, function errorCallback(response){
				alert('error sthn unverified');
				
			});
		}
		
	}
	
	
	
	$scope.showUser = function(username){
		
		userDataService.setUsername(username);
		$state.go('main.userpreview');
		
	}
});