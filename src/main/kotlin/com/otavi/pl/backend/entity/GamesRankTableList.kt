package com.otavi.pl.backend.entity

import javax.persistence.*

@Entity
@Table(name = "games_rank_table_list")
open class GamesRankTableList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Int = 0

    @Column(name = "group_name", length = 32)
    open var groupName: String = ""

    @Column(name = "sort_id")
    open var sortId: Int = 0
}