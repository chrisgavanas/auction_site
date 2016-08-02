var newAuctionController = router.controller('newAuctionController', function($scope, $state, $http,$cookies, $route, AuthenticationService, itemDataService){
	$scope.user = {};
	$scope.signedIn = {};
	$scope.item = {};
	$scope.item ={
		categories: []
	};
	$scope.item.geoLocationDto = {};
	$scope.categories = {};
	
	
	//if($cookies.getObject('item') != undefined){
	//	$scope.item = $cookies.getObject('item');
	//}
	
	
	
	
	
	if($cookies.get('signedIn') === 'yes'){
		$scope.user.userId = $cookies.get('userId');
		$scope.signedIn = true;
		var token = $cookies.get('authToken');
		$http.get('/api/user/'+ $scope.user.userId, {headers: {'authToken': token}}).then(function successCallback(response){
			$scope.user.username = response.data.username;
			$http.get('/api/category', {headers: {'authToken': token}}).then(function successCallback(response){
				$scope.categories = angular.copy(response.data);
				console.log($scope.categories[0].description);
			}, function errorCallback(response){
				alert("error");
				
				
			});
			
		}, function errorCallback(response){
			alert("error");
			
			
		});
		
		
		
	}else
		$scope.signedIn = false;
	
	
	$scope.myProfile = function(){
		$state.go("main.profile");
	};

	$scope.cont = function(){
		console.log($scope.item);
		$scope.item.userId = $scope.user.userId;
		$scope.item.images = [];
		$scope.item.categories.push($scope.selectedCat.categoryId);
		$cookies.putObject('item', $scope.item);
		$state.go('main.profile.userAuctions');
		
	};
	
	var formdata = new FormData();
	$scope.getTheFiles = function($files){
		console.log($files);
		angular.forEach($files, function(value, key){
			console.log(value);
			formdata.append(key, value);
			formdata.append(2, "koula");
		});
		alert(formdata);
	};
});