# Stage 1: Build stage using Maven
FROM maven:3.9.6-eclipse-temurin-17-alpine AS build
WORKDIR /app

# Copy pom and source code
COPY pom.xml .
COPY src ./src

# Build the application skipping tests to speed up the image creation
RUN mvn clean package -DskipTests

# Stage 2: Runtime stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy only the built artifact from the build stage
COPY --from build /app/target/*.jar app.jar

# Create a non-root user for security (Best Practice)
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Expose the port defined in your env variables
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]