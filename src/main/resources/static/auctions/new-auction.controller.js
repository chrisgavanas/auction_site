router.controller('newAuctionController', function(Upload,$scope, $timeout,$state, $http,$cookies, $route, AuthenticationService, AuctionItemService){
	$scope.user = {};
	$scope.signedIn = {};
	$scope.item = {};
	 $scope.item.auctionItemId = null;
	$scope.item ={
		categoryIds: []
	};
	$scope.submit = false;
	$scope.selected = [];
	$scope.item.geoLocationDto = {};
	$scope.categoryIds = {};
	$scope.user.userId = $cookies.get('userId');
	$scope.categoryCache = [];
	$scope.item.images = [];
	var token = $cookies.get('authToken');

	/*NgMap.getMap().then(function(map) {
	    console.log(map.getCenter());
	    console.log('markers', map.markers);
	    console.log('shapes', map.shapes);
	  });*/
	
	AuctionItemService.getCategories(token)
						.then(function(response){
							$scope.categoryIds = angular.copy(response.data);
							$scope.categoryCache = angular.copy($scope.categoryIds);
							console.log($scope.categoryIds);
						}, function(response){
							console.log(response);
						});

	$scope.cont = function(){
	
		$scope.item.userId = $scope.user.userId;
		
		
		if($scope.submit == true){
			AuctionItemService.addAuctionItem(token, $scope.item)
								.then(function(response){
									console.log(response);
									$state.go('main.profile.userAuctions');
								}, function(response){
									console.log(response);
								});
		}else{
			alert("tryagain");
		}
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
		$scope.item.categoryIds.push(category.categoryId);
		if(category.subCategories.length != 0){
			$scope.submit = false;
			console.log(category.description + category.categoryId);
			$scope.categoryIds = category.subCategories;
			
		}else{
			$scope.submit = true;
			$scope.categoryIds = [category];
		}
	}
	
	$scope.clear = function(){
		$scope.item.categoryIds = [];
		$scope.categoryIds = $scope.categoryCache;
		$scope.selected = [];
	}
	
	////////////
	$scope.uploadFiles = function(files, errFiles) {
        $scope.files = files;
        $scope.errFiles = errFiles;
        angular.forEach(files, function(file) {
        	console.log("to id einai: "+$scope.item.auctionItemId);
            file.upload = Upload.upload({
                url: '/api/auctionitem/'+ $scope.item.auctionItemId +'/user/'+ $scope.user.userId+ '/upload',
                data: {file: file}
            });

            file.upload.then(function (response) {
                $timeout(function () {
                    file.result = response.data;
                    $scope.item.auctionItemId = file.result;
                    console.log(file.result);
                   
                });
            }, function (response) {
                if (response.status > 0)
                    $scope.errorMsg = response.status + ': ' + response.data;
                	console.log(response);
            }, function (evt) {
                file.progress = Math.min(100, parseInt(100.0 * 
                                         evt.loaded / evt.total));
            });
        });
    }
});