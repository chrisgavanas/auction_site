router.controller('editAuctionController', function(Upload, NgMap,$stateParams,$scope,$timeout, $state, $http,$cookies, $route, AuthenticationService, AuctionItemService){
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
	var unchanged = true;
	$scope.images = [];
	$scope.imagesCounter = [];
	$scope.submit = false;
	$scope.selectedAll = [];
	var token = $cookies.get('authToken');

	AuctionItemService.getCategories(token)
						.then(function(response){
							$scope.categoryIds = angular.copy(response.data);
							$scope.categoryCache = angular.copy($scope.categoryIds);
							
						}, function(response){
							console.log(response);
						});

	AuctionItemService.getAuctionItemById(token, auctionId)
	.then(function(response){
		$scope.item = response.data;
		$scope.selected = $scope.item.categoryIds
		for (i = 0; i < $scope.item.images.length; i++){
			var res = $scope.item.images[i].replace(/\\/g, '/');
			var res2 =res.split('/static/');
			console.log(res2);
			$scope.images.push("./"+res2[1]);
			if(i!=0)
				$scope.imagesCounter.push[i];
		}
		for(i = 0; i < $scope.item.categoryIds.length; i++)
			$scope.selectedAll.push($scope.item.categoryIds[i].categoryId);
		console.log($scope.images);
	}, function(response){
		alert("error");
	});
	
	$scope.cont = function(){
		
		$scope.item.userId = $scope.user.userId;
		$scope.item.categoryIds = $scope.selectedAll;
		console.log($scope.item);
		if($scope.submit == true || unchanged){
			AuctionItemService.editAuctionItem(token, auctionId, $scope.item)
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
			console.log(category.description + category.categoryId);
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
                data: {file: file}, headers: {'authToken': token}
            });

            file.upload.then(function (response) {
                $timeout(function () {
                    file.result = response.data;
                    $scope.item.images.push(file.result);
                    var res = file.result.replace(/\\/g, '/');
            		var res2 =res.split('/static/');
            		
            		$scope.images.push("./"+res2[1]);
                   
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
	
	$scope.deleteExisting = function(url){
		for (var i =0; i < $scope.images.length; i++){
			   if ($scope.images[i] === url) {
			     
			      $scope.images.splice(i,1);
			      break;
			   }
		}
		var res2 =url.split('./');
		var fullUrl = './src/main/resources/static/' + res2[1];
		for (var i =0; i < $scope.item.images.length; i++){
			   if ($scope.item.images[i] === fullUrl.replace(/\//g,"\\")) {
			     
			      $scope.item.images.splice(i,1);
			      break;
			   }
		}
		console.log(fullUrl.replace(/\//g,"\\"));
		console.log($scope.item.images);
	};
});
