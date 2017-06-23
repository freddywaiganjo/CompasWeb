/**
 * Modify Services Angular Module
 */
'use strict';
angular.module('app.modifyservice', []).controller("modifyServiceCtrl", ["$scope", "$filter", "modifyServiceSvc","serviceSvc","locationSvc","programmeSvc","$modifyServiceValid", "$rootScope", "blockUI", "logger", "$location", function ($scope, $filter,modifyServiceSvc,serviceSvc,locationSvc,programmeSvc,$modifyServiceValid, $rootScope, blockUI, logger, $location) {

	var init;
	$scope.serTypes==[];   
	$scope.showParentService=true;
	$scope.serEditMode=true;
	$scope.showMarkets=true;
	$scope.isDisabled=true;
	$scope.iconStyle="glyphicon glyphicon-pencil";
	$scope.serTypes=[{'typeId':'1','typeName':'Parent'},
	                 {'typeId':'2','typeName':'SubService'},
	                 {'typeId':'3','typeName':'Markets'}];
	$scope.serDetails=[];
	$scope.serDetails=[{locationCode:'',locationName:''}]
	/**
	 * gets the list of parent services
	 */
	$scope.loadVoucherServices= function (programmeId,locationId) {
		$scope.pServices = []
		modifyServiceSvc.GetVoucherServices(programmeId,locationId).success(function (response) {
			return $scope.pServices = response
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
		});
	}
	$scope.loadServiceData = function () {
		$scope.services = [];
		serviceSvc.GetServices($scope).success(function (response) {
			return $scope.services = response
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
		});
	}

	$scope.loadServiceData();
	  /**
	 * gets the details of the location created
	 */
	$scope.loadLocationData = function () {
		$scope.locations = []

		locationSvc.GetLocations($rootScope.UsrRghts.linkId).success(function (response) {
			return $scope.locations = response
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
		});
	}
	$scope.loadLocationData();
    
    $scope.loadProgrammes = function () {
        programmeSvc.GetProgrammes($scope).success(function (response) {
            $scope.programmes = response;
            console.log($scope.programmes);
        }).error(function (data, status, headers, config) {
            logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
        });

    };
    $scope.loadProgrammes(); 
	/**
	 * selects the markets and dispalys the list of services for that market
	 */
	$scope.$watch("serviceSelect", function (newValue, oldValue) {
		$scope.pServices = [];
		if ($modifyServiceValid(newValue)) {
			if (newValue != oldValue) {
				$scope.moifyPrice=false;
				$scope.loadCommodityPrices(newValue);
			}

		}
		else
			$scope.pServices = [];
	});
	
	$scope.preview=function(){
		$scope.loadVoucherServices($scope.programmeSelect,$scope.locationSelect);
	}
	$scope.addElement = function() {
		$scope.locationDetails=[{locationCode:'',locationName:''}]
		console.log($scope.locationDetails);
	}
	/**
	 * enables the texbox in the gird for editing the details
	 */
	$scope.modifyService=function(service){
		if (!$filter('checkRightToEdit')($rootScope.UsrRghts.rightsHeaderList, "#" +
				$location.path())) {
			logger.log("Oh snap! You are not allowed to modify the service.");
			return false;
		}
		if(service.iconStyle=="glyphicon glyphicon-pencil"){
			service.isDisabled=false;
			service.iconStyle="glyphicon glyphicon-ok";
		}else{
			service.isDisabled=true;
			service.iconStyle="glyphicon glyphicon-pencil";
		}

	}

	/**
	 * cancels the modify service operation
	 */
	$scope.cancelSer = function () {
		$scope.showParentService=true;
		$scope.showMarkets=true;
		$scope.markets=[];
		$scope.pServices=[];
		$scope.locationSelect="";
		$scope.isDisabled=false;
		$scope.locationDetails=[{locationCode:'',locationName:''}]
	}
	$scope.loadUoms = function () {
		$scope.uoms = [];
		serviceSvc.GetUoms().success(function (response) {
			$scope.uoms = response
		})
	}
	$scope.loadUoms();
	/**
	 * update the modified service details
	 */
	$scope.updSer= function () {
		var ser = {};
		ser.services= $scope.pServices;
		ser.locationId=$scope.locationSelect;
		ser.createdBy = $rootScope.UsrRghts.sessionId;
		//ser.serviceType=$scope.typeSelect;

		blockUI.start()
		modifyServiceSvc.UpdPriceConfig(ser).success(function (response) {
			if (response.respCode == 200) {
				logger.logSuccess("Great! The Service information was saved succesfully")
				
				$scope.locationSelect="";
				$scope.locationDetails=[{locationCode:'',locationName:''}]
				$scope.pServices=[];
				$scope.serviceSelect="";
				$scope.typeSelect="";
				$scope.locationSelect="";
				$scope.isDisabled=false;
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

	$scope.onChangeService=function(){
		
	}
	
	
} ]).factory('modifyServiceSvc', function ($http) {
	var ervModifysvc = {};
	ervModifysvc.GetSubService = function () {
		return $http({
			url: '/erevenue/rest/service/gtSubServices/',
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};
//	/
	ervModifysvc.GetVoucherServices = function (programmeId,locationId) {
		return $http({
			url: '/compas/rest/service/gtVoucherServices/'+programmeId+','+locationId,
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};
	/*ervModifysvc.UpdPriceConfig = function (ser) {
		return $http({
			url: '/compas/rest/service/updPriceConfig',
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			data: ser
		});
	};
*/
	return ervModifysvc;
}).factory('$modifyServiceValid', function () {
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
});