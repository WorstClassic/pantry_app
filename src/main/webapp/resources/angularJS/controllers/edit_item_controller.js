'use strict';

angular.module("PantryApp").controller("EditItemController", ["$scope", "$location", "ContainerService", "InventoryService", EditItemController
]);

function EditItemController($scope, $location, ContainerService, InventoryService) {

	const upcValidateRegex = /^\d{12}$/;

	const vm = this;
	vm.workingItem = {};
	vm.initialItem = {};
	vm.upcQueryResponse = {};
	vm.upc = "";
	vm.errorMitigation = false;
	vm.successMessage = false;
	vm.upcIsRequested = false;
	vm.showRemoteInfo = false;
	vm.putIsSent = false;
	vm.containerId = findPathId();
	vm.upcLookupError = false;
	vm.upcQueryResolved = false;
	vm.upcQuery = lookUpUpc;
	vm.transferFromLoad = transferToWorkingItem;
	vm.transferAll = transferAll;
	vm.submitItem = submitUpdates;
	vm.resetForm = resetForm;
	vm.reopenForm = allowEditingUpc;
	vm.goToHome = navigateToInventoryPage;
	vm.deleteItem = deleteEntry;

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

	getEditingItem();

	function getEditingItem() {
		InventoryService.getItemById(findPathId())
			.then(function spreadInitialData(res) {
				vm.workingItem = { ...res.data };
				vm.workingItem.obtainDate = InventoryService.generateDateObject(vm.workingItem.obtainDate);
				vm.workingItem.expiryDate = InventoryService.generateDateObject(vm.workingItem.expiryDate);
				vm.initialItem = { ...vm.workingItem };
			},
				function initialLoadFails(err) { console.log(err.message) });
	}

	function findPathId() {
		//const pathRef = $location.path(); //For some reason, path() returns "" in my impl.
		const pathRef = $location.absUrl();
		const splitOnSlash = pathRef.split('/');
		const index = splitOnSlash.length - 1;
		return splitOnSlash[index];//Probably should reference this from someplace else.
	};

	function lookUpUpc(event) {
		event.preventDefault();
		vm.upcLookupError = false;
		const rawUpc = vm.workingItem.upc.substring(0);
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

	function submitUpdates(event) {
		event.preventDefault();
		vm.putIsSent = true;
		const putBody = { upc: vm.upc };
		vm.displayOrderer.forEach(entry => formatArguments(entry, putBody));
		putEntry(putBody).then(putSuccess, putFailMitigate).finally(putCleanup);
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
	function formatArguments(entry, putBody) {
		putBody[entry.name] = vm.workingItem[entry.name];
		if (entry.type === "date") {
			console.log(putBody[entry.name])
			//some processing on vm.working
		}
	}

	function putSuccess(res) {
		const data = res.data;
		const expiryDateObj = InventoryService.generateDateObject(data.expiryDate);
		const expiryString = expiryDateObj.toDateString();
		vm.successMessage = `Your ${data.naiiveItemName} has been edited!  \n   Remember that it will expire on: ${expiryString}.`;
	}

	function putFailMitigate(err) {
		vm.errorMessage = err.message;
	}
	function putCleanup() { }


	function putEntry(putBody) {
		return ItemService.putItem(putBody);
	}
	
	function deleteEntry(putBody){
		return ItemService.deleteItem(putBody.id);
	}

	function stopEditingUpc() {
		vm.upcIsRequested = true;
	};

	function allowEditingUpc() {
		//We should wipe out all data. $Pristine?
	};
	function resetForm() {
		vm.workingItem = { ...vm.initialItem };
		vm.upcQueryResponse = {};
		vm.upc = "";
		vm.errorMitigation = false;
		vm.successMessage = false;
		vm.upcQueryResolved = false;
		vm.upcIsRequested = false;
		vm.showRemoteInfo = false;
		vm.putIsSent = false;
	};
	function navigateToInventoryPage() {
		$location.path("/inventory");
	}


};