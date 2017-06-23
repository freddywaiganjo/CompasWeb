
/**
 * Beneficiary Import Angular Module
 */
'use strict';
angular.module('app.beneficiaryupload', []).controller("fileuploadCtrl", ["$scope", "$filter", "fileuploadSvc","locationSvc", "loginSvc", "localStorageService",  "$rootScope", "blockUI", "logger", "$location", function ($scope, $filter, fileuploadSvc,locationSvc, loginSvc, localStorageService, $rootScope, blockUI, logger, $location) {
    //var init;
	$scope.typeSelect=0;

    $scope.uploadBeneficiaryDetails = function () {
        //logger.logSuccess("Uploading file in progress");
        var file = $scope.myFile;
        //console.log('file is## ' + JSON.stringify(file));
        blockUI.start()
        fileuploadSvc.uploadBeneficiary(file,$rootScope.UsrRghts.sessionId).success(function (response) {
            if (response.respCode == 200) {
                logger.logSuccess("Great! The Card Holder Information Uploaded successfully")

                $location.path('/dashboard');

            } else {
                logger.logError(response.respMessage);
            }
            blockUI.stop();
        }).error(function (data, status, headers, config) {
            logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
            blockUI.stop();
        });
    };
    $scope.uploadHHDetails = function () {
        //logger.logSuccess("Uploading file in progress");
        var file = $scope.myFile;
        //console.log('file is## ' + JSON.stringify(file));
        blockUI.start()
        fileuploadSvc.uploadHHDetails(file,$rootScope.UsrRghts.sessionId,$scope.locationSelect).success(function (response) {
            if (response.respCode == 200) {
                logger.logSuccess("Great! The RDR Information Uploaded successfully")

                $location.path('/dashboard');

            } else {
                logger.logError(response.respMessage);
            }
            blockUI.stop();
        }).error(function (data, status, headers, config) {
            logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
            blockUI.stop();
        });
    };
    $scope.fileTypes=[{"id":"1","fTypeName":"Upload Card Holders"},{"id":"2","fTypeName":"Upload Household Details"}]

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
} ]).factory('fileuploadSvc', function ($http) {
    var uploadSvc = {};

    uploadSvc.uploadBeneficiary = function (file,createdBy) {
        var fd = new FormData();
        fd.append('file', file);
        fd.append('createdBy',createdBy);
        return $http({
        	 url: '/compas/rest/upload/uploadbeneficiary',
            method: 'POST',
            transformRequest: angular.identity,
            headers: { 'Content-Type': undefined },
            data: fd
        });
    };
    uploadSvc.uploadHHDetails = function (file,createdBy) {
        var fd = new FormData();
        fd.append('file', file);
        fd.append('createdBy',createdBy);
        return $http({
            url: '/compas/rest/upload/uploadhhdetails',
           
            method: 'POST',
            transformRequest: angular.identity,
            headers: { 'Content-Type': undefined },
            data: fd
        });
    };

 

    return uploadSvc;
}).directive('fileModel', ['$parse', function ($parse) {
    return {
        restrict: 'A',
        link: function(scope, element, attrs) {
            var model = $parse(attrs.fileModel);
            var modelSetter = model.assign;

            element.bind('change', function(){
                scope.$apply(function(){
                    modelSetter(scope, element[0].files[0]);
                });
            });
        }
    };
}]);