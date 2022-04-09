package com.otavi.pl.backend.entity

import javax.persistence.*

@Entity
@Table(name = "IpHistory")
open class IpHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Int? = null

    @Column(name = "id_ip")
    open var idIp: Int? = null

    @Column(name = "dbid")
    open var dbid: Int? = null

    @Column(name = "time")
    open var time: Int? = null
}