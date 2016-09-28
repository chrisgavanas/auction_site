router.controller('userAuctionsActiveController', function($state, $scope, $http, $cookies, AuthenticationService, AuctionItemService){
	
	$scope.items = {};
	$scope.pageCounter = 1;
	$scope.hasAuctions = false;
	$scope.to = null;
	$scope.from = 1;
	$scope.bidds  = [];
	
	if($scope.signedIn == false)
		$state.go('main.signedout');
	
	AuctionItemService.getAuctionItemsOfUserByStatus($scope.token, $scope.user.userId, "ACTIVE","1", "10")
						.then( function(response){
							if(response.data.length != 0)
									$scope.hasAuctions = true;
									$scope.items = {};
		
									$scope.items = response.data;
									$scope.to = $scope.items.length;
									var i;
								for(i = 0; i < $scope.items.length; i++){
									if($scope.items[i].buyout == null)
										$scope.items[i].hasBuyout = false;
										else
											$scope.items[i].hasBuyout = true;
									}
						}, function(response){
							console.log(response);
						});	
		
	$scope.timeUntil = function(stDate) {
	    var d = new Date(stDate);
	    var diff = d - new Date();
	    
	    diff = Math.abs(diff);
	  
	    var days = Math.round(diff / 8.64e7);
	    var hours = diff % 8.64e7 / 3.6e6 | 0;
	    var mins = diff % 3.6e6 / 6e4 | 0;
	    var secs = Math.round(diff % 6e4 / 1e3);
	      
	    return days + 'd ' + hours + 'h ' + mins + 'm ' + secs + 's';
	}
	
	$scope._timeLeft = [];
	
	var intervalID = window.setInterval(function() {
		for (var i=0; i<$scope.items.length; i++) {
			$scope._timeLeft[i] = $scope.timeUntil($scope.items[i].endDate);
		}
		$scope.$apply();
	}, 1000);
	  
	$(document).ready(function(){
			$('[data-toggle="popover"]').popover();
	});
	
	$scope.nextPage = function (){
		$scope.pageCounter++;
		$scope.to  = $scope.pageCounter * 10;
		$scope.from = $scope.to  - 9;
		AuctionItemService.getAuctionItemsOfUserByStatus($scope.token, $scope.user.userId, "ACTIVE",$scope.from, $scope.to )
		.then( function(response){
			if(response.data.length != 0)
					$scope.hasAuctions = true;
					$scope.items = {};

					$scope.items = response.data;
					
					var i;
				for(i = 0; i < $scope.items.length; i++){
					if($scope.items[i].buyout == null)
						$scope.items[i].hasBuyout = false;
						else
							$scope.items[i].hasBuyout = true;
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
		AuctionItemService.getAuctionItemsOfUserByStatus($scope.token, $scope.user.userId, "ACTIVE",$scope.from, $scope.to )
		.then( function(response){
			if(response.data.length != 0)
					$scope.hasAuctions = true;
					$scope.items = {};

					$scope.items = response.data;
					
					var i;
				for(i = 0; i < $scope.items.length; i++){
					if($scope.items[i].buyout == null)
						$scope.items[i].hasBuyout = false;
						else
							$scope.items[i].hasBuyout = true;
					}
		}, function(response){
			console.log(response);
		});	
	}
	
	$scope.getOffers = function(auctionItemId){
		AuctionItemService.getBidsOfAuctionItem($scope.token, auctionItemId)
						.then(function(response){
							console.log(response);
							$scope.bidds = response.data;
						
						}, function(response){
							console.log(response);
						})
	}
})