# StudieHub Renewal Project (1.0.2)

[English](README.md) | [日本語](README-jp.md) | [繁體中文](README-zh-tw.md)

歡迎來到StudieHub Renewal Project！  
StudieHub是一個Spring Boot作為技術核心、以編寫線上學習平台（像Udemy、edX）的專案，源自於2021年某次Java網頁開發訓練營、由一隻6人組成的小隊開發而成。  
本repository則是以**重構與翻新**該專案的[老舊程式庫](https://github.com/AWildHuskyAppeard/studiehubproj)為主要目標，關注點則置於架構改善、可維護性以及導向現代化編程風格。

## 專案目標

本翻新專案主要目標如下所述：

1. 作為個人Java後端技術展示用專案
2. 透過實際改造舊系統，練習並驗證下列工程實務：
   - REST架構設計
   - 自動化測試撰寫
   - Web關注點分離
   - 導入現代化編程風格
   - 套件升級
   - Domain與資料庫結構重新設計
   - 版本管理
   - 文件化
3. 補完當初團隊專案理想中可達成的成果，向原團隊致意

詳細請參考`ROADMAP.md`與`docs/version-work-details`。


## 執行方式

> 注意：本專案尚不備有一鍵設置。
> 由於此為小規模專案，您也可以選擇不架設專案、直接瀏覽文件以及原始碼來進行認識。

> 注意：本專案的文件、原始碼多由英文撰寫，專案之國際化尚未實裝，尚請留意。

### 環境需求
- Java 11
- SQL Server 2019
  - 將Collation（定序）設為`Chinese_Taiwan_Stroke_CI_AS`
  - ⚠️ 其餘的版本以及資料庫定序未經測試，因此無法保證預期行為
  - 若使用Docker啟動SQL Server，需於image啟動階段講collation設置好
    - 詳情可參考 `dev/db/sqlserver/00-docker-sqlserver-setup-note.md`
- 可執行指令的終端機環境

### 執行步驟
1. 將此repository clone到本機
2. 設置SQL Server環境
   1. Port設為`1433`（預設值）
   2. 建立名為`STUDIEHUB`的資料庫
   3. 使用`dev/db/sqlserver/03-login-and-user-template.sql`的SQL建立LOGIN與USER
3. 於專案根目錄執行以下指令：
   ```bash
   mvn spring-boot:run -Dspring-boot.run.profiles=dev
   ```
4. 啟動成功後，終端機會跑出一隻水獺和「DEV MODE」字樣的ASCII圖像
   - 本專案已內建Maven Wrapper，因此無需額外安裝Maven


## 功能概要

### Domain一覽
本應用程式的核心功能在於寫入與讀取與與「學習」相關的資源，因此主要包含以下domain：
- user
- course
- question
- event
- discussion thread
- order/cart

### 介面及功能
- 可透過sidebar進行功能導覽
- 部分功能（如寫入操作）需登入後使用：
  - 一般使用者：`id = test`, `password = test`
  - 管理員：`id = adming5`, `password = manager`

詳細流程請參考`docs/feature-flows`。

### ⚠️ 注意事項

#### 第三方金流（ECPay）服務
1. 本專案於本機端整合ECPay API，因此需搭配ngrok使用：
   1. 安裝並啟動ngrok
   2. 將ngrok提供的forwarding URL設進您本機的環境變數`ngrokHttp`
   3. 重新啟動應用程式
2. 測試用信用卡資料
   - 卡號：`4311 9522 2222 2222`
   - 有效期限：`12/55`
   - CVV：`222`
> 注意：目前僅實作信用卡付款，其餘付款方式尚未支援


## 致謝

- 這份README是由吾師ChatGPT之建議之上修改而成的成果。多謝吾師！