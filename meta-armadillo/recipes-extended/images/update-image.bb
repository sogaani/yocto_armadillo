DESCRIPTION = "Example image demonstrating how to build SWUpdate compound image"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit swupdate

SRC_URI = "\
    file://sw-description \
"

# do not append machine name to image file
SWUPDATE_IMAGES_NOAPPEND_MACHINE[armadillo-g3] = "0"

# images to build before building swupdate image
IMAGE_DEPENDS = "rootfs-image"

# images and files that will be included in the .swu image
SWUPDATE_IMAGES = "rootfs-image"

SWUPDATE_IMAGES_FSTYPES[rootfs-image] = ".ext4.gz"
