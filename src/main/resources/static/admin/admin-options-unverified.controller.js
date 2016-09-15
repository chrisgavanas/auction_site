router.controller('adminOptionsUnverifiedController', function($state, $scope, $cookies, $http, AuthenticationService){
	

	$scope.unverified = {};
	
	$scope.usernamesAndIds = [];
	
	$scope.pageCounter = 1;
	
	
		
		
	$http.get('/api/user/unverified/1-10', {headers: {'authToken': $scope.token}} ).then(function successCallback(response){
		$scope.unverified = angular.copy(response.data);
			
		var i;
		for(i = 0; i < $scope.unverified.length; i ++){
				$scope.usernamesAndIds.push( { id: $scope.unverified[i].userId, text: $scope.unverified[i].username } );
		}
			
	}, function errorCallback(response){
			
			
	});
		
	
	
	
	
	$scope.getUnverified = function(form, to){
		AuthenticationService.getUnverified($scope.token, form, to)
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
		
		$http.get('/api/user/unverified/' +from+'-'+to, {headers: {'authToken': $scope.token}} ).then(function successCallback(response){
			$scope.unverified = angular.copy(response.data);
		}, function errorCallback(response){
			
			
		});
		
	
		
	}
	
	$scope.previousPage = function(){
		$scope.pageCounter--;
		if($scope.pageCounter >= 1){
			var to = $scope.pageCounter * 10;
			var from = to - 9;
			
			$http.get('/api/user/unverified/' +from+'-'+to, {headers: {'authToken': token}} ).then(function successCallback(response){
				$scope.unverified = angular.copy(response.data);
			}, function errorCallback(response){
				alert('error sthn unverified');
				
			});
		}
		
	}
	
	$scope.verify = function(username){
		var userId = null;
		var i;
		
		for(i = 0; i < $scope.usernamesAndIds.length; i++){
			if($scope.usernamesAndIds[i].text == username){
				userId = $scope.usernamesAndIds[i].id;
				break;
			}
				
		}
	
		$http.post('/api/user/'+userId+'/verify-user', {}, {headers: {'authToken': token}}).then(function successCallback(response){
			$state.go($state.current, {}, {reload: true});
			
		}, function errorCallback(response){
			alert(response.data.message);
			
		});
	}
	
	$scope.showUser = function(username){
		
		userDataService.setUsername(username);
		$state.go('main.userpreview');
		
	}
});