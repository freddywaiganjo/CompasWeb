/**
 * 
 */
/**
 * User Angular Module
 */
'use strict';
angular.module('app.agent', []).controller("agentsCtrl", ["$scope", "$filter", "agentSvc","userSvc","merchantSvc","branchSvc","programmeSvc","$agentValid", "$rootScope", "blockUI", "logger", "$location", function ($scope, $filter,agentSvc,userSvc,merchantSvc,branchSvc,programmeSvc,$agentValid, $rootScope, blockUI, logger, $location) {
	
    var init;
    $scope.header="";
	$scope.agents = [];
	$scope.allItemsSelected = false;
	 $scope.selection=[];
	 
	 $scope.merchantListMode = false;
	    $scope.loadClassesList = function () {
	        $scope.classes = [];
	        $scope.userEditMode = false;
	        $scope.userEnrollment = false;
	        userSvc.GetClasses().success(function (response) {
	            $scope.classes = response;
	            if($rootScope.UsrRghts.userClassId==2){
	            angular.forEach($scope.classes, function (item, index) {
	                if (item.classId == 3 ) {
	                    $scope.classes.splice(index, 1);
	                }
	            });
	            }
	            if($rootScope.UsrRghts.userClassId==4){
	                angular.forEach($scope.classes, function (item, index) {
	                    if (item.classId == 2 || item.classId==1 ) {
	                        $scope.classes.splice(index, 1);
	                    }
	                });
	                }
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
	
	$scope.loadMerchantData = function () {
        $scope.merchants = [];
        merchantSvc.GetMerchants($rootScope.UsrRghts.linkId).success(function (response) {
            return $scope.merchants = response;
        });
  }
	$scope.loadMerchantData();
	  $scope.loadBranchesByMerchantId = function (merchantId) {
	        $scope.branches = [];
	        agentSvc.GetBranchesByMerchant(merchantId).success(function (response) {
	            return $scope.branches = response;
	        });
	  }
	//$scope.loadBranchesByMerchantId($scope.merchantSelect);
	 $scope.loadLocationsByMerchantId = function (merchantId) {
	        $scope.locations = [];
	        agentSvc.GetLocationsByMerchant(merchantId).success(function (response) {
	            return $scope.locations = response;
	        });
	  }
	$scope.loadLocationsByMerchantId($scope.merchantSelect);
    $scope.loadAgentData = function () {
        $scope.agents = [], $scope.searchKeywords = "", $scope.filteredAgents = [], $scope.row = "", $scope.agentEditMode = false; $scope.previewServices=false;
        agentSvc.GetAgents().success(function (response) {
            return $scope.agents = response, $scope.searchKeywords = "", $scope.filteredAgents = [], $scope.row = "", $scope.select = function (page) {
                var end, start;
                return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPageAgents = $scope.filteredAgents.slice(start, end)
            }, $scope.onFilterChange = function () {
                return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
            }, $scope.onNumPerPageChange = function () {
                return $scope.select(1), $scope.currentPage = 1
            }, $scope.onOrderChange = function () {
                return $scope.select(1), $scope.currentPage = 1
            }, $scope.search = function () {
                return $scope.filteredAgents = $filter("filter")($scope.agents, $scope.searchKeywords), $scope.onFilterChange()
            }, $scope.order = function (rowName) {
                return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredAgents = $filter("orderBy")($scope.agents, rowName), $scope.onOrderChange()) : void 0
            }, $scope.numPerPageOpt = [20, 40, 80, 100], $scope.numPerPage = $scope.numPerPageOpt[0], $scope.currentPage = 1, $scope.currentPageAgents = [], (init = function () {
                return $scope.search(), $scope.select($scope.currentPage)
            })();
        }).error(function (data, status, headers, config) {
            logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
        });
    }
    
$scope.vTypes=[{"vTypeId":1,"vTypeName":"Vendor"},{"vTypeId":2,"vTypeName":"Master"}];
    $scope.loadAgentData();
    $scope.$watch("classSelect", function (newValue, oldValue) {
        if ($agentValid(newValue)) {
            if (newValue != oldValue) {
                if (newValue == 1) {
                	 $scope.merchantListMode = false;
                	  $scope.linkId = -1;
                	  $scope.classId=$rootScope.UsrRghts.userClassId;
                	  $scope.loadBranchData ();
                      $scope.loadAgentData();
                   
                }
                if (newValue ==4) {
               	 $scope.merchantListMode = false;
               	  $scope.linkId = -1;
               	  $scope.classId=$rootScope.UsrRghts.userClassId;
               	  $scope.loadBranchData ();
                     $scope.loadAgentData();
                  
               }
                else if (newValue == 3) {
                	$scope.merchantListMode = true;
                	 $scope.linkId = $scope.merchantSelect;
                	  $scope.classId=$rootScope.UsrRghts.userClassId;
                	 $scope.loadMerchantData();
                }
            }
        }
    });

    $scope.$watch("merchantSelect", function (newValue, oldValue) {
        if ($agentValid(newValue)) {
            if (newValue != oldValue) {
            	//if($scope.merchantSelect==""){
            	$scope.loadLocationsByMerchantId(newValue);
            	//}
            	
            }
        }
        
    });
  
    $scope.preview = function () {
    	if (!$agentValid($scope.basketSelect)) {
            logger.logWarning("Opss! You may have skipped specifying the Basket. Please try again.")
            return false;
        }
    	$scope.previewServices=true;
    }
    
    $scope.loadProgrammeData = function () {
        $scope.programmes = [];
        programmeSvc.GetProgrammes($scope).success(function (response) {
            return $scope.programmes = response;
        });
    }
  
    
    
    $scope.editAgent = function (agent) {
    	if (!$filter('checkRightToEdit')($rootScope.UsrRghts.rightsHeaderList, "#" +
    			$location.path())) {
    		logger.log("Oh snap! You are not allowed to modify the Vendor.");
    		return false;
    	}
    	$scope.header="Edit Vendor";
        $scope.agentEditMode = true;
        $scope.isExisting = true;
        $scope.agentId = agent.agentId;
        $scope.agentCode = agent.agentCode;
        $scope.agentDesc = agent.agentDesc;
        $scope.active = agent.active;
        $scope.locationSelect=agent.locationId;
        // $scope.loadBranchData();
        $scope.merchantSelect=agent.merchantId;
        $scope.programmes=agent.programmes;
        $scope.rationNo=agent.rationNo;
        $scope.mobileNo=agent.mobileNo;
        $scope.section=agent.section;
        $scope.houseNo=agent.houseNo;
        $scope.device=agent.device;
        $scope.vTypeSelect=agent.vendorTypeId;
    };
  
    $scope.addAgent = function () {
        
    	if (!$filter('checkRightToAdd')($rootScope.UsrRghts.rightsHeaderList, "#" + $location.path())) {
    		logger.log("Oh snap! You are not allowed to create Vendor.");
    		return false;
    	}
    	$scope.header="Create Vendor";
    	$scope.loadProgrammeData();
    	 // $scope.loadBranchData();
        $scope.agentEditMode = true;
        $scope.isExisting = false;
        $scope.agentId = 0;
        $scope.agentCode = "";
        $scope.agentDesc = "";
        $scope.active = false;
        $scope.previewServices=false;
        $scope.isDisabled=true;
        $scope.locationSelect="";
        $scope.merchantSelect="";
        $scope.rationNo="";
        $scope.vTypeSelect=""
    };

    $scope.cancelAgent = function () {
        $scope.agentEditMode = false;
        $scope.previewServices=false;
        $scope.active = false;
        $scope.basketSelect="";
        $scope.locationSelect="";
        $scope.programmes=[];
        $scope.basketValue="";
        $scope.isDisabled=false;
        $scope.merchantSelect="";
        $scope.rationNo="";
        $scope.vTypeSelect=""
       
    }
    $scope.selectProgramme = function (index) {
        // If any entity is not checked, then uncheck the "allItemsSelected"
		// checkbox
    	$scope.selection=[];
        for (var i = 0; i < $scope.programmes.length; i++) {
        	// var idx = $scope.selection.indexOf(branchId);
        
        	   if (index > -1 && $scope.programmes[i].isActive) {
        		   $scope.selection.push(
     	        		  {"programmeId": $scope.programmes[i].programmeId
     	        			  });
        		  

        	   }
        	  else if(!$scope.programmes[i].isActive){
        	   
        		  $scope.selection.splice(i, 1);
        	   
        	  }

            if (!$scope.programmes[i].isActive) {
                $scope.allItemsSelected = false;
                return;
            }
        }

        // If not the check the "allItemsSelected" checkbox
        $scope.allItemsSelected = true;
    };

    // This executes when checkbox in table header is checked
    $scope.selectAllProgrammes = function () {
    	// Loop through all the entities and set their isChecked property
    	$scope.selection=[];
    	for (var i = 0; i < $scope.programmes.length; i++) {
    		$scope.programmes[i].isActive = $scope.allItemsSelected;
    		if ($scope.programmes[i].isActive) {
    			$scope.selection.push(
    					{"programmeId": $scope.programmes[i].programmeId
    					});


    		}
    		else if(!$scope.programmes[i].isActive){

    			$scope.selection.splice(i, 1);

    		}

    	}
    };

    $scope.updAgent= function () {
        var agent = {};

        if (!$agentValid($scope.agentCode)) {
            logger.logWarning("Opss! You may have skipped specifying the Vendor's Code. Please try again.")
            return false;
        }
        if (!$agentValid($scope.agentDesc)) {
            logger.logWarning("Opss! You may have skipped specifying the Vendor's Description. Please try again.")
            return false;
        }
        if (!$agentValid($scope.merchantSelect)) {
            logger.logWarning("Opss! You may have skipped specifying the Merchant. Please try again.")
            return false;
        }
        if (!$agentValid($scope.locationSelect)) {
            logger.logWarning("Opss! You may have skipped specifying the Camp. Please try again.")
            return false;
        }
        if (!$agentValid($scope.agentId))
            agent.agentId = 0;
        else
        	agent.agentId = $scope.agentId;
        agent.agentCode=$scope.agentCode;
        agent.agentDesc=$scope.agentDesc;
        agent.merchantId=$scope.merchantSelect;
        agent.locationId=$scope.locationSelect;
        agent.active = $scope.active;
        agent.createdBy = $rootScope.UsrRghts.sessionId;
        agent.rationNo=$scope.rationNo;
        agent.houseNo=$scope.houseNo;
        agent.section=$scope.section;
        agent.phoneNo=$scope.phoneNo;
        agent.vendorTypeId=$scope.vTypeSelect;
        blockUI.start()
        agentSvc.UpdAgent(agent).success(function (response) {
            if (response.respCode == 200) {
                logger.logSuccess("Great! The Vendor information was saved succesfully")
                $scope.agentId = 0;
                $scope.agentCode = "";
                $scope.agentDesc = "";
                $scope.active = false;
                $scope.projectId=0;
                $scope.merchantSelect="";
                $scope.basketId=0;$scope.rationNo=""
                $scope.agentEditMode = false;
                $scope.loadAgentData();
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

} ]).factory('agentSvc', function ($http) {
    var EvAgentsvc = {};
    EvAgentsvc.GetAgents = function () {
        return $http({
            url: '/compas/rest/agent/gtAgents/',
            method: 'GET',
            headers: { 'Content-Type': 'application/json' }
        });
    };
// /
    EvAgentsvc.GetBranchesByMerchant = function (merchantId) {
        return $http({
            url: '/compas/rest/agent/getBranchesByMerchant/'+merchantId,
            method: 'GET',
            headers: { 'Content-Type': 'application/json' }
        });
    };
    EvAgentsvc.GetLocationsByMerchant = function (merchantId) {
        return $http({
            url: '/compas/rest/agent/getLocationsByMerchant/'+merchantId,
            method: 'GET',
            headers: { 'Content-Type': 'application/json' }
        });
    };
    EvAgentsvc.UpdAgent = function (agent) {
        return $http({
            url: '/compas/rest/agent/updAgent',
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            data: agent
        });
    };
    
    EvAgentsvc.GetAgentsByMerchant=function(merchant){
		return $http({
			url: '/compas/rest/agent/getAgentsByMerchant/'+merchant,
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	}
    return EvAgentsvc;
}).factory('$agentValid', function () {
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