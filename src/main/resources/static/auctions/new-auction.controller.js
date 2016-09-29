router.controller('newAuctionController', function(Upload,$scope, $timeout,$state, $http,$cookies, $route, AuthenticationService, AuctionItemService){

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
	
	if($scope.signedIn == false)
		$state.go('main.signedout');
	
	AuctionItemService.getCategories($scope.token)
		.then(function(response){
			$scope.categoryIds = angular.copy(response.data);
			
		}, function(response){
			console.log(response);
		});
	
	
	$scope.cont = function(){
	
		$scope.item.userId = $scope.user.userId;
		console.log($scope.item);
		
		if($scope.item.country == 'United States')
			$scope.item.country = 'USA';
		if($scope.submit == true){
			AuctionItemService.addAuctionItem($scope.token, $scope.item)
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
		console.log($scope.item.geoLocationDto);
		$http.get('https://maps.googleapis.com/maps/api/geocode/json?latlng='+$scope.item.geoLocationDto.latitude+','+$scope.item.geoLocationDto.longitude+'&sensor=false&language=en')
			.then(function (response){
				$scope.item.country = response.data.results[response.data.results.length - 1].formatted_address;
			})
	   
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
	
	$scope.deleteExisting = function(i){
		console.log(i);
		$scope.bytes.splice(i,1);
			      $scope.item.images.splice(i,1);
			     
			
		
		
		
	};
});

