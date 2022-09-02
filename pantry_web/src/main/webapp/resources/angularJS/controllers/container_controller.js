'use strict';

angular.module("PantryApp").controller("ContainerController", ["$scope", "ContainerService", ContainerController]);

function ContainerController($scope, ContainerService) {
	const vm = this;
	vm.containers = [];
	vm.addContainerDialogue = false;
	vm.newContainer = {};

	vm.submitContainer = submitContainer;
	vm.updateContainer = updateContainer;
	vm.deleteContainer = deleteContainer;

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
				function exploreErr(err) {
					console.log(err);
					console.log(err.message);
				});
	}

	function updateContainer(event, containerObject) {
		const putBody = { id: containerObject.id, name: containerObject.newName, contents: [] };
		ContainerService.putContainer(putBody)
			.then(function updatePut(res) {
				console.log(res);
				const updateThisIndex = vm.containers.findIndex(container => container.id === res.data.id);
				vm.containers[updateThisIndex] = { ...res.data };
			},
				function errorPut(err) {
					console.log(err);
					console.log(err.message);
				});
	}

	function deleteContainer(event, containerObject) {
		const deletionId = containerObject.id;
		ContainerService.deleteContainer(containerObject)
			.then(function updateDelete(res) {
				console.log(res);
				vm.containers = vm.containers.filter(container => !(container.id === deletionId));
			},
				function errorDelete(err) {
					console.log(err);
					console.log(err.message);
				});
	}

};