package com.otavi.pl.backend.entity

import javax.persistence.*

@Entity
@Table(name = "rank_limit")
open class RankLimit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Int? = 0

    @Column(name = "rank_id")
    open var rankId: Int = 0

    @Column(name = "rank_name", length = 128)
    open var rankName: String = ""

    @Column(name = "limit_register_rank")
    open var limitRegisterRank: Int = 0
}