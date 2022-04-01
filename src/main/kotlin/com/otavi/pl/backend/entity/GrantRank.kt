package com.otavi.pl.backend.entity

import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "grant_rank")
open class GrantRank: Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Int? = null

    @Column(name = "rank_id")
    open var rankId: Int? = null

    @Column(name = "rank_name", length = 32)
    open var rankName: String? = null

    @Column(name = "group_id")
    open var groupId: Int? = null

    @Column(name = "acces_to_register")
    open var accesToRegister: Boolean? = null

    @Column(name = "acces_to_grant_rank")
    open var accesToGrantRank: Boolean? = null

    @Column(name = "acces_to_staff_user")
    open var accesToStaffUser: Boolean? = null

    @Column(name = "access_to_game_rank")
    open var accessToGameRank: Boolean? = null
}