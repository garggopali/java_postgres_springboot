# Use an official Java runtime as a parent image
#FROM openjdk:17-jdk-slim
#for gcp
FROM eclipse-temurin:17-jdk

# Set the working directory in the container
WORKDIR /app

# Copy the compiled JAR file into the container (Make sure to build your project first)
COPY target/*.jar app.jar

# Expose the port that your Java application runs on
EXPOSE 9092

# Run the Java application
#CMD ["java", "-jar", "app.jar"]
#for gcp
ENTRYPOINT ["java", "-jar", "app.jar"]