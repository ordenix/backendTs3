package com.otavi.pl.backend.entity

import javax.persistence.*

@Entity
@Table(name = "register_rank_province")
open class RegisterRankProvince {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Int? = null

    @Column(name = "rank_name", length = 32)
    open var rankName: String? = null

    @Column(name = "group_id")
    open var groupId: Int? = null

    @Column(name = "path", length = 32)
    open var path: String? = null
}