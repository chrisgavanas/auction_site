router.controller('newAuctionController', function(Upload,$scope, $timeout,$state, $http,$cookies, $route, AuthenticationService, AuctionItemService){
	//$scope.allowedLocation = this.getPosition();
	
	$scope.item = {};
	$scope.item.auctionItemId = null;
	$scope.item ={
		categoryIds: []
	};
	$scope.files2 = [];
	$scope.item.images = [];
	$scope.submit = false;
	$scope.selected = [];
	$scope.item.geoLocationDto = {};
	$scope.categoryIds = {};
	$scope.countries = {};
	$scope.categoryCache = [];
	$scope.images = [];
	$scope.bytes = [];
	$scope.categoryIds = {};
	$scope.notValidLocation = false;
	$scope.categoryMissing = false;
	
	
	if($scope.signedIn == false)
		$state.go('main.signedout');
	
	AuctionItemService.getCategories($scope.token)
						.then(function(response){
							$scope.categoryIds = angular.copy(response.data);
			
						}, function(response){
							console.log(response);
						});
	
	
	$scope.cont = function(){
	console.log($scope.item);
		$scope.item.userId = $scope.user.userId;
		if($scope.item.country == null)
			AuctionItemService.getCountry($scope.item.geoLocationDto.latitude,$scope.item.geoLocationDto.longitude)
					.then(function (response){
						$scope.item.country = response.data.results[response.data.results.length - 1].formatted_address;
					})
		if($scope.item.country == 'United States')
			$scope.item.country = 'USA';
		if($scope.submit == true){
			AuctionItemService.addAuctionItem($scope.token, $scope.item)
								.then(function(response){
									$state.go('main.profile.userAuctions');
								}, function(response){
									console.log(response);
								});
		}else
			$scope.categoryMissing = true;
	};
	
	
	$scope.getCurrentLocation = function(){
		$scope.pos = this.getPosition();
	
		$scope.item.geoLocationDto.latitude = $scope.pos.lat();
		$scope.item.geoLocationDto.longitude = $scope.pos.lng();
		AuctionItemService.getCountry($scope.item.geoLocationDto.latitude,$scope.item.geoLocationDto.longitude)
			.then(function (response){
				
				if(response.data.results.length == 0)
					$scope.notValidLocation = true;
				else{
					$scope.notValidLocation = false;
					$scope.item.country = response.data.results[response.data.results.length - 1].formatted_address;
				}
				
			});
	   
	};
	
	$scope.loadSubcategories = function(category){
		
		$scope.selected.push(category);
		$scope.item.categoryIds.push(category.categoryId);
		if(category.subCategories.length != 0){
			$scope.submit = false;
		
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
    console.log(files);
        
        $scope.errFiles = errFiles;
        angular.forEach(files, function(file) {
      	
            file.upload = Upload.upload({
                url: '/api/auctionitem/user/' + $scope.user.userId + '/upload',
                data: {file: file}, headers: {'authToken': $scope.token}
            });

            file.upload.then(function (response) {
                $timeout(function () {
                	
                    $scope.item.images.push(response.data);
                  
                   AuctionItemService.getImage(response.data)
                    		.then(function(response){
                    			
                    			$scope.bytes.push(response.data);
                    			
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
	
	$scope.deleteFile = function(file){
		for (var i =0; i < $scope.files.length; i++)
			   if ($scope.files[i].result === file.result) {
			      $scope.files.splice(i,1);
			      $scope.item.images.splice(i,1);
			      break;
			   }
	
		
	};
	
	$scope.deleteExisting = function(i){
		
		$scope.bytes.splice(i,1);
			      $scope.item.images.splice(i,1);
			     
			
		
		
		
	};
	
	$scope.setCountry = function(){
		
		if($scope.item.geoLocationDto.latitude != null && $scope.item.geoLocationDto.longitude != null)
			AuctionItemService.getCountry($scope.item.geoLocationDto.latitude,$scope.item.geoLocationDto.longitude)
			.then(function (response){
				
				if(response.data.results.length == 0)
					$scope.notValidLocation = true;
				else{
					$scope.notValidLocation = false;
					$scope.item.country = response.data.results[response.data.results.length - 1].formatted_address;
					if($scope.item.country == 'United States')
						$scope.item.country = 'USA';
				}
			});
		else
			$scope.item.country = null;
	}
});

