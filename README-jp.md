# StudieHub Renewal Project (1.0.0-SNAPSHOT)

[English](README.md) | [日本語](README-jp.md) | [繁體中文](README-zh-tw.md)

ようこそ、StudieHub Renewal Projectへ！  
StudieHubは、Spring Bootを駆使してオンライン学習サイト（Udemy、edX風）作りを目標としたプロジェクトでした。2021年のあるJava web開発ブートキャンプにて、6人のチームによる成果物です。  
本リポジトリは、旧プロジェクトの[コードベース](https://github.com/AWildHuskyAppeard/studiehubproj)に基づき、**再構築と更新**を目標として進行されています。現在の注目点は、アーキテクチャー改善、継続可能なメンテナンス導入、開発規約・シンタックスの現代化です。

## プロジェクト目標

StudieHub Renewal Projectは、下記の目標に心がけて進行されています。

1. **ポートフォリオ**
2. **レガシー現場を題目とした実践的学習**
   - RESTアーキテクチャー
   - テストの応用・自動化
   - Web関心事の分離（Separation of web concerns）
   - コーディングスタイル現代化への移行
   - 依存項目の整理・更新
   - ドメイン及びスキーマの再設計
   - バージョニング導入
   - ドキュメント作成
3. チームメンバーへのリスペクトとして、前プロジェクトの**理想図の完成**


## 実行方法

> 備考①：本プロジェクトは、現時点ではワンクリックデモは含まれていません。
> 小規模のコードベースであるため、アプリケーションを実行せず、
> ドキュメントを合わせてソースコードを直接参照することもできますので、
> お好みのスタイルでご確認いただければ幸いです。

> 備考②：ユーザーデータ・UIは中国語で、ソースコードやドキュメントは主に英語で構成されています。
> 現時点ではi18nは実装されず、README以外は日本語訳はないため、
> ご覧の際にはお気をつけください。

### 必要環境
- Java 11
- SQL Server 2019
  - Collationを`Chinese_Taiwan_Stroke_CI_AS`に設定
  - ⚠️ 他の設定については未検証
  - Docker利用時は、image起動時点でcollationを指定する必要があります
    - 詳細は`dev/db/sqlserver/00-docker-sqlserver-setup-note.md`を参照
- コマンド実行可能なターミナル環境

### 手順
1. 本リポジトリをローカル環境へclone
2. SQL Server環境構築
   1. Portを`1433`に設定（デフォルト値）
   2. `STUDIEHUB`データベースを作成
   3. `dev/db/sqlserver/03-login-and-user-template.sql`を使用してLOGIN/USERを作成
3. プロジェクトルートで下記を実行：
   ```bash
   mvn spring-boot:run -Dspring-boot.run.profiles=dev
   ```
4. 起動成功時、ターミナルに1匹のビーバーと「DEV MODE」と表示されたASCIIアートが出力されます
   - Maven Wrapperを同梱しているため、Mavenの事前インストールは不要です


## 機能概要

### ドメイン一覧
本アプリケーションは学習関連リソースを中心に、以下のドメインを扱います。
- user
- course
- question
- event
- discussion thread
- order/cart

### UI・機能関連
- Sidebarから各機能を確認可能
- 一部機能（書き込み操作など）はログインが必要
   - 一般ユーザー：`id = test`, `password = test`
   - 管理者：`id = adming5`, `password = manager`

詳細な操作フローは、`docs/feature-flows`をご参照ください。

### ⚠️ 注意点

下記の機能をご確認する際に、デフォルト以外の手順が必要になっています。

#### 外部決済サービス（ECPay）
1. ローカル環境でECPay APIを使用するため、ngrokを利用しています。
   1. ngrokをインストールし起動
   2. Forwarding URL（転送URL）を環境変数`ngrokHttp`に設定
   3. アプリケーションを再起動
2. テスト用クレジットカード情報
   - カード番号：`4311 9522 2222 2222`
   - 有効期限：`12/55`
   - CVV: `222`
> 備考：現時点では、クレジットカード決済のみ対応しています


## 謝辞

- 本READMEの作成は、ChatGPT大先生の助言を参考にしつつ修正したものです。我が師に感謝いたします。