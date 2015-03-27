(function() {

	var mockBackend = true;

	var slick = angular.module("slick", ["ngRoute", "ngResource", "ngMaterial"]);

	slick.config(function($mdThemingProvider, $mdIconProvider) {
		$mdThemingProvider.theme('default')
			.primaryPalette('indigo')
			.accentPalette('lime');
	});
	
	slick.config(function($routeProvider) {
		$routeProvider.when("/books", {
			templateUrl : "partial/books.html",
			controller : "BooksCtrl as ctrl",
			resolve : {
				items : function(backend) {
					return backend.books.query();
				}
			}
		});
		$routeProvider.when("/authors", {
			templateUrl : "partial/authors.html",
			controller : "AuthorsCtrl as ctrl",
			resolve : {
				items : function(backend) {
					return backend.authors.query();
				}
			}
		});
		$routeProvider.when("/categories", {
			templateUrl : "partial/categories.html",
			controller : "CategoriesCtrl as ctrl",
			resolve : {
				items : function(backend) {
					return backend.categories.query();
				}
			}
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

	slick.controller("BooksCtrl", function(items) {
		this.items = items;
	});

	slick.controller("AuthorsCtrl", function(items) {
		this.items = items;
	});

	slick.controller("CategoriesCtrl", function(items, backend) {
		this.items = items;
		
		this.icon = function(item) {
			if(item.hasChildren && !item.expanded) {
				return "fi-pricetag-multiple";
			} else {
				return "fi-price-tag";
			}
		};

		this.toggle = function(item) {
			if(item.hasChildren) {
				item.expanded = !item.expanded;
				if(item.expanded && !item.children) {
					backend.category.query(item.id).then(function(children) {
						item.children = children;
					})
				}				
			}
		};
	});

	slick.service("backend", function($resource, $q) {
		if(mockBackend) {
			return {
				books : {
					query : function() {
						return [{
							"id": 0,
							"authors": [{
								"id": 0,
								"name": "F. Scott Fitzgerald"
							}],
							"category": {
								"id": 2,
								"name": "Rich people",
								"parentId": 1,
								"hasChildren": false
							},
							"title": "The Great Gatsby",
							"publishDate": -16142
						}];
					}
				},
				authors : {
					query : function() {
						return [{
							"id": 0,
							"name": "F. Scott Fitzgerald"
						}];
					}
				},
				categories : {
					query : function() {
						return [{
							"id" : 1,
							"name" : "Root category",
							"hasChildren" : true
						}];
					}
				},
				category : {
					query : function(id) {
						return $q(function(resolve, reject) {
							resolve([{
								"name" : "Category with children",
								"hasChildren" : true
							}, {
								"name" : "Category with no children",
								"hasChildren" : false
							}]);
						});
					}
				}
			}
		} else {
			return {
				books : $resource("rest/books"),
				authors : $resource("rest/authors"),
				categories : $resource("rest/categories"),
				category : $resource("rest/categories/:id"),
			}			
		}
	});

})();