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
				if (Array.isArray(ItemElement.obtainDate)) {
					if (ItemElement.obtainDate.length === 3) {
						ItemElement.obtainDateObj = InventoryService.generateDateObject(ItemElement.obtainDate);
						ItemElement.obtainDateString = ItemElement.obtainDateObj.toDateString();
					}
				}
			}
			if (ItemElement.expiryDate) {
				if (Array.isArray(ItemElement.expiryDate)) {
					if (ItemElement.expiryDate.length === 3) { //I did a belt-and-suspenders here.
						ItemElement.expiryDateObj = InventoryService.generateDateObject(ItemElement.expiryDate);
						ItemElement.expiryDateString = ItemElement.expiryDateObj.toDateString();
						if (ItemElement.expiryDateObj < Date.now()) { }
						ItemElement.warning.concat("This item may be expired!");
					}
				}
			}
			if (ItemElement.containers.length > 1) {
				if (ItemElement.warning.length > 0) {
					ItemElement.warning.concat("<br>And ")
				}
				ItemElement.warning.concat("This item may be in more than one container!");
			}
		});
		vm.dataArrays.defaultLoad = [...rawReturn];
		vm.dataArrays.sortByExpiry = [...rawReturn];
		vm.dataArrays.sortByExpiry.sort((a, b) => {
			return b.expiryDateObj - a.expiryDateObj;
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