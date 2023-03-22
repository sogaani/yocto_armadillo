## 概要

[Yocto Project](https://www.yoctoproject.org/)を使って、Armadillo-iot-g3-m1用IoT-Gatewayの次のイメージを作成する。

* OS更新ソフト
* ブートローダー
* OSアップデートイメージ

## 事前準備

* 次のコマンドで本リポジトリをクローンしてください
  * `git clone --depth 1 --recursive --shallow-submodules リポジトリURL`
  * 本リポジトリはgitサブモジュールが含まれます。上記のコマンドでgitサブモジュールも同時にクローンできます。
* Yocto Projectのビルドに必要なパッケージをインストール
  * [必要なパッケージ](https://docs.yoctoproject.org/ref-manual/system-requirements.html#required-packages-for-the-build-host)
  * ubuntu22の場合: `sudo apt install gawk wget git diffstat unzip texinfo gcc build-essential chrpath socat cpio python3 python3-pip python3-pexpect xz-utils debianutils iputils-ping python3-git python3-jinja2 libegl1-mesa libsdl1.2-dev pylint xterm python3-subunit mesa-common-dev zstd liblz4-tool language-pack-en lzop`
  * python3.8以上が必要なので、`apt install python3`でインストールされるpythonのバージョンが低い場合はソースからビルドしてください。
  * pyenv経由でpythonを使うとビルドに失敗するので、pyenvを使っている場合は無効化してください。
* localeを`en_US.UTF-8`にする
  * ubuntu22の場合: `sudo update-locale LANG=en_US.UTF-8`
* 次のコマンドでs3から機密ファイルをダウンロードする(awsコマンドのセットアップをしておくこと)
  * `AWS_PROFILE=rsi-infra-dev aws s3 sync s3://riot-development-dev/confidential/gateway-update/ build/files/confidential`

## 各イメージのビルド方法

1. 本プロジェクトのルートディレクトリで次のコマンドを実行してbitbakeコマンドを使えるようにする。シェル毎に実行してください。実行すると/buildディレクトリに移動します。
    * `source poky/oe-init-build-env`
2. 下記のコマンドを実行。成果物は`tmp/deploy/images/armadillo-iot-g3-m1`に出力されます。
    * ビルド時にはVPNを切ってください。ビルド時にソースコードのダウンロードが実行されるため、VPN環境ではビルドがかなり遅くなります。

| ビルドコマンド |  成果物  | 説明 |
| ---- | ---- | ---- |
| `bitbake swupdate-image` | `armadillo_iotg_g3_m1.dtb` <br> `uImage` <br> `uRamdisk` | OS更新ソフト用のカーネル、Device tree blob、initramfs |
| `bitbake virtual/bootloader` | `u-boot-x1.bin` | ブートローダー |
| `bitbake update-image` | `update-image-armadillo-iot-g3-m1.swu` | OSアップデートイメージ |

## OSアップデートイメージにルートFSを取り込む方法

1. アップデートする次のファイルを`build/files`に置く
    * uImage : Linuxカーネル
    * armadillo_iotg_g3_m1.dtb : Device tree blob
    * rootfs.tar.gz : ルートFSを圧縮したファイル（ファイル名はなんでもいい）
2. `build/conf/local.conf`を編集して次の３つの値を書き換える。

```
## ROOTFS_TAR_GZは`build/files`に置いたルートFSを圧縮したファイルの名称を設定する
ROOTFS_TAR_GZ = "file://rootfs.tar.gz"
## ROOTFS_TAR_GZのMD5を設定する
ROOTFS_TAR_GZ_MD5 = "c3cd9cb07023e9a2e510e92270fb97b8"
## 今回のアップデートファイルのバージョンを設定する
OS_VERSION="1.0.0"
```
3. `bitbake update-image`を実行する
