router.controller('editAuctionController', function(Upload, NgMap,$stateParams,$scope,$timeout, $state, $http,$cookies, $route, AuthenticationService, AuctionItemService){
	
	$scope.bytes = [];
	$scope.item = {};
	$scope.item ={
		categories: []
	};
	$scope.selected = [];
	$scope.item.geoLocationDto = {};
	$scope.categories = {};
	
	$scope.categoryCache = [];
	var auctionId = $stateParams.id;
	var unchanged = true;
	$scope.images = [];
	$scope.imagesCounter = [];
	$scope.submit = false;
	$scope.selectedAll = [];
	
	if($scope.signedIn == false)
		$state.go('main.signedout');
	AuctionItemService.getCategories($scope.token)
						.then(function(response){
							$scope.categoryIds = angular.copy(response.data);
							$scope.categoryCache = angular.copy($scope.categoryIds);
							
						}, function(response){
							console.log(response);
						});

	AuctionItemService.getAuctionItemById($scope.token, auctionId)
					.then(function(response){
						$scope.item = response.data;
						if($scope.item.userId != $scope.user.userId)
							$state.go('main.forbidden');
						$scope.selected = $scope.item.categoryIds
						
						for (i = 0; i < $scope.item.images.length; i++){
							$http.get('/api/auctionitem/image?imagePath='+$scope.item.images[i])
                    			.then(function(response){
                    			
                    				$scope.bytes.push(response.data);
                    		
                    			}, function(response){
                    				console.log(response);
                    			});
						}
						for(i = 0; i < $scope.item.categoryIds.length; i++)
							$scope.selectedAll.push($scope.item.categoryIds[i].categoryId);
						console.log($scope.images);
					}, function(response){
						if(response.status == 404)
							$state.go('main.notfound');
		
					});
	
	$scope.cont = function(){
		
		$scope.item.userId = $scope.user.userId;
		$scope.item.categoryIds = $scope.selectedAll;
		console.log($scope.item);
		if($scope.submit == true || unchanged){
			AuctionItemService.editAuctionItem($scope.token, auctionId, $scope.item)
							.then(function(response){
									$state.go('main.profile.userAuctions');
							}, function(response){
								console.log(response);
							});
		}
	};
	
	
	
	$scope.getCurrentLocation = function(){
		$scope.pos = this.getPosition();
		$scope.item.geoLocationDto.latitude = $scope.pos.lat();
		$scope.item.geoLocationDto.longitude = $scope.pos.lng();
		console.log($scope.item);
		$http.get('https://maps.googleapis.com/maps/api/geocode/json?latlng='+$scope.item.geoLocationDto.latitude+','+$scope.item.geoLocationDto.longitude+'&sensor=false&language=en')
		.then(function (response){
			$scope.item.country = response.data.results[response.data.results.length - 1].formatted_address;
		})
	   
	};
	
	$scope.loadSubcategories = function(category){
		if(unchanged){
			$scope.selected = [];
			$scope.item.categoryIds = [];
			unchanged = false;
			$scope.selectedAll = [];
			
		}
			
		$scope.selected.push(category);
		$scope.selectedAll.push(category.categoryId);
		if(category.subCategories.length != 0){
			$scope.submit = false;
			
			$scope.categoryIds = category.subCategories;
			
		}else{
			$scope.submit = true;
			$scope.categoryIds = [category];
		}
	}
	
	$scope.clear = function(){
		$scope.selectedAll = [];
		$scope.categoryIds = $scope.categoryCache;
		$scope.selected = [];
	}
	
	$scope.uploadFiles = function(files, errFiles) {
        $scope.files = files;
       
        $scope.errFiles = errFiles;
        angular.forEach(files, function(file) {
      	
            file.upload = Upload.upload({
                url: '/api/auctionitem/user/' + $scope.user.userId + '/upload',
                data: {file: file}, headers: {'authToken': $scope.token}
            });

            file.upload.then(function (response) {
                $timeout(function () {
                	$scope.item.images.push(response.data);
                    
                    $http.get('/api/auctionitem/image?imagePath='+response.data)
                    		.then(function(response){
                    			
                    			$scope.bytes.push(response.data);
                    			console.log($scope.bytes);
                    		}, function(response){
                    			console.log(response);
                    		})
            	
                   
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
	
	$scope.deleteExisting = function(i){
		console.log(i);
		$scope.bytes.splice(i,1);
			      $scope.item.images.splice(i,1);
		
	};
});
