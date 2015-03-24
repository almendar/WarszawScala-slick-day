(function() {

    var slick = angular.module("slick", ["ngRoute", "ngMaterial"]);

    slick.config(function($mdThemingProvider, $mdIconProvider) {
        $mdThemingProvider.theme('default')
            .primaryPalette('indigo')
            .accentPalette('lime');
    });
    
	slick.config(function($routeProvider) {
		$routeProvider.when("/books", {
			templateUrl : "partial/books.html",
			controllerAs : "BooksCtrl"
		});
		$routeProvider.when("/authors", {
			templateUrl : "partial/authors.html",
			controllerAs : "AutorsCtrl"
		});
		$routeProvider.when("/categories", {
			templateUrl : "partial/categories.html",
			controllerAs : "CategoriesCtrl"
		});
		$routeProvider.otherwise("/books");
	});

	slick.controller("BooksCtrl", function() {
	});

	slick.controller("AuthorsCtrl", function() {
	});

	slick.controller("CategoriesCtrl", function() {
	});

})();