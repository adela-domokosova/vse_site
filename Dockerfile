FROM openjdk:22-jdk-slim
WORKDIR /app
EXPOSE 8080
COPY target/*.jar app.jar
CMD ["app.jar"]
ENTRYPOINT ["java", "-jar"]