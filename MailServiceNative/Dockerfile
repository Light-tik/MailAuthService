FROM eclipse-temurin:17-alpine AS build
WORKDIR /app
COPY gradlew build.gradle settings.gradle ./
COPY .gradle .gradle
COPY gradle gradle
COPY src src

RUN ./gradlew clean build -x test

FROM eclipse-temurin:17-alpine
ARG JAR_FILE_DIR=/app/build/libs
COPY --from=build /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]