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
	$scope.range = {};
	
	
	
	
	
	$scope.range.from = $scope.searchData.priceFrom = $stateParams.from;
	$scope.range.to = $scope.searchData.priceTo = $stateParams.to;
	
	if($stateParams.country == undefined)
		$scope.searchData.country = "";
	else
		$scope.searchData.country = $stateParams.country;
	if($stateParams.input == undefined)
		$scope.searchData.text = "";
	else
		$scope.searchData.text = $stateParams.input;
	
	if($stateParams.sellerId == undefined)
		$scope.searchData.sellerId = "";
	else
		$scope.searchData.sellerId = $stateParams.sellerId;
	console.log($stateParams);
	
	
	$http.get('https://restcountries.eu/rest/v1/all')
			.then(function(response){
				
				$scope.countries.array = response.data;
				$scope.countries.array.unshift({name:'All countries'});
				if($stateParams.country != undefined){
					var index = $scope.countries.array.map(function(e) { return e.name; }).indexOf($stateParams.country);
					$scope.countries.country = $scope.countries.array[index];
				}else{
					console.log('mphka');
					$scope.countries.country = $scope.countries.array[0];
				}
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
		console.log($scope.searchData);
		AuctionItemService.search(1, 10, $scope.searchData)
			.then(function(response){
				$scope.searchResults = response.data;
				console.log(response);
		
		
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
		
		$state.go('main.search', {input: $scope.searchData.text, catId: category.categoryId, country: $stateParams.country, from: $stateParams.from, to: $stateParams.to, sellerId: $stateParams.sellerId});
	};
	
	
	$scope.reset = function(){
		$state.go('main.search', {input: $scope.searchData.text, catId: 'ALL', country: $stateParams.country, from: $stateParams.from, to: $stateParams.to, sellerId: $stateParams.sellerId});
	}
	
	$scope.goBack = function(categoryId){
		$state.go('main.search', {input: $scope.searchData.text, catId: categoryId, country: $scope.currentCountry, from: $stateParams.from, to: $stateParams.to, sellerId: $stateParams.sellerId});
	}
	
	$scope.filterCountry = function(country){
		console.log(country.name);
		if(country.name == "All countries")
			$scope.currentCountry = "";
		else
			$scope.currentCountry = country.name;
		if($scope.currentCountry == 'United States')
			$scope.currentCountry = 'USA';
		$state.go('main.search', {input: $scope.searchData.text, catId: $stateParams.catId, country: $scope.currentCountry, from: $stateParams.from, to: $stateParams.to, sellerId: $stateParams.sellerId});
	}
	
	$scope.filterPrice = function(range){
		console.log(range);
		$state.go('main.search', {input: $scope.searchData.text, catId: $stateParams.catId, country: $stateParams.country, from : range.from, to: range.to, sellerId: $stateParams.sellerId});
	}
});