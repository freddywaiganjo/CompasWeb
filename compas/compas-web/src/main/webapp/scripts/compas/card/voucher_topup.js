/**
 * Created by Kibet on 5/23/2016.
 */

'use strict';
angular.module('app.voucher_topup', []).controller("voucherTopupCtrl", ["$scope", "$filter", "cardSvc","$rootScope",
    "blockUI", "logger" ,"$location","memberSvc","locationSvc","$vouchertopupValid","programmeSvc","bnfgrpSvc","voucherSvc","voucherTopupSvc","issueDeviceSvc",
    function ($scope, $filter,cardSvc, $rootScope, blockUI, logger, $location,memberSvc,locationSvc,$vouchertopupValid,programmeSvc,bnfgrpSvc,voucherSvc,voucherTopupSvc,issueDeviceSvc) {
        $scope.programmes = [];
        $scope.benefiaryGroups = [];
        $scope.selectedProgrammes = [];
        $scope.selection = [];
        $scope.bg_selection = [];
        $scope.allBgItemsSelected = false;
        $scope.showProgrammes  = false;
        $scope.showBeneficiaryGroups = false;
        $scope.showLocation=false;
        $scope.showBeneficiary = false;
        $scope.allRetailerSelected=false;
        $scope.showRetailers=false;
        $scope.showList=false;
        $scope.showTopupCreation=true;
        $scope.topupTypes = [
                             {
                                 type:"LOC",
                                 name:"Camp"
                             },
            {
                type:"PROG",
                name:"Programmes"
            },
            {
                type:"BG",
                name:"Vulnerability Criteria"

            },
            {
                type:"BEN",
                name:"Card Holder"
            }
        ];

        /**
         * loads programmes 
         */
        $scope.generateTopup=function(){
        	$scope.showTopupCreation=false;
        	 $scope.showList=true;
        }
        
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
        /**
         * loads Beneficiary group by programmeId 
         */
        $scope.loadBnfGrpsByPrgId= function (programmeId) {
        	voucherTopupSvc.GetBnfGrpsByPrgId(programmeId).success(function (response) {
                $scope.beneficiaryGroups = response;
                console.log($scope.programmes);
            }).error(function (data, status, headers, config) {
                logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
            });

        };
        /**
         * loads Beneficiary group by locationId 
         */
        $scope.loadBnfGrpsByLocId= function (locationId) {
        	voucherTopupSvc.GetBnfGrpsByLocId(locationId).success(function (response) {
                $scope.beneficiaryGroups = response;
                console.log($scope.programmes);
            }).error(function (data, status, headers, config) {
                logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
            });

        };
        
        $scope.loadBnfGrpsTopuped= function () {
        	voucherTopupSvc.GetBnfGrpsTopuped().success(function (response) {
                $scope.topups = response;
               
            }).error(function (data, status, headers, config) {
                logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
            });

        };
        
        $scope.loadBnfGrpsTopuped();
        /**
         * loads beneficiary groups when topup type is programme
         */
        $scope.loadBeneficiaryGroups = function () {
        	voucherTopupSvc.GetBnfGrpsForTopup().success(function (response) {
                console.log("Beneficiary groups##");
                console.log(response);
                $scope.beneficiaryGroups = response;
            }).error(function (data, status, headers, config) {
                logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
            });

        };

        /**
         *
         * @param value
         */
        $scope.handleTopupTypeSelect = function (value) {
            if(value==="PROG"){
                $scope.showProgrammes = false;
                $scope.showBeneficiaryGroups = false;
                $scope.showBeneficiary = false;
               
                $scope.loadProgrammes();
            }
            if (value === "BG"){
                $scope.showBeneficiaryGroups = true;
                $scope.showProgrammes  = false;
                $scope.showBeneficiary = false;
                $scope.showRetailers=true;
                $scope.loadBeneficiaryGroups();
            }

            if (value === "BEN"){
                $scope.showBeneficiary = true;
                $scope.showProgrammes  = false;
               $scope.showRetailers=true;
                $scope.showBeneficiaryGroups = false;
            }
            if (value === "LOC"){
                $scope.showBeneficiary = false;
                $scope.showLocation=true;
                $scope.showProgrammes  = false;
                $scope.showRetailers=true;
                $scope.showBeneficiaryGroups = false;
            }
        };

        /**
         * watches topup type selected
         */
        $scope.$watch("topupType", function (newValue,oldValue) {
            if($vouchertopupValid(newValue)){
                if(newValue!=oldValue){
                    $scope.handleTopupTypeSelect(newValue);
                }
            }
        });
        /**
         * watches topup type selected
         */
        $scope.$watch("programmeSelect", function (newValue,oldValue) {
            if($vouchertopupValid(newValue)){
                if(newValue!=oldValue){
                    $scope.showProgrammes=false;
                    $scope.loadBnfGrpsByPrgId(newValue)
                    $scope.showBeneficiaryGroups = true;
                    $scope.showBeneficiary = false;
                    $scope.showRetailers=true;
                }
            }
        });
        
        $scope.$watch("locationSelect", function (newValue,oldValue) {
            if($vouchertopupValid(newValue)){
                if(newValue!=oldValue){
                	if($scope.topupType=='BEN'){
                		$scope.showProgrammes=false;
                       
                          $scope.loadVendorsData(newValue) ;
                        $scope.showBeneficiaryGroups = false;
                        $scope.showBeneficiary = true;
                        $scope.showRetailers=true;
                	}else if($scope.topupType=='LOC'){
                    $scope.showProgrammes=false;
                    $scope.loadBnfGrpsByLocId(newValue)
                      $scope.loadVendorsData(newValue) ;
                    $scope.showBeneficiaryGroups = true;
                    $scope.showBeneficiary = false;
                    $scope.showRetailers=true;
                	}
                }
            }
        });


        /**
         * select one item
         * @param index
         */
        $scope.selectProgramme = function (index) {
            // If any entity is not checked, then uncheck the "allItemsSelected"
            // checkbox
            console.log("Selected Index##");
            console.log(index);
            $scope.selection=[];
            for (var i = 0; i < $scope.programmes.length; i++) {
                // var idx = $scope.selection.indexOf(branchId);
                console.log($scope.programmes[i].isActive);
                
                if (index > -1 && $scope.programmes[i].isActive) {
                    $scope.selection.push(
                        $scope.programmes[i].programmeId
                    );

                }
                else if(!$scope.programmes[i].isActive){
                    $scope.selection.splice(i, 1);
                }

                if (!$scope.programmes[i].isActive) {
                    $scope.allItemsSelected = false;
                    return;
                }
            }
            $scope.allItemsSelected = true;
        };

        /**
         * This executes when checkbox in table header is checked
         * selects all programmes
         */
        $scope.selectAllProgrammes = function () {
            // Loop through all the entities and set their isChecked property
            $scope.selection=[];
            for (var i = 0; i < $scope.programmes.length; i++) {
                $scope.programmes[i].isActive = $scope.allItemsSelected;
                if ($scope.programmes[i].isActive) {
                    $scope.selection.push(
                        $scope.programmes[i].programmeId
                    );

                    //$scope.selectedProgrammes.push($scope.programmes[i].programmeId);
                }
                else if(!$scope.programmes[i].isActive){
                    $scope.selection.splice(i, 1);
                   // $scope.selectedProgrammes.splice(i,1);
                }

            }
        };


        /**
         * This executes when checkbox in table header is checked
         * selects all the beneficiary groups
         */

        $scope.selectAllBgs = function () {
            // Loop through all the entities and set their isChecked property
            $scope.bg_selection=[];
            var total = 0;
            $scope.total=0;
            for (var i = 0; i < $scope.beneficiaryGroups.length; i++) {
                $scope.beneficiaryGroups[i].isActive = $scope.allBgItemsSelected;
                if ($scope.beneficiaryGroups[i].isActive) {
                	total += $scope.beneficiaryGroups[i].topupValue;
                    
                    $scope.total=total;
                    $scope.bg_selection.push(
                        $scope.beneficiaryGroups[i].bnfGrpId
                    );
                }
                else if(!$scope.beneficiaryGroups[i].isActive){
                    $scope.bg_selection.splice(i, 1);

                }
            }
        };


        /**
         * select one item
         * @param index
         */
        $scope.selectBg = function (index) {
            // If any entity is not checked, then uncheck the "allItemsSelected"
            // checkbox
            console.log("Selected Index##");
            console.log(index);
            $scope.bg_selection=[];
            
            var total = 0;
            $scope.total=0;
            for (var i = 0; i < $scope.beneficiaryGroups.length; i++) {
                console.log($scope.beneficiaryGroups[i].isActive);
                
                if (index > -1 && $scope.beneficiaryGroups[i].isActive) {
                	 
                      
                           
                          total += $scope.beneficiaryGroups[i].topupValue;
                       
                       $scope.total=total;
                    $scope.bg_selection.push(
                        $scope.beneficiaryGroups[i].bnfGrpId
                    );
                }
                if(!$scope.beneficiaryGroups[i].isActive){
                    $scope.bg_selection.splice(i, 1);

                }

                if (!$scope.beneficiaryGroups[i].isActive) {
                    $scope.allBgItemsSelected = false;
                    return;
                }

            }
            $scope.allBgItemsSelected = true;
           
        };


        /**
         * do the top up here now
         * generates topup values in the db.
         */
        $scope.doTopUp = function () {
        	$scope.topupDetails=[];
        	$scope.retailerSelected=[];
        	for(var j=0;j<$scope.retailers.length;j++){
        		if($scope.retailers[j].isActive){
        			$scope.retailerSelected.push($scope.retailers[j].agentId);
        		}
        	}
            var value = $scope.topupType;
            if($vouchertopupValid(value)){
                if(value==="PROG"){
                    //logger.log(value);
                   // console.log($scope.selection);
                    //console.log($scope.selection.length());
                    var topup ={};
                    $scope.bg_selection=[];
                    for(var i=0;i<$scope.beneficiaryGroups.length;i++){
                		if($scope.beneficiaryGroups[i].isActive){
                			$scope.topupDetails.push($scope.beneficiaryGroups[i])
                			$scope.bg_selection.push($scope.beneficiaryGroups[i].bnfGrpId);
                		}
                	}
                    topup.beneficiary_groups = $scope.bg_selection;
                    topup.requestType = 'PROG';
                    topup.bgTopupDetails=$scope.topupDetails
                    topup.retailerSelected=$scope.retailerSelected
                    topup.programme_id=$scope.programmeSelect;
                    if($scope.bg_selection.length > 0){
                        blockUI.start();
                        voucherTopupSvc.doTopup(topup).success(function (response) {
                            if (response.respCode == 200) {
                                logger.logSuccess("Great! The voucher topup saved successfully");
                                $location.path('/');
                            }
                            else  {
                                logger.logWarning(response.respMessage);
                            }
                            blockUI.stop();
                        }).error(function (data, status, headers, config) {
                            logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
                            blockUI.stop();
                        });
                    } else{
                        logger.logError("Error ! You have skipped selecting programmes, Please try again");
                    }


                }
                if(value==="LOC"){
                    //logger.log(value);
                   // console.log($scope.selection);
                    //console.log($scope.selection.length());
                    var topup ={};
                    if($rootScope.UsrRghts.topupLimit<$scope.total){
            			
           			 topup.topupStatus="P";
                    }else if($rootScope.UsrRghts.topupLimit>=$scope.total){
                   	topup.topupStatus="A";
                    }
                    $scope.bg_selection=[];
                    for(var i=0;i<$scope.beneficiaryGroups.length;i++){
                		if($scope.beneficiaryGroups[i].isActive){
                			$scope.topupDetails.push($scope.beneficiaryGroups[i])
                			$scope.bg_selection.push($scope.beneficiaryGroups[i].bnfGrpId);
                		}
                	}
                    topup.beneficiary_groups = $scope.bg_selection;
                    topup.requestType = 'LOC';
                    topup.bgTopupDetails=$scope.topupDetails
                    topup.retailerSelected=$scope.retailerSelected
                    topup.location_id=$scope.locationSelect;
                    if($scope.bg_selection.length > 0){
                        blockUI.start();
                        voucherTopupSvc.doTopup(topup).success(function (response) {
                        	if(topup.topupStatus=='P'){
                       		 logger.logWarning("Opss! Sorry The Topup limit for the user is not sufficient,Waiting for approval. Please try again.")
                       		 $location.path('/');
                       		 return;
                       	}
                            if (response.respCode == 200) {
                                logger.logSuccess("Great! The voucher topup saved successfully");
                                $location.path('/');
                            }
                            else  {
                                logger.logWarning(response.respMessage);
                            }
                            blockUI.stop();
                        }).error(function (data, status, headers, config) {
                            logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
                            blockUI.stop();
                        });
                    } else{
                        logger.logError("Error ! You have skipped selecting programmes, Please try again");
                    }


                }

                if (value === "BG"){
                    //logger.log(value);
                    // console.log($scope.selection);
                    //console.log($scope.selection.length());
                	 if($rootScope.UsrRghts.topupLimit<$scope.total){
        			
        			 topup.topupStatus="P";
                 }else if($rootScope.UsrRghts.topupLimit>=$scope.total){
                	topup.topupStatus="A";
                 }
                	$scope.bg_selection=[];
                	for(var i=0;i<$scope.beneficiaryGroups.length;i++){
                		if($scope.beneficiaryGroups[i].isActive){
                			$scope.topupDetails.push($scope.beneficiaryGroups[i])
                			$scope.bg_selection.push($scope.beneficiaryGroups[i].bnfGrpId);
                		}
                	}
                    var topup ={};
                    
                    topup.beneficiary_groups = $scope.bg_selection;
                    topup.requestType = 'BG';
                    topup.bgTopupDetails=$scope.topupDetails
                    topup.retailerSelected=$scope.retailerSelected
                    topup.programme_id=0;
                    if($scope.bg_selection.length > 0){
                        blockUI.start();
                        voucherTopupSvc.doTopup(topup).success(function (response) {
                        	//if(topup.topupStatus=='P'){
                        		// logger.logWarning("Opss! Sorry The Topup limit for the user is not sufficient,Waiting for approval. Please try again.")
                        		 //$location.path('/');
                        //	}
                            if (response.respCode == 200) {
                                logger.logSuccess("Great! The voucher topup saved successfully!!");
                                $location.path('/');
                            }
                            else  {
                                logger.logWarning(response.respMessage);
                            }
                            blockUI.stop();
                        }).error(function (data, status, headers, config) {
                            logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
                            blockUI.stop();
                        });
                    } else{
                        logger.logError("Error ! You have skipped selecting beneficiary groups, Please try again");
                    }
                }

                if (value === "BEN"){
                    //logger.log(value);
                    // console.log($scope.selection);
                    //console.log($scope.selection.length());
                    var topup ={};
                    topup.idPassportNo = $scope.idPassportNo;
                    topup.topupValue = $scope.topupValue;
                    topup.remarks = $scope.remarks;
                    topup.requestType = 'BEN';
                    topup.location_id=$scope.locationSelect;
                    topup.topupStatus="A";
                    topup.retailerSelected=$scope.retailerSelected;
                    if($vouchertopupValid($scope.idPassportNo)){
                        blockUI.start();
                        voucherTopupSvc.doTopup(topup).success(function (response) {
                            if (response.respCode == 200) {
                                logger.logSuccess("Great! The voucher topup saved successfully!!");
                                $scope.programmes = [];
                                $scope.benefiaryGroups = [];
                                $scope.selectedProgrammes = [];
                                $scope.selection = [];
                                $scope.bg_selection = [];
                                $scope.allBgItemsSelected = false;
                                $location.path('/');
                            }
                            else  {
                                logger.logWarning(response.respMessage);
                            }
                            blockUI.stop();
                        }).error(function (data, status, headers, config) {
                            logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
                            blockUI.stop();
                        });
                    } else{
                        logger.logError("Error ! You have skipped entering the Ration No, Please try again");
                    }
                }
            }
        }

        /**
         * exit topup screen
         */
        $scope.cancelTopup = function () {
            //logger.logSuccess("Firmware Uploaded successfully!!");
            $scope.programmes = [];
            $scope.benefiaryGroups = [];
            $scope.selectedProgrammes = [];
            $scope.selection = [];
            $scope.bg_selection = [];
            $scope.allBgItemsSelected = false;
            $location.path('/');
        }
        
        $scope.updateTopupValue=function(bg){
        
        }
        $scope.getTotal = function(){
            var total = 0;
            $scope.total=0;
            for(var i = 0; i < $scope.beneficiaryGroups.length; i++){
                var product = $scope.beneficiaryGroups[i];
                total += (product.topupValue);
            }
            $scope.total=$filter('number')(total, fractionSize);
           // return total;
        }
        $scope.loadVendorsData= function (locationId) {
	        $scope.retailers = []
	       voucherTopupSvc.GetVendorsByLocation(locationId).success(function (response) {
	          $scope.retailers = response
	            })
        }
      
        
        /**
         * This executes when checkbox in table header is checked
         * selects all the retailers
         */

        $scope.selectAllRetailers = function () {
            // Loop through all the entities and set their isChecked property
        	 $scope.selection=[];
            for (var i = 0; i < $scope.retailers.length; i++) {
                $scope.retailers[i].isActive = $scope.allRetailerSelected;
                if ($scope.retailers[i].isActive) {
                    $scope.selection.push(
                        $scope.retailers[i].bnfGrpId
                    );
                }
                else if(!$scope.retailers[i].isActive){
                    $scope.selection.splice(i, 1);

                }
            }
        };


        /**
         * select one item
         * @param index
         */
        $scope.selectRetailer = function (index) {
            // If any entity is not checked, then uncheck the "allItemsSelected"
            // checkbox
            console.log("Selected Index##");
            console.log(index);
            $scope.selection=[];
            for (var i = 0; i < $scope.retailers.length; i++) {
                console.log($scope.retailers[i].isActive);
                
                if (index > -1 && $scope.retailers[i].isActive) {
                    $scope.selection.push(
                        $scope.retailers[i].agentId
                    );
                }
                if(!$scope.retailers[i].isActive){
                    $scope.selection.splice(i, 1);

                }

                if (!$scope.retailers[i].isActive) {
                    $scope.allRetailerSelected = false;
                    return;
                }

            }
            $scope.allRetailerSelected = true;
        };

    }]).factory('voucherTopupSvc', function ($http) {
    	var voucherTopupSvc = {};
    	

    	voucherTopupSvc.doTopup = function (topup) {
    		return $http({
    			url: '/compas/rest/vouchertopup/topup',
    			method: 'POST',
    			headers: { 'Content-Type': 'application/json' },
    			data: topup
    		});
    	};
    	voucherTopupSvc.GetBnfGrpsForTopup=function(){
    		return $http({
    			url: '/compas/rest/vouchertopup/gtBnfgrpsForTopup/',
    			method: 'GET',
    			headers: { 'Content-Type': 'application/json' }
    		});
    	}
    	voucherTopupSvc.GetBnfGrpsByPrgId=function(programmeId){
    		return $http({
    			url: '/compas/rest/vouchertopup/gtBnfgrpsByPrgId/'+programmeId,
    			method: 'GET',
    			headers: { 'Content-Type': 'application/json' }
    		});
    	}
    	voucherTopupSvc.GetBnfGrpsByLocId=function(locationId){
    		return $http({
    			url: '/compas/rest/vouchertopup/gtBnfgrpsByLocId/'+locationId,
    			method: 'GET',
    			headers: { 'Content-Type': 'application/json' }
    		});
    	}//
    	voucherTopupSvc.GetVendorsByLocation=function(locationId){
    		return $http({
    			url: '/compas/rest/vouchertopup/gtVendorsByLocId/'+locationId,
    			method: 'GET',
    			headers: { 'Content-Type': 'application/json' }
    		});
    	}
     	voucherTopupSvc.GetBnfGrpsTopuped=function(){
    		return $http({
    			url: '/compas/rest/vouchertopup/gtBnfgrpsTopuped/',
    			method: 'GET',
    			headers: { 'Content-Type': 'application/json' }
    		});
    	}

    	return voucherTopupSvc;
    }).factory('$vouchertopupValid', function () {
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

