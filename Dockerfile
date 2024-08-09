# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the project’s jar file to the container
COPY target/qrScanner.jar app.jar

# Expose the port the app runs on
EXPOSE 8000

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]