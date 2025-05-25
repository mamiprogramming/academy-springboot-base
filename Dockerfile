# 1. ビルドステージ
FROM eclipse-temurin:17 AS builder

WORKDIR /app

# gradleラッパーとソースを全部コピー
COPY gradlew .
COPY gradle ./gradle
COPY build.gradle .
COPY settings.gradle .
COPY src ./src

# ビルド実行（必要に応じて ./gradlew build --no-daemon --stacktrace など）
RUN ./gradlew build --no-daemon

# 2. 実行ステージ
FROM eclipse-temurin:17

WORKDIR /app

COPY --from=builder /app/build/libs/spring-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]

EXPOSE 8080