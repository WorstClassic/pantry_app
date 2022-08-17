'use strict';

angular.module("PantryApp").controller("AddItemController", ["$scope", "$location", "ContainerService", "InventoryService", AddItemController
]);

function AddItemController($scope, $location, ContainerService, InventoryService) {

	const upcValidateRegex = /^\d{12}$/;

	const vm = this;
	vm.workingItem = {};
	vm.upcQueryResponse = {};
	vm.upc = "";
	vm.errorMitigation = false;
	vm.successMessage = false;
	vm.upcIsRequested = false;
	vm.showRemoteInfo = false;
	vm.postIsSent = false;
	vm.containerId = findPathId();
	vm.upcLookupError = false;
	vm.upcQueryResolved = false;
	vm.upcQuery = lookUpUpc;
	vm.transferFromLoad = transferToWorkingItem;
	vm.transferAll = transferAll;
	vm.submitItem = submitNewItem;
	vm.resetForm = resetForm;
	vm.reopenForm = allowEditingUpc;
	vm.goToHome = navigateToContainersPage;

	vm.displayOrderer = [
		{
			name: "obtainDate",
			copy: false,
			optional: true,
			type: "date",
			placeHolder: "Today"
		},
		{
			name: "expiryDate",
			copy: false,
			optional: true,
			type: "date",
		},
		{ name: "naiiveItemName" },
		{
			name: "naiiveItemDescription",
			type: "textarea"
		},
		{ name: "unit_amount" },
		{ name: "unit" }
	];

	function findPathId() {
		//const pathRef = $location.path(); //For some reason, path() returns "" in my impl.
		const pathRef = $location.absUrl();
		const splitOnSlash = pathRef.split('/');
		const index = splitOnSlash.length - 2;
		return splitOnSlash[index];//Probably should reference this from someplace else.
	};

	function lookUpUpc(event) {
		event.preventDefault();
		vm.upcLookupError = false;
		const rawUpc = vm.upc.substring(0);
		const queryUpc = rawUpc.replace(/-|\s/g, "");
		if (!upcValidateRegex.test(queryUpc)) {
			vm.upcLookupError = "Could not read that as a UPC-A. A UPC-A is a 12 digit code.";
			return;
		}
		stopEditingUpc();
		console.log("seeking upc");
		InventoryService.getItemByUpc(queryUpc)
			.then(function spreadData(res) {
				vm.upcQueryResponse = res.data;
				//$scope.containers = res.data;
			}).catch(
				function mitigateError(err) {
					console.log(err);
				}).finally(upcCallComplete);
	};

	function submitNewItem(event) {
		event.preventDefault();
		vm.postIsSent = true;
		const postBody = { upc: vm.upc };
		vm.displayOrderer.forEach(entry => formatArguments(entry, postBody));
		postEntry(postBody).then(postSuccess, postFailMitigate).finally(postCleanup);
	}

	function transferToWorkingItem(variableName) {
		vm.workingItem[variableName] = vm.upcQueryResponse.item[variableName];
		//If this unifies references rather than leaving them independent...
		//Consider interpolation...
		//newString = `${oldString}`;
		//researching- it may be that certain browsers require an even more bonkers solution.
		//newString=(' ' + oldString).slice(1);
		//Might be required in Chrome.
	}

	function transferAll() {
		vm.displayOrderer.forEach(entry => {
			if (entry.copy === undefined || entry.copy)
				transferToWorkingItem(entry.name);
		});
	}

	function upcCallComplete() {
		vm.upcQueryResolved = true;
		vm.showRemoteInfo = (vm.upcQueryResponse.source != "LOCAL_CACHE");
		if (!vm.showRemoteInfo) { //TODO Lotta ! that could be omitted by renaming the boolean.
			vm.displayOrderer.forEach(entry => {
				if (entry.copy === undefined) {
					vm.workingItem[entry.name] = vm.upcQueryResponse.item[entry.name];
				}
			});
		}
	};
	function formatArguments(entry, postBody) {
		postBody[entry.name] = vm.workingItem[entry.name];
		if (entry.type === "date") {
			console.log(postBody[entry.name])
			//some processing on vm.working
		}
	}

	function postSuccess(res) {
		const data = res.data;
		const expiryDateObj = InventoryService.generateDateObject(data.expiryDate);
		const expiryString = expiryDateObj.toDateString();
		vm.successMessage = `Your ${data.naiiveItemName} has been added!  \n   Remember that it will expire on: ${expiryString}.`;
	}

	function postFailMitigate(err) {
		vm.errorMessage = err.message;
	}
	function postCleanup() { }


	function postEntry(postBody) {
		return ContainerService.postItemToContainer(postBody, vm.containerId)
	}

	function stopEditingUpc() {
		vm.upcIsRequested = true;
	};

	function allowEditingUpc() {
		//We should wipe out all data. $Pristine?
	};
	function resetForm() {
		vm.workingItem = {};
		vm.upcQueryResponse = {};
		vm.upc = "";
		vm.errorMitigation = false;
		vm.successMessage = false;
		vm.upcQueryResolved = false;
		vm.upcIsRequested = false;
		vm.showRemoteInfo = false;
		vm.postIsSent = false;
	};
	function navigateToContainersPage() {
		$location.path("/containers");
	}


};