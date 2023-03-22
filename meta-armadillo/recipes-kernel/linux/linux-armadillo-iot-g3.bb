DESCRIPTION = "Linux Kernel for Raspberry Pi"
SECTION = "kernel"
LICENSE = "GPL-2.0-only"

PE = "1"

inherit siteinfo
require recipes-kernel/linux/linux-yocto.inc

LINUX_VERSION ?= "4.9"
COMPATIBLE_MACHINE = 'armadillo-iot-g3'

SRC_URI = " \
    https://armadillo.atmark-techno.com/files/downloads/armadillo-iot-g3/source/linux-4.9-x1-at28.tar.gz;name=kernel_source \
    file://001_resolve_dtc_build_error.patch;patch=1 \
    file://002_resolve_address_build_error.patch;patch=1 \
    file://armadillo-rescue.cfg \
    file://armadillo-firmware.cfg \
    file://firmware.tar.gz;name=firmware \
    "
SRC_URI[kernel_source.md5sum] = "925c38fea478b7d5fbf0c6d9d226354b"

# tar.gzを展開したあと、kernelのソースコードが入っているフォルダを指定
S = "${WORKDIR}/linux-4.9-x1-at28"

# PV変数でのカーネルのバージョンチェックをスキップ
KERNEL_VERSION_SANITY_SKIP="1"

KCONFIG_MODE = "alldefconfig"
KBUILD_DEFCONFIG = "x1_defconfig"
PROVIDES = "virtual/kernel"

# auto load kernel module
## KERNEL_MODULE_AUTOLOAD +=

# ビルドで使うコマンドを依存に追加
DEPENDS += "lzop-native u-boot-mkimage-native" 

do_patch:append () {
    # Copy firmware to kernel src
    mkdir -p ${S}/prebuilds
    cp -r ${WORKDIR}/firmware ${S}/prebuilds/
}

UBOOT_ENTRYPOINT = "0x82000000"
UBOOT_LOADADDRESS = "0x82000000"
KERNEL_EXTRA_ARGS += "LOADADDR=${UBOOT_ENTRYPOINT}"
