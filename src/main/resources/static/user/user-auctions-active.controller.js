router.controller('userAuctionsActiveController', function($state, $scope, $http, $cookies, AuthenticationService, AuctionItemService){
	
	$scope.items = {};
	$scope.pageCounter = 1;
	$scope.hasAuctions = false;
	$scope.to = null;
	$scope.from = 1;
	$scope.bidds  = [];
	$scope.pageCounter = 1;
	$scope.totalAuctions = 0;
	
	$scope.pageNumbers = 0;
	$scope.maxSize = 5;

	$scope.bigCurrentPage = 1;
	$scope.currentPage = 4;
	if($scope.signedIn == false)
		$state.go('main.signedout');
	
	AuctionItemService.getAuctionItemsOfUserByStatus($scope.token, $scope.user.userId, "ACTIVE","1", "10")
						.then( function(response){
							if(response.data.length != 0)
									$scope.hasAuctions = true;
									$scope.items = {};
		
									$scope.items = response.data;
									$scope.to = $scope.items.length;
									$scope.totalAuctions = response.headers().totalauctions;
									$scope.pageNumbers = Math.ceil($scope.totalAuctions / 10);
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
	

	$scope.change = function (current){
		$scope.to = current * 10;
		$scope.from = $scope.to - 9;
		AuctionItemService.getAuctionItemsOfUserByStatus($scope.token, $scope.user.userId, "ACTIVE",$scope.from, $scope.to )
		.then( function(response){
			if(response.data.length != 0)
				$scope.hasAuctions = true;
			$scope.itemsSold = {};
			$scope.itemsSold = response.data;
			var i;
			for(i = 0; i < $scope.itemsSold.length; i++){
				if($scope.itemsSold[i].buyout == null)
					$scope.itemsSold[i].hasBuyout = false;
				else
					$scope.itemsSold[i].hasBuyout = true;

				if($scope.itemsSold[i].buyerId != null){

					AuthenticationService.getSeller($scope.itemsSold[i].buyerId, $scope.token)
						.then(function(response){
						
							$scope.buyerUsername = response.data.username;
						
						}, function (response){
							console.log(response);
						});
					$scope.itemsSold[i].buyerUsername = $scope.buyerUsername;

				}
			}
		}, function(response){
			console.log(response);
		});	
	}
	
	
	
	$scope.getOffers = function(auctionItemId){
		AuctionItemService.getBidsOfAuctionItem($scope.token, auctionItemId)
						.then(function(response){
							
							$scope.bidds = response.data;
						
						}, function(response){
							console.log(response);
						})
	}
})