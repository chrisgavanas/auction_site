 var itemDataService = router.service("itemDataService", function () {
	 var item = {};
	 
	 var setItem = function (item){
		 this.item = angular.copy(item);
		 console.log(this.item);
	};
	
	var getItem = function(){
		return this.item;
	}
	return {
		setItem: setItem,
		getItem: getItem
		};
 });