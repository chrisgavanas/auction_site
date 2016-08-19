router.controller('newAuctionController', function(Upload,$scope, $timeout,$state, $http,$cookies, $route, AuthenticationService, AuctionItemService){
	$scope.user = {};
	$scope.signedIn = {};
	$scope.item = {};
	$scope.item ={
		categories: []
	};
	$scope.selected = [];
	$scope.item.geoLocationDto = {};
	$scope.categories = {};
	//$scope.user.userId = $cookies.get('userId');
	$scope.categoryCache = [];
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

	$scope.cont = function(){
	
		$scope.item.userId = $scope.user.userId;
		$scope.item.images = [];
		console.log("paw na koumpwsw");
		console.log($scope.item.categories);
		AuctionItemService.addAuctionItem(token, $scope.item)
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
		
		$scope.selected.push(category);
		$scope.item.categories.push(category.categoryId);
		if(category.subCategories.length != 0){
			console.log(category.description + category.categoryId);
			$scope.categories = category.subCategories;
			
		}else{
			$scope.categories = [category];
		}
	}
	
	$scope.clear = function(){
		$scope.item.categories = [];
		$scope.categories = $scope.categoryCache;
		$scope.selected = [];
	}
	
	////////////
	$scope.uploadFiles = function(files, errFiles) {
        $scope.files = files;
        $scope.errFiles = errFiles;
        angular.forEach(files, function(file) {
            file.upload = Upload.upload({
                url: 'https://angular-file-upload-cors-srv.appspot.com/upload',
                data: {file: file}
            });

            file.upload.then(function (response) {
                $timeout(function () {
                    file.result = response.data;
                });
            }, function (response) {
                if (response.status > 0)
                    $scope.errorMsg = response.status + ': ' + response.data;
            }, function (evt) {
                file.progress = Math.min(100, parseInt(100.0 * 
                                         evt.loaded / evt.total));
            });
        });
    }
});