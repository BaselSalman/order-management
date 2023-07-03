From openjdk:19
EXPOSE 8081
ADD target/order-management-api-0.0.1-SNAPSHOT.jar order-management.jar
ENTRYPOINT ["java","-jar","order-management.jar"]