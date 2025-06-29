FROM eclipse-temurin:17-jdk-focal AS build
WORKDIR /app

COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle
RUN ./gradlew dependencies --no-daemon

COPY src ./src
RUN ./gradlew build --no-daemon -x test

FROM eclipse-temurin:17-jre-focal
WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT exec java $JAVA_OPTS -jar app.jar