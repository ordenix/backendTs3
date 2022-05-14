package com.otavi.pl.backend.entity

import javax.persistence.*

@Entity
@Table(name = "action_ban_type")
open class ActionBanType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Int? = null

    @Column(name = "name", length = 256)
    open var name: String? = null

    @Column(name = "group_id")
    open var groupId: Int? = null

    @Column(name = "action", length = 256)
    open var action: String? = null

    @Column(name = "time")
    open var time: Boolean? = null
}