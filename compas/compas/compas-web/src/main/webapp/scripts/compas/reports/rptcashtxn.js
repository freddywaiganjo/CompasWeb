/**
 * Transaction Held Angular Module
 */
'use strict';
angular.module('app.rptCashTransaction', []).controller("rptcashTransactionCtrl", ["$scope", "$filter", "memSvc","memberInquirySvc","agentSvc","deviceSvc","voucherTopupSvc", "$memValid", "$rootScope", "blockUI", "logger", "$location","$http","$window", function ($scope, $filter, memSvc,memberInquirySvc,agentSvc,deviceSvc,voucherTopupSvc,$memValid, $rootScope, blockUI, logger, $location,$http,$window) {
    $scope.transMemList = [];
    $scope.mem=[];
    $scope.agentSelect="";
   $scope.previewReport=function(mem)
   {
  	 if (!$memValid(mem.FromDt)) {
         logger.logWarning("Opss! You may have skipped specifying the From Date. Please try again.")
         return false;
     }
	 else  if (!$memValid(mem.ToDt)) {
         logger.logWarning("Opss! You may have skipped specifying the To Date. Please try again.")
         return false;
     }
  	
	    var FromDt= $filter('date')(mem.FromDt,'yyyy-MM-dd');
	    var ToDt=$filter('date')(mem.ToDt,'yyyy-MM-dd');
	   
	    	
	    	 var agentId=$scope.agentSelect;
	    
    	 $scope.url = '/compas/reports?type=PT&FrDt=' +FromDt+'&ToDt='+ToDt+'&agentId='+$scope.agentSelect;
   }
   $scope.previewDeviceReport=function(mem)
   {
  	 if (!$memValid(mem.FromDt)) {
         logger.logWarning("Opss! You may have skipped specifying the From Date. Please try again.")
         return false;
     }
	 else  if (!$memValid(mem.ToDt)) {
         logger.logWarning("Opss! You may have skipped specifying the To Date. Please try again.")
         return false;
     }
  	
	    var FromDt= $filter('date')(mem.FromDt,'yyyy-MM-dd');
	    var ToDt=$filter('date')(mem.ToDt,'yyyy-MM-dd');
	   
	    	
	    	 var deviceId=$scope.deviceSelect;
	    
    	 $scope.url = '/compas/reports?type=DV&FrDt=' +FromDt+'&ToDt='+ToDt+'&DeviceId='+$scope.deviceSelect;
   }
   $scope.loadAgentData = function () {
       $scope.agents = []
       agentSvc.GetAgents($rootScope.UsrRghts.linkId).success(function (response) {
           return $scope.agents = response
       });
   }
   $scope.loadAgentData();
   $scope.loadDeviceData = function () {
       $scope.devices = []
       deviceSvc.GetDevices($scope).success(function (response) {
           return $scope.devices = response
       })
   }
   $scope.loadDeviceData ();
   $scope.transMemList = [];

   $scope.loadcardnumbers=function(mem){
 	 $scope.memEnrollment=true;
     $scope.members = []
     memberInquirySvc.GetMembers().success(function (response) {
     	
         $scope.members = response,
       	   $scope.cardNumber = void 0;
              for (var i = 0; i <= response.length - 1; i++) {
                  $scope.transMemList.push(response[i].idPassPortNo);
              }
       }).error(function (data, status, headers, config) {
           logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
       });
}
   $scope.loadcardnumbers();
     $scope.previewStatement=function(mem)
     {
    	 if (!$memValid(mem.FromDt)) {
           logger.logWarning("Opss! You may have skipped specifying the From Date. Please try again.")
           return false;
       }
  	 else  if (!$memValid(mem.ToDt)) {
           logger.logWarning("Opss! You may have skipped specifying the To Date. Please try again.")
           return false;
       }
    	
  	    var FromDt= $filter('date')(mem.FromDt,'yyyy-MM-dd');
  	    var ToDt=$filter('date')(mem.ToDt,'yyyy-MM-dd');
  	   
  	    	
  	    	 var cardNo=mem.cardNumber;
  	    
      	 $scope.url = '/compas/reports?type=BS&FrDt=' +FromDt+'&ToDt='+ToDt+'&cardNo='+cardNo;
     }
     $scope.exportPdfReport = function(mem) {

    	 if (!$memValid(mem.FromDt)) {
           logger.logWarning("Opss! You may have skipped specifying the From Date. Please try again.")
           return false;
       }
  	 else  if (!$memValid(mem.ToDt)) {
           logger.logWarning("Opss! You may have skipped specifying the To Date. Please try again.")
           return false;
       }
    	
  	    var FromDt= $filter('date')(mem.FromDt,'yyyy-MM-dd');
  	    var ToDt=$filter('date')(mem.ToDt,'yyyy-MM-dd');
  	   
  	    	
  	    	 var cardNo=mem.cardNumber;
  	    
      	 $scope.url = '/compas/reports?type=BS&eType=P&FrDt=' +FromDt+'&ToDt='+ToDt+'&cardNo='+cardNo;
 	}


 	$scope.exportExcelReport = function(mem) {
 	
 		 if (!$memValid(mem.FromDt)) {
 	           logger.logWarning("Opss! You may have skipped specifying the From Date. Please try again.")
 	           return false;
 	       }
 	  	 else  if (!$memValid(mem.ToDt)) {
 	           logger.logWarning("Opss! You may have skipped specifying the To Date. Please try again.")
 	           return false;
 	       }
 		 
 	    	
 	  	    var FromDt= $filter('date')(mem.FromDt,'yyyy-MM-dd');
 	  	    var ToDt=$filter('date')(mem.ToDt,'yyyy-MM-dd');
 	  	   
 	  	    	
 	  	    	 var cardNo=mem.cardNumber;
 	  	    
 	      	 $scope.url = '/compas/reports?type=BS&eType=E&FrDt=' +FromDt+'&ToDt='+ToDt+'&cardNo='+cardNo;

 		
 	}
     $scope.loadBeneficiaryGroups = function () {
     	voucherTopupSvc.GetBnfGrpsForTopup().success(function (response) {
           
             $scope.beneficiaryGroups = response;
         }).error(function (data, status, headers, config) {
             logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
         });

     };
     $scope.loadBeneficiaryGroups(); 
     $scope.previewTopupReport=function(mem)
     {
    	 if (!$memValid(mem.FromDt)) {
           logger.logWarning("Opss! You may have skipped specifying the From Date. Please try again.")
           return false;
       }
  	 else  if (!$memValid(mem.ToDt)) {
           logger.logWarning("Opss! You may have skipped specifying the To Date. Please try again.")
           return false;
       }
    	
  	    var FromDt= $filter('date')(mem.FromDt,'yyyy-MM-dd');
  	    var ToDt=$filter('date')(mem.ToDt,'yyyy-MM-dd');
  	   
  	    	
  	    	 var bnfgrpid=mem.cardNumber;
  	    
      	 $scope.url = '/compas/reports?type=AT&FrDt=' +FromDt+'&ToDt='+ToDt+'&bnfGrpId='+$scope.bnfGrpSelect;
     }
}]).factory('memSvc', function ($http) {
    var shpMemStatement = {};

   
    
    return shpMemStatement;
}).factory('$memValid', function () {
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
