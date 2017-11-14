/**
 * Transaction Held Angular Module
 */
'use strict';
angular.module('app.rptmasterlisting', []).controller("rptmasterlistingCtrl", ["$scope", "$filter", "mSvc", "$mValid", "$rootScope", "blockUI", "logger", "$location","$http","$window", function ($scope, $filter, mSvc, $mValid, $rootScope, blockUI, logger, $location,$http,$window) {
    $scope.transMemList = [];
    $scope.mem=[];
    var init;
    $scope.masters=[]
    $scope.showdata=true;
    $scope.showSer=true;
    $scope.showAgent=false;

    $scope.previewPriceReport=function(mem)
    {
   	 if (!$memValid($scope.locationSelect)) {
          logger.logWarning("Opss! You may have skipped specifying the Camp. Please try again.")
          return false;
      }
     	 $scope.url = '/compas/reports?type=PR&locationId='+$scope.locationSelect;
    }
      
}]).factory('mSvc', function ($http) {
    var shpMemStatement = {};
   
    return shpMemStatement;
}).factory('$mValid', function () {
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
