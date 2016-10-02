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
	$scope.bytes = [];
	$scope.noResults = false;
	$scope.totalAuctions = 0;
	
	$scope.pageNumbers = 0;
	$scope.maxSize = 5;

	$scope.bigCurrentPage = 1;
	 $scope.currentPage = 4;
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

	
	
	AuctionItemService.getAllCountries()
			.then(function(response){
				
				$scope.countries.array = response.data;
				$scope.countries.array.unshift({name:'All countries'});
				if($stateParams.country != undefined){
					var index = $scope.countries.array.map(function(e) { return e.name; }).indexOf($stateParams.country);
					$scope.countries.country = $scope.countries.array[index];
				}else{
					
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
	
		AuctionItemService.search(1, 10, $scope.searchData)
			.then(function(response){
				$scope.searchResults = response.data;
				$scope.totalAuctions = response.headers().totalauctions;
				$scope.pageNumbers = Math.ceil($scope.totalAuctions / 10);
				console.log($scope.pageNumbers);
				if($scope.searchResults.length == 0)
					$scope.noResults = true;
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
		
		if(country.name == "All countries")
			$scope.currentCountry = "";
		else
			$scope.currentCountry = country.name;
		if($scope.currentCountry == 'United States')
			$scope.currentCountry = 'USA';
		$state.go('main.search', {input: $scope.searchData.text, catId: $stateParams.catId, country: $scope.currentCountry, from: $stateParams.from, to: $stateParams.to, sellerId: $stateParams.sellerId});
	}
	
	$scope.filterPrice = function(range){
	
		$state.go('main.search', {input: $scope.searchData.text, catId: $stateParams.catId, country: $stateParams.country, from : range.from, to: range.to, sellerId: $stateParams.sellerId});
	}
	
	$scope.setPage = function (pageNo) {
	    $scope.currentPage = pageNo;
	  
	};
	  
	$scope.change = function (current){
		$scope.to = current * 10;
		$scope.from = $scope.to - 9;
		AuctionItemService.search($scope.from, $scope.to, $scope.searchData)
		.then(function(response){
			$scope.searchResults = response.data;
			
		
		}, function(response){
			console.log(response);
		});
	}

	  
});