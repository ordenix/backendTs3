package com.otavi.pl.backend.entity

import javax.persistence.*

@Entity
@Table(name = "ban_table")
open class BanTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Int? = null

    @Column(name = "name", length = 1024)
    open var name: String? = null

    @Column(name = "description", length = 1024)
    open var description: String? = null

    @Column(name = "skip_grant")
    open var skipGrant: Boolean? = null

    @Column(name = "active")
    open var active: Boolean? = null
}