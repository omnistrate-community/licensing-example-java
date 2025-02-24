# Build stage
FROM maven:3.9-eclipse-temurin-21-alpine AS build

# Set the working directory
WORKDIR /app

# Copy the current directory contents into the container at /app
COPY . /app

# Clean and compile the project
RUN mvn clean install

# Final stage
FROM jetty:11-jre21

# Set the working directory
WORKDIR $JETTY_HOME/webapps

# Copy the war file from the build stage
COPY --from=build /app/target/licensing-example-java.war $JETTY_BASE/webapps/root.war

# Expose port 8080
EXPOSE 8080

# Command to start Jetty with the WAR file
CMD ["java", "-jar", "/usr/local/jetty/start.jar"]
