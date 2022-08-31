<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Edit or Delete an Item</title>
<link href="/pantry_app/resources/css/style.css" rel="stylesheet">
<link href="/pantry_app/resources/css/spinner.css" rel="stylesheet">

<!-- AngularJS -->
<script
	src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.9/angular.js"></script>
<script src="/pantry_app/resources/angularJS/app.js"></script>
<script
	src="/pantry_app/resources/angularJS/controllers/edit_item_controller.js"></script>
<script
	src="/pantry_app/resources/angularJS/services/container_service.js"></script>
<script
	src="/pantry_app/resources/angularJS/services/inventory_service.js"></script>
</head>
<body ng-app="PantryApp">
	<div id="homepage-navbar" class="navbar">
		<h3 class="center">Add an item to ${containerName}!</h3>
		<span class="locations banner"><a href="/pantry_app/inventory">Full
				Inventory</a><a href="/pantry_app/containers">Containers</a></span>
	</div>
	<div ng-controller="EditItemController as editItem">
		<button type="button" ng-click="editItem.deleteItem($event)">Just
			Delete It</button>
		<form id="edit-item-form"
		ng-submit="editItem.submitItem($event)" 
		ng-hide="editItem.successMessage || editItem.errorMessage">
			<div id="upc-div">
				<input id="upc-input-input" class="input large"
					ng-model="editItem.workingItem.upc" ng-disabled="editItem.upcIsRequested"
					type="text" placeHolder="First- enter the item's UPC here" required></input>
				<button name="getUpc"
				type="button"
					ng-disable="editItem.upcIsRequested"
					ng-click="editItem.upcQuery($event)">Load Different UPC Data</button>
				<span ng-show="editItem.upcLookupError">{{editItem.upcLookupError}}</span>
			</div>
			<div>
				<div ng-show="editItem.upcIsRequested">
					<h3>Looking for info on that UPC</h3>
					<div id="upc-lookup-spinner" class="purple-loading-spinner"></div>
				</div>
				<h2>Please fill in these details:</h2>
				<div class="lookup row"
					ng-repeat="displayEntry in editItem.displayOrderer">
					<div class="Left 50">
						<span ng-if="displayEntry.name"> {{displayEntry.name}}: </span> <input
							ng-if="displayEntry.type!='textarea'"
							ng-model="editItem.workingItem[displayEntry.name]"
							name="{{displayEntry.name}}"
							type="{{(displayEntry.type || 'text')}}"
							placeholder="{{displayEntry.placeHolder}}"
							ng-required="!!!displayEntry.optional"></input>
						<textarea ng-if="displayEntry.type==='textarea'"
							ng-model="editItem.workingItem[displayEntry.name]"
							name="{{displayEntry.name}}" type="text"
							ng-required="!displayEntry.optional"></textarea>
					</div>
					<div ng-show="editItem.showRemoteInfo">
						<div class="Center 10">
							<button ng-if="displayEntry.copy===undefined"
							type="button"
								ng-click="editItem.transferFromLoad(displayEntry.name)"><-</button>
						</div>
						<div class="Right 40" ng-if="displayEntry.copy===undefined">{{editItem.upcQueryResponse.item[displayEntry.name]}}</div>
					</div>
				</div>
				<div ng-show="editItem.showRemoteInfo">
					This data came from {{editItem.upcQueryResponse.source}}.
					<button type="button" ng-click="editItem.transferAll()">I want to use all
						the sourced data.</button>
				</div>
				<div ng-show="editItem.upcIsRequested">
					<input type="submit" name="submit"
						value="Add this item to the container.">
				</div>
			</div>
		</form>
		<div ng-show="editItem.postIsSent">
			<div ng-hide="editItem.successMessage || editItem.errorMessage">
				<h3>Working on that!</h3>
				<div id="posting-spinner" class="purple-loading-spinner"></div>
			</div>
			<div ng-show="editItem.errorMessage">
				<span class="api-message error">{{editItem.errorMessage}}</span>
				<button ng-click="editItem.reopenForm()">I'd like to review
					my information and try again.</button>
			</div>
			<div ng-show="editItem.successMessage">
				<span class="api-message success">{{editItem.successMessage}}</span>
				<button ng-click="editItem.resetForm()">I'd like to edit
					another item to this container.</button>
			</div>
			<button ng-click="editItem.goToHome()">Back to the inventory.</button>
		</div>
	</div>
</body>
</html>