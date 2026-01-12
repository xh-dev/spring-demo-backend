ARG CORS_URL

# --- STAGE 1: Build Stage ---
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# 1. Copy only pom.xml to cache dependencies (this speeds up future builds)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# 2. Copy source code and build the application


COPY src ./src
RUN sed -i 's|\(    public static final String CORS_URL = "\)\([a-zA-Z:/\.0-9]\+\)\("; // CORS_URL\)$|\1'"$CORS_URL"'\3|' src/main/java/me/xethh/libs/web/Const.java
RUN cat  src/main/java/me/xethh/libs/web/Const.java
RUN mvn clean package -DskipTests

# --- STAGE 2: Runtime Stage ---
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# 3. Create a non-root user for security
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# 4. Copy only the built JAR from the first stage
COPY --from=build /app/target/*.jar app.jar

# 5. Expose the Spring Boot port (default 8080)
EXPOSE 8080

# 6. Optimized JVM settings for container environments
ENTRYPOINT ["java", \
            "-XX:+UseContainerSupport", \
            "-XX:MaxRAMPercentage=75.0", \
            "-Dspring.profiles.active=prod", \
            "-jar", "app.jar"]