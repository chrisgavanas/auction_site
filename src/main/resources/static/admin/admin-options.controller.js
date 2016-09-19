router.controller('adminOptionsController', function($timeout,$window, $state, $scope, $cookies, $http, AuthenticationService){
	$scope.user = {};
	
	$scope.signedIn = {};


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
	$scope.xmldata = null;
	$scope.buttonClicked = false;
	$scope.prepareXML = function(){
		$scope.buttonClicked = true;
		$scope.wait = true;
		var promise = AuthenticationService.exportToXML(token);

								promise.then(function(response){
									$timeout(function () {
										console.log(response);
										$scope.xmldata = response.data;
										blob = new Blob([$scope.xmldata], { type: 'text/plain' }),
										url = $window.URL || $window.webkitURL;
										$scope.fileUrl = url.createObjectURL(blob);
									});
							    
								}, function(response){
									console.log(response);
								}, function (evt) {
									console.log(evt);
					                $scope.progress = Math.min(100, parseInt(100.0 * 
	                                         evt.loaded / evt.total));
								});
	};
});