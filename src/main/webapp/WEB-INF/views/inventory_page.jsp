<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Inventory</title>
<link href="/pantry_app/resources/css/inventory.css" rel="stylesheet">
<!-- AngularJS -->
<script
	src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.9/angular.js"></script>
<script src="/pantry_app/resources/angularJS/app.js"></script>
<script
	src="/pantry_app/resources/angularJS/controllers/inventory_controller.js"></script>
<script
	src="/pantry_app/resources/angularJS/services/inventory_service.js"></script>
</head>
<body ng-app="PantryApp">
	<div id="homepage-navbar" class="navbar">
		<h3 class="center">Welcome to your inventory</h3>
		<span class="locations banner"><a href="./inventory">Full
				Inventory</a><a href="./containers">Containers</a></span>
	</div>
	<div ng-controller="InventoryController as ctrl" id="inventory-root"
		class="root inventory">
		<button id="sort-toggle" type="button" ng-click="ctrl.toggleSort()">{{ctrl.sortByString}}</button>
		<div id="list-container">
			<div ng-repeat="item in ctrl.displayData" id="inventory-display"
				class="inventory box display ng-hook">
				<div ng-attr-id="{{'item-id-'+item.id}}"
					ng-attr-class="{{item.warning?'item card error':'item card'}}">
					<div>upc: {{item.upc}}</div>
					<div>name: {{item.naiiveItemName}} - {{item.unit_amount}}
						{{item.unit}}</div>
					<div ng-id="{{'item-id-'+ item.id + '-warning'}}"
						ng-if="item.warning"><pre>{{item.warning}}</pre></div>
					<div>Expires: {{item.expiryDateString}}</div>
					<div>Got: {{item.obtainDateString}}</div>
					<div class="description">
						Description:
						<br>
						<div>{{item.naiiveItemDescription}}</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
