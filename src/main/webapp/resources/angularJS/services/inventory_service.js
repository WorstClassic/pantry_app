'use strict';

angular.module("PantryApp").factory("InventoryService", InventoryServiceFactory);

InventoryServiceFactory.$inject = ["$http"];

function InventoryServiceFactory($http) {
	const API_ROOT = "http://localhost:8080/pantry_app/api";
	const ITEM = "/items";
	const UPC = "/upc";

	const factory = {
		getAllItems: getAllItems,
		getItemById: getItemById,
		putItem: putItem,
		deleteItem: deleteItem,
		getItemByUpc: getItemByUpc,

		generateDateObject: generateDateObject
	};

	return factory;

	function getAllItems() {
		return $http.get(`${API_ROOT}${ITEM}`);
	}

	function getItemById(idValue) {
		return $http.get(`${API_ROOT}${ITEM}\\${idValue}`);
	}

	function getItemByUpc(upc) {
		return $http.get(`${API_ROOT}${ITEM}${UPC}\\${upc}`);
	}

	function putItem(updatedItem) {
		return $http.put(`${API_ROOT}${ITEM}/${updatedItem.id}`, updatedItem);
	}

	function deleteItem(deletedItem) {
		return $http.delete(`${API_ROOT}${ITEM}/${deletedItem.id}`);
	}

	function generateDateObject(dateJSONArray) {
		if (dateJSONArray) {
			if (typeof dateJSONArray === "object" && Array.isArray(dateJSONArray)) {
				if (dateJSONArray.length === 3) {
					dateJSONArray[1]--;
					return new Date(...dateJSONArray);
				}
			}
		}
	}

}