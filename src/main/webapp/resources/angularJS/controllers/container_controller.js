'use strict';

angular.module("PantryApp").controller("ContainerController", ["$scope", "ContainerService", ContainerController]);

function ContainerController($scope, ContainerService) {
	const vm = this;
	vm.containers = [];
	vm.addContainerDialogue = false;
	vm.newContainer = {};

	vm.submitContainer = submitContainer;

	getAllContainers();

	function getAllContainers() {
		console.log("seeking containers");
		ContainerService.getAllContainersAndContents()
			.then(function spreadData(res) {
				vm.containers = res.data;
				$scope.containers = res.data;
			},
				function mitigateError(err) {
					console.log(err);
				});
	}

	function submitContainer() {
		vm.newContainer.contents = [];
		ContainerService.postContainer(vm.newContainer)
			.then(function exploreRes(res) {
				console.log(res);
			},
			function exploreErr(err){
				console.log(err);
			});
	}

};