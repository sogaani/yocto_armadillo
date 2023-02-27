# see https://patchwork.ozlabs.org/project/swupdate/patch/6a28b18d-f12d-4e46-8576-5db43038b9ea@googlegroups.com/

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
SRC_URI += "file://enable-download.cfg"
DEPENDS += " curl"
RDEPENDS:${PN} += "libgcc"
