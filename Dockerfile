FROM openjdk:24-slim
WORKDIR /app
COPY target/wise.catalog-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]


