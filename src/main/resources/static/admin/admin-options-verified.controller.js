router.controller('adminOptionsVerifiedController', function($state, $scope, $cookies, $http, AuthenticationService, AdminService){
	
	$scope.verified = {};
	$scope.usernamesAndIds = [];
	$scope.pageCounter = 1;
	
	
	if($scope.signedIn == false)
		$state.go('main.signedout');
		
	AdminService.getVerified($scope.token, 1, 10)
				.then(function successCallback(response){
					$scope.verified = angular.copy(response.data);
			
					var i;
					for(i = 0; i < $scope.verified.length; i ++){
						$scope.usernamesAndIds.push( { id: $scope.unverified[i].userId, text: $scope.unverified[i].username } );
					}
			
				}, function errorCallback(response){
					$cookies.remove('userId');
					$cookies.remove('authToken');
					$cookies.put('signedIn', 'no');
					
					$scope.signedIn = false;
					$state.go('main.signedout');
			
				});
		
	
	
	
	$scope.nextPage = function(){
		$scope.pageCounter++;
		var to = $scope.pageCounter * 10;
		var from = to - 9;
		
		AdminService.getVerified($scope.token, from, to)
					.then(function successCallback(response){
						$scope.verified = angular.copy(response.data);
					}, function errorCallback(response){
						$cookies.remove('userId');
						$cookies.remove('authToken');
						$cookies.put('signedIn', 'no');
						
						$scope.signedIn = false;
						$state.go('main.signedout');
			
					});
		
	
		
	};
	
	$scope.previousPage = function(){
		$scope.pageCounter--;
		if($scope.pageCounter >= 1){
			var to = $scope.pageCounter * 10;
			var from = to - 9;
			
			AdminService.getVerified($scope.token, from, to)
						.then(function successCallback(response){
							$scope.verified = angular.copy(response.data);
						}, function errorCallback(response){
							$cookies.remove('userId');
							$cookies.remove('authToken');
							$cookies.put('signedIn', 'no');
							
							$scope.signedIn = false;
							$state.go('main.signedout');
						});
		}
		
	};
	
	
	
	$scope.showUser = function(username){
		$state.go('main.userpreview');
		
	};
});