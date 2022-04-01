package com.otavi.pl.backend.entity

import javax.persistence.*

@Entity
@Table(name = "privilage_to_rank")
open class PrivilageToRank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Int? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DBID", referencedColumnName = "dbid")
    open var dbid: UsersRegister? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rank_id", referencedColumnName = "rank_id")
    open var rank_id: GrantRank? = null
}