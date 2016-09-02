router.controller('newAuctionController', function(Upload,$scope, $timeout,$state, $http,$cookies, $route, AuthenticationService, AuctionItemService){
	$scope.user = {};
	$scope.signedIn = {};
	$scope.item = {};
	$scope.item.auctionItemId = null;
	$scope.item ={
		categoryIds: []
	};
	$scope.item.images = [];
	$scope.submit = false;
	$scope.selected = [];
	$scope.item.geoLocationDto = {};
	$scope.categoryIds = {};
	$scope.user.userId = $cookies.get('userId');
	$scope.categoryCache = [];

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
		console.log($scope.item);
		
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
      	
            file.upload = Upload.upload({
                url: '/api/auctionitem/user/' + $scope.user.userId + '/upload',
                data: {file: file}, headers: {'authToken': token}
            });

            file.upload.then(function (response) {
                $timeout(function () {
                    file.result = response.data;
                    $scope.item.images.push(file.result);
                    
                   
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
	
	$scope.deleteFile = function(file){
		for (var i =0; i < $scope.files.length; i++)
			   if ($scope.files[i].result === file.result) {
			      $scope.files.splice(i,1);
			      $scope.item.images.splice(i,1);
			      break;
			   }
	
		
	};
});

/*<span class="progress" ng-show="file.progress >= 0">
<div style="width:{{file.progress}}%"  
	 ng-bind="file.progress + '%'"></div>
</span>*/
/*<li ng-repeat="f in errFiles" style="font:smaller"><font size="1">{{f.name}} {{f.$error}} {{f.$errorParam}}</font>
</li> */