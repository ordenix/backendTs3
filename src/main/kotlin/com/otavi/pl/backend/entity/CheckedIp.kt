package com.otavi.pl.backend.entity

import javax.persistence.*

@Entity
@Table(name = "checked_ip")
open class CheckedIp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Int? = null

    @Column(name = "ip", length = 128)
    open var ip: String? = null

    @Column(name = "asn", length = 128)
    open var asn: String? = null

    @Column(name = "provider", length = 128)
    open var provider: String? = null

    @Column(name = "continent", length = 128)
    open var continent: String? = null

    @Column(name = "country", length = 128)
    open var country: String? = null

    @Column(name = "city", length = 128)
    open var city: String? = null

    @Column(name = "region", length = 128)
    open var region: String? = null

    @Column(name = "region_code", length = 128)
    open var regionCode: String? = null

    @Column(name = "latitude", length = 128)
    open var latitude: String? = null

    @Column(name = "longitude", length = 128)
    open var longitude: String? = null

    @Column(name = "iso_code", length = 128)
    open var isoCode: String? = null

    @Column(name = "proxy", length = 128)
    open var proxy: String? = null

    @Column(name = "type", length = 128)
    open var type: String? = null

    @Column(name = "port", length = 128)
    open var port: String? = null

    @Column(name = "risk", length = 128)
    open var risk: String? = null

    @Column(name = "attack_history", length = 128)
    open var attackHistory: String? = null

    @Column(name = "last_seen_human", length = 128)
    open var lastSeenHuman: String? = null

    @Column(name = "last_seen_unix", length = 128)
    open var lastSeenUnix: String? = null
}