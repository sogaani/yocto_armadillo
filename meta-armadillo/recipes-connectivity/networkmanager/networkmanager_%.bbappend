FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
SRC_URI += 'file://NetworkManager.conf'
PACKAGECONFIG = "readline nss ifupdown dnsmasq nmcli vala wifi"

do_install:append() {
    install -m 0644 ${WORKDIR}/NetworkManager.conf ${D}${sysconfdir}/NetworkManager/
}
