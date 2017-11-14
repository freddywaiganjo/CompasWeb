/**
 * 
 */
/**
 * User Angular Module
 */
'use strict';
angular.module('app.confighhvalue', []).controller("confighhvalueCtrl", ["$scope", "$filter", "confighhvalueSvc","bnfgrpSvc","$confighhvalueValid", "$rootScope", "blockUI", "logger", "$location", function ($scope, $filter,confighhvalueSvc,bnfgrpSvc,$confighhvalueValid, $rootScope, blockUI, logger, $location) {
	
    var init;

	$scope.bnfgrps = [];
	$scope.checkSelect=[];
	$scope.checkSelect.allItemsSelected = false;
	$scope.selection=[];
	$scope.header="";
	
	
    $scope.loadBnfgrpData = function () {
        $scope.bnfgrps = [];
        bnfgrpSvc.GetBnfgrps($rootScope.UsrRghts.linkId).success(function (response) {
            return $scope.bnfgrps = response
        }).error(function (data, status, headers, config) {
            logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
        });
    }
    $scope.loadBnfgrpData();
   
    $scope.loadAgeGrpByBnfGrp = function (bnfGrpId) {
        $scope.locAgeGrps = [];
        confighhvalueSvc.GetAgeGroupsBnfgrp(bnfGrpId).success(function (response) {
            return $scope.locAgeGrps = response
        }).error(function (data, status, headers, config) {
            logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
        });
    }
   
    $scope.onBnfGrpChange=function (bnfgrpId) {
        if ($confighhvalueValid(bnfgrpId)) {
            
            	//if($scope.merchantSelect==""){
            	$scope.loadAgeGrpByBnfGrp(bnfgrpId);
            	//}
            	
        }
        }	
    $scope.editBnfgrp = function (bnfgrp) {
    	if (!$filter('checkRightToEdit')($rootScope.UsrRghts.rightsHeaderList, "#" +
    			$location.path())) {
    		logger.log("Oh snap! You are not allowed to modify the bnfgrp.");
    		return false;
    	}
    	$scope.header="Edit Beneficiary Group";
        $scope.bnfgrpEditMode = true;
        $scope.isExisting = true;
        $scope.bnfGrpId = bnfgrp.bnfGrpId;
        $scope.bnfGrpCode = bnfgrp.bnfGrpCode;
        $scope.bnfGrpName = bnfgrp.bnfGrpName;
        $scope.active = bnfgrp.active;
        $scope.productSelect=bnfgrp.productId;
        $scope.houseHoldValue=bnfgrp.houseHoldValue;
        $scope.minCap=bnfgrp.minCap;
        $scope.maxCap=bnfgrp.maxCap;
        $scope.ageGroups=bnfgrp.ageGroups;
    };
  
    $scope.addBnfgrp = function () {
        
    	if (!$filter('checkRightToAdd')($rootScope.UsrRghts.rightsHeaderList, "#" + $location.path())) {
    		logger.log("Oh snap! You are not allowed to create bnfgrp.");
    		return false;
    	}
    	$scope.header="Create Beneficiary Group";
    	 // $scope.loadBranchData();
        $scope.bnfgrpEditMode = true;
        $scope.isExisting = false;
        $scope.bnfGrpId = 0;
        $scope.bnfGrpCode = "";
        $scope.bnfGrpName = "";
        $scope.active = false;
        $scope.previewServices=false;
        $scope.isDisabled=true;
        $scope.productSelect="";
        $scope.houseHoldValue="";
        $scope.minCap="";
        $scope.maxCap="";
      
    };

    $scope.cancelBnfgrp = function () {
        $scope.bnfgrpEditMode = false;
        $scope.previewServices=false;
        $scope.active = false;
        $scope.basketSelect="";
        $scope.productSelect="";
        $scope.programmes=[];
        $scope.isDisabled=false;
        $scope.houseHoldValue="";
        $scope.minCap="";
        $scope.maxCap="";
       
    }
    $scope.selectLocation= function (index) {
        // If any entity is not checked, then uncheck the "allItemsSelected"
		// checkbox
    	$scope.selection=[];
        for (var i = 0; i < $scope.locAgeGrps.length; i++) {
        	// var idx = $scope.selection.indexOf(productId);
        
        	   if (index > -1 && $scope.locAgeGrps[i].isActive) {
        		   $scope.selection.push(
     	        		  {"locationId": $scope.locAgeGrps[i].locationId
     	        			  });
        		  

        	   }
        	  else if(!$scope.locAgeGrps[i].isActive){
        	   
        		  $scope.selection.splice(i, 1);
        	   
        	  }

            if (!$scope.locAgeGrps[i].isActive) {
                $scope.checkSelect.allItemsSelected = false;
                return;
            }
        }

        // If not the check the "allItemsSelected" checkbox
        $scope.allItemsSelected = true;
    };

    // This executes when checkbox in table header is checked
    $scope.selectAllLocations = function () {
    	// Loop through all the entities and set their isChecked property
    	$scope.selection=[];
    	for (var i = 0; i < $scope.locAgeGrps.length; i++) {
    		$scope.locAgeGrps[i].isActive = $scope.checkSelect.allItemsSelected;
    		if ($scope.locAgeGrps[i].isActive) {
    			$scope.selection.push(
    					{"locationId": $scope.locAgeGrps[i].locationId
    					});


    		}
    		else if(!$scope.locAgeGrps[i].isActive){

    			$scope.selection.splice(i, 1);

    		}

    	}
    };

    $scope.updAgegrpDtl= function () {
        var bnfgrp = {};
        bnfgrp.createdBy = $rootScope.UsrRghts.sessionId;
        bnfgrp.bnfGrpId = $scope.bnfGrpSelect;
        bnfgrp.locations=$scope.locAgeGrps
      //bnfgrp.programmes=$scope.programmes;
        blockUI.start()
        confighhvalueSvc.UpdLocAgeGrpDtl(bnfgrp).success(function (response) {
            if (response.respCode == 200) {
            	
            	logger.logSuccess("Great! The Cash Allocation information was saved succesfully")
            	$scope.bnfGrpSelect = 0;
            	$scope.locAgeGrps=[];
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

} ]).factory('confighhvalueSvc', function ($http) {
    var EvBnfgrpsvc = {};
    EvBnfgrpsvc.GetAgeGroupsBnfgrp = function (bnfgrpId) {
        return $http({
            url: '/compas/rest/product/gtAgeGroupsByBnfGrp/'+bnfgrpId,
            method: 'GET',
            headers: { 'Content-Type': 'application/json' }
        });
    };

   
    EvBnfgrpsvc.UpdLocAgeGrpDtl = function (bnfgrp) {
        return $http({
            url: '/compas/rest/product/updLocAgeGrpDtl',
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            data: bnfgrp
        });
    };
    return EvBnfgrpsvc;
}).factory('$confighhvalueValid', function () {
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