// Commands used to build the spring boot application using maven:
mvn clean
mvn install -DskipTests

// Commands used to build the docker image of the spring boot application:
docker build -t order_management .
docker tag order_management baselsalman/order_management
// The image was tagged with my docker hub username (baselsalman) to be able to push it on docker hub
docker push baselsalman/order_management

// To run the application, you must pull the image from docker hub, create a network to link the spring boot application with the MySQL server, run a container that holds the MySQL server and then run the spring boot application
docker network create spring-net
docker run --name order_mysqldb -e MYSQL_ROOT_PASSWORD=baselsalman123 -e MYSQL_DATABASE="order_management_db" --network=spring-net -p 3306:3306 -d mysql:latest
// Make sure to use the same name (order_mysqldb) for the database container since it's the name that was set in application.properties file
docker pull baselsalman/order_management
docker run -d --name order_management_container -e DB_USERNAME=root -e DB_PASSWORD=baselsalman123 -e DB_URL='jdbc:mysql://mysqldb:3306/order_management_db' --network=spring-net  -p 8081:8081 baselsalman/order-management

// Now you can get access to the application user this url: http://localhost:8081/
// use the url http://localhost:8081/swagger-ui/index.html to open swagger ui documentation for the api, and also you can test it from there