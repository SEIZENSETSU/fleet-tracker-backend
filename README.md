# 環境構築手順
以下に環境構築手順を記載します。

## 事前準備
まずはIntelliJ IDEAをインストールし、このプロジェクトを開いてください。

## DBの起動
local-devディレクトリに移動し、以下のコマンドを実行してください。

```bash
docker compose up -d
```

### 環境変数の設定
Intellij IDEAの右上にある`Edit Configurations`をクリックし、`Environment`タブを選択して環境変数を設定してください。
または、bashに以下の環境変数を設定してください。(exportコマンドを使用)
```bash
SPRING_APPLICATION_NAME=demo1
SPRING_DATASOURCE_URL=jdbc:postgresql://127.0.0.1:5432/postgres
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=password
SPRING_DATASOURCE_DRIVER_CLASS_NAME=org.postgresql.Driver
```

## Spring Bootアプリケーションの起動

### 方法1
IntelliJ IDEAで`src/main/java/com/example/demo/DemoApplication.java`を開き、Runボタンを押すか、画面右上のRunボタンをクリックしてください。

### 方法2
プロジェクトのルートディレクトリで以下のコマンドを実行してください。
85% EXECUTINGという表示が出れば成功です。
```bash
./gradlew bootRun
```

## 動作確認
ブラウザで `http://localhost:8080/hello` にアクセスし、以下のメッセージが表示されれば成功です。

```
Hello, World!
```

## おまけ
Dockerを用いてSpring Bootアプリケーションを起動する方法もあります。
プロジェクトのルートディレクトリで以下のコマンドを実行してください。

```bash
docker build --no-cache -t spring-boot-demo .
```

```bash
docker run -p 8080:8080 spring-boot-demo
```