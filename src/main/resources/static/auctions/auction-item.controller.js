router.controller('itemController', function($scope, $state, $http,$cookies, $route, $stateParams, AuthenticationService, AuctionItemService){
	$scope.item = {};
	var auctionItemId = $stateParams.id;
	
	$scope.user = {};
	$scope.hasLatLon = null;
	$scope.hasBuyout = null;
	
	AuctionItemService.getAuctionItemById(token, auctionItemId)
						.then(function(response){
							$scope.item = response.data;
							var lat = $scope.item.geoLocationDto.latitude;
							var lon = $scope.item.geoLocationDto.longitude;
							if(lat == null || lon == null)
								$scope.hasLatLon = false;
							else
								$scope.hasLatLon = true;
							
							if($scope.item.buyout == undefined){
								$scope.hasBuyout = false;
							}else
								$scope.hasBuyout = true;
							
							
							latlon = new google.maps.LatLng(lat, lon)
						    mapholder = document.getElementById('mapholder')
						    mapholder.style.height = '300px';
						    mapholder.style.width = '300px';

						    var myOptions = {
						    		center:latlon,zoom:14,
						    		mapTypeId:google.maps.MapTypeId.ROADMAP,
						    		mapTypeControl:false,
						    		navigationControlOptions:{style:google.maps.NavigationControlStyle.SMALL}
						    }
						    
						    var map = new google.maps.Map(document.getElementById("mapholder"), myOptions);
						    var marker = new google.maps.Marker({position:latlon,map:map,title:"You are here!"});
						}, function(response){
							console.log(response);
						});	
	
	

});