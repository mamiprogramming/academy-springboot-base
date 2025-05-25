# ビルド用ステージ
FROM eclipse-temurin:17 AS builder

WORKDIR /app

# Gradle Wrapperなどのビルドに必要なファイルをコピー
COPY gradlew .
COPY gradle ./gradle
COPY build.gradle .
COPY settings.gradle .
COPY src ./src

# ビルド実行（--no-daemon はRenderのようなCI向け）
RUN ./gradlew build --no-daemon

# 実行用ステージ
FROM eclipse-temurin:17

WORKDIR /app

# ビルドステージの成果物をコピー
COPY --from=builder /app/build/libs/spring-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]

EXPOSE 8080