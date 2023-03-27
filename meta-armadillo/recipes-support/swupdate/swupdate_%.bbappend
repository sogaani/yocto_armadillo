# see https://patchwork.ozlabs.org/project/swupdate/patch/6a28b18d-f12d-4e46-8576-5db43038b9ea@googlegroups.com/

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
SRC_URI += " \
    file://enable-download.cfg \
    file://enable-signed-image.cfg \
    file://enable-encrypted-image.cfg \
"
DEPENDS += " curl"
RDEPENDS:${PN} += "libgcc"

do_install:append () {
	install -d ${D}/${sysconfdir}
	install -m 600 ${TOPDIR}/files/confidential/swupdate_public.pem ${D}/${sysconfdir}/
	install -m 600 ${TOPDIR}/files/confidential/swupdate_aes_key ${D}/${sysconfdir}/
}
