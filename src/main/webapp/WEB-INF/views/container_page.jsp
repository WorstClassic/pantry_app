<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Inventory</title>
<link href="/pantry_app/resources/css/style.css" rel="stylesheet">
<!-- AngularJS -->
<script
	src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.9/angular.js"></script>
<script src="/pantry_app/resources/angularJS/app.js"></script>
<script
	src="/pantry_app/resources/angularJS/controllers/container_controller.js"></script>
<script
	src="/pantry_app/resources/angularJS/services/container_service.js"></script>
</head>
<body ng-app="PantryApp">
	<div id="homepage-navbar" class="navbar">
		<h3 class="center">Welcome to your containers.</h3>
		<span class="locations banner"><a href="./inventory">Full
				Inventory</a><a href="./containers">Containers</a></span>
	</div>
	<div ng-controller="ContainerController as ctrl" id="container-root"
		class="root container">
		<div class="add-container">
			<button ng-hide="ctrl.addContainerDialogue"
				ng-click="ctrl.addContainerDialogue=true;">I'm adding
				another container</button>
			<div ng-show="ctrl.addContainerDialogue">
				<form action="/pantry_app/containers" method="POST">
					<input ng-model="ctrl.newContainer.name" name="newContainer"
						placeholder="What will it be named?" type="text" required></input>
					<input type="submit" id="new-container-submit" value="Submit"></input>
				</form>
			</div>
			<div>${error}</div>
		</div>
		<div ng-repeat="container in ctrl.containers"
			class="container box display ng-hook" id="container-display">
			<div><h2>{{container.name}}</h2>
				<button ng-click="container.editName=true">Change Name or Delete</button>
				<div ng-show="container.editName">
					<input type="text" ng-model="container.newName" placeholder="What will the new name be?">
					<button ng-click="ctrl.updateContainer($event, container)">Attempt to update the name</button>
					<div ng-show="container.updateError">SOMETHING WENT WRONG</div>
					<button ng-click="ctrl.deleteContainer($event, container)">Delete it</button>
				</div> </div> 
				<a ng-href="./containers/{{container.id}}/new-item"> Add an Item to This Container </a>
			<div ng-attr-id="{{'container-id-'+ container.id}}"
				class="container card entry">
				<div ng-repeat="item in container.contents">
					<div>upc: {{item.upc}}</div>
					<div>name: {{item.naiiveItemName}} - {{item.unit_amount}}
						{{item.unit}}</div>
					<div>Got:
						{{item.obtainDate[0]}}Year-{{item.obtainDate[1]}}Month-{{item.obtainDate[2]}}Day
					</div>
					<div>Expires:
						{{item.expiryDate[0]}}Year-{{item.expiryDate[1]}}Month-{{item.expiryDate[2]}}Day
					</div>
					<div>
						Description:
						<div>{{item.naiiveItemDescription}}</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
