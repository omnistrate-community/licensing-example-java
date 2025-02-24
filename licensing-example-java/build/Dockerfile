# Build stage
FROM maven:3.9-eclipse-temurin-23-alpine AS build

# Set the working directory
WORKDIR /app

# Copy the current directory contents into the container at /app
COPY . /app

# Compile the project
RUN mvn clean install

# Final stage
FROM eclipse-temurin:23-alpine

# Set the working directory
WORKDIR /app

# Copy the jar file from the build stage
COPY --from=build /app/target/licensing-example-java.jar /app/licensing-example-java.jar

# Run the application
CMD ["java", "-jar", "licensing-example-java.jar"]
