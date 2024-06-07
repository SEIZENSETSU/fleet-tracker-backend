# ベースイメージとして、Amazon Corretto 17を含むAlpine Linuxを使用
FROM amazoncorretto:17-alpine-jdk

# アプリケーション用の作業ディレクトリを作成
WORKDIR /app

# 現在のディレクトリの内容をコンテナの作業ディレクトリにコピー
COPY . .

# アプリケーションのビルド。テストをスキップしてクリーンビルドを行う
RUN ./gradlew clean build -x test

# アプリケーションが使用する8080番ポートを開放
EXPOSE 8080

# アプリケーションの実行コマンドを設定
CMD ["java", "-jar", "./build/libs/fleet-tracker-0.0.1-SNAPSHOT.jar"]
