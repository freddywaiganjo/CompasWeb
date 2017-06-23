/**
 * 
 */
/**
 * User Angular Module
 */
'use strict';
angular.module('app.service', []).controller("servicesCtrl", ["$scope", "$filter", "serviceSvc","categorySvc","$serviceValid", "$rootScope", "blockUI", "logger", "$location", function ($scope, $filter,serviceSvc,categorySvc, $serviceValid, $rootScope, blockUI, logger, $location) {

	var init;
	$scope.header="";
	$scope.services = [];
	$scope.curSelect="";
	$scope.serviceEditMode = false;
	$scope.priceActive=false;
	$scope.showUOM=true;
	$scope.showCur=true;
	$scope.uomSelect=""
	$scope.myCroppedImage=""
	// $scope.isDisabled=false;
	
	$scope.loadServiceData = function () {
		$scope.services = [], $scope.searchKeywords = "", $scope.filteredServices = [], $scope.row = "", $scope.serviceEditMode = false;
		serviceSvc.GetServices($scope).success(function (response) {
			return $scope.services = response, $scope.searchKeywords = "", $scope.filteredServices = [], $scope.row = "", $scope.select = function (page) {
				var end, start;
				return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPageServices = $scope.filteredServices.slice(start, end)
			}, $scope.onFilterChange = function () {
				return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
			}, $scope.onNumPerPageChange = function () {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.onOrderChange = function () {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.search = function () {
				return $scope.filteredServices = $filter("filter")($scope.services, $scope.searchKeywords), $scope.onFilterChange()
			}, $scope.order = function (rowName) {
				return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredServices = $filter("orderBy")($scope.services, rowName), $scope.onOrderChange()) : void 0
			}, $scope.numPerPageOpt = [3, 5, 10, 20], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentPages = [], (init = function () {
				return $scope.search(), $scope.select($scope.currentPage)
			})();
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
		});
	}

	$scope.loadServiceData();
	$scope.loadCategoryData = function () {
        $scope.categories = []
        categorySvc.GetCategories().success(function (response) {
         $scope.categories = response
        })
	}
	$scope.loadCategoryData()
	$scope.loadCurrencies = function () {
		$scope.currencies = [];
		serviceSvc.GetCurrencies().success(function (response) {
			$scope.currencies = response
		})
	}
	
	$scope.loadUoms = function () {
		$scope.uoms = [];
		serviceSvc.GetUoms().success(function (response) {
			$scope.uoms = response
		})
	}
	$scope.loadUoms();
	$scope.loadCurrencies();

	$scope.editService = function (service) {
// if (!$filter('checkRightToEdit')($rootScope.UsrRghts.rightsHeaderList, "#" +
// $location.path())) {
// logger.log("Oh snap! You are not allowed to modify the project.");
// return false;
// }
		$scope.header="Edit Commodity";
		$scope.serviceEditMode = true;
		$scope.isExisting = true;
		$scope.serviceId = service.serviceId;
		$scope.serviceCode = service.serviceCode;
		$scope.serviceName = service.serviceName;
		$scope.active = service.active;
		$scope.minPrice=service.minPrice;
		$scope.maxPrice=service.maxPrice;
		$scope.categorySelect=service.categoryId;
		if(service.compoType=="CM"){
			
			$scope.typeSelect=1
			$scope.showUOM=false;
    		$scope.showCur=true;
			$scope.uomSelect=service.compoName;
			
		}else if(service.compoType=="CA"){
			$scope.typeSelect=2
			 $scope.showUOM=true;
        		$scope.showCur=false;
			$scope.curSelect=service.compoName;
		}else{
			$scope.uomSelect="";
			$scope.curSelect="";
			$scope.showUOM=true;
			$scope.showCur=true;
		}
		$scope.myCroppedImage=service.image
		$scope.serviceDesc=service.serviceDesc
	};

	$scope.types=[{typeId:1,typeName:"Comodity"},{typeId:2,typeName:"Cash"}]
//	$scope.uoms=[{uomName:"KG"},
//	             {uomName:"LTR"},
//	             {uomName:"PCS"},{uomName:"USD"}]
//	$scope.currencies=[{curName:"USD"},
//	             {curName:"KSH"},
//	             {curName:"AED"}]
	$scope.$watch("typeSelect", function (newValue, oldValue) {
        if ($serviceValid(newValue)) {
            if (newValue != oldValue) {
               if(newValue==1){
            		$scope.showUOM=false;
            		$scope.showCur=true;
               }else{
            	   $scope.showUOM=true;
           		$scope.showCur=false;
               }
            }
        }
        else
            $scope.groups = [];
    });
	$scope.addService = function () {
		$scope.header="Create Commodity";
		$scope.serviceEditMode = true;
		$scope.isExisting = false;
		$scope.serviceId = 0;
		$scope.serviceCode = "";
		$scope.serviceName = "";
		$scope.active = false;
		// $scope.isDisabled = true;
		$scope.serviceSelect="";
		$scope.typeSelect="";
		$scope.showUOM=true;
		$scope.showCur=true;
		$scope.categorySelect="";
		$scope.minPrice="";
		$scope.maxPrice="";
		$('#icon').removeAttr('src')
		$scope.serviceDesc=""
	
	};

	$scope.cancelService = function () {
		$scope.serviceEditMode = false;
		$scope.active = false;
		$scope.serviceSelect=0;
		$scope.serviceEditMode = false;
		$scope.serviceSelect="";
		$scope.typeSelect="";
		$scope.showUOM=true;
		$scope.showCur=true;
		$scope.uomSelect=0;
		$scope.curSelect=0;
		$scope.minPrice="";
		$scope.maxPrice="";
		$scope.categorySelect="";
		$scope.myCroppedImage=""
		$('#icon').removeAttr('src')
		$scope.serviceDesc=""
	}

	$scope.updService = function () {
		var service = {};

		if (!$serviceValid($scope.serviceCode)) {
            logger.logWarning("Opss! You may have skipped specifying the Commodity's Code. Please try again.")
            return false;
        }
        if (!$serviceValid($scope.serviceName)) {
            logger.logWarning("Opss! You may have skipped specifying the Commodity's Description. Please try again.")
            return false;
        }
        if (!$serviceValid($scope.typeSelect)) {
            logger.logWarning("Opss! You may have skipped specifying the Commodity's type. Please try again.")
            return false;
        }
        /*if($scope.typeSelect==1){
        	 if (!$serviceValid($scope.uomSelect)) {
                 logger.logWarning("Opss! You may have skipped specifying the Commodity's uom. Please try again.")
                 return false;
             }
        }else{
        	 if (!$serviceValid($scope.curSelect)) {
                 logger.logWarning("Opss! You may have skipped specifying the Commodity's currency. Please try again.")
                 return false;
             }
        }*/
			if (!$serviceValid($scope.serviceId))
				service.serviceId = 0;
			else
				service.serviceId = $scope.serviceId;
			service.serviceCode = $scope.serviceCode;
			service.serviceName=$scope.serviceName;
			if($scope.typeSelect==1){
				service.compoType="CM";
				service.compoName="NA"//$scope.uomSelect;
			}else if($scope.typeSelect==2){
				service.compoType="CA";
				service.compoName="NA";
			}
			service.serviceDesc=$scope.serviceDesc
			service.image=$scope.myCroppedImage;
			service.active = $scope.active;
			service.categoryId=$scope.categorySelect;
			service.createdBy = $rootScope.UsrRghts.sessionId;
			service.minPrice=0;
			service.maxPrice=0;
		
		blockUI.start()
		serviceSvc.UpdService(service).success(function (response) {
			if (response.respCode == 200) {
				logger.logSuccess("Great! The Commodity information was saved succesfully")
				
				$scope.serviceId = 0;
				$scope.serviceCode = "";
				$scope.serviceName = "";
				$scope.active = false;
				$scope.serviceEditMode = false;
				$scope.serviceSelect=0;
				$scope.priceActive=false;
				$scope.loadServiceData();
				$scope.serviceValue="";
				$scope.isExisting=false;
				$scope.typeSelect="";
				$scope.showUOM=true;
				$scope.showCur=true;
				$scope.serviceDesc="";
				$scope.uomSelect=0;
				$scope.curSelect=0;
				$scope.minPrice="";
				$scope.maxPrice="";
				
			}
			else  {
				logger.logWarning(response.respMessage);
			}

			blockUI.stop();
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
			blockUI.stop();
		});
	};

    var handleFileSelect=function(evt) {
  	  $scope.showCamera=true;
        $scope.capPic=false;
        $scope.showImage=false;
      var file=evt.currentTarget.files[0];
      var reader = new FileReader();
      reader.onload = function (evt) {
        $scope.$apply(function($scope){
          $scope.myCroppedImage=evt.target.result;
        });
      };
      reader.readAsDataURL(file);
      if($(this).get(0).files.length > 0){ // only if a file is selected
		    var fileSize = $(this).get(0).files[0].size;
		    console.log(fileSize);
		  }
    };
    angular.element(document.querySelector('#fileInput')).on('change',handleFileSelect);

} ]).factory('serviceSvc', function ($http) {
	var EvServiceSvc = {};

	EvServiceSvc.GetServices = function ($scope) {
		return $http({
			url: '/compas/rest/service/gtServices',
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};
	EvServiceSvc.GetSubServices = function (serviceId) {
		return $http({
			url: '/compas/rest/service/gtSubServices/'+serviceId,
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};
	EvServiceSvc.GetParameter = function ($scope) {
		return $http({
			url: '/compas/rest/service/gtParams',
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};
	EvServiceSvc.GetSubServiceById = function (parentServiceId) {
		return $http({
			url: '/compas/rest/service/gtSubServices/'+parentServiceId,
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};
	EvServiceSvc.UpdService = function (service) {
		return $http({
			url: '/compas/rest/service/updService',
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			data: service
		});
	};
	
	EvServiceSvc.GetCurrencies = function () {
		return $http({
			url: '/compas/rest/service/gtCurrencies/',
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};
	EvServiceSvc.GetUoms = function () {
		return $http({
			url: '/compas/rest/service/gtUoms/',
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};
	return EvServiceSvc;
}).factory('$serviceValid', function () {
	return function (valData) {
		if (angular.isUndefined(valData))
			return false;
		else {
			if (valData == null)
				return false;
			else {
				if (valData.toString().trim() == "")
					return false;
				else
					return true;
			}
		}
		return false;
	};
})  .directive('dropDown', function($compile) {
	return {

		restrict: 'E',

		scope: {
			user: '=user'
		},

		controller: function($scope) {

			$scope.addChild = function (child) {
				var index = $scope.user.children.length;
				$scope.user.children.push({
					"parent": $scope.user,
					"children": [],
					"index": index
				});
			}

			$scope.remove = function () {
				if ($scope.user.parent) {
					var parent = $scope.user.parent;
					var index = parent.children.indexOf($scope.user);
					parent.children.splice(index, 1);
				}
			}
		},

		// templateUrl: '/compas/masters/service',

		link: function ($scope, $element, $attrs) {

		},

		compile: function(tElement, tAttr) {
			var contents = tElement.contents().remove();
			var compiledContents;
			return function(scope, iElement, iAttr) {
				if(!compiledContents) {
					compiledContents = $compile(contents);
				}
				compiledContents(scope, function(clone, scope) {
					iElement.append(clone); 
				});
			};
		}
	};
});