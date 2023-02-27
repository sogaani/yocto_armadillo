* ビルド環境作成
  * VirtualBoxインストール
  * ATDEのダウンロード
    * https://armadillo.atmark-techno.com/resources/software/atde/atde-v8
 * VirtualBoxでATDE起動
  * VirtualBoxのゲストツールインストール(コピペ使えなくて不便なので、使えるようにする)
    * https://linuxize.com/post/how-to-install-virtualbox-guest-additions-on-debian-10/
 * Yoctoプロジェクトのビルドに必要なパッケージをインストール
    * sudo apt install gawk wget git diffstat unzip texinfo gcc build-essential chrpath socat cpio python3 python3-pip python3-pexpect xz-utils debianutils iputils-ping python3-git python3-jinja2 libegl1-mesa libsdl1.2-dev pylint3 xterm python3-subunit mesa-common-dev zstd liblz4-tool
  * ubuntu22の場合：sudo apt install gawk wget git diffstat unzip texinfo gcc build-essential chrpath socat cpio python3 python3-pip python3-pexpect xz-utils debianutils iputils-ping python3-git python3-jinja2 libegl1-mesa libsdl1.2-dev pylint xterm python3-subunit mesa-common-dev zstd liblz4-tool language-pack-en lzop
    * https://docs.yoctoproject.org/ref-manual/system-requirements.html#required-packages-for-the-build-host
  * python3.8以上が必要なので、ソースからビルド
       * sudo apt install build-essential zlib1g-dev libncurses5-dev libgdbm-dev libnss3-dev libssl-dev libsqlite3-dev libreadline-dev libffi-dev curl libbz2-dev
    * curl -O https://www.python.org/ftp/python/3.8.2/Python-3.8.2.tar.xz
 * ダンロード
 * git clone https://github.com/openembedded/meta-openembedded.git
 * git clone https://github.com/sbabic/meta-swupdate.git
 * git clone git://git.yoctoproject.org/poky.git
 * bitbake使えるようにする
    * sudo update-locale LANG=en_US.UTF-8
   * poky/oe-init-build-env build

 * bitbake swupdate-image


* 参考サイト:https://corgi-lab.com/linux/yocto-raspberrypi4/
https://mickey-happygolucky.hatenablog.com/entry/2018/12/25/135954
新しいマシーン作成：https://docs.yoctoproject.org/dev/dev-manual/new-machine.html

* 切り方: https://armadillo.atmark-techno.com/blog/615/3048
* ubootのパラメータ書き換え　https://armadillo.atmark-techno.com/blog/53/3841

nmcli device wifi con gamera-g password dphses0203; apt update; apt install -y openssh-server u-boot-tools; useradd -m sogaani; echo "05046295\n05046295\n" | passwd sogaani; echo "sogaani   ALL=(ALL:ALL) ALL" >> /etc/sudoers; chsh -s /bin/bash sogaani

apt-get install -y u-boot-tools
echo "/dev/mmcblk2boot0           0xe0000         0x2000" >> /etc/fw_env.config

## uboot環境変数書き換え
事前にuboot内でsaveenvってやっておかないとデフォルトの環境変数でいろいろ上書きされちゃう
sudo sh -c "echo 0 > /sys/block/mmcblk2boot0/force_ro"
fw_setenv [variable name] [variable value]
sudo sh -c "echo 1 > /sys/block/mmcblk2boot0/force_ro"
// これはやっちゃだめ。
// なんかうまくデータが読み込めなくて環境変数を全部書き換えちゃってる。

## パーテーション
sudo fdisk /dev/sdd

sudo mount /dev/sdd1 /mnt/sdcard

## 起動

mkimage -A arm -O linux -T ramdisk -d swupdate-image-armadillo-g3-20230226085506.rootfs.cpio.gz uRamdisk

setenv mmcdev 0
setenv mmcpart 1
mmc rescan
setenv bootargs console=${console},${baudrate}
{
run loadimage
run loadfdt
fatload mmc ${mmcdev}:${mmcpart} 0x86000000 uRamdisk
bootm ${loadaddr} 0x86000000 ${fdt_addr}
}else
{
fatload mmc ${mmcdev}:${mmcpart} ${loadaddr} fitImage
}
bootm ${loadaddr} 0x86000000 ${fdt_addr}
run mmcboot


## install

swupdate -i /mnt/update-image-armadillo-g3-20230226113939.swu -H armadillo-g3-m1:1.0  -v
swupdate -d "-u http://172.16.10.12/update-image-armadillo-g3-20230227110610.swu" -H armadillo-g3-m1:1.0  -v

* https://manpages.debian.org/experimental/swupdate/swupdate.1.en.html

## 無線LAN接続
ifconfig wlan0 up

/etc/wpa_supplicant.conf 書き換え
network={
        ssid="gamera-g"
        psk="dphses0203"
}

wpa_supplicant -iwlan0 -Dwext -c/etc/wpa_supplicant.conf &

dhcpの場合
dhclient wlan0