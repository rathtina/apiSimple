# -------- Build Stage --------
FROM maven:3.8.5-eclipse-temurin-21 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# -------- Runtime Stage --------
FROM eclipse-temurin:21.0.1_2-jdk as runtime
WORKDIR /app
COPY --from=builder /app/target/auth-api-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
