var newAuctionController = router.controller('newAuctionController', function($scope, $state, $http,$cookies, $route, AuthenticationService, itemDataService){
	$scope.user = {};
	$scope.signedIn = {};
	$scope.item = {};
	$scope.item ={
		categories: []
	};
	$scope.item.geoLocation = {};
	
	$scope.categories = [
	{id: 1, text:'Appliances'}, 
	{id: 2, text:'Gadgets'},
	{id: 3, text:'Hardware'},
	{id: 4, text:'koula'},
	{id: 5, text:'mitsos'},
	{id: 6, text:'takis'},
	{id: 7, text:'aleksis'}, 
	{id: 8, text:'kristo'},
	{id: 9, text:'maria'}
	];
	
	if( $cookies.getObject('item') != null){
		$scope.item = $cookies.getObject('item');
	}
	/*$http.get('/api/category').then(function successCallback(response){
		console.log(response);
	}), function errorCallback(response){
		alert("error");
		
	}*/
	
	
	
	
	
	if($cookies.get('signedIn') === 'yes'){
		$scope.user.userId = $cookies.get('userId');
		$scope.signedIn = true;
		var token = $cookies.get('authToken');
		$http.get('/api/user/'+ $scope.user.userId, {headers: {'authToken': token}}).then(function successCallback(response){
			$scope.user.username = response.data.username;
		}, function errorCallback(response){
			alert("error");
			
			
		});
		
		
		
	}else
		$scope.signedIn = false;
	
	
	$scope.myProfile = function(){
		$state.go("profile");
	};

	$scope.cont = function(){
		console.log($scope.item);
		$scope.item.userId = $scope.user.userId;
		$scope.item.images = [];
		$cookies.putObject('item', $scope.item);
		$state.go('itemPreview');
		
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