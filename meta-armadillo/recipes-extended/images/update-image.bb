DESCRIPTION = "Example image demonstrating how to build SWUpdate compound image"

FILESEXTRAPATHS:prepend := "${TOPDIR}/files:"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit swupdate

SRC_URI = "\
    file://sw-description \
    file://uImage \
    file://armadillo_iotg_g3_m1.dtb \
"

OS_VERSION??="1.0.0"

# do not append machine name to image file
SWUPDATE_IMAGES_NOAPPEND_MACHINE[uImage] = "1"
SWUPDATE_IMAGES_NOAPPEND_MACHINE[armadillo_iotg_g3_m1.dtb] = "1"

# images to build before building swupdate image
IMAGE_DEPENDS = "rootfs-image"

# images and files that will be included in the .swu image
SWUPDATE_IMAGES = "rootfs-image"

SWUPDATE_IMAGES_FSTYPES[rootfs-image] = ".ext4.gz.enc"

SWUPDATE_SIGNING = "RSA"

SWUPDATE_PRIVATE_KEY = "${TOPDIR}/files/confidential/swupdate_private_3072.pem"
SWUPDATE_PASSWORD_FILE = "${TOPDIR}/files/confidential/passphrase"

python do_swuimage:append() {
    import json

    osVersion = d.getVar('OS_VERSION')
    hardware = "arimadillo_iot_g3"
    modelName = "m1"
    fileName = d.getVar('IMAGE_NAME', True) + '.swu'
    metadata = {
        "channel": "main",
        "hardware": hardware,
        "modelName": modelName,
        "osVersion": osVersion,
        "fullFilePath": f"/gateway-os/main/{hardware}/{modelName}/{fileName}"
    }
    outdir = d.getVar('SWUDEPLOYDIR')
    os.makedirs(outdir, exist_ok=True)
    metadata_file = f"{outdir}/metadata.json"
    json.dump(metadata, open(metadata_file, "w"), indent=4)
}
