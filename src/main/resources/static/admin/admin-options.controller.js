router.controller('adminOptionsController', function($state, $scope, $cookies, $http, AuthenticationService){
	$scope.user = {};
	$scope.nouminaia = "nouminaia";
	$scope.signedIn = {};

	console.log("geia");
	if($cookies.get('signedIn') === 'yes'){
		$scope.user.userId = $cookies.get('userId');
		$scope.signedIn = true;
		var token = $cookies.get('authToken');
		
		$http.get('/api/user/'+ $scope.user.userId, {headers: {'authToken': token}}).then(function successCallback(response){
			$scope.user = angular.copy(response.data);

		}, function errorCallback(response){
			
			$cookies.remove('userId');
			$cookies.remove('authToken');
			$cookies.put('signedIn', 'no');
			$state.go('main.welcome');
			
		});
		
		
		
	}
	
	
	$scope.koula = function(){
		$state.go('main.verified');
	};
	
	$scope.kitsos = function(){
		$state.go('main.unverified');
	};
	
	$scope.exportToXML = function(){
		AuthenticationService.exportToXML(token)
								.then(function(response){
									console.log(response);
								}, function(response){
									console.log(response);
								});
	};
});