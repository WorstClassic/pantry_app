## For_Fun.Pantry_App

This project is meant to help manage a home pantry, and especially its dry goods and spices inventories. The kinds of things that a person could easily forget they were out of and need to go out to get because they're stashed in an unmarked box high in the cupboard. The feature goals are:  
1. Track containers and items within, initially relying on human recognizance of containers to correctly ID and track container location.  
   * Including expiry information, so that ingredients near expiry can be put to the top for replenishment or use.
2. Cache information about items in a near-remote location for faster retrieval and easier reference. 
3. Hold information about recipes.  
4. Use the information about expiry and recipes to recommend recipe plans and inventory replenishment plans.  

It should be noted that because this is meant to perform local-remote caching, it is intended to rely on microservices as little as possible and is a bit of a monolith. Also there are currently no plans to differentiate between users.  


### Development Setup
The current development scheme is using Maven Jetty to bootstrap the project. Upon executing `mvn jetty:run` the build should be immediately launched at **localhost:8080/jetty**.