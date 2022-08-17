'use strict';

angular.module("PantryApp").factory("ContainerService", ContainerServiceFactory);

ContainerServiceFactory.$inject = ["$http"];

function ContainerServiceFactory($http){
	const API_ROOT = "http://localhost:8080/pantry_app/api";
	const CONTAINER = "/containers";
	const ITEM = "/items";
	
	const factory = {
		getAllContainersAndContents: getAllContainersAndContents,
		postContainer: postContainer,
		postItemToContainer: postItemToContainer
	};
	
	return factory;
	
	function getAllContainersAndContents() {
		return $http.get(`${API_ROOT}${CONTAINER}`);
	}
	
	function postContainer(newContainer) {
		return $http.post(`${API_ROOT}${CONTAINER}`, newContainer);
	}
	
	function postItemToContainer(newItem, containerId){
		return $http.post(`${API_ROOT}${CONTAINER}/${containerId}/${ITEM}`, newItem);
	}
}