DESCRIPTION = "A rootfs image for Armadillo IoT G3."

SRC_URI = "https://armadillo.atmark-techno.com/files/downloads/armadillo-iot-g3/debian/debian-buster-armhf_aiotg3_20221118.tar.gz;name=rootfs \
           "
SRC_URI[rootfs.md5sum] = "c3cd9cb07023e9a2e510e92270fb97b8"
S = "${WORKDIR}/debian-buster-armhf_aiotg3_20221118"

IMAGE_FSTYPES = "ext4.gz"

inherit core-image
