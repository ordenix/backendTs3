package com.otavi.pl.backend.entity

import javax.persistence.*

@Entity
@Table(name = "NickHistory")
open class NickHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Long? = null

    @Lob
    @Column(name = "Nick")
    open var nick: String? = null

    @Column(name = "dbid")
    open var dbid: Int? = null

    @Column(name = "time")
    open var time: Int? = null
}