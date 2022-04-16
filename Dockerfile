FROM openjdk:11
COPY target/ecommerce-app-0.0.1-SNAPSHOT.jar ecommerce-app.jar
RUN mkdir -p src/main/resources/
COPY src/main/resources/ src/main/resources/
ENTRYPOINT ["java","-jar","ecommerce-app.jar"]

