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

	slick.controller("NavCtrl", function($mdSidenav, $mdMedia, $location, $scope) {
		this.toggle = function() {
			$mdSidenav("left").toggle();
		};
		this.items = [{
			route : "/books",
			title : "Books",
			icon : "fi-book"
		}, {
			route : "/authors",
			title : "Authors",
			icon : "fi-torso"
		}, {
			route : "/categories",
			title : "Categories",
			icon : "fi-price-tag"
		}];
		this.selected = function(item) {
			return $location.path() === item.route;
		};
		this.current = function() {
			return this.items.filter(function(item) {				
				return this.selected(item);
			}, this).pop();
		};
		$scope.$on("$routeChangeStart", function() {
			if(!$mdMedia("gt-md")) {
				$mdSidenav("left").close();
			}
		});
	});

	slick.controller("BooksCtrl", function() {
	});

	slick.controller("AuthorsCtrl", function() {
	});

	slick.controller("CategoriesCtrl", function() {
	});

})();