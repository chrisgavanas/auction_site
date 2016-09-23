router.controller('searchController', function($http, $stateParams, $scope, $state, AuthenticationService, AuctionItemService){
	
	$scope.searchResults = [];
	$scope.searchData = {};
	$scope.pageCounter = 1;
	$scope.countries = {};
	$scope.countries.country = {};
	$scope.to = 10;
	$scope.from = 1;
	$scope.searchData.text = $stateParams.input;
	$scope.searchData.categoryId = $stateParams.catId;
	$scope.currentCategory = {};
	$scope.clicked = false;
	
	
	
	
	
	$http.get('https://restcountries.eu/rest/v1/all')
			.then(function(response){
				
				$scope.countries.array = response.data;
				$scope.countries.country = $scope.countries.array[0];
			}, function(response){
				console.log(response);
			})
			
	if($stateParams.catId == 'ALL')
		AuctionItemService.getCategories($scope.token)
			.then(function(response){
				$scope.displayedCategories = angular.copy(response.data);
				$scope.categoriesBasic = angular.copy(response.data);
			
			}, function(response){
				console.log(response);
			});
	else
		AuctionItemService.getCategory($stateParams.catId)
		.then(function(response){
			
			
				
			
			$scope.currentCategory = response.data;
			
			if($scope.currentCategory.subCategories.length == 0)
				$scope.clicked = true;
			
				
			$scope.displayedCategories = angular.copy(response.data.subCategories);
			$scope.categoriesBasic = angular.copy(response.data.subCategories);
			
			if($scope.currentCategory.parentId != null)
				AuctionItemService.getCategory($scope.currentCategory.parentId)
									.then(function(response){
										$scope.parentCategory = response.data;
									}, function(response){
										console.log(response);
									});
			
		}, function(response){
			console.log(response);
		});
	
	var search = function(){
		
		AuctionItemService.search(1, 10, $scope.searchData)
			.then(function(response){
				$scope.searchResults = response.data;
		
		
		
				var i;
				for (i = 0; i < $scope.searchResults.length; i++){
			
					if($scope.searchResults[i].images.length > 0){
						var res = $scope.searchResults[i].images[0].replace(/\\/g, '/');
						var res2 =res.split('/static/');
						console.log(res2);
						$scope.searchResults[i].displayImage = res2[1];
					
					}else{
						$scope.searchResults[i].displayImage = null;
					}
		
			}
			
			}, function(response){
				console.log(response);
			});
	
	
	};
	
	
	search();
	
	$scope.nextPage = function (){
		$scope.pageCounter++;
		$scope.to  = $scope.pageCounter * 10;
		$scope.from = $scope.to  - 9;
		AuctionItemService.search($scope.from, $scope.to, $scope.searchData)
			.then(function(response){
				
				$scope.searchResults = response.data;
				var i;
				for (i = 0; i < $scope.searchResults.length; i++){
				
					
					if($scope.searchResults[i].images.length > 0){
						var res = $scope.searchResults[i].images[0].replace(/\\/g, '/');
						var res2 =res.split('/static/');
						console.log(res2);
						$scope.searchResults[i].displayImage = res2[1];
						if(i!=0)
							$scope.imagesCounter.push[i];
					}else{
						$scope.searchResults[i].displayImage = null;
					}
			
				}
			
			
			}, function(response){
				console.log(response);
			});
		
	}
	
	$scope.previousPage = function(){
		$scope.pageCounter--;
		if($scope.pageCounter >= 1){
			$scope.to  = $scope.pageCounter * 10;
			$scope.from = $scope.to  - 9;
		}
		AuctionItemService.search($scope.from, $scope.to, $scope.searchData)
			.then(function(response){
				$scope.searchResults = response.data;
				var i;
			for (i = 0; i < $scope.searchResults.length; i++){
			
					if($scope.searchResults[i].images.length > 0){
						var res = $scope.searchResults[i].images[0].replace(/\\/g, '/');
						var res2 =res.split('/static/');
						console.log(res2);
						$scope.searchResults[i].displayImage = res2[1];
						if(i!=0)
							$scope.imagesCounter.push[i];
					}else{
						$scope.searchResults[i].displayImage = null;
					}
			
				}
			
			}, function(response){
				console.log(response);
			});
	}

	$scope.filter = function(category){
		
		$state.go('main.search', {input: $scope.searchData.text, catId: category.categoryId});
	};
	
	
	$scope.reset = function(){
		$state.go('main.search', {input: $scope.searchData.text, catId: 'ALL'});
	}
	
	$scope.goBack = function(categoryId){
		$state.go('main.search', {input: $scope.searchData.text, catId: categoryId});
	}
	
});