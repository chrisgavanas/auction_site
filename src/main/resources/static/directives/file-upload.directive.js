var fileUpload = router.directive('fileUpload', ['$parse', function($parse){
	function fn_link(scope, element, attrs){
		var onChange = $parse(attrs.fileUpload);
		element.on('change', function(event){
			onChange(scope, { $files: event.target.files});
		});
	};
	
	return {
		link: fn_link
	}
}])


