DESCRIPTION = "A rootfs image for Armadillo IoT G3."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

FILESEXTRAPATHS:prepend := "${TOPDIR}/files:"

ROOTFS_TAR_GZ ??= "https://armadillo.atmark-techno.com/files/downloads/armadillo-iot-g3/debian/debian-buster-armhf_aiotg3_20221118.tar.gz"
ROOTFS_TAR_GZ_MD5 ??= "c3cd9cb07023e9a2e510e92270fb97b8"
SRC_URI = "${ROOTFS_TAR_GZ};name=rootfs; \
           "
SRC_URI[rootfs.md5sum] := "${ROOTFS_TAR_GZ_MD5}"

## 何もインストールしない
IMAGE_INSTALL = ""
PACKAGE_INSTALL = ""
IMAGE_FSTYPES = "ext4.gz.enc"
SWUPDATE_AES_FILE = "${TOPDIR}/files/confidential/swupdate_aes"

python __anonymous() {
    d.delVarFlag("do_fetch", "noexec")
    filepath = bb.fetch2.localpath(d.getVar('ROOTFS_TAR_GZ'), d)
    d.setVar('ROOTFS_FILEPATH', filepath)
}

ROOTFS_POSTPROCESS_COMMAND = "make_rootfs;"

make_rootfs () {
    cd ${IMAGE_ROOTFS}
    tar --extract --no-same-owner -z -f ${ROOTFS_FILEPATH}
}

inherit image swupdate-enc
