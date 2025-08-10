FROM openjdk:24-slim
WORKDIR /app
COPY target/wise.catalog-0.0.1.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
