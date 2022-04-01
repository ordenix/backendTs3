package com.otavi.pl.backend.entity

import javax.persistence.*

@Entity
@Table(name = "modules_settings")
open class ModulesSetting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Int? = null

    @Column(name = "setting", length = 32)
    open var setting: String? = null

    @Column(name = "options", length = 32)
    open var options: String? = null
}