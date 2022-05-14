package com.otavi.pl.backend.entity

import javax.persistence.*

@Entity
@Table(name = "active_modules")
open class ActiveModule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Int? = null

    @Column(name = "module_name", length = 32)
    open var moduleName: String? = null

    @Column(name = "status")
    open var status: Boolean? = null
}