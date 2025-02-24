# Use an official OpenJDK runtime as a parent image
FROM openjdk:11-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the current directory contents into the container at /app
COPY . /app

# Compile the project
RUN ./gradlew build

# Run the application
CMD ["java", "-jar", "build/libs/licensing-example-java.jar"]
