# --- Stage 1: Build Stage ---
FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /app

# 1. Copy only the build configuration first to leverage Docker layer caching
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

# 2. Download dependencies (this layer is cached until pom.xml changes)
RUN ./mvnw dependency:go-offline

# 3. Copy source and build the JAR
COPY src ./src
RUN ./mvnw clean package -DskipTests

# --- Stage 2: Runtime Stage ---
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# 4. Security: Create a non-root user to run the app
RUN groupadd -r spring && useradd -r -g spring spring
USER spring

# 5. Copy only the compiled JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# 6. Expose the port your app runs on
EXPOSE 8080

# 7. Optimize JVM for container environments
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]
