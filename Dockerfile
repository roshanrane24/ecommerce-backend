FROM amazoncorretto:11-alpine-jdk
COPY target/ecommerce-app-0.0.1-SNAPSHOT.jar ecommerce-app.jar
RUN mkdir -p src/main/resources/images
COPY src/main/resources/images/ src/main/resources/images/
COPY src/main/resources/*.jrxml src/main/resources/
ENTRYPOINT ["java","-jar","ecommerce-app.jar"]

