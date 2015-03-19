(function(){

    var slick = angular.module("slick", ["ngMaterial"]);

    slick.config(function($mdThemingProvider, $mdIconProvider) {
        $mdThemingProvider.theme('default')
            .primaryPalette('indigo')
            .accentPalette('lime');
    });

})();