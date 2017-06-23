/**
 * Location Angular Module
 */
'use strict';
angular.module('app.location', []).controller("locationCtrl", ["$scope", "$filter", "locationSvc","areaSvc","serviceSvc","programmeSvc","$locationValid", "$rootScope", "blockUI", "logger", "$location", function ($scope, $filter,locationSvc,areaSvc,serviceSvc,programmeSvc,$locationValid, $rootScope, blockUI, logger, $location) {

	var init;
	$scope.header="";
	$scope.locations = [];
	$scope.locationDetails=[];
	$scope.locationDetails=[{ locationCode:'',locationName:''}]
	$scope.serDetails=[];
	$scope.serDetails=[{uom:'',quantity:'',maxPrice:''}]
	/**
	 * gets the details of the location created
	 */
	$scope.loadLocationData = function () {
		$scope.locations = [], $scope.searchKeywords = "", $scope.filteredLocations = [], $scope.row = "", $scope.locationEditMode = false;
		locationSvc.GetLocations($rootScope.UsrRghts.linkId).success(function (response) {
			return $scope.locations = response, $scope.searchKeywords = "", $scope.filteredLocations = [], $scope.row = "", $scope.select = function (page) {
				var end, start;
				return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPageLocations = $scope.filteredLocations.slice(start, end)
			}, $scope.onFilterChange = function () {
				return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
			}, $scope.onNumPerPageChange = function () {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.onOrderChange = function () {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.search = function () {
				return $scope.filteredLocations = $filter("filter")($scope.locations, $scope.searchKeywords), $scope.onFilterChange()
			}, $scope.order = function (rowName) {
				return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredLocations = $filter("orderBy")($scope.locations, rowName), $scope.onOrderChange()) : void 0
			}, $scope.numPerPageOpt = [3, 5, 10, 20], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentPages = [], (init = function () {
				return $scope.search(), $scope.select($scope.currentPage)
			})();
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
		});
	}
	$scope.loadLocationData();
	$scope.loadUoms = function () {
		$scope.uoms = [];
		serviceSvc.GetUoms().success(function (response) {
			$scope.uoms = response
		})
	}
	$scope.loadUoms();
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
	 * initialize the form for creating the new location
	 */
	$scope.addLocation = function () {
		if (!$filter('checkRightToAdd')($rootScope.UsrRghts.rightsHeaderList, "#" +
				$location.path())) {
			logger.log("Oh snap! You are not allowed to create camps.");
			return false;
		}
		$scope.header="Create Camp";
		$scope.locationEditMode = true;
		$scope.isExisting = false;
		$scope.locationId = 0;
		$scope.locationCode = "";
		$scope.locationName = "";
		$scope.areaSelect = "";
		$scope.isDisabled = true;
		$scope.locationDetails=[];
		$scope.locationDetails=[{ locationCode:'',locationName:''}]
		
	};
	
	/**
	 * gets the details of the selected location
	 * 
	 * @param location-object
	 *            of location details
	 */
	$scope.editLocation = function (location) {
		if (!$filter('checkRightToEdit')($rootScope.UsrRghts.rightsHeaderList, "#" +
				$location.path())) {
			logger.log("Oh snap! You are not allowed to modify the camp.");
			return false;
		}
		$scope.header="Edit Camp";
		$scope.locationEditMode = true;
		$scope.isExisting = true;
		$scope.locationId = location.locationId;
		$scope.locationCode = location.locationCode;
		$scope.locationName = location.locationName;
		$scope.areaSelect = location.areaId;
		$scope.active = location.active;
		$scope.satus=location.status;
	};
	
	/**
	 * gets the details of the active areas
	 */
	$scope.loadActiveAreaData = function () {
		$scope.areas = [];

		locationSvc.GetActiveAreas($rootScope.UsrRghts.linkId).success(function (response) {
			$scope.areas = response
		})
	};
	$scope.loadActiveAreaData();

	/**
	 * updates the location details
	 */
	$scope.updLocation = function () {
		var location = {};
		$scope.tempLocations=[];

		if ($scope.locationId==0){
			location.locationId = 0;
			for(var i=0;i<$scope.locationDetails.length;i++){
				if($scope.locationDetails[i].locationCode!="" && $scope.locationDetails[i].locationName!=""){
					$scope.tempLocations.push({"locationCode":$scope.locationDetails[i].locationCode,
						"locationName":$scope.locationDetails[i].locationName});
				}
			}
			console.log($scope.tempLocations)
			
			if($scope.tempLocations.length==0){
				logger.logWarning("Opss! You may have skipped specifying the Camp's Details. Please try again.")
				return false;
			}
			location.locationDetails = $scope.tempLocations;
		}
		else{
			if (!$locationValid($scope.locationCode)) {
				logger.logWarning("Opss! You may have skipped specifying the Camp's Code. Please try again.")
				return false;
			}
			if (!$locationValid($scope.locationName)) {
				logger.logWarning("Opss! You may have skipped specifying the Camp's Name. Please try again.")
				return false;
			}
			location.locationId = $scope.locationId;
			location.locationCode=$scope.locationCode;
			location.locationName=$scope.locationName;
		}

		if (!$locationValid($scope.areaSelect)) {
			logger.logWarning("Opss! You may have skipped specifying the Zone. Please try again.")
			return false;
		}

		location.areaId=$scope.areaSelect;
		location.active = $scope.active;
		location.createdBy = $rootScope.UsrRghts.sessionId;
		blockUI.start()
		locationSvc.UpdLocation(location).success(function (response) {
			if (response.respCode == 200) {
				logger.logSuccess("Great! The Camp information was saved succesfully")
				$scope.locationId = 0;
				$scope.locationCode = "";
				$scope.locationName = "";
				$scope.areaSelect=0;
				$scope.active = false;
				$scope.locationEditMode = false;
				$scope.loadLocationData();
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
	
	/**
	 * cancels the operation of location creation
	 */
	$scope.cancelLocation= function () {
		$scope.locationEditMode = false;
		$scope.active = false;
		$scope.isExisting = false;
		$scope.locationId = 0;
		$scope.locationCode = "";
		$scope.locationName = "";
		$scope.areaSelect = 0;
		$scope.isDisabled = true;
		$scope.locationDetails=[];
		$scope.locationDetails=[{ locationCode:'',locationName:''}]
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
	 * addes the texbox dynamically for location name,location code
	 */
	$scope.addElement = function() {
		$scope.locationDetails.push({locationCode:'',locationName:'' })
		console.log($scope.locationDetails);
	}//
	$scope.addElementPrice = function() {
		$scope.serDetails.push({uom:'',quantity:'',maxPrice:'' })
		console.log($scope.serDetails);
	}
	$scope.updSer= function () {
		var ser = {};
		ser.services= $scope.serDetails;
		ser.locationId=$scope.locationSelect;
		ser.serviceId=$scope.serviceSelect;
		ser.createdBy = $rootScope.UsrRghts.sessionId;
		//ser.serviceType=$scope.typeSelect;

		blockUI.start()
		locationSvc.UpdPriceConfig(ser).success(function (response) {
			if (response.respCode == 200) {
				logger.logSuccess("Great! The Service information was saved succesfully")
				
				$scope.locationSelect="";
				$scope.serDetails=[{uom:'',quantity:'',maxPrice:''}]
				$scope.serviceSelect="";
				$scope.pServices=[];
				$scope.typeSelect="";
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
} ]).factory('locationSvc', function ($http) {
	var ervLocationSvc = {};
	ervLocationSvc.GetLocations = function () {
		return $http({
			url: '/compas/rest/region/gtLocations/',
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};
	ervLocationSvc.GetActiveAreas = function () {
		return $http({
			url: '/compas/rest/region/gtActiveAreas/',
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};

	ervLocationSvc.UpdLocation = function (location) {
		return $http({
			url: '/compas/rest/region/updLocation',
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			data:location
		});
	};
	ervLocationSvc.UpdPriceConfig = function (ser) {
		return $http({
			url: '/compas/rest/service/updPriceConfig',
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			data: ser
		});
	};
	return ervLocationSvc;
}).factory('$locationValid', function () {
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