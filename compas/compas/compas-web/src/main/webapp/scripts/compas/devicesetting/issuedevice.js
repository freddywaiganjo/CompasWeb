/**
 * 
 */
/**
 * User Angular Module
 */
'use strict';
angular.module('app.issueDevice', []).controller("issueDeviceCtrl", ["$scope", "$filter", "issueDeviceSvc","agentSvc","deviceSvc","merchantSvc","userSvc","branchSvc","$issueDeviceValid", "$rootScope", "blockUI", "logger", "$location", function ($scope, $filter,issueDeviceSvc,agentSvc,deviceSvc,merchantSvc,userSvc,branchSvc, $issueDeviceValid, $rootScope, blockUI, logger, $location) {
	
    var init;
    $scope.classes = [];
	$scope.issueDevices = [];
	 $scope.merchantListMode = false;
	 $scope.editMode=true;
	   
	    $scope.loadIssueDeviceData = function () {
	        $scope.issueDevices = [], $scope.searchKeywords = "", $scope.filteredIssueDevices = [], $scope.row = "", $scope.issueDeviceEditMode = false;
	        issueDeviceSvc.GetIssueDevices($scope).success(function (response) {
	            return $scope.issueDevices = response, $scope.searchKeywords = "", $scope.filteredIssueDevices = [], $scope.row = "", $scope.select = function (page) {
	                var end, start;
	                return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPageIssueDevices = $scope.filteredIssueDevices.slice(start, end)
	            }, $scope.onFilterChange = function () {
	                return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
	            }, $scope.onNumPerPageChange = function () {
	                return $scope.select(1), $scope.currentPage = 1
	            }, $scope.onOrderChange = function () {
	                return $scope.select(1), $scope.currentPage = 1
	            }, $scope.search = function () {
	                return $scope.filteredIssueDevices = $filter("filter")($scope.issueDevices, $scope.searchKeywords), $scope.onFilterChange()
	            }, $scope.order = function (rowName) {
	                return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredIssueDevices = $filter("orderBy")($scope.issueDevices, rowName), $scope.onOrderChange()) : void 0
	            }, $scope.numPerPageOpt = [20, 40, 80, 100], $scope.numPerPage = $scope.numPerPageOpt[1], $scope.currentPage = 1, $scope.currentPages = [], (init = function () {
	                return $scope.search(), $scope.select($scope.currentPage)
	            })();
	        }).error(function (data, status, headers, config) {
	            logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
	        });
	    }
	  $scope.loadMerchantData = function () {
	        $scope.merchants = [];
	        merchantSvc.GetMerchants($rootScope.UsrRghts.linkId).success(function (response) {
	            return $scope.merchants = response;
	        });
	  }

	   
	    $scope.$watch("merchantSelect", function (newValue, oldValue) {
	        if ($issueDeviceValid(newValue)) {
	            if (newValue != oldValue) {
	                $scope.linkId = $scope.merchantSelect;
	                 $scope.loadBranchData ();
	                $scope.loadIssueDeviceData();
	                $scope.loadBranchData ();
	            }
	        }
	        else
	            $scope.groups = [];
	    });
	    $scope.$watch("branchSelect", function (newValue, oldValue) {
	        if ($issueDeviceValid(newValue)) {
	            if (newValue != oldValue) {
	                $scope.branchId = $scope.branchSelect;
	                $scope.loadAgentData();
	            }
	        }
	        else
	            $scope.users = [];
	    });
   
    $scope.loadIssueDeviceData();
    $scope.loadBranchData = function () {
        $scope.branches = [];
        branchSvc.GetBranches($scope).success(function (response) {
            return $scope.branches = response;
        });
  }
    $scope.loadAgentData = function () {
        $scope.agents = [];
        issueDeviceSvc.GetActiveAgents($scope.branchSelect).success(function (response) {
            $scope.agents = response;
        });
    }
    $scope.loadAgentData();
   // $scope.loadUserData();
    
    $scope.loadDeviceData = function () {
        $scope.devices = [];
        issueDeviceSvc.GetActiveDevices($scope).success(function (response) {
            return $scope.devices = response
        })
    }
    $scope.loadDeviceData ();
    $scope.loadLicense = function () {
        $scope.licenses = [];
        issueDeviceSvc.GetLicense().success(function (response) {
            return $scope.licenses = response
            console.log($scope.licenses);
        })
    }
    $scope.loadLicense();
    $scope.editIssueDevice = function (issueDevice) {
    	$scope.editMode=false;
    	  $scope.selectMode=false;
    	if (!$filter('checkRightToEdit')($rootScope.UsrRghts.rightsHeaderList, "#" +
    			$location.path())) {
    		logger.log("Oh snap! You are not allowed to modify the Device Issue Info.");
    		return false;
    	}
        $scope.issueDeviceEditMode = true;
        $scope.isExisting = true;
        $scope.issueId = issueDevice.issueId;
        if( $scope.regId=="-1"){
        	 $scope.rdReturnDevice = "RD";
        	 $scope.selectDevice=true;
        }else{
        	$scope.deviceSelect=issueDevice.regId;
        	$scope.rdNewDevice="ND"
        	$scope.selectDevice=false;
        }
        $scope.agentSelect = issueDevice.agentId;
        $scope.branchSelect = issueDevice.branchId;
        $scope.licenseSelect=issueDevice.licenseNumber;
        $scope.serialNo=issueDevice.serialNo;
        $scope.agentDesc=issueDevice.agentDesc;
    };
    
    $scope.showDeviceSelect=function(mode){
    	if(mode=="RD"){
    		$scope.selectDevice=true;
    	}
    	else
    	{
    		$scope.selectDevice=false;
    	}
    }
 
    $scope.addIssueDevice = function () {
        
    	if (!$filter('checkRightToAdd')($rootScope.UsrRghts.rightsHeaderList, "#" +
    			$location.path())) {
    		logger.log("Oh snap! You are not allowed to create Issue Device Info.");
    		return false;
    	}
    	$scope.isExisting = false;
        $scope.issueDeviceEditMode = true;
        $scope.issueId = 0;
        $scope.serialNo= "";
        $scope.agentId =0;
        $scope.selectDevice=false;
        $scope.selectMode=true;
        $scope.licenseSelect="";
        $scope.serialNo="";
        $scope.agentDesc="";
        $scope.editMode=true;
        $scope.loadAgentData();
        $scope.loadDeviceData ();
    };

    $scope.cancelIssueDevice= function () {
        $scope.issueDeviceEditMode = false;
        $scope.issueId = 0;
        $scope.serialNo= "";
        $scope.deviceSelect=0;
        $scope.agentSelect=0;
        $scope.isExisting = false;
    }
    

    $scope.updIssueDevice = function () {
        var issueDevice = {};

     
        if (!$issueDeviceValid($scope.deviceSelect)) {
            logger.logWarning("Opss! You may have skipped specifying the Serial No. Please try again.")
            return false;
        }
//        if (!$issueDeviceValid($scope.branchSelect)) {
//            logger.logWarning("Opss! You may have skipped specifying the Branch Name. Please try again.")
//            return false;
//        }
        if($scope.rdNewDevice!="RD"){
        if (!$issueDeviceValid($scope.agentSelect)) {
            logger.logWarning("Opss! You may have skipped specifying the Vendor Name. Please try again.")
            return false;
        }}
        if (!$issueDeviceValid($scope.issueId))
        	issueDevice.issueId = 0;
        else
        	issueDevice.issueId = $scope.issueId;
      // if($scope.rdNewDevice=="ND"){
        	  issueDevice.regId = $scope.deviceSelect;
        	  issueDevice.agentId = $scope.agentSelect;
        	  issueDevice.branchId = $scope.branchSelect;
        	  issueDevice.licenseNumber = $scope.licenseSelect;
     // }
      if($scope.rdNewDevice=="RD"){
        	 issueDevice.regId="-1";
        	 issueDevice.agentId=0;
        }
      
        
        issueDevice.createdBy =  $rootScope.UsrRghts.sessionId;
        blockUI.start()
        issueDeviceSvc.UpdIssueDevice(issueDevice).success(function (response) {
            if (response.respCode == 200) {
                logger.logSuccess("Great! The device issued succesfully to the user")
                if (parseFloat($scope.userId) == parseFloat($rootScope.UsrRghts.sessionId)) {
                	localStorageService.clearAll();
                	loginSvc.GetRights($rootScope.UsrRghts.sessionId).success(function (rightLst)
                			{
                		localStorageService.set('rxr', rightLst);
                		$rootScope.UsrRghts = rightLst;
                			}).error(function (data, status, headers, config) {
                				logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
                			});
                }
                $scope.issueId = 0;
                $scope.serialNo = "";
                $scope.userName = "";
                $scope.agentSelect="";
                $scope.deviceSelect=""
                $scope.issueDeviceEditMode = false;
                $scope.loadIssueDeviceData();
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

} ]).factory('issueDeviceSvc', function ($http) {
    var compasIssueDeviceSvc = {};
    compasIssueDeviceSvc.GetIssueDevices = function ($scope) {
        return $http({
            url: '/compas/rest/device/gtIssueDevices/'+0+ ',' +0,
            method: 'GET',
            headers: { 'Content-Type': 'application/json' }
        });
    };

    compasIssueDeviceSvc.UpdIssueDevice = function (issueDevice) {
        return $http({
            url: '/compas/rest/device/updIssueDevice',
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            data: issueDevice
                
        });
    };
    compasIssueDeviceSvc.GetActiveDevices = function ($scope) {
        return $http({
            url: '/compas/rest/device/gtActiveDevices',
            method: 'GET',
            headers: { 'Content-Type': 'application/json' }
        });
    };
    
    compasIssueDeviceSvc.GetActiveAgents = function (branchId) {
        return $http({
            url: '/compas/rest/device/gtActiveAgents/'+0,
            method: 'GET',
            headers: { 'Content-Type': 'application/json' }
        });
    };
    
    compasIssueDeviceSvc.GetLicense = function () {
        return $http({
            url: '/compas/rest/device/gtLicense',
            method: 'GET',
            headers: { 'Content-Type': 'application/json' }
        });
    };
    return compasIssueDeviceSvc;
}).factory('$issueDeviceValid', function () {
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