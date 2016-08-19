

router.controller('editAuctionController', function( NgMap,$stateParams,$scope, $state, $http,$cookies, $route, AuthenticationService, AuctionItemService){
	$scope.user = {};
	$scope.signedIn = {};
	$scope.item = {};
	$scope.item ={
		categories: []
	};
	$scope.selected = [];
	$scope.item.geoLocationDto = {};
	$scope.categories = {};
	$scope.user.userId = $cookies.get('userId');
	$scope.categoryCache = [];
	var auctionId = $stateParams.id;
	var cleared = false;
	var token = $cookies.get('authToken');

	/*NgMap.getMap().then(function(map) {
	    console.log(map.getCenter());
	    console.log('markers', map.markers);
	    console.log('shapes', map.shapes);
	  });*/
	
	AuctionItemService.getCategories(token)
						.then(function(response){
							$scope.categories = angular.copy(response.data);
							$scope.categoryCache = angular.copy($scope.categories);
							console.log($scope.categories);
						}, function(response){
							console.log(response);
						});

	AuctionItemService.getAuctionItemById(token, auctionId)
	.then(function(response){
		$scope.item = response.data;
		$scope.item.categories = [];
	}, function(response){
		alert("error");
	});
	
	$scope.cont = function(){
	
		$scope.item.userId = $scope.user.userId;
		$scope.item.images = [];
		console.log("paw na koumpwsw");
		console.log($scope.item.categories);
		AuctionItemService.editAuctionItem(token, auctionId, $scope.item)
							.then(function(response){
									$state.go('main.profile.userAuctions');
							}, function(response){
								console.log(response);
							});
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
	
	$scope.getCurrentLocation = function(){
		$scope.pos = this.getPosition();
		$scope.item.geoLocationDto.latitude = $scope.pos.lat();
		$scope.item.geoLocationDto.longitude = $scope.pos.lng();
	   
	};
	
	$scope.loadSubcategories = function(category){
		console.log($scope.item.categoryResponseDtoList);
		if(!cleared){
			alert('clear first');
			
		}else{
			$scope.selected.push(category);
			$scope.item.categories.push(category.categoryId);
			$scope.item.categoryResponseDtoList.push(category);
			if(category.subCategories.length != 0){
				console.log(category.description + category.categoryId);
				$scope.categories = category.subCategories;
			
			}else{
				$scope.categories = [category];
			}
		}
	}
	
	$scope.clear = function(){
		$scope.item.categories = [];
		cleared = true;
		$scope.item.categoryResponseDtoList = [];
		$scope.categories = $scope.categoryCache;
		$scope.selected = [];
	}
});
