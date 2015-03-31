(function () {
	var slick = angular.module("slick", ["ngRoute", "ngResource", "ngMessages", "ngMaterial"]);

	slick.config(function ($mdThemingProvider, $mdIconProvider) {
		$mdThemingProvider.theme('default')
			.primaryPalette('indigo')
			.accentPalette('lime');
	});
	
	slick.config(function ($routeProvider) {
		$routeProvider.when("/books", {
			templateUrl : "partial/books.html",
			controller : "BooksCtrl as ctrl",
			resolve : {
				items : function (backend) {
					return backend.books.query();
				}
			}
		});
		$routeProvider.when("/books/add", {
			templateUrl : "partial/editBook.html",
			controller : "AddBookCtrl as ctrl",
			resolve : {
				authors : function (backend) {
					return backend.authors.query();
				},
				categories : function (categoryTree) {
					return categoryTree();
				}
			}
		});
		$routeProvider.when("/authors", {
			templateUrl : "partial/authors.html",
			controller : "AuthorsCtrl as ctrl",
			resolve : {
				items : function (backend) {
					return backend.authors.query();
				}
			}
		});
		$routeProvider.when("/authors/add", {
			templateUrl : "partial/editAuthor.html",
			controller : "AddAuthorCtrl as ctrl"
		});
		$routeProvider.when("/categories", {
			templateUrl : "partial/categories.html",
			controller : "CategoriesCtrl as ctrl",
			resolve : {
				items : function (backend) {
					return backend.categories.query();
				}
			}
		});
		$routeProvider.when("/categories/add", {
			templateUrl : "partial/editCategory.html",
			controller : "AddCategoryCtrl as ctrl"
		});
		$routeProvider.otherwise("/books");
	});

	slick.controller("NavCtrl", function ($mdSidenav, $mdMedia, $location, $scope) {
		this.toggle = function () {
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
		this.selected = function (item) {
			return $location.path() === item.route;
		};
		this.current = function () {
			return this.items.filter(function (item) {				
				return this.selected(item);
			}, this).pop();
		};
		$scope.$on("$routeChangeStart", function () {
			if(!$mdMedia("gt-md")) {
				$mdSidenav("left").close();
			}
		});
	});

	slick.service("notify", function ($mdToast) {
		return function (message) {
			var toast = $mdToast.simple().content(message).action("OK");
			$mdToast.show(toast);
		};
	});

	slick.controller("BooksCtrl", function (items, $location) {
		this.items = items;
		this.add = function () {
			$location.path("/books/add");
		};
	});

	slick.controller("AddBookCtrl", function (authors, categories, backend, notify, $location) {
		this.authors = authors;
		this.categories = categories;
		var book = {
			authors : []
		};
		this.book = book;
		this.selectedAuthors = function (state) {
			return authors.filter(function (item) {
				return !state && angular.isUndefined(item.selected) || state === item.selected;
			});
		};
		this.addAuthor = function () {
			this.author.selected = true;
			delete this.author;
		};
		this.removeAuthor = function ($index) {
			this.selectedAuthors(true)[$index].selected = false;
		};
		this.save = function () {
			this.selectedAuthors(true).forEach(function (item) {
				book.authors.push({
					id : item.id,
					name : item.name
				});
			});
			book.category = angular.copy(this.category);
			book.publishDate = moment(this.publishDate).unix() / (24 * 60 * 60);
			delete book.category.path;
			backend.books.save(book, function () {
				notify("Saved");
				$location.path("/books");
			}, function () {
				notify("Server error");
			});
		};
	});

	slick.controller("AuthorsCtrl", function (items, $location) {
		this.items = items;
		this.add = function () {
			$location.path("/authors/add");
		};
	});

	slick.controller("AddAuthorCtrl", function (backend, notify, $location) {
		var author = {};
		this.author = author;
		this.save = function () {
			backend.authors.save(author, function () {
				notify("Saved");
				$location.path("/authors");
			}, function () {
				notify("Server error");
			});
		};
	});

	slick.controller("CategoriesCtrl", function (items, backend, $location) {
		this.items = items;		
		this.icon = function (item) {
			if(item.hasChildren && !item.expanded) {
				return "fi-pricetag-multiple";
			} else {
				return "fi-price-tag";
			}
		};
		this.toggle = function (item) {
			if(item.hasChildren) {
				item.expanded = !item.expanded;
				if(item.expanded && !item.children) {
					backend.category.query({ 
							id : item.id 
						}, function (children) {
						item.children = children;
					});
				}				
			}
		};
		this.add = function () {
			$location.path("/categories/add");
		};
	});

	slick.controller("AddCategoryCtrl", function (backend, notify, $location) {
		var category = {
			hasChildren : false
		};
		this.category = category;
		this.save = function () {
			backend.categories.save(category, function () {
				notify("Saved");
				$location.path("/categories");
			}, function () {
				notify("Server error");
			});
		};
	});

	slick.service("backend", function ($resource, $q) {
		return {
			books : $resource("rest/books"),
			authors : $resource("rest/authors"),
			categories : $resource("rest/categories"),
			category : $resource("rest/categories/:id"),
		};
	});

	slick.service("categoryTree", function ($q, backend) {
		function fetchTree(parent) {
			if(parent.hasChildren) {
				return backend.category.query({ 
					id : parent.id 
				}).$promise.then(function (children) {
					return $q.all(children.map(fetchTree)).then(function (results) {
						results.unshift(parent);
						return results;
					});
				});				
			} else {
				return $q(function (resolve) {
					resolve(parent);
				});
			}
		}
		function flatten(array) {
			var out = [];
			array.forEach(function (elem) {
				if(elem.length && typeof elem !== "string") {
					Array.prototype.push.apply(out, flatten(elem));
				} else {
					out.push(elem);
				}
			});
			return out;
		}
		function addPaths(categories) {
			var byId = {};
			categories.forEach(function (category) {
				byId[category.id] = category;
			});
			function path(category) {
				if(!category.path) {
					if(typeof category.parentId !== "undefined") {
						category.path = path(byId[category.parentId]) + " / " + category.name;
					} else {
						category.path = category.name;
					}
				}
				return category.path;
			}
			categories.forEach(path);
			return categories.sort(function (c1, c2) {
				return c1.path > c2.path;
			});
		}
		return function () {
			return backend.categories.query().$promise.then(function (rootCategories) {
				return $q.all(rootCategories.map(fetchTree));
			}).then(function (categories) {
				return addPaths(flatten(categories));
			});
		};
	});
})();