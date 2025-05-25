FROM eclipse-temurin:17

WORKDIR /app

# ビルド済みのJARファイルをイメージにコピー
COPY build/libs/spring-0.0.1-SNAPSHOT.jar app.jar

# アプリケーション実行コマンド
ENTRYPOINT ["java", "-jar", "app.jar"]

# 使用ポートを指定（Renderなどクラウド環境で必要）
EXPOSE 8080