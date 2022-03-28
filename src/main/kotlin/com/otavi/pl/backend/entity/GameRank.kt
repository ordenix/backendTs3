package com.otavi.pl.backend.entity

import javax.persistence.*

@Entity
@Table(name = "game_rank")
open class GameRank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Int = 0

    @Column(name = "rank_name", length = 32)
    open var rankName: String = "null"

    @Column(name = "group_id")
    open var groupId: Int = 0

    @Column(name = "path", length = 32)
    open var path: String = "null"

    @Column(name = "group_name", length = 32)
    open var groupName: String = "null"

    @Column(name = "sort_id")
    open var sortId: Int = 0
}