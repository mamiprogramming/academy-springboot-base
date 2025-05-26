# ビルドステージ
FROM eclipse-temurin:17 AS builder

WORKDIR /app

COPY gradlew .
COPY gradle ./gradle
COPY build.gradle .
COPY settings.gradle .
COPY src ./src

RUN ./gradlew bootJar --no-daemon

# 実行ステージ
FROM eclipse-temurin:17

WORKDIR /app

COPY --from=builder /app/build/libs/spring-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]

EXPOSE 8080