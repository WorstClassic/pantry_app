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
		putContainer: putContainer,
		deleteContainer: deleteContainer,
		postItemToContainer: postItemToContainer
	};
	
	return factory;
	
	function getAllContainersAndContents() {
		return $http.get(`${API_ROOT}${CONTAINER}`);
	}
	
	function postContainer(newContainer) {
		return $http.post(`${API_ROOT}${CONTAINER}`, newContainer);
	}
	function putContainer(updatedContainer) {
		return $http.put(`${API_ROOT}${CONTAINER}/${updatedContainer.id}`, updatedContainer);
	}
	function deleteContainer(deletedContainer) {
		return $http.delete(`${API_ROOT}${CONTAINER}/${deletedContainer.id}`);
	}
	
	function postItemToContainer(newItem, containerId){
		return $http.post(`${API_ROOT}${CONTAINER}/${containerId}/${ITEM}`, newItem);
	}
}