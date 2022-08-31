<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Adding an Item</title>
<link href="/pantry_app/resources/css/style.css" rel="stylesheet">
<link href="/pantry_app/resources/css/spinner.css" rel="stylesheet">

<!-- AngularJS -->
<script
	src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.9/angular.js"></script>
<script src="/pantry_app/resources/angularJS/app.js"></script>
<script
	src="/pantry_app/resources/angularJS/controllers/add_item_controller.js"></script>
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
	<div ng-controller="AddItemController as addItem">
		<form id="add-item-form"
		ng-submit="addItem.submitItem($event)" 
		ng-hide="addItem.successMessage || addItem.errorMessage">
			<div id="upc-div">
				<input id="upc-input-input" class="input large"
					ng-model="addItem.upc" ng-disabled="addItem.upcIsRequested"
					type="text" placeHolder="First- enter the item's UPC here" required></input>
				<button name="getUpc"
				type="button"
					ng-disable="addItem.upcIsRequested"
					ng-click="addItem.upcQuery($event)">Find that item!</button>
				<span ng-show="addItem.upcLookupError">{{addItem.upcLookupError}}</span>
			</div>
			<div ng-show="addItem.upcIsRequested">
				<div class="lookup-spinner" ng-hide="addItem.upcQueryResolved">
					<h3>Looking for info on that UPC</h3>
					<div id="upc-lookup-spinner" class="purple-loading-spinner"></div>
				</div>
				<h2>Please fill in these details:</h2>
				<div class="lookup row"
					ng-repeat="displayEntry in addItem.displayOrderer">
					<div class="Left 50">
						<span ng-if="displayEntry.name"> {{displayEntry.name}}: </span> <input
							ng-if="displayEntry.type!='textarea'"
							ng-model="addItem.workingItem[displayEntry.name]"
							name="{{displayEntry.name}}"
							type="{{(displayEntry.type || 'text')}}"
							placeholder="{{displayEntry.placeHolder}}"
							ng-required="!!!displayEntry.optional"></input>
						<textarea ng-if="displayEntry.type==='textarea'"
							ng-model="addItem.workingItem[displayEntry.name]"
							name="{{displayEntry.name}}" type="text"
							ng-required="!displayEntry.optional"></textarea>
					</div>
					<div ng-show="addItem.showRemoteInfo">
						<div class="Center 10">
							<button ng-if="displayEntry.copy===undefined"
							type="button"
								ng-click="addItem.transferFromLoad(displayEntry.name)"><-</button>
						</div>
						<div class="Right 40" ng-if="displayEntry.copy===undefined">{{addItem.upcQueryResponse.item[displayEntry.name]}}</div>
					</div>
				</div>
				<div ng-show="addItem.showRemoteInfo">
					This data came from {{addItem.upcQueryResponse.source}}.
					<button type="button" ng-click="addItem.transferAll()">I want to use all
						the sourced data.</button>
				</div>
				<div ng-show="addItem.upcIsRequested">
					<input type="submit" name="submit"
						value="Add this item to the container.">
				</div>
			</div>
		</form>
		<div ng-show="addItem.postIsSent">
			<div ng-hide="addItem.successMessage || addItem.errorMessage">
				<h3>Working on that!</h3>
				<div id="posting-spinner" class="purple-loading-spinner"></div>
			</div>
			<div ng-show="addItem.errorMessage">
				<span class="api-message error">{{addItem.errorMessage}}</span>
				<button ng-click="addItem.reopenForm()">I'd like to review
					my information and try again.</button>
			</div>
			<div ng-show="addItem.successMessage">
				<span class="api-message success">{{addItem.successMessage}}</span>
				<button ng-click="addItem.resetForm()">I'd like to add
					another item to this container.</button>
			</div>
			<button ng-click="addItem.goToHome()">Back to the containers
				list.</button>
		</div>
	</div>
</body>
</html>