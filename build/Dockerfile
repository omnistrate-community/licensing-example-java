# Build stage
FROM maven:3.9-eclipse-temurin-21-alpine AS build

# Set the working directory
WORKDIR /app

# Copy the current directory contents into the container at /app
COPY . /app

# Clean and compile the project
RUN mvn clean install

# Final stage
FROM eclipse-temurin:21-jre-alpine

# Set the working directory
WORKDIR /app

# Copy the jar file from the build stage
COPY --from=build /app/target/licensing-example-java.jar /app/licensing-example-java.jar

# Create a group (2000) and user (1000)
RUN addgroup -S servicegroup -g 2000 && adduser -S serviceuser -G servicegroup -u 1000

# Expose port 8080
EXPOSE 8080

# Change the user to serviceuser
USER serviceuser:servicegroup
# Command to start the HTTP server
CMD ["java", "-cp", "/app/licensing-example-java.jar", "com.omnistrate.HttpServerApp"]
