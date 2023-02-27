DESCRIPTION = "A rootfs image for Armadillo IoT G3."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "https://armadillo.atmark-techno.com/files/downloads/armadillo-iot-g3/debian/debian-buster-armhf_aiotg3_20221118.tar.gz;name=rootfs \
           "
SRC_URI[rootfs.md5sum] = "c3cd9cb07023e9a2e510e92270fb97b8"

## 何もインストールしない
IMAGE_INSTALL = ""
PACKAGE_INSTALL = ""
IMAGE_FSTYPES = "ext4.gz"

python __anonymous() {
    d.delVarFlag("do_fetch", "noexec")
}

ROOTFS_POSTPROCESS_COMMAND = "make_rootfs;"

make_rootfs () {
    cd ${IMAGE_ROOTFS}
    tar --extract --no-same-owner -z -f ${DL_DIR}/debian-buster-armhf_aiotg3_20221118.tar.gz
}

inherit image
