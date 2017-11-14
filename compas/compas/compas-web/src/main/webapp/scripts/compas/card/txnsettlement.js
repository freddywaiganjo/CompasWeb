/**
 * Transaction Held Angular Module
 */
'use strict';
angular.module('app.txnsettlement', []).controller("txnSettlementCtrl", ["$scope", "$filter", "txnSettlementSvc","$txnSettlementValid", "$rootScope", "blockUI", "logger", "$location","$http","$window", function ($scope, $filter, txnSettlementSvc,$txnSettlementValid, $rootScope, blockUI, logger, $location,$http,$window) {
 var init;
   $scope.previewReport=function(mem)
   {
  	
   }
  
  
 
   
}]).factory('txnSettlementSvc', function ($http) {
    var compasTxnSettlement = {};
    compasTxnSettlement.GetSafComTxns= function (cardnumber) {
        return $http({
            url: '/compas/rest/transaction/gtSafComTxns/'+cardnumber,
            method: 'GET',
            headers: { 'Content-Type': 'application/json' }
        });
    };
   
   
    return compasTxnSettlement;
}).factory('$txnSettlementValid', function () {
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
