HOMEPAGE = "http://www.denx.de/wiki/U-Boot/WebHome"
SECTION = "bootloaders"
DEPENDS += "flex-native bison-native python3-setuptools-native"

PE = "1"

LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"

SRC_URI = " \
    https://armadillo.atmark-techno.com/files/downloads/armadillo-iot-g3/source/uboot_2016.07-at23.tar.gz;name=uboot-source \
    file://001_updatetool.patch \
    "

SRC_URI[uboot-source.md5sum] = "f57b3f19b315636c64d3517e7b273a03"

S = "${WORKDIR}/uboot_2016.07-at23"
B = "${WORKDIR}/build"

UBOOT_INITIAL_ENV = ""

require recipes-bsp/u-boot/u-boot.inc
