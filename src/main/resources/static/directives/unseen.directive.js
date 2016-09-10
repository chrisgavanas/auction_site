router.directive('unseen', function () {
        return {
        	require: "ngModel",
    		restrict: '',
            link: function(scope, element, attrs, ngModel) {
                	if(ngModel.$modelValue == true)
                		
                	}, function(newValue) {
                     console.log(newValue);
                	});
              }

                
            }
        });