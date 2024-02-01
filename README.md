# Ayatsurikun
### このアプリは「あやつり君」を操作するための専用アプリです。
## 「あやつり君」とは？
現在の家電やデバイスではエアコンや照明、プロジェクターなど赤外線リモコンで操作するものが多くあります。つまり、その家電やデバイスの数だけリモコンが存在することになります。そのため、リモコンの紛失や電池交換など、リモコンの管理がだんだん煩わしくなってきます。
そこで、「あやつり君」を開発し、「あやつり君」1台で赤外線で操作する複数の機器を制御可能にするとともに、スマホから操作可能にすることを目指しました。
ガジェット側の開発にはESP32-WROOM-32Dを使用

## 使用言語、IDE、ライブラリ等
- 言語：Java
- IDE：Android Studio
- 使用ライブラリ：usb-serial-for-android

## デバイス選択画面
- アプリを開くと最初に表示される​
- 接続するデバイス(あやつり君)、および接続方式の選択​
#### 接続方式
- USB
- Bluetooth(SPP)

## ボタン登録ダイアログ
- あやつり君からシリアル通信で信号データを受信したときに表示される​
- ボタン名を入力し、シリアル通信で受信した信号データのラベル付けを行う​

## リモコン画面
- 登録されたボタンの一覧が表示される​
- 押されたボタン名に対応した信号データをあやつり君にシリアル通信で送信する
