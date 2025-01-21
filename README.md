# MyFirstKMPApp - 郵便番号検索アプリ

KMM（Kotlin Multiplatform Mobile）を使用した郵便番号検索アプリケーションです。iOS/Androidの両プラットフォームで動作します。

## 必要な環境

- Android Studio Hedgehog | 2024.1.1
- Xcode 16.0以上
- JDK 21以上
- Kotlin 1.9.20以上
- Gradle 8.0以上

## セットアップ

### 共通設定（KMP）

`build.gradle.kts`（プロジェクトレベル）に以下の依存関係を追加：

```kotlin
plugins {
    kotlin("multiplatform") version "1.9.20"
    kotlin("plugin.serialization") version "1.9.20"
}

dependencies {
    // Kotlinx Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    
    // Kotlinx Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
    
    // Ktor
    implementation("io.ktor:ktor-client-core:2.3.5")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.5")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.5")
}
```

### Android設定

`androidApp/build.gradle.kts`に以下を追加：

```kotlin
plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "com.example.myfirstkmpapp.android"
    compileSdk = 34
    
    defaultConfig {
        applicationId = "com.example.myfirstkmpapp.android"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }
}

dependencies {
    implementation(project(":shared"))
    implementation("androidx.activity:activity-compose:1.8.1")
    implementation("androidx.compose.ui:ui:1.5.4")
    implementation("androidx.compose.material3:material3:1.1.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
}
```

AndroidManifest.xmlにインターネットパーミッションを追加：

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

### iOS設定

1. Xcode プロジェクトの設定

- ターゲットの最小デプロイメントターゲットを iOS 14.0以上に設定
- Build Settings > Build Options > Enable Bitcode を No に設定

2. Podfileの設定

```ruby
target 'iosApp' do
  use_frameworks!
  platform :ios, '14.0'
  pod 'shared', :path => '../shared'
end
```

3. CocoaPodsのインストールと更新

```bash
pod install
```

## アプリケーションの実行

### Android
1. Android Studioでプロジェクトを開く
2. `androidApp`モジュールを選択
3. 実行ボタンをクリック

### iOS
1. プロジェクトルートで`pod install`を実行
2. `iosApp.xcworkspace`を開く
3. Xcodeで実行ボタンをクリック

## アーキテクチャ

### 共通（KMP）
- `ZipCodeViewModel`: 郵便番号検索のビジネスロジックを管理
- `ZipCodeApi`: APIリクエストの処理
- `AddressData`: 住所データのモデルクラス

### Android
- Jetpack Composeを使用したUI実装
- ViewModelとStateFlowによる状態管理

### iOS
- SwiftUIを使用したUI実装
- ObservableObjectとConcurrencyによる状態管理

## ライセンス

このプロジェクトはMITライセンスの下で公開されています。