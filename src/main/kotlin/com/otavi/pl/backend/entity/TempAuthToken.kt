package com.otavi.pl.backend.entity

import javax.persistence.*

@Entity
@Table(name = "temp_auth_token")
open class TempAuthToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Int = 0

    @Column(name = "DBID")
    open var dbid: Int = 0

    @Column(name = "token", length = 32)
    open var token: String = ""
}