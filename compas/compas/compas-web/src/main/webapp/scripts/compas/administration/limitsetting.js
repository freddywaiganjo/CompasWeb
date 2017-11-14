/**
 * 
 */
/**
 * User Angular Module
 */
'use strict';
angular.module('app.limitsetting', []).controller("limitSettingCtrl", ["$scope", "$filter", "limitSvc","userSvc","$limitValid", "$rootScope", "blockUI", "logger", "$location","localStorageService","loginSvc", function ($scope, $filter,limitSvc,userSvc,$limitValid, $rootScope, blockUI, logger, $location,localStorageService,loginSvc) {
	
    var init;
    $scope.header="";
	$scope.limits = [];
	$scope.allItemsSelected = false;
	 $scope.selection=[];
	 
	
	 
    $scope.loadLimitData = function () {
        $scope.limits = [], $scope.searchKeywords = "", $scope.filteredLimits = [], $scope.row = "", $scope.limitEditMode = false; $scope.previewServices=false;
        limitSvc.GetLimits().success(function (response) {
            return $scope.limits = response, $scope.searchKeywords = "", $scope.filteredLimits = [], $scope.row = "", $scope.select = function (page) {
                var end, start;
                return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPageLimits = $scope.filteredLimits.slice(start, end)
            }, $scope.onFilterChange = function () {
                return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
            }, $scope.onNumPerPageChange = function () {
                return $scope.select(1), $scope.currentPage = 1
            }, $scope.onOrderChange = function () {
                return $scope.select(1), $scope.currentPage = 1
            }, $scope.search = function () {
                return $scope.filteredLimits = $filter("filter")($scope.limits, $scope.searchKeywords), $scope.onFilterChange()
            }, $scope.order = function (rowName) {
                return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredLimits = $filter("orderBy")($scope.limits, rowName), $scope.onOrderChange()) : void 0
            }, $scope.numPerPageOpt = [3, 5, 10, 20], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentPageLimits = [], (init = function () {
                return $scope.search(), $scope.select($scope.currentPage)
            })();
        }).error(function (data, status, headers, config) {
            logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
        });
    }
    $scope.loadLimitData ()

    $scope.loadWebUsers = function () {
        $scope.users = [];
        limitSvc.GetWebUsers().success(function (response) {
            return $scope.users = response;
        });
  }
    $scope.loadWebUsers();
    
    
    $scope.editLimit = function (limit) {
    	if (!$filter('checkRightToEdit')($rootScope.UsrRghts.rightsHeaderList, "#" +
    			$location.path())) {
    		logger.log("Oh snap! You are not allowed to modify the Limit.");
    		return false;
    	}
    	$scope.header="Edit Limit";
        $scope.limitEditMode = true;
        $scope.isExisting = true;
        $scope.limitId = limit.limitId;
        $scope.userSelect = limit.userId;
        $scope.limit = limit.limit;
       
    };
  
    $scope.addLimit = function () {
        
    	if (!$filter('checkRightToAdd')($rootScope.UsrRghts.rightsHeaderList, "#" + $location.path())) {
    		logger.log("Oh snap! You are not allowed to create Limit.");
    		return false;
    	}
    	$scope.header="Create Limit";
    
        $scope.limitEditMode = true;
        $scope.isExisting = false;
        $scope.limitId = 0;
        $scope.userSelect = "";
        $scope.limit= "";
    
    };

    $scope.cancelLimit = function () {
        $scope.limitEditMode = false;
        $scope.limitId = 0;
        $scope.userSelect = "";
        $scope.limit= "";
       
    }

    $scope.updLimit= function () {
        var limit = {};

        if (!$limitValid($scope.userSelect)) {
            logger.logWarning("Opss! You may have skipped specifying the User Name. Please try again.")
            return false;
        }
        if (!$limitValid($scope.limit)) {
            logger.logWarning("Opss! You may have skipped specifying the Limit. Please try again.")
            return false;
        }
      
        if (!$limitValid($scope.limitId))
            limit.limitId = 0;
        else
        	limit.limitId = $scope.limitId;
        limit.userId=$scope.userSelect;
        limit.limit=$scope.limit;
      
        limit.createdBy = $rootScope.UsrRghts.sessionId;
        blockUI.start()
        limitSvc.UpdLimit(limit).success(function (response) {
            if (response.respCode == 200) {
                logger.logSuccess("Great! The Limit information was saved succesfully")
                localStorageService.clearAll();
					loginSvc.GetRights($rootScope.UsrRghts.sessionId).success(function (rightLst) {
						localStorageService.set('rxr', rightLst);
						$rootScope.UsrRghts = rightLst;
					}).error(function (data, status, headers, config) {
						logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
					});
              $scope.limitId = 0;
        $scope.userSelect = "";
        $scope.limit= "";
                $scope.limitEditMode = false;
                $scope.loadLimitData();
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

} ]).factory('limitSvc', function ($http) {
    var EvLimitsvc = {};
    EvLimitsvc.GetLimits = function () {
        return $http({
            url: '/compas/rest/vouchertopup/gtLimits/',
            method: 'GET',
            headers: { 'Content-Type': 'application/json' }
        });
    };
    EvLimitsvc.GetWebUsers = function () {
        return $http({
            url: '/compas/rest/vouchertopup/gtwebusers/',
            method: 'GET',
            headers: { 'Content-Type': 'application/json' }
        });
    };
// /
   
    EvLimitsvc.UpdLimit = function (limit) {
        return $http({
            url: '/compas/rest/vouchertopup/updLimit',
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            data: limit
        });
    };
    
   
    return EvLimitsvc;
}).factory('$limitValid', function () {
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