router.controller('itemController', function($scope, $state, $http,$cookies, $route, AuthenticationService){

	$scope.item = {};
	$scope.user = {};
	$scope.hasLatLon = null;
	$scope.hasBuyout = null;
	
	$scope.item = $cookies.getObject('item');
	var lat = $scope.item.geoLocationDto.latitude;
	var lon = $scope.item.geoLocationDto.longitude;

	if(lat == null || lon == null){
			$scope.hasLatLon = false;
		
	}else
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
	
	$scope.signedIn = false;
	if($cookies.get('signedIn') === 'yes'){
		
		$scope.user.userId = $cookies.get('userId');
		$scope.signedIn = true;
		var token = $cookies.get('authToken');
		$http.get('/api/user/'+ $scope.user.userId, {headers: {'authToken': token}}).then(function successCallback(response){
			$scope.user = angular.copy(response.data);
			}, function errorCallback(response){
			alert("error");
			
			
		});
		
		
			
			
	}
})