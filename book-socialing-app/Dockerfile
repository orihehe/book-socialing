# Build stage
FROM gradle:8.5-jdk17 AS build
WORKDIR /app
COPY . .
RUN gradle clean build -x test --no-daemon

# Runtime stage
FROM openjdk:17-jdk-slim
COPY --from=build /app/build/libs/*.jar app.jar
ENV JAVA_OPTS=""
ENTRYPOINT exec java $JAVA_OPTS -jar app.jar
