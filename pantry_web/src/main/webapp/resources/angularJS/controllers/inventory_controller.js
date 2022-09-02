'use strict';

angular.module("PantryApp").controller("InventoryController", ["$scope", "InventoryService", InventoryController]);

function InventoryController($scope, InventoryService) {
	const vm = this;
	vm.dataArrays = { defaultLoad: [], sortByExpiry: [] };
	vm.displayData = vm.dataArrays.defaultLoad;
	vm.isSortByExpiry = false;
	vm.sortByString = `Click to sort by ${vm.isSortByExpiry ? 'Id' : 'Expiry Date'}!`;
	vm.toggleSort = toggleSort

	getAllItems();

	function getAllItems() {
		InventoryService.getAllItems()
			.then(getAllSuccess, getAllFail)
	}

	function getAllSuccess(res) {
		const rawReturn = res.data;
		rawReturn.forEach(ItemElement => {//There is a version of me that made a 
			//subroutine to do this taking in an item object and a keystring to create it.
			//but in the battle between YAGNI and DRY; YAGNI won out this time.
			ItemElement.warning = "";
			if (ItemElement.obtainDate) {
				ItemElement.obtainDate = InventoryService.generateDateObject(ItemElement.obtainDate);
				ItemElement.obtainDateString = ItemElement.obtainDate.toLocaleDateString();
			}
			if (ItemElement.expiryDate) {
				ItemElement.expiryDate = InventoryService.generateDateObject(ItemElement.expiryDate);
				ItemElement.expiryDateString = ItemElement.expiryDate.toLocaleDateString();
				if (ItemElement.expiryDate < Date.now()) {
					ItemElement.warning = ItemElement.warning.concat("This item may be expired!");
				}
			}
			if (ItemElement.containers.length > 1) {
				if (ItemElement.warning.length > 0) {
					ItemElement.warning.concat("&#10;And ")
				}
				ItemElement.warning = ItemElement.warning.concat("This item may be in more than one container!");
			}
		});
		vm.dataArrays.defaultLoad = [...rawReturn];
		vm.dataArrays.sortByExpiry = [...rawReturn];
		vm.dataArrays.sortByExpiry.sort((a, b) => {
			return a.expiryDate - b.expiryDate;
		});
		vm.displayData = vm.dataArrays.defaultLoad;
	}
	function getAllFail(err) {
		console.log(err);
		console.log(err.message);
	}

	function toggleSort() {
		vm.isSortByExpiry = !vm.isSortByExpiry;
		vm.displayData = vm.isSortByExpiry ? vm.dataArrays.sortByExpiry : vm.dataArrays.defaultLoad;
		vm.sortByString = `Click to sort by ${vm.isSortByExpiry ? 'Id' : 'Expiry Date'}!`
	}

}