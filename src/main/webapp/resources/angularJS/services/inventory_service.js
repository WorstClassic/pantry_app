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
		getItemByUpc:getItemByUpc
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

}