INITRAMFS_FSTYPES = "cpio.gz"
IMAGE_FSTYPES = "${INITRAMFS_FSTYPES}"

IMAGE_INSTALL:append = " \
  udev \
  dbus \
  curl \
  jq \
  rng-tools \
  u-boot-fw-utils \
  wpa-supplicant \
  networkmanager \
  networkmanager-nmcli \
  networkmanager-wifi \
"

# ビルドで使うコマンドを依存に追加
DEPENDS += "u-boot-mkimage-native"

UBOOT_ARCH ?= "arm"

IMAGE_POSTPROCESS_COMMAND += "make_uboot_img ;"

make_uboot_img () {
    uboot-mkimage -A ${UBOOT_ARCH} -O linux -T ramdisk -d ${WORKDIR}/deploy-${PN}-image-complete/${IMAGE_NAME}.rootfs.cpio.gz ${WORKDIR}/deploy-${PN}-image-complete/uRamdisk
}
